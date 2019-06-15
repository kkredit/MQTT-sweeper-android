package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.joda.time.DateTime;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.mqtt_sweeper.DataStores.Broker;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;

public class AddBrokerActivity extends AppCompatActivity {

    //static String MQTTHOST = "tcp://broker.hivemq.com:1883";
//    static String USERNAME = "USERNAME";                        Test Credentials
 //   static String PASSWORD = "PASSWORD";

      MqttAndroidClient client;

    @BindView(R.id.hostText) EditText mqttHost;
    @BindView(R.id.brokerName) EditText brokerName;
    @BindView(R.id.usernameText) EditText usernameText;
    @BindView(R.id.passwdText) EditText passwordText;
    @BindView(R.id.passwdVerify) EditText passwordVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_broker);
        ButterKnife.bind(this);

        connectBroker();

    }

    @OnClick(R.id.connectBtn)
    public void connectBroker() {
        String MQTTHOST = mqttHost.getText().toString();
        String USERNAME = usernameText.getText().toString();
        String PASSWORD = passwordText.getText().toString();
        String passVerify = passwordVerify.getText().toString();

        if (MQTTHOST.length() == 0 || USERNAME.length() == 0
                || PASSWORD.length() == 0 || passVerify.length() == 0) {
            Toast.makeText(AddBrokerActivity.this, "field is required",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (!PASSWORD.contains(passVerify) ) {
            Toast.makeText(AddBrokerActivity.this, "passwords do not match",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplication(),MQTTHOST,
                clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {


                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Intent result = new Intent();
                    Broker aBroker = new Broker();
                    aBroker.url =  mqttHost.getText().toString();
                    aBroker.servername = brokerName.getText().toString();
                    aBroker.username = usernameText.getText().toString();
                    aBroker.password = passwordText.getText().toString();
                    aBroker.bid = clientId;
                    Parcelable parcel = Parcels.wrap(aBroker);
                    result.putExtra("Broker",parcel);
                    setResult(RESULT_OK,result);
                    client.close();
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

}