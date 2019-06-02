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
import android.widget.Button;
import android.widget.TextView;

import edu.gvsu.cis.mqtt_sweeper.dummy.BrokerContent;

public class BrokerActivity extends AppCompatActivity {

    private BrokerContent.BrokerItem m_broker;

    TextView m_nameField, m_idField, m_addrField, m_scanField;
    Button m_scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_nameField = (TextView) findViewById(R.id.brokerName);
        m_idField = (TextView) findViewById(R.id.id_field);
        m_addrField = (TextView) findViewById(R.id.addr_field);
        m_scanField = (TextView) findViewById(R.id.scan_field);

        m_scanButton = (Button) findViewById(R.id.button_scan);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String brokerId = extras.getString("BrokerId");
        m_broker = BrokerContent.ITEM_MAP.get(brokerId);

        updateFields();
        setupButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout) {
            Intent intent = new Intent(BrokerActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    private void updateFields() {
        m_nameField.setText(m_broker.name);
        m_idField.setText("ID: " + m_broker.id);
        m_addrField.setText("URL: " + m_broker.url);
        m_scanField.setText("Scan summary: " + m_broker.scanSummary);
    }

    private void setupButtons() {
        m_scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(BrokerActivity.this, ScanActivity.class);
            intent.putExtra("BrokerId", m_broker.id);
            startActivity(intent);
        });
    }
}
