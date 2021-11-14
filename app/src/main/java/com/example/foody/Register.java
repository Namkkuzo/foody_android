package com.example.foody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.EmailUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;

import java.util.Locale;

public class Register extends AppCompatActivity {
    Button btnReturnLogin;
    Button btnRegister;
    Button btnSendCode;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtRepass;
    EditText txtCode;
    EditText txtUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        getView();
        listenViewOnclick();
    }

    void getView() {
        btnReturnLogin = findViewById(R.id.btnReturnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        txtCode = findViewById(R.id.verifyCode);
        btnSendCode = findViewById(R.id.getCode);
        txtEmail = findViewById(R.id.txtEmailAddress);
        txtPassword = findViewById(R.id.txtPassword);
        txtUsername = findViewById(R.id.editTextUserName);
        txtRepass = findViewById(R.id.editTextRePassword);

    }

    void listenViewOnclick() {
        btnReturnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(Register.this, LoginActivity.class);
                register.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(register);
                finish();
            }
        });
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerifyCode(txtEmail.getText().toString());
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(txtUsername.getText().toString(), txtEmail.getText().toString(), txtCode.getText().toString(), txtPassword.getText().toString(), txtRepass.getText().toString());
            }
        });
    }


    public void register(String username, String email, String verify, String password, String rePassword) {
        EmailUser emailUser = new EmailUser.Builder()
                .setEmail(email)
                .setVerifyCode(verify)
                .setPassword(password) //optional
                .build();
        AGConnectAuth.getInstance().createUser(emailUser)
                .addOnSuccessListener(signInResult -> {
                    signInResult.getUser().getUid();
                    // After an account is created, the user has signed in by default.
                    saveUserToFirebase(username, email, signInResult.getUser().getUid());
                })
                .addOnFailureListener(new OnFailureListener() {
                    private static final String TAG = "Register";

                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "Error: " + e.getMessage());
                    }
                });
    }
    
    void saveUserToFirebase(String username, String email, String userID){
        
    }
    

    void getVerifyCode(String emailStr) {
        VerifyCodeSettings settings = new VerifyCodeSettings.Builder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                .sendInterval(30)
                .locale(Locale.CHINA)
                .build();
        Task<VerifyCodeResult> task = AGConnectAuth.getInstance().requestVerifyCode(emailStr, settings);
        task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
            @Override
            public void onSuccess(VerifyCodeResult verifyCodeResult) {
                // The verification code request is successful.
                Toast.makeText(Register.this, "Check your email!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e("Register", "Error: " + e.getMessage());
            }
        });
    }

}