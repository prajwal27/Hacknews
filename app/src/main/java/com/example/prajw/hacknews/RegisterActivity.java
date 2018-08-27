package com.example.prajw.hacknews;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prajw.hacknews.ProfileActivity;
import com.example.prajw.hacknews.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText username, password, retypePassword, email;
    private Button signup, login;
    private String uid;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username_register);
        password = findViewById(R.id.password_register);
        retypePassword = findViewById(R.id.re_type_password);
        email = findViewById(R.id.email_register);
        signup = findViewById(R.id.btn_signUp);
        login = findViewById(R.id.registerToLogin);
        firebaseFirestore = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = username.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String repass= retypePassword.getText().toString();

                if(!TextUtils.isEmpty(mail)&&!TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(repass)){

                    if(pass.equals(repass)){

                        mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    uid = mAuth.getCurrentUser().getUid();
                                    //firebaseFirestore.collection(uid).document("profile");
                                    startActivity(new Intent(RegisterActivity.this,ProfileActivity.class).putExtra("start","1"));
                                    finish();
                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "error: " + error, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(RegisterActivity.this,"passwords must be equal! ",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this,"Fill all the details",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
