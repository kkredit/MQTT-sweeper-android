package edu.gvsu.cis.mqtt_sweeper.Scanner;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.DataUpdateListener;

//import static edu.gvsu.cis.mqtt_sweeper.ApiKeys.SHODAN_API_KEY;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class ScannerService extends IntentService
        implements ScanRunner.ScanReportUpdater {

    public static final String BROADCAST_SCAN_RESULT = "edu.gvsu.cis.mqtt_sweeper.Scanner.action.BROADCAST";

    private static final String ACTION_START_SCAN = "edu.gvsu.cis.mqtt_sweeper.Scanner.action.START";
    private static final String ACTION_CANCEL_SCAN = "edu.gvsu.cis.mqtt_sweeper.Scanner.action.CANCEL";

    // TODO: Rename parameters
    private static final String EXTRA_BROKER_ID = "edu.gvsu.cis.mqtt_sweeper.Scanner.extra.BROKER_ID";
    private static final String EXTRA_SHODAN_KEY = "edu.gvsu.cis.mqtt_sweeper.Scanner.extra.SHODAN_KEY";

    private ScanRunner m_runner;

    public ScannerService() {
        super("ScannerService");

//        m_runner = new ScanRunner(this, getApplicationContext(), m_broker, SHODAN_API_KEY);
    }

    public static void startActionStartScan(Context context, String brokerId, String shodanKey) {
        Intent intent = new Intent(context, ScannerService.class);
        intent.setAction(ACTION_START_SCAN);
        intent.putExtra(EXTRA_BROKER_ID, brokerId);
        intent.putExtra(EXTRA_SHODAN_KEY, shodanKey);
        context.startService(intent);
    }

    public static void startActionCancelScan(Context context, String brokerId) {
        Intent intent = new Intent(context, ScannerService.class);
        intent.setAction(ACTION_CANCEL_SCAN);
        intent.putExtra(EXTRA_BROKER_ID, brokerId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_SCAN.equals(action)) {
                final String brokerId = intent.getStringExtra(EXTRA_BROKER_ID);
                final String shodanKey = intent.getStringExtra(EXTRA_SHODAN_KEY);
                handleActionStartScan(brokerId, shodanKey);
            } else if (ACTION_CANCEL_SCAN.equals(action)) {
                final String brokerId = intent.getStringExtra(EXTRA_BROKER_ID);
                handleActionCancelScan(brokerId);
            }
        }
    }

    private void handleActionStartScan(String brokerId, String shodanKey) {
        BrokerContent.BrokerItem broker = BrokerContent.ITEM_MAP.get(brokerId);
        m_runner = new ScanRunner(this, getApplicationContext(), broker, shodanKey);
        m_runner.runScans();
    }

    private void handleActionCancelScan(String brokerId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void scanReportHasUpdate() {
        Intent result = new Intent(BROADCAST_SCAN_RESULT);

//        result.putExtra("SUMMARY", condition);
//        result.putExtra("TEMPERATURE", temp);
//        result.putExtra("ICON", icon);
//
//        result.putExtra("KEY", key);
        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }
}
