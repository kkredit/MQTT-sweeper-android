package edu.gvsu.cis.mqtt_sweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserDetailsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        TextView userName = findViewById(R.id.userName);

        mAuth = FirebaseAuth.getInstance();

          displayUser(userName);


}
    public void displayUser(View view) {
       String user = mAuth.getCurrentUser().getEmail();
        view.setTex

    }

}
