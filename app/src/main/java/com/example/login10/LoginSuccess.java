package com.example.login10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class LoginSuccess extends AppCompatActivity {

    ImageView image;
    TextView t2;
    FirebaseStorage storage = FirebaseStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        Intent i = getIntent();
        final String id_ = i.getStringExtra("key");
        StorageReference reference = storage.getReferenceFromUrl("gs://login-10-145ef.appspot.com/Images").child(id_+".jpg");
        image=(ImageView)findViewById(R.id.image1);
        t2=(TextView)findViewById(R.id.textView4);
        try {
            final File file=File.createTempFile("image","jpg");
            reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                    String wel="Welcome "+id_;
                    t2.setText(wel);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginSuccess.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (IOException e){

            e.printStackTrace();
        }

    }
}
