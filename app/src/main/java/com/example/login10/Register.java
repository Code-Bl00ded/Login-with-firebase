package com.example.login10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    EditText FullName_,EMail_,Password_,MobileNumber_,ConfirmPassword;
    Button SubmitBtn_;
    FirebaseAuth fAuth;
    Members members;
    DatabaseReference reff;
    long maxid=0;

    public void login(View view){
        Intent intent = new Intent(Register.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FullName_ = findViewById(R.id.etFname);
        EMail_ = findViewById(R.id.etMail);
        Password_=findViewById(R.id.etPass);
        MobileNumber_=findViewById(R.id.etMoNo);
        SubmitBtn_=findViewById(R.id.btnSubmit);
        ConfirmPassword = findViewById(R.id.etPass2);
        fAuth=FirebaseAuth.getInstance();

        members = new Members();
        reff = FirebaseDatabase.getInstance().getReference().child("Members");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxid = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }*/

        SubmitBtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = EMail_.getText().toString().trim();
                String password=Password_.getText().toString().trim();
                final String fullName=FullName_.getText().toString().trim();
                String confirmPassword=ConfirmPassword.getText().toString().trim();
                final String mobile= MobileNumber_.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    EMail_.setError("Email is required !");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Password_.setError("Password is required");
                    return;
                }
                if(Password_.length()<8){
                    Password_.setError("Must be at least 8 charector long");
                    return;
                }
                if(!(password.equals(confirmPassword))){
                    ConfirmPassword.setError("Password Do Not Match!");
                    return;
                }
                if (mobile.length()!=10){
                    MobileNumber_.setError("Invalid Mobile Number!");
                    return;
                }
                if (TextUtils.isEmpty(fullName)){
                    FullName_.setError("Please Enter Your Name");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            members.setFullName(fullName);
                            members.setEmail(email);
                            members.setMobile(mobile);
                            reff.child(String.valueOf(maxid + 1)).setValue(members);
                            Toast.makeText(Register.this,"User created Successfully",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(Register.this,ProfilePic.class);
                            i.putExtra("key",email);
                            startActivity(i);
                        }else{
                            Toast.makeText(Register.this,"Error !"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        });
    }
}
