package com.example.login10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText EMail_,Password_;
    Button LoginBtn_;
    FirebaseAuth fAuth;

    public void createAcc(View view){
        Intent j = new Intent(MainActivity.this,Register.class);
        startActivity(j);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EMail_ = findViewById(R.id.etMailLogin);
        Password_=findViewById(R.id.etPassLogin);
        LoginBtn_=findViewById(R.id.btnLogin);
        fAuth=FirebaseAuth.getInstance();

        LoginBtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = EMail_.getText().toString().trim();
                String password=Password_.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    EMail_.setError("Email is required !");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Password_.setError("Password is required");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,LoginSuccess.class);
                            intent.putExtra("key",email);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"Error !"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });
    }
}
