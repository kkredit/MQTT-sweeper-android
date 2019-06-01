package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.gvsu.cis.mqtt_sweeper.InternetExposureChecker.IEC_Handler;
import edu.gvsu.cis.mqtt_sweeper.dummy.BrokerContent;

import static edu.gvsu.cis.mqtt_sweeper.ApiKeys.SHODAN_API_KEY;

public class DashboardActivity extends AppCompatActivity
        implements BrokerFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InternetExposureChecker iec = new InternetExposureChecker(
                        SHODAN_API_KEY,
                        new IEC_Handler() {
                            @Override
                            public void iecOnComplete() {
                                System.out.println("IEC Complete!");
                            }

                            @Override
                            public void iecOnError(Throwable e) {
                                System.out.println("IEC error!");
                                e.printStackTrace();
                            }

                            @Override
                            public void iecReceiveAnswer(boolean connected) {
                                System.out.println("IEC answer is " + (connected ? "TRUE" : "FALSE"));
                            }
                        });
                iec.getExposed();
            }
        });

        Intent payload = getIntent();
        if (payload.hasExtra("email")) {
            String email = payload.getStringExtra("email");
//            TextView user = (TextView)findViewById(R.id.user);
//            user.setText(email);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout) {
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
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
        finish();
    }
}
