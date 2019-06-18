package edu.gvsu.cis.mqtt_sweeper;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.net.InetAddress;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class BackgroundNotifier extends IntentService {

    private static final String ACTION_START_BG_SERVICE = "edu.gvsu.cis.mqtt_sweeper.action.START_BG";

    public static final String BG_NOTIF_EXTRA = "NOTIFICATION_ID";

    private Integer m_notificationId = 0;

    public BackgroundNotifier() {
        super("BackgroundNotifier");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionBgService(Context context) {
        Intent intent = new Intent(context, BackgroundNotifier.class);
        intent.setAction(ACTION_START_BG_SERVICE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_BG_SERVICE.equals(action)) {
                runBgService();
            }
        }
    }

    private void runBgService() {
        while (true) {
            for (BrokerContent.BrokerItem broker : BrokerContent.ITEM_MAP.values()) {
                try {
                    InetAddress address = InetAddress.getByName(new URI(broker.broker.url).getHost());
                    if (address.isReachable(20000)) {
                        tryConnectAlertFailure(broker);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                TimeUnit.DAYS.sleep(1);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void tryConnectAlertFailure(BrokerContent.BrokerItem broker) {
        String clientId = MqttClient.generateClientId();
        MqttAndroidClient client = new MqttAndroidClient(this.getApplicationContext(),
                broker.broker.url, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        if (broker.broker.username.length() != 0 && broker.broker.password.length() != 0) {
            options.setUserName(broker.broker.username);
            options.setPassword(broker.broker.password.toCharArray());
        }

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    try {
                        client.disconnect();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    sendConnectFailNotification(broker);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void sendConnectFailNotification(BrokerContent.BrokerItem broker) {
        // prepare intent which is triggered if the
        // notification is selected

        Intent intent1 = new Intent(this, AddBrokerActivity.class);
        intent1.putExtra("BrokerId", broker.id);
        intent1.putExtra(BG_NOTIF_EXTRA, m_notificationId);
        PendingIntent fixIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Intent intent2 = new Intent(this, DashboardActivity.class);
        intent2.putExtra(BG_NOTIF_EXTRA, m_notificationId);
        PendingIntent openIntent = PendingIntent.getActivity(this, 0, intent2, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Cannot connect to broker")
                .setContentText("Connecting to your MQTT broker \"" + broker.broker.servername +
                        "\" failed.")
                .setSmallIcon(R.drawable.sweep_bw)
                .setContentIntent(openIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.sweep_bw, "Fix", fixIntent)
                .addAction(R.drawable.sweep_bw, "Open", openIntent).build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(m_notificationId, n);
        m_notificationId++;
        if (m_notificationId > 1000) {
            m_notificationId = 0;
        }
    }
}
