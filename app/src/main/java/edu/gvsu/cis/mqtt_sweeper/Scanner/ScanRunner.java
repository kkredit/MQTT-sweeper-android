package edu.gvsu.cis.mqtt_sweeper.Scanner;

import java.util.ArrayList;
import java.util.List;

import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.ScanResultItem;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.Result;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests.TestNull;

public class ScanRunner {

    private BrokerContent.BrokerItem m_broker;
    private String m_shodanApiKey;

    private static final List<ScannerTest> TESTS = new ArrayList<ScannerTest>();

    static {
        /* Init tests list */
        TESTS.add(new TestNull());
    }

    public ScanRunner(BrokerContent.BrokerItem broker, String shodanApiKey) {
        m_broker = broker;
        m_shodanApiKey = shodanApiKey;
    }

    public void runScans() {

        /* Standard Tests */
        for (ScannerTest test : TESTS) {
            ScanResultItem item = new ScanResultItem(test.getDescription());
            System.out.println("RUNNING TEST: " + item.name);

            Result result = test.run(m_broker);
            item.setResult(result);
            m_broker.addScanResultItem(item);
            System.out.println("TEST " + item.name + " RESULT: " + result.toString());
        }

        /* Special Tests */
        Result result = run_InternetExposed(m_shodanApiKey);
        ScanResultItem item = getDescription_InternetExposed();
        item.setResult(result);
        m_broker.addScanResultItem(item);
    }

    private ScanResultItem getDescription_InternetExposed() {
        return new ScanResultItem(
                "Internet exposed",
                "The MQTT broker is visible over the public internet.",
                ScanResultContent.Severity.MODERATE
        );
    }

    private Result run_InternetExposed(String shodanApiKey) {
        InternetExposureChecker iec = new InternetExposureChecker(
                shodanApiKey,
                new InternetExposureChecker.IEC_Handler() {
                    @Override
                    public void iecOnComplete() {
                        System.out.println("IEC Complete!");
                    }

                    @Override
                    public void iecOnError(Throwable e) {
                        System.out.println("IEC error!");
                        e.printStackTrace();
                    }

                    @Override
                    public void iecReceiveAnswer(boolean connected) {
                        System.out.println("IEC answer is " + (connected ? "TRUE" : "FALSE"));
                    }
                });
        boolean exposed = iec.getExposed();
        /* TODO: use callbacks. This isn't actually right */
        return exposed ? Result.CONDITION_PRESENT : Result.CONDITION_NOT_PRESENT;
    }
}
