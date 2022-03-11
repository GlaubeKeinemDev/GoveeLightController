package de.glaubekeinemdev.goveelightcontroller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class GoveeLightController {

    private final String apiKey;
    private static GoveeLightController instance;

    public GoveeLightController(String apiKey) {
        this.apiKey = apiKey;
        instance = this;
    }

    public ArrayList<Device> getDevices() {
        if (apiKey == null)
            return new ArrayList<>();

        try {
            final JSONObject jsonObject = new JSONObject(getJsonResponse(sendRequest
                    ("GET", "devices").getInputStream()));

            if (jsonObject.isNull("data"))
                return new ArrayList<>();

            JSONArray jsonArray = (JSONArray) ((JSONObject) jsonObject.get("data")).get("devices");

            final ArrayList<Device> list = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject currentObject = (JSONObject) jsonArray.get(i);

                final String model = currentObject.getString("model");
                final String macAdress = currentObject.getString("device");

                final JSONObject deviceState = getDeviceState(model, macAdress);

                if(deviceState == null)
                    return new ArrayList<>();

                if(deviceState.isNull("properties"))
                    return new ArrayList<>();

                final JSONArray stateProperties = deviceState.getJSONArray("properties");

                Color color = Color.WHITE;
                final boolean enabled = stateProperties.getJSONObject(1).getString("powerState").equals("on");
                final int brightness = stateProperties.getJSONObject(2).getInt("brightness");

                if (!stateProperties.getJSONObject(3).isNull("color")) {
                    final JSONObject colorObject = stateProperties.getJSONObject(3).getJSONObject("color");

                    color = new Color(colorObject.getInt("r"), colorObject.getInt("g"), colorObject.getInt("b"));
                }

                final Device device = new Device(model, macAdress, currentObject.getString("deviceName"),
                        enabled, brightness, color);

                list.add(device);
            }
            return list;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean updateDevice(final Device device, final UpdateType updateType, final Object value) {
        if (apiKey == null)
            return false;
        if (device == null || updateType == null || value == null)
            return false;

        try {
            final JSONObject command = new JSONObject();
            command.put("name", updateType.getName());

            if (updateType == UpdateType.ENABLE) {
                if (!(value instanceof Boolean))
                    return false;

                final boolean operation = (boolean) value;
                command.put("value", (operation ? "on" : "off"));

                return sendUpdateDeviceRequest(device, command);
            }
            if (updateType == UpdateType.BRIGHTNESS || updateType == UpdateType.TEMPERATURE) {
                if (!(value instanceof Integer))
                    return false;

                final int operation = (int) value;

                if (updateType == UpdateType.BRIGHTNESS) {
                    if (operation < 1 || operation > 100)
                        return false;
                } else {
                    if (operation < 2000 || operation > 9000)
                        return false;
                }

                command.put("value", operation);

                return sendUpdateDeviceRequest(device, command);
            }
            if (updateType == UpdateType.COLOR) {
                if (!(value instanceof Color))
                    return false;

                final Color operation = (Color) value;

                if (operation.getRed() > 255 || operation.getGreen() > 255 || operation.getBlue() > 255)
                    return false;

                final JSONObject colorObject = new JSONObject();
                colorObject.put("r", operation.getRed());
                colorObject.put("g", operation.getGreen());
                colorObject.put("b", operation.getBlue());

                command.put("value", colorObject);

                return sendUpdateDeviceRequest(device, command);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private JSONObject getDeviceState(final String model, final String macAdress) {
        if (apiKey == null)
            return null;
        if (model == null || macAdress == null)
            return null;

        try {
            final JSONObject jsonObject = new JSONObject(getJsonResponse(
                    sendRequest("GET", "devices/state?device=" +
                            macAdress + "&model=" + model).getInputStream()));
            return (JSONObject) jsonObject.get("data");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean sendUpdateDeviceRequest(final Device device, final JSONObject command) {
        try {
            final HttpURLConnection connection = sendRequest("PUT", "devices/control");

            JSONObject requestBody = new JSONObject();
            requestBody.put("device", device.getMacAdress());
            requestBody.put("model", device.getModel());
            requestBody.put("cmd", command);

            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            final OutputStream outputStream = connection.getOutputStream();
            outputStream.write(input, 0, input.length);

            return connection.getResponseCode() == 200;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private HttpURLConnection sendRequest(final String requestType, final String endpoint) throws IOException {
        final URL url = new URL("https://developer-api.govee.com/v1/" + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestType);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Govee-API-Key", this.apiKey);
        connection.setDoOutput(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        return connection;
    }

    private String getJsonResponse(final InputStream inputStream) {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder stringBuilder = new StringBuilder();
        String output;

        try {
            while ((output = bufferedReader.readLine()) != null)
                stringBuilder.append(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public String getApiKey() {
        return apiKey;
    }

    public static GoveeLightController getInstance() {
        return instance;
    }
}
