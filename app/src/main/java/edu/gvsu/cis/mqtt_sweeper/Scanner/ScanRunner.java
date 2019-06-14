package edu.gvsu.cis.mqtt_sweeper.Scanner;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.ScanResultItem;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.Result;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests.TestBrokerValidatesClients;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests.TestInternetExposed;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests.TestNull;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests.TestPasswordStrength;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests.TestUsingEncryption;

import static edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest.BROKER_ID_ARG;
import static edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest.SHODAN_KEY_ARG;

public class ScanRunner implements ScannerTest.ScanReportReciever {

    public interface ScanReportUpdater {
        void scanReportHasUpdate();
    }

    private ScanReportUpdater m_updater;
    private Context m_context;
    private BrokerContent.BrokerItem m_broker;
    private String m_shodanApiKey;

    private static final List<ScannerTest> TESTS = new ArrayList<ScannerTest>();

    static {
        /* Init tests list */
        TESTS.add(new TestInternetExposed());
        TESTS.add(new TestUsingEncryption());
        TESTS.add(new TestBrokerValidatesClients());
        TESTS.add(new TestPasswordStrength());
    }

    public ScanRunner(ScanReportUpdater updater, Context context, BrokerContent.BrokerItem broker, String shodanApiKey) {
        m_updater = updater;
        m_context = context;
        m_broker = broker;
        m_shodanApiKey = shodanApiKey;
    }

    public void runScans() {
        int key = 0;

        Bundle args = new Bundle();
        args.putString(BROKER_ID_ARG, m_broker.id);
        args.putString(SHODAN_KEY_ARG, m_shodanApiKey);

        for (ScannerTest test : TESTS) {
            ScanResultItem item = new ScanResultItem(test.getDescription());
            System.out.println("RUNNING TEST: " + item.name);

            test.run(this, m_context, key, args);
            key++;
        }
    }

    @Override
    public void scanComplete(int key, Result result, String details) {
        ScannerTest test = TESTS.get(key);
        ScanResultItem item = new ScanResultItem(test.getDescription());
        System.out.println("COMPLETED TEST: " + item.name);
        item.setResult(result, details);
        m_broker.addScanResultItem(item);
        System.out.println("TEST " + item.name + " RESULT: " + result.toString() + ": " + details);
        m_updater.scanReportHasUpdate();
    }
}
