package com.example.trackone;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class RegisterActivity extends AppCompatActivity {

    EditText regEmail;
    Button next1;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regEmail = findViewById(R.id.regEmail);
        next1 = findViewById(R.id.next1);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
    }

    public void goToPasswordActivity(View view) {

        dialog.setMessage("Checking email Address");
        dialog.show();

        //Check if this Email Already Exists or Not
        auth.fetchSignInMethodsForEmail(regEmail.getText().toString())
        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Intent myIntent = new Intent(RegisterActivity.this, PasswordActivity.class);
        //Passing Email to Password Activity
                    myIntent.putExtra("email", regEmail.getText().toString());
                    startActivity(myIntent);
                    finish();

                }
                else {
                    dialog.dismiss();
                    Log.i("ERROR", task.getException().toString());
                }
            }
        });
    }
}
