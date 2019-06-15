package edu.gvsu.cis.mqtt_sweeper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;

public class ScanResultActivity extends AppCompatActivity {

    private BrokerContent.BrokerItem m_broker;
    private ScanResultContent.ScanResultItem m_result;

    @BindView(R.id.toolbar) Toolbar m_toolbar;
    @BindView(R.id.name) TextView m_name;
    @BindView(R.id.description) TextView m_description;
    @BindView(R.id.severity_logo) ImageView m_severityLogo;
    @BindView(R.id.your_result) TextView m_yourResult;
    @BindView(R.id.more_information) TextView m_moreInformation;
    @BindView(R.id.button_link) Button m_linkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        ButterKnife.bind(this);
        setSupportActionBar(m_toolbar);

        getIntentArgs();
        updateFields();
    }

    private void getIntentArgs() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String brokerId = extras.getString("BrokerId");
        m_broker = BrokerContent.ITEM_MAP.get(brokerId);

        String scanResultId = extras.getString("ScanResultId");
        List<ScanResultContent.ScanResultItem> results = m_broker.getScanResults();
        m_result = null;
        for (ScanResultContent.ScanResultItem result : results) {
            if (result.id.equals(scanResultId)) {
                m_result = result;
            }
        }
    }

    private void updateFields() {
        if (null != m_result) {
            m_name.setText(m_result.name);
            m_description.setText(m_result.details);
            int image = MyScanResultRecyclerViewAdapter.getImageFromSeverity(m_result.result, m_result.severity);
            m_severityLogo.setImageResource(image);
            m_yourResult.setText(m_result.resultDetails);
            m_moreInformation.setText(m_result.moreInfo);
            m_linkButton.setText(m_result.linkText);
        }
    }

    @OnClick(R.id.button_link)
    void onClickLinkButton() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Uri.decode(m_result.linkUri)));
        startActivity(browserIntent);
    }
}
