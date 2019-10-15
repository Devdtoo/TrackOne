package com.example.trackone;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    String email;
    EditText regPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        regPass = findViewById(R.id.regPass);

        //Getting Email from Register Activity
        Intent myIntent = getIntent();
        if (myIntent != null) {
            //Storing Email
            email = myIntent.getStringExtra("email");

        }
    }

    public void gotoNameActivity(View view) {

        if (regPass.getText().toString().length() > 7) {

            Intent myIntent = new Intent(PasswordActivity.this, NameActivity.class);
            myIntent.putExtra("email", email);
            myIntent.putExtra("password", regPass.getText().toString());
            startActivity(myIntent);
            finish();
        }
        else {
            Toast.makeText(this, "Password Length should be more then 7", Toast.LENGTH_SHORT).show();
        }

    }
}
