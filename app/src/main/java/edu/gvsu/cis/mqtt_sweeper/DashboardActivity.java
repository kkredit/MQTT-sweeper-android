package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;

public class DashboardActivity extends AppCompatActivity
        implements BrokerFragment.OnListFragmentInteractionListener {

    @BindView(R.id.toolbar) Toolbar m_toolbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);


        setSupportActionBar(m_toolbar);
        mAuth = FirebaseAuth.getInstance();
//        popScreen();

    }

    @OnClick (R.id.fab)
    void popScreen(){
        startActivity(new Intent(DashboardActivity.this, AddBrokerActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if(item.getItemId() ==R.id.action_accountDetails ) {
            Intent intent = new Intent(this, UserDetailsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return false;
    }

    @Override
    public void onListFragmentInteraction(BrokerContent.BrokerItem item) {
        System.out.println("Interact!");

        Intent intent = new Intent(DashboardActivity.this, BrokerActivity.class);
        intent.putExtra("BrokerId", item.id);
        startActivity(intent);
    }

}
