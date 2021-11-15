package com.example.foody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.EmailUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;

import java.util.HashMap;
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
    private DatabaseReference mReference;
    boolean result = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        getView();
        mReference = FirebaseDatabase.getInstance().getReference();
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
                Boolean result = register(txtUsername.getText().toString().trim(), txtEmail.getText().toString().trim(), txtCode.getText().toString().trim(), txtPassword.getText().toString().trim(), txtRepass.getText().toString().trim());

                if (result){
                    AGConnectAuth.getInstance().signOut();
                    Intent register = new Intent(Register.this, LoginActivity.class);
                    register.putExtra("Pass", txtPassword.getText().toString().trim());
                    register.putExtra("Email",txtEmail.getText().toString().trim() );
                    startActivity(register);
                    finish();
                }
            }
        });
    }


    public Boolean register(String username, String email, String verify, String password, String rePassword) {
        Boolean Check = true;
        if (username.isEmpty()) {
            txtUsername.setError("Tên không được trống");
            Check = false;
        } else txtUsername.setError(null);
        if (email.isEmpty()) {
            txtEmail.setError("Email không được để trống");
            Check = false;
        } else txtEmail.setError(null);
        if (password.isEmpty()) {
            txtPassword.setError("Mật khẩu không được bỏ trống");
            Check = false;
        } else txtPassword.setError(null);
        if (!rePassword.equals(password)) {
            txtRepass.setError("Mật khẩu không trùng khớp");
            Check = false;
        } else txtRepass.setError(null);
        if (Check) {
            EmailUser emailUser = new EmailUser.Builder()
                    .setEmail(email)
                    .setVerifyCode(verify)
                    .setPassword(password) //optional
                    .build();

            AGConnectAuth.getInstance().createUser(emailUser)
                    .addOnSuccessListener(signInResult -> {
                        String userId = signInResult.getUser().getUid();
                        final String TAG = "Register";
                        Log.e(TAG, userId);
                        // After an account is created, the user has signed in by default.
                        saveUserToFirebase(username, email, userId);
                        result = true;
                    })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(Register.this, "Có lỗi xảy ra!" + e.getMessage(), Toast.LENGTH_LONG).show();
                            result = false;
                        }

                    });
            return result;
        } else {
            Toast.makeText(Register.this, "Có lỗi xảy ra!", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    void saveUserToFirebase(String username, String email, String userID) {
        HashMap<String, String> newuser = new HashMap<>();
        newuser.put("ID", userID);
        newuser.put("UserName", username);
        newuser.put("Picture", "default");
        newuser.put("Email", email);
        mReference.child("User").child(userID).child("Profile").setValue(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                Intent newActivity = new Intent(getApplicationContext(), MainActivity.class);
                newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(newActivity);
            }
        });
        finish();
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
                Toast.makeText(Register.this, "Check your email!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(Register.this, "Email not sent." + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}