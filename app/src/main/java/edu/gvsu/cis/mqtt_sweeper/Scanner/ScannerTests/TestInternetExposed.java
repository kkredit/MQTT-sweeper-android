package edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests;

import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.R;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest;

public class TestInternetExposed extends ScannerTest {

    private static final ScanResultContent.ScanResultItem description = new ScanResultContent.ScanResultItem(
            "Internet exposed",
            "Tests whether the MQTT broker is visible over the public internet.",
            ScanResultContent.Severity.MODERATE
    );

    @Override
    public ScanResultContent.ScanResultItem getDescription() {
        return description;
    }

    @Override
    protected void doTest() {
        InternetExposureChecker iec = new InternetExposureChecker(
                m_shodanKey,
                new InternetExposureChecker.IEC_Handler() {
                    @Override
                    public void iecOnComplete() {
                        System.out.println("IEC Complete!");
                    }

                    @Override
                    public void iecOnError(Throwable e) {
                        System.out.println("IEC error!");
                        e.printStackTrace();
                        m_reportReceiver.scanComplete(m_key, ScanResultContent.Result.ERROR_WHILE_RUNNING,
                                m_context.getResources().getString(R.string.error_details_network_error));
                    }

                    @Override
                    public void iecReceiveAnswer(boolean connected) {
                        System.out.println("IEC answer is " + (connected ? "TRUE" : "FALSE"));
                        m_reportReceiver.scanComplete(m_key, (connected ? ScanResultContent.Result.CONDITION_PRESENT :
                                ScanResultContent.Result.CONDITION_NOT_PRESENT),
                                "This broker is visible from anywhere on the internet.");
                    }
                });
        iec.getExposed();
    }
}
