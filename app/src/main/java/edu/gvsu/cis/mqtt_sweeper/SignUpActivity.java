package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    private FirebaseAuth mAuth;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        EditText passwd = (EditText) findViewById(R.id.password);
        EditText verifyPasswd = (EditText) findViewById(R.id.password2);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                String emailStr = email.getText().toString();
                if (emailStr.length() == 0) {
                    Snackbar.make(email, R.string.email_required,
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!EMAIL_REGEX.matcher(emailStr).find()) {
                    Snackbar.make(email, R.string.incorrect_email,
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                String passStr = passwd.getText().toString();
                String verifyPassStr = verifyPasswd.getText().toString();
                if (!verifyPassStr.equals(passStr)) {
                    verifyPasswd.startAnimation(shake);
                    passwd.startAnimation(shake);
                    return;
                }

                Snackbar.make(email, "Login verified",
                        Snackbar.LENGTH_LONG).show();
                mAuth.createUserWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(SignUpActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
                        intent.putExtra("email",emailStr);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                    } else {
                        String msg = task.getException().getMessage();
                        Snackbar.make(email, msg, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
