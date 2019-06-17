package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import edu.gvsu.cis.mqtt_sweeper.DataStores.Topic;

public class BrokerActivity extends AppCompatActivity {

    final int NEW_TOPIC_REQUEST = 1;
    private BrokerContent.BrokerItem m_broker = null;
    MqttAndroidClient client;
    private final int SCAN_RESULT = 0;

    @BindView(R.id.toolbar) Toolbar m_toolbar;
    @BindView(R.id.brokerName) TextView m_nameField;
    @BindView(R.id.id_field) TextView m_idField;
    @BindView(R.id.addr_field) TextView m_addrField;
    @BindView(R.id.scan_field) TextView m_scanField;
    @BindView(R.id.connectBroker) Button connButton;
    Broker  broker;
    private FirebaseAuth mAuth;
    DatabaseReference topRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker);
        ButterKnife.bind(this);

        setSupportActionBar(m_toolbar);

        retrieveBroker();
        updateFields();
        connectBroker();
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
//      m_scanField.setText("Scan summary: " + m_broker.scanSummary);
    }

    @OnClick(R.id.button_scan)
    void onClickScan() {
        Intent intent = new Intent(BrokerActivity.this, ScanActivity.class);
        intent.putExtra("BrokerId", m_broker.id);
        startActivityForResult(intent, SCAN_RESULT);
    }

     @OnClick(R.id.addtopic)
    void onClickFab(View view) {
         Intent newBroker = new Intent(
                 BrokerActivity.this, PublishTopic.class);
         startActivityForResult(newBroker, NEW_TOPIC_REQUEST);
    }

    @OnClick(R.id.connectBroker)
    public void connectBroker(){
        String clientId = MqttClient.generateClientId();
        MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(),broker.url,
                        clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(broker.username);
        options.setPassword(broker.password.toCharArray());

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(BrokerActivity.this,"Connected",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(BrokerActivity.this,"Connection failed",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        if(client.isConnected()){
            connButton.setText("Disconnect");
            try {
                IMqttToken disconToken = client.disconnect();
                disconToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // we are now successfully disconnected
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken,
                                          Throwable exception) {
                        // something went wrong, but probably we are disconnected anyway
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_TOPIC_REQUEST) {
            if (data != null && data.hasExtra("Broker")) {
                Parcelable topicData = data.getParcelableExtra("Topic");
                Topic topic = Parcels.unwrap(topicData);
                String newTopic = topic.topic;
                String payload = topic.message;
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(true);
                    client.publish(newTopic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }

                Toast.makeText(BrokerActivity.this, "Broker Added", Toast.LENGTH_LONG).show();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

     @OnClick(R.id.button_delete)
      void brokerDelete(){
         FirebaseDatabase dbRef = FirebaseDatabase.getInstance();
         FirebaseUser mUser = mAuth.getCurrentUser();
         mAuth = FirebaseAuth.getInstance();
         String uid = mUser.getUid();
         topRef = dbRef.getReference(uid);
     }
}



