package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.mqtt_sweeper.DataStores.Broker;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;

public class BrokerActivity extends AppCompatActivity {

    private BrokerContent.BrokerItem m_broker = null;
    private Broker broker;
    MqttAndroidClient client;

    private final int SCAN_RESULT = 0;

    @BindView(R.id.toolbar) Toolbar m_toolbar;
    @BindView(R.id.brokerName) TextView m_nameField;
    @BindView(R.id.id_field) TextView m_idField;
    @BindView(R.id.addr_field) TextView m_addrField;
    @BindView(R.id.scan_field) TextView m_scanField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker);
        ButterKnife.bind(this);

        setSupportActionBar(m_toolbar);
        retrieveBroker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFields();
    }

    private void retrieveBroker() {
        Intent intent = getIntent();
        Parcelable brokerData = intent.getParcelableExtra("BrokerObject");
        broker = Parcels.unwrap(brokerData);
    }

    private void updateFields() {
        m_nameField.setText(broker.servername);
        m_idField.setText("ID: " + broker.bid);
        m_addrField.setText("URL: " + broker.url);
        m_scanField.setText("Scan summary: " + m_broker.scanSummary);
    }

    @OnClick(R.id.button_scan)
    void onClickScan() {
        Intent intent = new Intent(BrokerActivity.this, ScanActivity.class);
        intent.putExtra("BrokerId", m_broker.id);
        startActivityForResult(intent, SCAN_RESULT);
    }

     @OnClick(R.id.button_edit)
     void onClickPub() {
         String clientId = MqttClient.generateClientId();
         client = new MqttAndroidClient(this.getApplication(),broker.url,
                 clientId);
         MqttConnectOptions options = new MqttConnectOptions();
         options.setUserName(broker.username);
         options.setPassword(broker.password.toCharArray());

         try {
             IMqttToken token = client.connect(options);
             token.setActionCallback(new IMqttActionListener() {

                 @Override
                 public void onSuccess(IMqttToken asyncActionToken) {
                     // We are connected
                 }
                 @Override
                 public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                     // Something went wrong e.g. connection timeout or firewall problems
                 }
             });
          } catch (MqttException e) {
             e.printStackTrace();
         }
     }

     public void Publish(View view) {
        String topic = "foo/bar";
        String payload = "the payload";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);

        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.fab)
    void onClickFab(View view) {

        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
