package com.example.guitest2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;


public class UploadActivity extends AppCompatActivity {
    private ImageButton imageButtonLow, imageButtonHigh;
    int SELECT_PICTURE = 200;
    private String resolution,lowFileName, highFileName;
    private String name, description , price;
    private EditText nameEditText, descriptionEditText, priceEditText;
    // Get the last ImageButton's layout parameters
    RelativeLayout.LayoutParams params;

    DBHelper dbHelper = new DBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imageButtonLow = findViewById(R.id.imageButtonLow);
        imageButtonHigh = findViewById(R.id.imageButtonHight);
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);
        Button uploadButton = findViewById(R.id.uploadButton);

        imageButtonLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolution = "low";
                imageChooser();
            }
        });

        imageButtonHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolution = "high";
                imageChooser();
            }
        });

        //upload button
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                price = priceEditText.getText().toString();
                dbHelper.insertContact(name, description,price,lowFileName,highFileName);
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
                builder.setTitle("Uploaded");
                builder.setMessage("You have uploaded a new item!");
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                //set to default value
                imageButtonLow.setImageResource(android.R.drawable.ic_menu_gallery);
                imageButtonHigh.setImageResource(android.R.drawable.ic_menu_gallery);
                nameEditText.setText("");
                descriptionEditText.setText("");
                priceEditText.setText("");
                uploadButton.setText("Thanks for support!");
            }
        });
    }

    //choose image
    void imageChooser() {
        // create an instance of the intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it  with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                // update the preview image in the layout
                if (null != selectedImageUri) {
                    //if the image is low resolution picture
                    if(resolution == "low"){
                        lowFileName = selectedImageUri.getLastPathSegment();
                        lowFileName = lowFileName.substring(lowFileName.lastIndexOf("/") + 1);
                        lowFileName = lowFileName.substring(0, lowFileName.lastIndexOf('.'));
                        imageButtonLow.setImageURI(selectedImageUri);
                    }
                    else{ //if the image is high resolution picture
                        highFileName = selectedImageUri.getLastPathSegment();
                        highFileName = highFileName.substring(highFileName.lastIndexOf("/") + 1);
                        highFileName = highFileName.substring(0, highFileName.lastIndexOf('.'));
                        imageButtonHigh.setImageURI(selectedImageUri);
                    }
                }
            }
        }
    }
}
