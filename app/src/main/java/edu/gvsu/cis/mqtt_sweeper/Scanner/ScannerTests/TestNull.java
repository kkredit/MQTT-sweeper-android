package edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests;

import android.os.Bundle;

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
    public void run(ScanReportReciever receiver, int key, Bundle args) {
        super.initArgs(receiver, key, args);

        m_reportReceiver.scanComplete(m_key, Result.CONDITION_PRESENT);
    }
}
