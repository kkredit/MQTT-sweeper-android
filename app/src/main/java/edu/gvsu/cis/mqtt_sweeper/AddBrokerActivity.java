package edu.gvsu.cis.mqtt_sweeper;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class AddBrokerActivity extends AppCompatActivity {

//    static String MQTTHOST = "tcp://broker.hivemq.com:1883";
//    static String USERNAME = "USERNAME";
//    static String PASSWORD = "PASSWORD";
    MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_broker);

        Button connectBtn = findViewById(R.id.connectBtn);
        EditText mqttHost = findViewById(R.id.hostText);
        EditText usernameText = findViewById(R.id.usernameText);
        EditText passwordText = findViewById(R.id.passwdText);
        EditText passwordVerify = findViewById(R.id.passwdVerify);
        Context context = this.getApplicationContext();


        connectBtn.setOnClickListener(v -> {
            String MQTTHOST = mqttHost.getText().toString();
            String USERNAME = usernameText.getText().toString();
            String PASSWORD = passwordText.getText().toString();
            String passVerify = passwordVerify.getText().toString();

            if (MQTTHOST.length() == 0 || USERNAME.length() == 0
                       || PASSWORD.length()== 0 || passVerify.length() == 0 ) {
                Toast.makeText(AddBrokerActivity.this,"field is required",
                        Toast.LENGTH_LONG).show();
                return;
            }



            String clientId = MqttClient.generateClientId();
            client = new MqttAndroidClient(context,MQTTHOST,
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
                        Toast.makeText(AddBrokerActivity.this,"Connected",Toast.LENGTH_LONG).show();
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
        });

    }

}
