package com.example.trackone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinCircleActivity extends AppCompatActivity {

    Pinview pinview;
    DatabaseReference databaseReference, currentReference, circleReference;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id, join_user_id;
    Button joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        current_user_id = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
        pinview = findViewById(R.id.pinview);
        joinButton = findViewById(R.id.join_btn);

    }

    public void joinButtonClicked(View view) {
        //1. check if input code is present in Database or not
        //2. if code is present, find that User, n create a node(CircleMember)

        Query query = databaseReference.orderByChild("code").equalTo(pinview.getValue());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    CreateUser createUser = null;
                    for (DataSnapshot childDss : dataSnapshot.getChildren()) {
                        createUser = childDss.getValue(CreateUser.class);
                        join_user_id = createUser.userId;

                        circleReference = databaseReference.child(join_user_id).child("CircleMembers");

                        CircleJoin circleJoin = new CircleJoin(current_user_id);
                        CircleJoin circleJoin1 = new CircleJoin(join_user_id);

                        circleReference.child(user.getUid()).setValue(circleJoin)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(JoinCircleActivity.this, "Joined Circle Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
                else {
                    Toast.makeText(JoinCircleActivity.this, "Invalid Code!!!\nDoesn't Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}




































