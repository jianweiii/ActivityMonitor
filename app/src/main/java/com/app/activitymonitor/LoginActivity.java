package com.app.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail,loginPassword;
    TextView welcomeText;
    Button loginButton,registerButton;
    FirebaseAuth firebaseAuth;

    LinearLayout layoutBackground;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.loginEmailInputId);
        loginPassword = findViewById(R.id.loginPasswordInputId);
        loginButton = findViewById(R.id.loginLoginButtonId);
        registerButton = findViewById(R.id.loginRegisterButtonId);
        layoutBackground = findViewById(R.id.layoutBackground);
        welcomeText = findViewById(R.id.welcomeText);

        preferences = getSharedPreferences("color", MODE_PRIVATE);
        String bgdColor = preferences.getString("background_color", getString(R.string.black));
        String txtColor = preferences.getString("text_color", getString(R.string.white));

        if (bgdColor.equals("#000000")) {
            layoutBackground.setBackgroundResource(R.drawable.gradient);
            loginEmail.setBackgroundColor(Color.parseColor("#11000000"));
            loginPassword.setBackgroundColor(Color.parseColor("#11000000"));
        } else {
            layoutBackground.setBackgroundColor(Color.parseColor(bgdColor));

            loginEmail.setBackgroundColor(Color.parseColor(txtColor));
            loginPassword.setBackgroundColor(Color.parseColor(txtColor));
        }
        welcomeText.setTextColor(Color.parseColor(txtColor));
        registerButton.setTextColor(Color.parseColor(txtColor));

        firebaseAuth = FirebaseAuth.getInstance();

        // Login in main page
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                }

                if(password.length()<6){
                    Toast.makeText(getApplicationContext(),"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                }

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"E-mail or password is wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Redirect back to registration page
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                finish();
            }
        });

        // If already login, direct straight to main page
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }
}
