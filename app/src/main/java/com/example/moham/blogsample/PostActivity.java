package com.example.moham.blogsample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST=1;
    private ImageButton mSeclectImage;
    private EditText mPostTitle;
    private EditText mPostDescription;
    private Button mSubmit;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog mProgressDialog;
    private Uri uri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mSeclectImage= (ImageButton) findViewById(R.id.select_image);
        mPostDescription= (EditText) findViewById(R.id.post_description);
        mPostTitle= (EditText) findViewById(R.id.post_title);
        mSubmit= (Button) findViewById(R.id.submit);
        mStorageReference= FirebaseStorage.getInstance().getReference();
        mDatabaseReference=FirebaseDatabase.getInstance().getReference().child("blogs");
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("uploading .............");

        mSeclectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);

                intent.setType("image/*");

                startActivityForResult(intent,GALLERY_REQUEST);
            }
        });


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
                startPost();
            }
        });
    }


    private void startPost(){
      final String title_val=mPostTitle.getText().toString().trim();
      final String description_val=mPostDescription.getText().toString().trim();
      if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(description_val) && uri!=null){
          StorageReference reference=mStorageReference.child("blogs_images").child(uri.getLastPathSegment());
          reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
              @Override
              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                  String  url=String.valueOf(taskSnapshot.getDownloadUrl());
                  DatabaseReference newpost=mDatabaseReference.push();
                  newpost.child("title").setValue(title_val);
                  newpost.child("desc").setValue(description_val);
                  newpost.child("image").setValue(url);
                  mProgressDialog.dismiss();
                  Toast.makeText(PostActivity.this, "Upload is done" + ("\ud83d\ude01"),Toast.LENGTH_LONG).show();

                  startActivity(new Intent(PostActivity.this,MainActivity.class));
                  finish();
              }
          });
      }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
             uri=data.getData();
            mSeclectImage.setImageURI(uri);
        }
    }
}
