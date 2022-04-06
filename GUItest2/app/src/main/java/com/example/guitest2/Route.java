package com.example.guitest2;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Route {
    public static void get_image_detail(int id){
        //reference to get data
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("images");
        //arrayList for store data
//        ArrayList<String> data = new ArrayList<>();

        //hashmap version for store data
        HashMap<String, String> data= new HashMap<>();

        // event listener when data in database changed
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    data.put(snapshot.getKey().toString(), snapshot.getValue().toString());
                }

                //data got the chnaged data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void create_image(String[] data){
        HashMap<String, Object> map = new HashMap<>();
        map.put("prize", 123);
        map.put("title", "abc");
        map.put("description", "des");

//        FirebaseDatabase.getInstance().getReference().child("images").child("id").setValue(map);
        FirebaseDatabase.getInstance().getReference().child("images").push().setValue(map);

        //upload image to server
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
        ref.putFile(Uri.parse("filepath"));
        // reference to add listener
//        .addOnSuccessListener(
//                new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                    @Override
//                    public void onSuccess(
//                            UploadTask.TaskSnapshot taskSnapshot)
//                    {
//
//                        // Image uploaded successfully
//                        // Dismiss dialog
//                        progressDialog.dismiss();
//                        Toast
//                                .makeText(MainActivity.this,
//                                        "Image Uploaded!!",
//                                        Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                })
//
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e)
//                    {
//
//                        // Error, Image not uploaded
//                        progressDialog.dismiss();
//                        Toast
//                                .makeText(MainActivity.this,
//                                        "Failed " + e.getMessage(),
//                                        Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                })
//                .addOnProgressListener(
//                        new OnProgressListener<UploadTask.TaskSnapshot>() {
//
//                            // Progress Listener for loading
//                            // percentage on the dialog box
//                            @Override
//                            public void onProgress(
//                                    UploadTask.TaskSnapshot taskSnapshot)
//                            {
//                                double progress
//                                        = (100.0
//                                        * taskSnapshot.getBytesTransferred()
//                                        / taskSnapshot.getTotalByteCount());
//                                progressDialog.setMessage(
//                                        "Uploaded "
//                                                + (int)progress + "%");
//                            }
//                        });

    }

    public static void buy_image(String image) throws IOException {
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + image);

        File localFile = File.createTempFile("images", "jpg");

        ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    // to-do, not necessary
    public static void create_user(){

    }

    // to-do, not necessary
    public static void login(){

    }
}
