package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.mqtt_sweeper.DataStores.Broker;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;

public class AddBrokerActivity extends AppCompatActivity {

    //static String MQTTHOST = "tcp://broker.hivemq.com:1883";
//    static String USERNAME = "USERNAME";                        Test Credentials
    //   static String PASSWORD = "PASSWORD";
    MqttAndroidClient client;
    private BrokerContent.BrokerItem m_broker = null;

    @BindView(R.id.brokerName) EditText brokerNameField;
    @BindView(R.id.brokerURL) EditText brokerUrl;
    @BindView(R.id.usernameText) EditText usernameText;
    @BindView(R.id.passwdText) EditText passwordText;
    @BindView(R.id.passwdVerify) EditText passwordVerify;
    @BindView(R.id.connectBtn) Button connectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_broker);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String brokerId = intent.getStringExtra("BrokerId");
        if (null != brokerId) {
            m_broker = BrokerContent.getBroker(brokerId);
            setFields();
        }
    }

    private void setFields() {
        if (null != m_broker) {
            brokerNameField.setText(m_broker.broker.servername);
            brokerUrl.setText(m_broker.broker.url);
            usernameText.setText(m_broker.broker.username);
            passwordText.setText(m_broker.broker.password);
            passwordVerify.setText(m_broker.broker.password);
            connectBtn.setText("Update");
        }
    }

    @OnClick(R.id.connectBtn)
    public void connectBroker() {
        String brokerName = brokerNameField.getText().toString();
        String MQTTHOST = brokerUrl.getText().toString();
        String USERNAME = usernameText.getText().toString();
        String PASSWORD = passwordText.getText().toString();
        String passVerify = passwordVerify.getText().toString();

        if (MQTTHOST.length() == 0 || brokerName.length() == 0) {
            Toast.makeText(AddBrokerActivity.this, "Name and URL fields are required",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (!PASSWORD.equals(passVerify) ) {
            Toast.makeText(AddBrokerActivity.this, "Passwords do not match",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(),MQTTHOST,
                clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        if (USERNAME.length() != 0 && PASSWORD.length() != 0) {
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
        }

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Intent result = new Intent();
                    Broker aBroker = new Broker();
                    aBroker.url = MQTTHOST;
                    aBroker.servername = brokerName;
                    aBroker.username = USERNAME;
                    aBroker.password = PASSWORD;
                    aBroker.bid = brokerName + USERNAME;
                    Parcelable parcel = Parcels.wrap(aBroker);
                    result.putExtra("Broker", parcel);
                    setResult(RESULT_OK, result);
                    disconnectBroker();
                    finish();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(AddBrokerActivity.this,"Connection failed",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnectBroker(){
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