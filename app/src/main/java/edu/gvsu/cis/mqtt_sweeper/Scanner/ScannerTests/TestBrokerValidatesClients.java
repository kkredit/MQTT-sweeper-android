package edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests;

import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest;

public class TestBrokerValidatesClients extends ScannerTest {

    private static final ScanResultContent.ScanResultItem description = new ScanResultContent.ScanResultItem(
            "Broker authenticates clients",
            "Tests whether the broker performs any authentication of clients.",
            ScanResultContent.Severity.SEVERE
    );

    @Override
    public ScanResultContent.ScanResultItem getDescription() {
        return description;
    }

    @Override
    protected void doTest() {
        m_reportReceiver.scanComplete(m_key, ScanResultContent.Result.CONDITION_PRESENT, "This is a dummy test.");
    }
}
