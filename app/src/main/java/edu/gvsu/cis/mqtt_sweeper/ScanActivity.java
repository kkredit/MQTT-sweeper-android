package edu.gvsu.cis.mqtt_sweeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

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

    @BindView(R.id.toolbar)
    Toolbar m_toolbar;
    @BindView(R.id.summary_text)
    TextView m_summary;

    private BroadcastReceiver scanRunReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Test results update!");
            updateSummaryText();
            for (DataUpdateListener listener : m_listeners) {
                listener.onDataUpdate();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_listeners = new ArrayList<>();
        updateBrokerId();
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        setSupportActionBar(m_toolbar);
        updateSummaryText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter scanRunFilter = new IntentFilter(BROADCAST_SCAN_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(scanRunReceiver, scanRunFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(scanRunReceiver);
    }

    private void updateBrokerId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String brokerId = extras.getString("BrokerId");
        m_broker = BrokerContent.ITEM_MAP.get(brokerId);
    }

    public synchronized String registerDataUpdateListener(DataUpdateListener listener) {
        m_listeners.add(listener);
        return m_broker.id;
    }

    public synchronized void unregisterDataUpdateListener(DataUpdateListener listener) {
        m_listeners.remove(listener);
    }

    @Override
    public void onListFragmentInteraction(ScanResultContent.ScanResultItem item) {
        System.out.println("Interact!");

        Intent intent = new Intent(ScanActivity.this, ScanResultActivity.class);
        intent.putExtra("BrokerId", m_broker.id);
        intent.putExtra("ScanResultId", item.id);
        startActivity(intent);
    }

    @OnClick(R.id.button)
    void onClickScan() {
        m_broker.clearScanResults();
        ScannerService.startActionStartScan(getApplicationContext(), m_broker.id, SHODAN_API_KEY);
    }

    private void updateSummaryText() {
        m_summary.setText(m_broker.scanSummary);
    }
}
