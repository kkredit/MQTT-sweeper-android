package edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests;

import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent.BrokerItem;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.ScanResultItem;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest;

import static edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.Result;
import static edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.Severity;

public class TestNull extends ScannerTest {

    private static final ScanResultItem description = new ScanResultItem(
            "NULL TEST",
            "Dummy test that does nothing",
            Severity.MINOR
    );

    @Override
    public ScanResultItem getDescription() {
        return description;
    }

    @Override
    public Result run(BrokerItem broker) {
        return Result.CONDITION_NOT_PRESENT;
    }
}
