package com.example.guitest2;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.biometrics.BiometricPrompt;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class ItemDisplayActivity extends AppCompatActivity {

    private String lowResolutionFileName, highResolutionFileName;
    private int id;
    private String[] temp;
    private TextView nameTextView, descriptionTextView, priceTextView;
    private ImageView imageView;
    private Button buy;
    private String imageurl;

    //db variable
    private DBHelper dbHelper = new DBHelper(this);

    //finger print
    //create a CancellationSignal variable
    private CancellationSignal cancellationSignal = null;

    // create an authenticationCallback
    private BiometricPrompt.AuthenticationCallback authenticationCallback;

    @RequiresApi(api = Build.VERSION_CODES.P)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display);

        nameTextView = findViewById(R.id.itemNameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        priceTextView = findViewById(R.id.priceTextView);
        imageView = findViewById(R.id.imageView);
        buy = findViewById(R.id.buyButton);

        Intent intent = getIntent();
        if (intent != null) {
            id = Integer.valueOf(intent.getStringExtra("id"));

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("images");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int counter = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (counter == id) {
                            nameTextView.setText(snapshot.child("title").getValue().toString());
                            descriptionTextView.setText(snapshot.child("description").getValue().toString());

                            priceTextView.setText(snapshot.child("price").getValue().toString());
                            imageurl = snapshot.child("imageurl").getValue().toString();
                            priceTextView.setText(snapshot.child("price").getValue().toString());
                        }
                        counter++;
                    }

                    Glide.with(ItemDisplayActivity.this).load(imageurl).override(50, 20).fitCenter().into(imageView);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



//            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + imageID + "");
//            final Uri[] link = new Uri[1];
//            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    link[0] = uri;
//                }
//            });
//            priceTextView.setText(link.toString());
//            GlideApp.with(this).load(link).into(imageView);

        }

        //authenticationCallback
        authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
            // here we need to implement two methods
            // onAuthenticationError and onAuthenticationSucceeded If the
            // fingerprint is not recognized by the  app it will call onAuthenticationError
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

                    if (km.isKeyguardSecure()) {
                        Intent authIntent = km.createConfirmDeviceCredentialIntent("PIN", "Please input your password");
                        startActivityForResult(authIntent, 1);
                    }
                }
                //notifyUser("Authentication Error : " + errString);
            }

            // If the fingerprint is recognized by the app
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                buySeccess();
            }
        };

        checkBiometricSupport(); // create a biometric dialog on Click of button
        buy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                // cancel process
                BiometricPrompt biometricPrompt = new BiometricPrompt
                        .Builder(getApplicationContext())
                        .setTitle("Fingerprint Authentication")
                        .setSubtitle("Please unlock before buying the picture")
                        .setDescription("Uses FP")
                        .setNegativeButton("Cancel", getMainExecutor(), new DialogInterface.OnClickListener() {
                            @Override
                            public void
                            onClick(DialogInterface dialogInterface, int i) {
                                notifyUser("Authentication Cancelled");
                            }
                        }).build();

                // start the authenticationCallback in
                // mainExecutor
                biometricPrompt.authenticate(getCancellationSignal(), getMainExecutor(), authenticationCallback);
            }
        });
    }

    // when authentication is cancelled
    private CancellationSignal getCancellationSignal() {
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(
                new CancellationSignal.OnCancelListener() {
                    @Override
                    public void onCancel() {
                        notifyUser("Authentication was Cancelled by the user");
                    }
                });
        return cancellationSignal;
    }

    // it checks whether has fingerprint permission
    @RequiresApi(Build.VERSION_CODES.M)
    private Boolean checkBiometricSupport() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (!keyguardManager.isDeviceSecure()) {
            notifyUser("Fingerprint authentication has not been enabled in settings");
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint Authentication Permission is not enabled");
            return false;
        }
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            return true;
        } else {
            return true;
        }
    }

    //toast
    private void notifyUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // call back when password is correct
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                buySeccess();
            }
        }
    }

    //if user buy the picture
    protected void buySeccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemDisplayActivity.this);
        builder.setTitle("Thanks for support");
        builder.setMessage("You have bought the HD version!");
        builder.setNeutralButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        Glide.with(ItemDisplayActivity.this).load(imageurl).into(imageView);

//        imageView.setImageResource(getResources().getIdentifier(highResolutionFileName, "drawable", getPackageName()));
        buy.setText("Thanks for support!");
    }
}
