package edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests;

import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.R;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest;

import java.net.URI;
import java.net.URISyntaxException;

public class TestUsingEncryption extends ScannerTest {

    private static final ScanResultContent.ScanResultItem description = new ScanResultContent.ScanResultItem(
            "Using encryption",
            "Tests whether the connection uses encrypted protocols.",
            ScanResultContent.Severity.MODERATE
    );

    @Override
    public ScanResultContent.ScanResultItem getDescription() {
        return description;
    }

    protected void doTest() {
        ScanResultContent.Result result = ScanResultContent.Result.CONDITION_NOT_PRESENT;
        String details = m_strings.getString(R.string.error_details_using_encryption);

        try {
            URI vURI = new URI(m_broker.url);
            if (vURI.getScheme().equals("ws")){
                result = ScanResultContent.Result.CONDITION_PRESENT;
                details = m_strings.getString(R.string.error_details_not_using_encryption);
            }
            else if (vURI.getScheme().equals("wss")) {
                /* defaults are correct */
            }
            else if (!vURI.getPath().equals("")) {
                details = m_strings.getString(R.string.error_details_invalid_config);
            }
            else if (vURI.getScheme().equals("tcp")) {
                result = ScanResultContent.Result.CONDITION_PRESENT;
                details = m_strings.getString(R.string.error_details_not_using_encryption);
            }
            else if (vURI.getScheme().equals("ssl")) {
                /* defaults are correct */
            }
            else if (vURI.getScheme().equals("local")) {
                details = m_strings.getString(R.string.error_details_using_local_conn);
            }
            else {
                details = m_strings.getString(R.string.error_details_invalid_config);
            }
        } catch (URISyntaxException ex) {
            details = m_strings.getString(R.string.error_details_invalid_config);
        }

        m_reportReceiver.scanComplete(m_key, result, details);
    }
}