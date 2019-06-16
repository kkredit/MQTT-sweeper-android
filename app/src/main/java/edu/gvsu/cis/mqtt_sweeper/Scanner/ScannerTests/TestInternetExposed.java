package edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests;

import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.R;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest;

public class TestInternetExposed extends ScannerTest {

    private static final ScanResultContent.ScanResultItem description = new ScanResultContent.ScanResultItem(
            "Internet exposed",
            "Tests whether the MQTT broker is visible over the public internet.",
            ScanResultContent.Severity.SEVERE,
            "Being exposed to the internet is not itself a security vulnerability. " +
                    "However, it dramatically increases the importance of real " +
                    "vulnerabilities.\n\n" +
                    "While making it available on the internet increases usability, it also " +
                    "makes it directly attackable from anywhere. Internet exposed MQTT brokers " +
                    "must either use strong client authentication or else handle absolutely " +
                    "no sensitive data.\n\n" +
                    "If this doesn't absolutely need to be accessed from anywhere, putting it " +
                    "behind a network firewall is advisable.",
            "Read more on Wikipedia",
            "https://en.wikipedia.org/wiki/Firewall_(computing)"
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
                        if (connected) {
                            System.out.println("IEC answer is TRUE");
                            m_reportReceiver.scanComplete(m_key, ScanResultContent.Result.CONDITION_PRESENT,
                                    "This broker is visible from anywhere on the internet.");
                        }
                        else {
                            System.out.println("IEC answer is FALSE");
                            m_reportReceiver.scanComplete(m_key, ScanResultContent.Result.CONDITION_NOT_PRESENT,
                                    "This broker is not visible from the internet.");
                        }
                    }
                });
        iec.getExposed();
    }
}
