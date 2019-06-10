package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText email = (EditText) findViewById(R.id.email);

        EditText passwd = (EditText) findViewById(R.id.password2);
        Button signin = (Button) findViewById(R.id.signin);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        signin.setOnClickListener(v -> {
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

            mAuth.signInWithEmailAndPassword(emailStr, passStr)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Snackbar.make(email, "Login verified", Snackbar.LENGTH_LONG).show();
                            Intent toMain = new Intent(this, DashboardActivity.class);
                            toMain.putExtra("email", emailStr);
                            startActivity(toMain);
                            finish();
                        } else {
                            signin.startAnimation (shake);
                            String msg = task.getException().getMessage();
                            Snackbar.make(email,msg,
                                    Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });
        });

        Button register = (Button) findViewById(R.id.signup);
        register.setOnClickListener( v-> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity (intent);
        });
    }
      @Override
    public void onResume(){
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent toMain = new Intent(this, DashboardActivity.class);
            startActivity(toMain);
            finish();
        }

    }
}
