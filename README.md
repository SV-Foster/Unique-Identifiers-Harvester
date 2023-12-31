# Unique Identifiers Harvester

Unique Identifiers Harvester is the demo application for collecting every unique identifier possible of an android-based device in all ways possible. The goal of this app is to discover all the ways exist to find unique identifiers and collect them with one application. 

Upload and save processes of the data collected with this application can be automated with a simple instructions file, this makes Unique Identifiers Harvester the best tool for inventory!

![Screenshot](Documents/screenshot%20001%20mini.png) ![Screenshot](Documents/screenshot%20002%20mini.png)


## Installation

To install the Unique Identifiers Harvester app, you need to have the following prerequisites:
* A device with Android 5.1 (API level 22) or higher
* The Unique Identifiers Harvester APK file

### Manual

1. Copy the Unique Identifiers Harvester app APK file to your device's storage, such as the internal memory or the SD card.
2. Enable the installation of apps from unknown sources on your device. You can do this by going to Settings > Security > Unknown sources and turning on the option.
3. Use a file manager app to locate and open the Unique Identifiers Harvester app APK file on your device. You will see a confirmation dialog box that asks you to install the app. Tap on Install to proceed, or Cancel to cancel.
4. Wait for the installation to finish. You will see a message that indicates whether the installation was successful or not. Tap on Open to launch the app, or Done to exit the installer.

You have successfully installed the Unique Identifiers Harvester app on your device!

### With ADB

1. On your android device, go to Settings > About phone and tap on the Build number several times until you see a message that says "You are now a developer".
2. Go back to Settings > System > Developer options and turn on the USB debugging option. You may need to confirm this action by tapping OK on a pop-up window.
3. On your computer, download and install the Android SDK Platform Tools package. This package contains the ADB tool that you will use to install apps on your device. Make sure path to adb.exe file is included into the %PATH% enviroment variable.
4. Connect your android device to your computer with a USB cable. You may need to allow USB debugging on your device by tapping OK on a pop-up window.
5. Open a command prompt or a terminal on your computer and navigate to the folder where Unique Identifiers Harvester APK is located with the `cd` command.
6. To check if your device is connected and recognized by ADB, run the following command: `adb devices`
7. You should see a list of devices attached, with a serial number and a device status. If you see your device with the status "device", it means that ADB can communicate with it. If you see your device with the status "unauthorized", it means that you need to grant permission on your device by tapping OK on a pop-up window. If you don't see your device at all, it means that ADB cannot detect it. You may need to check your USB connection, USB driver, or USB debugging option.
8. Run the installation command:
```
adb install -r Unique-Identifiers-Harvester.apk
```
You should see a message that says "Performing Streamed Install" followed by "Success" if the installation is successful. If you see an error message, it means that the installation failed. You may need to check the APK file, the device storage, or the app permissions.

You have successfully installed the Unique Identifiers Harvester app on your device!

## Usage

### Manual

To use the Unique ID Harvester app, you need to launch the app, wait a bit while it collects data, and select the task from the menu - copy collected data to clipboard, upload and save the data to a server or save it to a file.

### Unattended

1. Upload `Unattended UIDs.xml` file, containing instructions for the app, to the folder   `Android/data/SVFoster.Android.UniqueIdentifiersHarvester/files/` on the SD card of your android-based device. If there are more than one SD card, upload to any of them, app checks every SD inserted. See "Unattended mode configuration file" for more information about this file.
2. Start the app, running following command in the command line:
```
adb shell monkey -p SVFoster.Android.UniqueIdentifiersHarvester -v 1
```
(Make shure path to adb.exe file is included into the %PATH% enviroment variable)

Now, you see the progress and status of the upload and save process on the screen.

## Unattended mode configuration file

This file is an XML file that contains the settings for the unattended mode of the android app that collects IDs of the android device. The unattended mode allows the app to run in the background and collect the IDs without any user interaction. The IDs are then stored in a file or uploaded to a server, depending on the settings.

### File Structure

The file has the following structure:

```xml
<UnattendedJob DataStructureVersion="0">
  <AutoCloseApp></AutoCloseApp>
  <CopyToClipboard></CopyToClipboard>
  <FileFolderPreset>//</FileFolderPreset>
  <FileNamePreset></FileNamePreset>
  <FileNameType></FileNameType>
  <JobType></JobType>
  <SaveToFile></SaveToFile>
  <UploadToHTTP></UploadToHTTP>
  <UploadURL></UploadURL>
</UnattendedJob>
```

### File Elements

The file consists of the following elements:

* UnattendedJob: The root element that contains the attributes and subelements for the unattended mode. The DataStructureVersion attribute specifies the version of the file structure, which is currently 0.

* JobType: A subelement that specifies the type of operating mode. The value can be 1 or 2, where 1 means standard work, 2 means unattended mode. The default value is `1`.

* CopyToClipboard: A subelement that specifies whether to copy the collected IDs to the clipboard or not. The value can be true or false. The default value is `false`.

* SaveToFile: A subelement that specifies whether to save the collected IDs to a file or not. The value can be true or false. The default value is `false`.

* UploadToHTTP: A subelement that specifies whether to upload the collected IDs to a server or not. The value can be true or false. The default value is `false`.

* AutoCloseApp: A subelement that specifies whether to close the app automatically after the unattended mode is finished or not. The value can be true or false. The default value is `false`.

* FileFolderPreset: A subelement that specifies the folder where the file with the collected IDs will be saved. The value is an absolute, such as /sdcard/Android/data/SVFoster.Android.UniqueIdentifiersHarvester/files/. The default value is `/sdcard/`.

* FileNamePreset: A subelement that specifies the name of the file with the collected IDs. The value can be any valid file name, such as "UIDs harvested.xml" or "IDs.xml". The default value is `UIDs harvested.xml`.

* UploadURL: A subelement that specifies the URL of the server where the collected IDs will be uploaded. The value can be any valid URL, such as `http://demo.sv-foster.com/b/`.


### File Example

Here is an example of a config file that sets the unattended mode to collect and save the IDs in an XML file in the /sdcard/ folder and exits:

```xml
<UnattendedJob DataStructureVersion="0">
   <AutoCloseApp>true</AutoCloseApp>
   <CopyToClipboard>false</CopyToClipboard>
   <FileFolderPreset>/sdcard/Android/data/SVFoster.Android.UniqueIdentifiersHarvester/files/</FileFolderPreset>
   <FileNamePreset>UIDs harvested.xml</FileNamePreset>
   <FileNameType>1</FileNameType>
   <JobType>2</JobType>
   <SaveToFile>true</SaveToFile>
   <UploadToHTTP>true</UploadToHTTP>
   <UploadURL>http://demo.sv-foster.com/b/</UploadURL>
</UnattendedJob>
```


## Building

1. Open the project in Android Studio. You can either import an existing project from a folder or a version control system, or create a new project from scratch or a template.
2. Select the build variant that you want to use for the APK. You can choose between debug and release variants, or create your own custom variants. You can select the build variant from the Build Variants tool window or from the toolbar.
3. Build the APK by clicking on the Build menu and selecting Build Bundle(s) / APK(s) > Build APK(s). You can also use the keyboard shortcut Ctrl+F9. This will generate the APK file and save it in the app/build/outputs/apk folder.
4. Locate the APK file in the app/build/outputs/apk folder and copy it to your device or emulator. You can also use the Run menu and select Run 'app' to install and run the APK on your device or emulator directly.

You have successfully built the APK out of the source code in the Android Studio.


## Authors

This app was written and is maintained by SV Foster.


## License

This program is available under EULA, see [EULA text file](EULA.txt) for the complete text of the license. This program is free for personal, educational and/or non-profit usage.
