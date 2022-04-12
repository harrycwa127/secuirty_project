package com.example.guitest2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class UploadActivity extends AppCompatActivity {
    private ImageButton imageButtonLow, imageButtonHigh;
    int SELECT_PICTURE = 200;
    private String resolution, lowFileName, highFileName;
    private String name, description, price;
    private EditText nameEditText, descriptionEditText, priceEditText;
    ImageView image;
    // Get the last ImageButton's layout parameters
    RelativeLayout.LayoutParams params;
    Uri selectedImageUri;
    ArrayList<String> imageData;
    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imageData = new ArrayList<>();
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);
        Button uploadButton = findViewById(R.id.uploadButton);

        image = findViewById(R.id.imageView2);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                dbHelper.insertContact(name, description, price, lowFileName, highFileName);
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
//                imageButtonLow.setImageResource(android.R.drawable.ic_menu_gallery);
//                imageButtonHigh.setImageResource(android.R.drawable.ic_menu_gallery);
                nameEditText.setText("");
                descriptionEditText.setText("");
                priceEditText.setText("");
                uploadButton.setText("Thanks for support!");

                // upload the photo to backend
                imageData.add(price);
                imageData.add(name);
                imageData.add(description);
                imageData.add(selectedImageUri.toString());
                Route.create_image(imageData);

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
                selectedImageUri = data.getData();
                // update the preview image in the layout
                if (null != selectedImageUri) {
                    //if the image is low resolution picture
                    image.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
