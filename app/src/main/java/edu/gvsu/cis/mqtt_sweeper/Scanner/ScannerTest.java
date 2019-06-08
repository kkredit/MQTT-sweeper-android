package edu.gvsu.cis.mqtt_sweeper.Scanner;

import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent.BrokerItem;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.ScanResultItem;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.Result;

public abstract class ScannerTest {
    public abstract ScanResultItem getDescription();
    public abstract Result run(BrokerItem broker);
}
