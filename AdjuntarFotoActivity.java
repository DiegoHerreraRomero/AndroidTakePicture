package com.example.yourpackage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.yourpackage.R;
import com.example.yourpackage.Images;

public class AdjuntarFotoActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    private File previousImage;
    private File actualImage;
    private File newImage;
    private ImageButton btnTakeImage;
    private TextView tvTakeImage;
    private String prefix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjuntar_foto);


        getSupportActionBar().setTitle(getResources().getString(R.string.adjuntar_foto));

        btnTakeImage = (ImageButton) findViewById(R.id.btnTakeImage);
        tvTakeImage = (TextView) findViewById(R.id.tvTakeImage);

        prefix = getIntent().getStringExtra("prefix");
        String path = getIntent().getStringExtra("path");
        if(path != null && !path.equals("")){
            previousImage = new File(path);
            btnTakeImage.setImageBitmap(Images.fileToBitmap(previousImage));
            tvTakeImage.setVisibility(View.GONE);
        }

        btnTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage();
            }
        });
    }

    public void takeImage(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                newImage = Images.createImageFile(this, prefix);
            } catch (Exception ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (newImage != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.yourpackage.fileprovider",
                        newImage);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void goBack(View v){
        if(actualImage != null)
            actualImage.delete();
        setResult(RESULT_CANCELED);
        finish();
    }

    public void saveImage(View v){
        if(actualImage == null){
            actualImage = previousImage;
        }else if(previousImage != null &&
                !previousImage.getAbsolutePath().equals(actualImage.getAbsolutePath())){
            previousImage.delete();
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("path",actualImage.getAbsolutePath());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_TAKE_PHOTO:
                    actualImage = newImage;
                    Images.scaleImage(actualImage);
                    btnTakeImage.setImageBitmap(Images.fileToBitmap(actualImage));
                    tvTakeImage.setVisibility(View.GONE);
                    break;
            }
        }else{
            newImage.delete();
        }
    }
}
