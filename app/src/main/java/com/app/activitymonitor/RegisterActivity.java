package com.app.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    EditText emailInput,passwordInput,usernameInput;
    Button registerButton,loginButton;
    FirebaseAuth firebaseAuth;
    SharedPreferences preferences;
    LinearLayout layoutBackground;
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = (EditText) findViewById(R.id.emailInputId);
        passwordInput = (EditText) findViewById(R.id.passwordInputId);
        registerButton = (Button) findViewById(R.id.registerButtonId);
        loginButton = (Button) findViewById(R.id.loginButtonId);
        usernameInput = findViewById(R.id.usernameInputId);
        welcomeText = findViewById(R.id.welcomeText);
        layoutBackground = findViewById(R.id.layoutBackground);

        preferences = getSharedPreferences("color", MODE_PRIVATE);
        String bgdColor = preferences.getString("background_color", getString(R.string.black));
        String txtColor = preferences.getString("text_color", getString(R.string.white));

        if (bgdColor.equals("#000000")) {
            layoutBackground.setBackgroundResource(R.drawable.gradient);
            usernameInput.setBackgroundColor(Color.parseColor("#11000000"));
            emailInput.setBackgroundColor(Color.parseColor("#11000000"));
            passwordInput.setBackgroundColor(Color.parseColor("#11000000"));
        } else {
            layoutBackground.setBackgroundColor(Color.parseColor(bgdColor));
            usernameInput.setBackgroundColor(Color.parseColor(txtColor));
            emailInput.setBackgroundColor(Color.parseColor(txtColor));
            passwordInput.setBackgroundColor(Color.parseColor(txtColor));
        }
        welcomeText.setTextColor(Color.parseColor(txtColor));
        loginButton.setTextColor(Color.parseColor(txtColor));

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
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
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    // Setting user display name
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(usernameInput.getText().toString()).build();

                                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Register", "User profile updated.");
                                            }
                                        }
                                    });

                                    // Moving user to main activity
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


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });


        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }
}
