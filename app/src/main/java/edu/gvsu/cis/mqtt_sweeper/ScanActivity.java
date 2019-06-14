package edu.gvsu.cis.mqtt_sweeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerService;

import static edu.gvsu.cis.mqtt_sweeper.ApiKeys.SHODAN_API_KEY;
import static edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerService.BROADCAST_SCAN_RESULT;


public class ScanActivity extends AppCompatActivity
        implements ScanResultFragment.OnListFragmentInteractionListener {

    private BrokerContent.BrokerItem m_broker;
    private List<DataUpdateListener> m_listeners;

    @BindView(R.id.toolbar) Toolbar m_toolbar;

    private BroadcastReceiver scanRunReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Bundle bundle = intent.getExtras();
//            double temp = bundle.getDouble("TEMPERATURE");
//            String summary = bundle.getString("SUMMARY");
//            String icon = bundle.getString("ICON").replaceAll("-", "_");
//            String key = bundle.getString("KEY");
//            int resID = getResources().getIdentifier(icon , "drawable", getPackageName());

            System.out.println("Test results update!");
            for (DataUpdateListener listener : m_listeners) {
                listener.onDataUpdate();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_listeners = new ArrayList<>();
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        setSupportActionBar(m_toolbar);

        updateBrokerId();

        if (savedInstanceState == null) {
            // First-time init; create fragment to embed in activity.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment newFragment = ScanResultFragment.newInstance(1, m_broker.id);
            ft.replace(R.id.scanresult_fragment, newFragment);
//            ft.add(R.id.scanresult_fragment, newFragment);
            ft.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter scanRunFilter = new IntentFilter(BROADCAST_SCAN_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(scanRunReceiver, scanRunFilter);
    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(scanRunReceiver);
    }

    private void updateBrokerId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String brokerId = extras.getString("BrokerId");
        m_broker = BrokerContent.ITEM_MAP.get(brokerId);
    }

    public synchronized void registerDataUpdateListener(DataUpdateListener listener) {
        m_listeners.add(listener);
    }

    public synchronized void unregisterDataUpdateListener(DataUpdateListener listener) {
        m_listeners.remove(listener);
    }

    @Override
    public void onListFragmentInteraction(ScanResultContent.ScanResultItem item) {
        System.out.println("Interact!");
    }

    @OnClick(R.id.button)
    void onClickScan() {
        ScannerService.startActionStartScan(getApplicationContext(), m_broker.id, SHODAN_API_KEY);
    }
}
