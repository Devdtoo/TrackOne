package com.example.trackone;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button signIn;
    Button signUp;

    FirebaseAuth auth;
    FirebaseUser user;
    PermissionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null)
        {
            setContentView(R.layout.activity_main);
            manager = new PermissionManager() {};
            manager.checkAndRequestPermissions(this);
        }
        else
        {
            Intent myIntent = new Intent(MainActivity.this, UserLocationMainActivity.class);
            startActivity(myIntent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        manager.checkResult(requestCode,permissions, grantResults);

        ArrayList<String> denied_permissions = manager.getStatus().get(0).denied;

        if (denied_permissions.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Permission Enabled", Toast.LENGTH_SHORT).show();
        }

    }





    public void signInTapped(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
    public void signUpTapped(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
}
