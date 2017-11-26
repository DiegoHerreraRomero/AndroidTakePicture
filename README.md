# AndroidTakePicture

Example for take a picture in android with an Activity.

## Modify AndroidManifest.xml file

Add the lines below on your AndroidManifest.xml file 

```
<manifest ...>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
                     android:maxSdkVersion="18" />
    <uses-feature android:name="android.hardware.camera"
                  android:required="true" />
  <application...>
        <activity android:name=".AdjuntarFotoActivity"
                  android:configChanges="orientation|keyboardHidden|screenSize" //Necessary for mobiles that rotate the image result (like samsungs)
                  android:screenOrientation="portrait">  
        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="com.example.yourpackage.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths"></meta-data>
        </provider>     
  </application>
</manifest>
```

## Create file_paths.xml file in resources

The file should be created on res/xml resource folder, if you don't have the folder, you can create it in "res->secondary click->new->android resource directory->select xml"

```
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="my_images" path="Android/data/com.example.yourpackage/files/Pictures" />
</paths>
```

## Copy files

Copy the activities AdjuntarFotoActivity.java and Images.java on the same package and the activity_adjuntar_foto.xml to "res->layout".

**__Don't forget replace "com.example.yourpackage" with your info, inspect the files.__**

## How to use?

It's too easy, just call the Activity AdjuntarFotoActivity.class in an intent. Example:

```
Intent i = new Intent(this, AdjuntarFotoActivity.class);
i.putExtra("prefix", prefixName);
i.putExtra("path", relativePath);
startActivityForResult(i, REQUEST_INTENT_IDENTIFIER);
```

**Definitions:**
> prefixName : String to include in the image name.

> relativePath : String of actual relative path, if you have an assigned image this is the relative path of this image.

> REQUEST_INTENT_IDENTIFIER : int that identify the result of the Activity.

On the result you get the new (or actual if you decide don't replace it) path of image captured through data.getStringExtra("path"). Example:

```
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(resultCode == Activity.RESULT_OK){
        switch (requestCode){
            case REQUEST_INTENT_IDENTIFIER:
                relativePath = data.getStringExtra("path");
                break;
        }
    }
}
```
And it's done!.


