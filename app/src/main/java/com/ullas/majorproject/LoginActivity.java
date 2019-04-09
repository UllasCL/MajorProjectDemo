package com.ullas.majorproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    static String firebasetoken;
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    public static String email;
    private DatabaseReference mDatabase;

    private ProgressDialog progressdailog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ConstraintLayout layout = findViewById(R.id.animation);
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setFillAfter(true);
        animation.setDuration(1200);
        layout.startAnimation(animation);

        Name = findViewById(R.id.etName);
        Password = findViewById(R.id.etPassword);
        Info = findViewById(R.id.tvInfo);
        Login = findViewById(R.id.btnLogin);
        TextView userRegistraion = findViewById(R.id.tvRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        progressdailog = new ProgressDialog(this);
        //FirebaseUser user = firebaseAuth.getCurrentUser();

//
//      if(user !=null)
//        {
//            finish();
//            email=firebaseAuth.getCurrentUser().getEmail();
//              startActivity( new Intent(LoginActivity.this,SelectActivity.class));
//        }

        Login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String UserName = ((EditText) findViewById(R.id.etName)).getText().toString();
                String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                //Toast.makeText(Login.this,UserName,Toast.LENGTH_LONG).show();
                // Toast.makeText(Login.this,password,Toast.LENGTH_LONG).show();
                //viewAll();
                email = UserName;
                UserName = UserName + "@gmail.com";
                if (Validatenull(UserName, password))
                    validate(UserName, password);
                else {
                    counter--;
                    Info.setText("Attempts remaining :" + String.valueOf(counter));
                    Snackbar.make(Login, "Please enter all the details", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Name.setText(null);
                    Password.setText(null);
                    if (counter == 0) {
                        Login.setVisibility(View.INVISIBLE);
                        Login.setEnabled(false);
                    }
                    //Intent intent=new Intent(Login.this,Login.class);
                }
            }
        });
        userRegistraion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validate(String username, String password) {
        progressdailog.setMessage("Wait a minute until you are verified");
        progressdailog.setCancelable(false);
        progressdailog.show();
        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressdailog.dismiss();
                    firebasetoken = FirebaseInstanceId.getInstance().getToken();
                    Log.d("FCMToken", "token " + firebasetoken);
                    mDatabase.child("FCMtokens").child(LoginActivity.num()).child("token").setValue(LoginActivity.firebasetoken);
                    Toast.makeText((LoginActivity.this), "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, SelectActivity.class));
                    Name.setText(null);
                    Password.setText(null);
                } else {
                    Toast.makeText((LoginActivity.this), "Login failed", Toast.LENGTH_SHORT).show();
                    Name.setText(null);
                    Password.setText(null);
                    counter--;
                    progressdailog.dismiss();
                    if (counter == 0)
                        Login.setEnabled(false);
                    Info.setText("Attempts remaining :" + String.valueOf(counter));
                }
            }
        });
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public boolean Validatenull(String u, String p) {
        if (u.isEmpty() || p.isEmpty())
            return false;
        else
            return true;
    }

    public static String num() {
        return email;
    }
}
