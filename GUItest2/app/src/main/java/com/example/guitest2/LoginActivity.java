package com.example.guitest2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText ETLoginEmail;
    EditText ETLoginPassword;
    TextView TVRegisterHere;
    Button BTNLogin;
    FirebaseAuth mAuth;
    int wrongPasswordTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ETLoginEmail = findViewById(R.id.ETLoginEmail);
        ETLoginPassword = findViewById(R.id.ETLoginPassword);
        TVRegisterHere = findViewById(R.id.TVRegisterHere);
        BTNLogin = findViewById(R.id.BTNLogin);

        mAuth = FirebaseAuth.getInstance();

        BTNLogin.setOnClickListener(view -> {
            loginUser();
        });

    }

    private void loginUser(){
        String email = ETLoginEmail.getText().toString();
        String password = ETLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            ETLoginEmail.setError("Email cannot empty");
            ETLoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            ETLoginPassword.setError("Password cannot empty");
            ETLoginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.i("mAuth task", task.toString());
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "login successfully", Toast.LENGTH_SHORT).show();
                        //let password to empty if login success
                        ETLoginPassword.setText("");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{

                        Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                        wrongPasswordTime++;
                        if (wrongPasswordTime > 5){
                            wrongPasswordTime = 0;
                            ETLoginPassword.postDelayed(new Runnable() {
                                public void run() {
                                    BTNLogin.setTextColor(Color.rgb(250,0,0));
                                    BTNLogin.setText("Wrong email or password more than 5 times \nPlease wait for 30 second");
                                    ETLoginPassword.setVisibility(View.INVISIBLE);
                                }
                            }, 0);
                            ETLoginPassword.postDelayed(new Runnable() {
                                public void run() {
                                    BTNLogin.setTextColor(Color.rgb(0,0,0));
                                    BTNLogin.setText("LOGIN");
                                    ETLoginPassword.setVisibility(View.VISIBLE);
                                }
                            }, 30000);
                        }
                    }
                }
            });
        }
    }

}