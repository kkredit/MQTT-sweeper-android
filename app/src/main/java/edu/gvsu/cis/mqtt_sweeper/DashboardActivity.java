package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.mqtt_sweeper.DataStores.Broker;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;

public class DashboardActivity extends AppCompatActivity
        implements BrokerFragment.OnListFragmentInteractionListener {

    final int NEW_BROKER_REQUEST = 1;
    @BindView(R.id.toolbar) Toolbar m_toolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        setSupportActionBar(m_toolbar);
        mAuth = FirebaseAuth.getInstance();
        BrokerContent.initDb();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.addtopic)
    void popScreen() {
        Intent newBroker = new Intent(
                DashboardActivity.this, AddBrokerActivity.class);
        startActivityForResult(newBroker, NEW_BROKER_REQUEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_accountDetails) {
            Intent intent = new Intent(this, UserDetailsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return false;
    }


    @Override
    public void onListFragmentInteraction(Broker item) {
        System.out.println("Interact!");
        Intent intent = new Intent(DashboardActivity.this, BrokerActivity.class);
        intent.putExtra("BrokerId", BrokerContent.getBrokerIdByBroker(item));
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_BROKER_REQUEST) {
            if (data != null && data.hasExtra("Broker")) {
                Parcelable brokerData = data.getParcelableExtra("Broker");
                Broker broker = Parcels.unwrap(brokerData);

                BrokerContent.addBroker(broker);
                Toast.makeText(DashboardActivity.this, "Broker Added", Toast.LENGTH_LONG).show();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

}