# GoveeLightController

A Java integration to the official Govee Rest API to control your lights easily.

![Screenshot](img/govee.png)

## Supported device models
```
H6104, H6109, H6110, H6117, H6159, H7022, H6086, H6089, H6182,
H6085, H7014, H5081, H6188, H6135, H6137, H6141, H6142, H6195,
H7005, H6083, H6002, H6003, H6148, H6052, H6143, H6144, H6050,
H6199, H6054, H5001, H6050, H6154, H6143, H6144, H6072, H6121,
H611A, H5080, H6062, H614C, H615A, H615B, H7020, H7021, H614D,
H611Z, H611B, H611C, H615C, H615D, H7006, H7007, H7008, H7012,
H7013, H7050, H6051, H6056, H6061, H6058, H6073, H6076, H619A,
H619C, H618A, H618C, H6008, H6071, H6075, H614A, H614B, H614E,
H618E, H619E, H605B, H6087, H6172, H619B, H619D, H619Z, H61A0,
H7060, H610A, H6059, H7028, H6198, H6049
```

_If you can't find your device in this list this API will not work for you_

## Getting started

### How to get a Govee API Key

1. Open the Govee Home app on your smartphone
2. Navigate to the profile tab (at the bottom right)
3. Select "About us"
4. Select "Request API key"
5. Fill out the form the API key should be sent in a few minutes

_Disclaimer: By default the Govee API has a limit of 100 requests per minute._

### Implement the API

Gradle:

```gradle
repositories {
   maven { url 'https://jitpack.io' }
}

dependencies {
   implementation 'com.github.GlaubeKeinemDev:GoveeLightController:1.0-SNAPSHOT'
}
```

Maven:

```maven
<repositories>
   <repository>
     <id>jitpack.io</id>
     <url>https://jitpack.io</url>
   </repository>
</repositories>
  
<dependencies>
  <dependency>
     <groupId>com.github.GlaubeKeinemDev</groupId>
     <artifactId>GoveeLightController</artifactId>
     <version>1.0-SNAPSHOT</version>  
  </dependency>
</dependencies>
```

1. First start to create a instance of `GoveeLightController` and set your API key
2. `GoveeLightController#getDevices` will return you a list with all controlable devices (if any errors it will return a empty list)
3. With `Device#changeColor` you can change the color from a device
4. With `Device#changeBrightness` you can change the brightness from a device (1-100)
5. With `Device#enable` you can power on or power off a device (true/false)
6. With `Device#changeTemperature` you can change the color temperature from a device if supported (2000-9000)

## Examples

Visit our [Wiki](https://github.com/GlaubeKeinemDev/GoveeLightController/wiki) for all kind of coding examples


