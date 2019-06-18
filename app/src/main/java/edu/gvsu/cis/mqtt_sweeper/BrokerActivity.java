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

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;
import org.parceler.Parcels;

import java.util.Properties;

import javax.net.ssl.SSLContext;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.mqtt_sweeper.DataStores.Broker;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.Topic;
import edu.gvsu.cis.mqtt_sweeper.DataStores.TopicContent;

public class BrokerActivity extends AppCompatActivity implements TopicsFragment.OnListFragmentInteractionListener   {

    private BrokerContent.BrokerItem m_broker = null;
    MqttAndroidClient m_client;

    private final int SCAN_RESULT = 0;
    private final int NEW_TOPIC_REQUEST = 1;
    private final int UPDATE_RESULT = 2;

    @BindView(R.id.toolbar) Toolbar m_toolbar;
    @BindView(R.id.brokerName) TextView m_nameField;
    @BindView(R.id.id_field) TextView m_idField;
    @BindView(R.id.addr_field) TextView m_addrField;
    @BindView(R.id.scan_field) TextView m_scanField;
    @BindView(R.id.connectBroker) Button connButton;

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
        String brokerId = intent.getStringExtra("BrokerId");
        m_broker = BrokerContent.getBroker(brokerId);
    }

    private void updateFields() {
        m_nameField.setText(m_broker.broker.servername);
        m_idField.setText("ID: " + m_broker.broker.bid);
        m_addrField.setText("URL: " + m_broker.broker.url);
        m_scanField.setText("Scan summary: " + m_broker.scanSummary);
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

        if (connButton.getText().equals("Connect")) {
            String clientId = MqttClient.generateClientId();
            m_client = new MqttAndroidClient(this.getApplicationContext(), m_broker.broker.url, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            if (m_broker.broker.username.length() != 0 && m_broker.broker.password.length() != 0) {
                options.setUserName(m_broker.broker.username);
                options.setPassword(m_broker.broker.password.toCharArray());
            }

//            if (m_broker.broker.url.startsWith("ssl")) {
//                SSLContext sc = SSLContext.getInstance("SSL");
//                sc.init(myKeyManagerFactory.getKeyManagers(), myTrustManagerArray, new java.security.SecureRandom());
//                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//
//                Properties sslProps = new Properties();
//                sslProps.put(SSLSocketFactoryFactory.TRUSTSTORE, configuration.getTruststore());
//                sslProps.put(SSLSocketFactoryFactory.TRUSTSTOREPWD, configuration.getTruststorePassword());
//                sslProps.put(SSLSocketFactoryFactory.TRUSTSTORETYPE, "JKS");
//                sslProps.put(SSLSocketFactoryFactory.CLIENTAUTH, false);
//                options.setSSLProperties(sslProps);
//            }

            try {
                IMqttToken token = m_client.connect();
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // We are connected
                        Toast.makeText(BrokerActivity.this, "Connected", Toast.LENGTH_LONG).show();
                        connButton.setText("Disconnect");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        // Something went wrong e.g. connection timeout or firewall problems
                        Toast.makeText(BrokerActivity.this, "Connection failed", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                IMqttToken disconToken = m_client.disconnect();
                disconToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // we are now successfully disconnected
                        connButton.setText("Connect");
                        Toast.makeText(BrokerActivity.this, "Disconnected", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken,
                                          Throwable exception) {
                        // something went wrong, but probably we are disconnected anyway
                        Toast.makeText(BrokerActivity.this, "Error disconnecting", Toast.LENGTH_LONG).show();
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
            if (data != null && data.hasExtra("Topic_Item")) {
                Parcelable topicData = data.getParcelableExtra("Topic_Item");
                Topic topic = Parcels.unwrap(topicData);
//              topRef.push().setValue(topic);
                Toast.makeText(BrokerActivity.this, "Broker Added", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == UPDATE_RESULT) {
            if (data != null && data.hasExtra("Broker")) {
                Parcelable brokerData = data.getParcelableExtra("Broker");
                Broker broker = Parcels.unwrap(brokerData);

                BrokerContent.updateBroker(m_broker.id, broker);
                updateFields();
                Toast.makeText(BrokerActivity.this, "Broker updated", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.button_delete)
    void brokerDelete() {
        m_client.close();
        BrokerContent.delBroker(m_broker.id);
        finish();
    }

    @OnClick(R.id.button_edit)
    void onClickEdit() {
        Intent intent = new Intent(BrokerActivity.this, AddBrokerActivity.class);
        intent.putExtra("BrokerId", m_broker.id);
        startActivityForResult(intent, UPDATE_RESULT);
    }

    @Override
    public void onListFragmentInteraction(Topic item) {
     System.out.println("interact") ;
     Intent intent = new Intent(this, TopicViewActivity.class);
     intent.putExtra("TOPIC_NAME", item.topic);
     startActivity(intent);
    }
}
