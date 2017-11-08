package com.example.yourpackage;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by OWL Chile on 04-08-2017.
 */

public class Images {

    private static int WIDTH = 600;
    private static int HEIGHT = 800;

    public static File createImageFile(Context context, String name) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = name + "_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static void scaleImage(File file){
        try{
            Bitmap originalBitmap = ScalingUtilities.decodeFile(file, WIDTH, HEIGHT, ScalingUtilities.ScalingLogic.FIT);
            Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(originalBitmap, WIDTH, HEIGHT, ScalingUtilities.ScalingLogic.FIT);

            FileOutputStream outStream = new FileOutputStream(file);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 98, outStream);
            outStream.flush();
            outStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Bitmap fileToBitmap(File file){
        return ScalingUtilities.decodeFile(file, WIDTH, HEIGHT, ScalingUtilities.ScalingLogic.FIT);
    }

    public static void deleteNoRequiredImages(Context context, String[] paths){
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        for(File picture : storageDir.listFiles()){
            if(!Arrays.asList(paths).contains(picture.getAbsolutePath()))
                picture.delete();
        }
    }

}
