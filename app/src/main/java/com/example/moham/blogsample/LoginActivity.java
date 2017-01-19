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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by moham on 23/11/2016.
 */

public class LoginActivity extends AppCompatActivity {
   private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private TextView mSignup;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail= (EditText) findViewById(R.id.email);
        mPassword= (EditText) findViewById(R.id.pass);
        mLogin=(Button)findViewById(R.id.login);
        mSignup= (TextView) findViewById(R.id.signup_text);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("Log in .... ");
        mAuth=FirebaseAuth.getInstance();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString();
                String password=mPassword.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    mProgressDialog.show();
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()){
                             startActivity(new Intent(LoginActivity.this,MainActivity.class));
                             mProgressDialog.dismiss();
                             finish();
                         } else{
                             Toast.makeText(LoginActivity.this, "Error when login", Toast.LENGTH_SHORT).show();
                         }
                        }
                    });
                }
            }
        });


        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SingUpActivity.class));
            }
        });

    }
}
