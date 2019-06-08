package edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests;

import android.os.Bundle;

import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest;

public class TestInternetExposed extends ScannerTest {

    private static final ScanResultContent.ScanResultItem description = new ScanResultContent.ScanResultItem(
            "Internet exposed",
            "The MQTT broker is visible over the public internet.",
            ScanResultContent.Severity.MODERATE
    );

    @Override
    public ScanResultContent.ScanResultItem getDescription() {
        return description;
    }

    @Override
    public void run(ScanReportReciever receiver, int key, Bundle args) {
        super.initArgs(receiver, key, args);

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
                        m_reportReceiver.scanComplete(key, ScanResultContent.Result.ERROR_WHILE_RUNNING);
                    }

                    @Override
                    public void iecReceiveAnswer(boolean connected) {
                        System.out.println("IEC answer is " + (connected ? "TRUE" : "FALSE"));
                        receiver.scanComplete(key, (connected ? ScanResultContent.Result.CONDITION_PRESENT :
                                ScanResultContent.Result.CONDITION_NOT_PRESENT));
                    }
                });
        iec.getExposed();
    }
}
