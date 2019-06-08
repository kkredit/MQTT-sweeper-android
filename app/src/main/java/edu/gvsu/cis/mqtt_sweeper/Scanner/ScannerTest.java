package edu.gvsu.cis.mqtt_sweeper.Scanner;

import android.os.Bundle;

import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent.BrokerItem;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.ScanResultItem;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.Result;

public abstract class ScannerTest {

    protected ScanReportReciever m_reportReceiver;
    protected int m_key;
    protected BrokerItem m_broker;
    protected String m_shodanKey;

    public static final String BROKER_ID_ARG = "0";
    public static final String SHODAN_KEY_ARG = "1";

    public interface ScanReportReciever {
        void scanComplete(int key, Result result);
    }

    public abstract ScanResultItem getDescription();
    public abstract void run(ScanReportReciever receiver, int key, Bundle args);

    protected void initArgs(ScanReportReciever receiver, int key, Bundle args) {
        m_reportReceiver = receiver;
        m_key = key;
        m_broker = BrokerContent.ITEM_MAP.get(args.getString(BROKER_ID_ARG));
        m_shodanKey = args.getString(SHODAN_KEY_ARG);
    }
}
