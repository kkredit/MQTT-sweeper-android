package edu.gvsu.cis.mqtt_sweeper.Scanner;

import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;

public class ScanRunner {

    private BrokerContent.BrokerItem m_broker;
    private String m_shodanApiKey;

    public ScanRunner(BrokerContent.BrokerItem broker, String shodanApiKey) {
        m_broker = broker;
        m_shodanApiKey = shodanApiKey;
    }

    public void runScans() {
        InternetExposureChecker iec = new InternetExposureChecker(
                m_shodanApiKey,
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
        iec.getExposed();
    }
}
