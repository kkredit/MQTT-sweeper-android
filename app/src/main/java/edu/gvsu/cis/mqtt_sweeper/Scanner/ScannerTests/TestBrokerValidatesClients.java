package edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests;

import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest;

public class TestBrokerValidatesClients extends ScannerTest {

    private static final ScanResultContent.ScanResultItem description = new ScanResultContent.ScanResultItem(
            "Broker authenticates clients",
            "Tests whether the broker performs any authentication of clients.",
            ScanResultContent.Severity.SEVERE,
            "TODO More info."
    );

    @Override
    public ScanResultContent.ScanResultItem getDescription() {
        return description;
    }

    @Override
    protected void doTest() {
        ScanResultContent.Result result = ScanResultContent.Result.CONDITION_NOT_PRESENT;
        String details;

        /* TODO: update to rate the actual password */
        /* TODO: update to check for more than presence of password */

        String password = m_broker.name;

        if (password.isEmpty()) {
            result = ScanResultContent.Result.CONDITION_PRESENT;
            details = "This connection does not use password authentication.";
        }
        else {
            details = "This connection uses password authentication.";
        }

        m_reportReceiver.scanComplete(m_key, result, details);
    }
}
