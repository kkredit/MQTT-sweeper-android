package edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests;

import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.ScanResultItem;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest;

import static edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.Result;
import static edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.Severity;

public class TestNull extends ScannerTest {

    private static final ScanResultItem description = new ScanResultItem(
            "NULL TEST",
            "Dummy test that does nothing",
            Severity.MINOR,
            "This is a dummy test that exists as a template for real tests.",
            "Read more on Wikipedia",
            "https://en.wikipedia.org"
    );

    @Override
    public ScanResultItem getDescription() {
        return description;
    }

    @Override
    protected void doTest() {
        m_reportReceiver.scanComplete(m_key, Result.CONDITION_PRESENT, "This is a dummy test.");
    }
}
