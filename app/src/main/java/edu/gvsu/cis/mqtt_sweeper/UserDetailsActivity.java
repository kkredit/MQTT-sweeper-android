package edu.gvsu.cis.mqtt_sweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @BindView(R.id.activity_login) TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        ButterKnife.bind(this);


        mAuth = FirebaseAuth.getInstance();

        displayUser();


}

    public void displayUser() {
        String user = mAuth.getCurrentUser().getEmail();
        userName.setText(user);
    }

}
