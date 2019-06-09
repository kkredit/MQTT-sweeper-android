package edu.gvsu.cis.mqtt_sweeper;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScanRunner;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;

import static edu.gvsu.cis.mqtt_sweeper.ApiKeys.SHODAN_API_KEY;

public class ScanActivity extends AppCompatActivity
        implements ScanResultFragment.OnListFragmentInteractionListener, ScanRunner.ScanReportUpdater {

    private BrokerContent.BrokerItem m_broker;
    private ScanRunner m_runner;

    @BindView(R.id.toolbar) Toolbar m_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        setSupportActionBar(m_toolbar);

        updateBrokerId();

        m_runner = new ScanRunner(this, m_broker, SHODAN_API_KEY);

        if (savedInstanceState == null) {
            // First-time init; create fragment to embed in activity.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment newFragment = ScanResultFragment.newInstance(1, m_broker.id);
            ft.add(R.id.scanresult_fragment, newFragment);
            ft.commit();
        }
    }

    private void updateBrokerId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String brokerId = extras.getString("BrokerId");
        m_broker = BrokerContent.ITEM_MAP.get(brokerId);
    }

    @Override
    public void onListFragmentInteraction(ScanResultContent.ScanResultItem item) {
        System.out.println("Interact!");
    }

    @OnClick(R.id.button)
    void onClickScan()  {
        m_runner.runScans();
    }

    @Override
    public void scanReportHasUpdate() {
        System.out.println("Test results update!");
    }
}
