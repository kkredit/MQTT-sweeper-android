package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Bundle;
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
        implements ScanResultFragment.OnListFragmentInteractionListener {

    private BrokerContent.BrokerItem m_broker;

    @BindView(R.id.toolbar) Toolbar m_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        setSupportActionBar(m_toolbar);

        updateBrokerId();
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
        ScanRunner runner = new ScanRunner(m_broker, SHODAN_API_KEY);
        runner.runScans();
    }

//    /* TODO: where does this go?? */
//    @Override
//    public Fragment getItem(int position) {
//        // getItem is called to instantiate the fragment for the given page.
//        // Return a PlaceholderFragment (defined as a static inner class below).
//        return ScanResultFragment.newInstance(position + 1, m_broker.id);
//    }
}
