package edu.gvsu.cis.mqtt_sweeper.Scanner;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent.BrokerItem;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.ScanResultItem;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.Result;

public abstract class ScannerTest {

    protected ScanReportReciever m_reportReceiver;
    protected Context m_context;
    protected Resources m_strings;
    protected int m_key;
    protected BrokerItem m_broker;
    protected String m_shodanKey;

    static final String BROKER_ID_ARG = "0";
    static final String SHODAN_KEY_ARG = "1";

    public interface ScanReportReciever {
        void scanComplete(int key, Result result, String details);
    }

    public abstract ScanResultItem getDescription();

    void run(ScanReportReciever receiver, Context context, int key, Bundle args) {
        m_reportReceiver = receiver;
        m_context = context;
        m_strings = context.getResources();
        m_key = key;
        m_broker = BrokerContent.ITEM_MAP.get(args.getString(BROKER_ID_ARG));
        m_shodanKey = args.getString(SHODAN_KEY_ARG);

        doTest();
    }

    protected abstract void doTest();
}
