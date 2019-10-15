package com.example.trackone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InviteCodeActivity extends AppCompatActivity {

    String name, email, password, date, isSharing, code;
    Uri imageUri;
    TextView inviteCode;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    StorageReference storageReference;
    StorageReference imgRef;

    ProgressDialog dialog;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);


        inviteCode = findViewById(R.id.inviteCode);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users");
//        storageReference = FirebaseStorage.getInstance().getReference().getRoot().child("Users");

        storageReference = FirebaseStorage.getInstance().getReference().child("User_Images");

        Intent myIntent = getIntent();
        if (myIntent != null) {
            //Storing Email and Password
            name = myIntent.getStringExtra("name");
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
            date = myIntent.getStringExtra("date");
            isSharing = myIntent.getStringExtra("isSharing");
            code = myIntent.getStringExtra("code");
            imageUri = myIntent.getParcelableExtra("default");
        }
        inviteCode.setText(code);
    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    public void registerUser (View view) {

        dialog.setMessage("Please wait while we're setting things up for you");
        dialog.show();


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Storing user Data in Database

                            user = auth.getCurrentUser();
                            assert user != null;
                            userID = user.getUid();
                            Log.i("GOT_UID", "UID STORED");

                            CreateUser createUser = new CreateUser(name, email, password,  code, "false", date,"na", "na", "na", userID);


                            reference.child(userID).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.i("LOGIN_DONE", "LOGIN EXECUTED");

                                                //Saving Image to Firebase Storage

                                                imgRef = storageReference.child(user.getUid() + ".jpg");
                                                imgRef.putFile(imageUri)
                                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                Log.i("IMAGE_UPLOAD_DONE", "UPLOAD EXECUTED");
                                                                if (task.isSuccessful())
                                                                {
                                                                    //This gets Image Downloadable Link from storage n saves it in Database Child...Found it on StackOverFlow
                                                                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                        @Override
                                                                        public void onSuccess(Uri uri) {
                                                                            Uri downloadUrl = uri;
                                                                            reference.child(userID).child("imageUrl").setValue(downloadUrl.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful())
                                                                                    {
                                                                                        dialog.dismiss();
                                                                                        Toast.makeText(InviteCodeActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
//                                                                                        auth.signOut();
//                                                                                        finish();
                                                                                        Intent myIntent = new Intent(InviteCodeActivity.this, UserLocationMainActivity.class);
                                                                                        startActivity(myIntent);
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        dialog.dismiss();
                                                                                        Toast.makeText(InviteCodeActivity.this, "Error occured while Creating User Account,\nPlease Try again!", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                    /*This Method Doesn't Work
                                                                    String download_image_path = imgRef.getDownloadUrl().toString();
                                                                    reference.child(userID).child("imageUrl").setValue(download_image_path)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful())
                                                                                    {
                                                                                        dialog.dismiss();
                                                                                        Toast.makeText(InviteCodeActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
//                                                                                        auth.signOut();
//                                                                                        finish();
                                                                                        Intent myIntent = new Intent(InviteCodeActivity.this, UserLocationMainActivity.class);
                                                                                        startActivity(myIntent);
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        dialog.dismiss();
                                                                                        Toast.makeText(InviteCodeActivity.this, "Error occured while Creating User Account,\nPlease Try again!", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });*/
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                        else {
                            //Toast Failure Message message
                            dialog.dismiss();
                            Toast.makeText(InviteCodeActivity.this, "Unable to create USER", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
