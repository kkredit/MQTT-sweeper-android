package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.mqtt_sweeper.dummy.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.dummy.ScanResultContent;

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
        InternetExposureChecker iec = new InternetExposureChecker(
            SHODAN_API_KEY,
            new InternetExposureChecker.IEC_Handler() {
                @Override
                public void iecOnComplete() {
                    System.out.println("IEC Complete!");
                }

                @Override
                public void iecOnError(Throwable e) {
                    System.out.println("IEC error!");
                    e.printStackTrace();
                }

                @Override
                public void iecReceiveAnswer(boolean connected) {
                    System.out.println("IEC answer is " + (connected ? "TRUE" : "FALSE"));
                }
            });
        iec.getExposed();
    }
}
