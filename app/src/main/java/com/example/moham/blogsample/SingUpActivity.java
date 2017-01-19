package com.example.moham.blogsample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.ui.email.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by moham on 22/11/2016.
 */

public class SingUpActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mFullNmaeEditText;
    private EditText mConfirmPassword;
    private Button mSubmitButton;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        mEmailEditText= (EditText) findViewById(R.id.userEmailId);
        mPasswordEditText= (EditText) findViewById(R.id.password);
        mConfirmPassword= (EditText) findViewById(R.id.confirmPassword);
        mFullNmaeEditText= (EditText) findViewById(R.id.fullName);
        mSubmitButton= (Button) findViewById(R.id.signUpBtn);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("myBlogs");
        mAuth=FirebaseAuth.getInstance();
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("Register.....");
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 final String fullName=mFullNmaeEditText.getText().toString();
                final String email=mEmailEditText.getText().toString();
                final String password=mPasswordEditText.getText().toString();
                String confirm=mConfirmPassword.getText().toString();
                final String email_key=email.replace(".","");
                if (!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(confirm)){
                    mProgressDialog.show();
                    if(!password.equals(confirm)){
                        Toast.makeText(SingUpActivity.this, "Not match password ", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        return;
                    }
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                DatabaseReference reference=mDatabaseReference.child(email_key);
                                reference.child("fullname").setValue(fullName);
                                reference.child("email").setValue(email);
                                reference.child("password").setValue(password);
                                mProgressDialog.dismiss();
                                startActivity(new Intent(SingUpActivity.this,MainActivity.class));

                            }else{ mProgressDialog.dismiss();
                                Toast.makeText(SingUpActivity.this, "Fail in Register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {

                }
            }
        });

    }
}
