package com.example.guitest2;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Route {
    public static void get_image_detail(String id){
        //reference to get data
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("images").child(id);
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
                    data.put(snapshot.getKey(), snapshot.getValue().toString());
                }

                //data got the chnaged data
                //to-do load low resolution image
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        });
    }

    public static void get_image_all(){
        //reference to get data
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("images");
        //arrayList for store data
//        ArrayList<String> data = new ArrayList<>();

        //hashmap version for store data
        HashMap<String, Object> data= new HashMap<>();

        // event listener when data in database changed
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    // change reference to the record in Firebase
                    ArrayList<String> list = new ArrayList<>();
                    String key = snapshot.getKey();
                    snapshot.child(key);

                    //loop the value in the child and add to array
                    for(DataSnapshot dsp : dataSnapshot.getChildren()){
                        list.add(dsp.getValue(String.class));
                    }
                    data.put(key, list);
                }

                //data got the chnaged data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        });
    }

    public static int create_image(ArrayList<String> data){
        HashMap<String, Object> map = new HashMap<>();
        map.put("prize", Integer.parseInt(data.get(0)));
        map.put("title", data.get(1));
        map.put("description", data.get(2));

        //upload image name to firebase
        String image_name = UUID.randomUUID().toString();
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + image_name);
        if(data.get(3) == null) return 1;   //1 represent something get error

        map.put("image_name", image_name);
        ref.putFile(Uri.parse(data.get(3)));

        // reduce resolution and upload to firebase sample
//        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), data.get(3));
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
//        byte[] image_data = baos.toByteArray();
//        //uploading the image
//        UploadTask uploadTask2 = ref.putBytes(image_data);
//        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                Toast.makeText(Profilepic.this, "Upload successful", Toast.LENGTH_LONG).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
////                Toast.makeText(Profilepic.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
//            }
//        });

        FirebaseDatabase.getInstance().getReference().child("images").push().setValue(map);// reference to add listener

        // upload image and notify success
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
        return 0;
    }

    public static void buy_image(String image) throws IOException {
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + image);

        File localFile = File.createTempFile(image, "jpg");

        ref.getFile(localFile);

//        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Local temp file has been created
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
    }

    // to-do, not necessary
    public static void create_user(){

    }
}
