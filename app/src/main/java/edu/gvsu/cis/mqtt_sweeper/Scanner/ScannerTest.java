package edu.gvsu.cis.mqtt_sweeper.Scanner;

import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent.BrokerItem;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.ScanResultItem;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.Result;

public abstract class ScannerTest {

    public interface ScanReportReciever {
        public void scanComplete(int key, Result result);
    }

    public abstract ScanResultItem getDescription();
    public abstract void run(ScanReportReciever receiver, BrokerItem broker, int key);
}
