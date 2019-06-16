package edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests;

import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest;

public class TestBrokerValidatesClients extends ScannerTest {

    private static final ScanResultContent.ScanResultItem description = new ScanResultContent.ScanResultItem(
            "Broker authenticates clients",
            "Tests whether the broker performs any authentication of clients.",
            ScanResultContent.Severity.MODERATE,
            "Client authentication makes it so that only those who are authorized to " +
                    "connect to a broker can actually do so. While its importance depends on " +
                    "your threat model and things such as whether the broker is exposed to the " +
                    "internet, in almost no situation is the complete lack of client " +
                    "authentication the right choice.\n\n" +
                    "MQTT allows for several forms of client authentication: username and " +
                    "password, cryptographic certificate, and client ID whitelisting. This test " +
                    "checks for whether your connection uses password authentication.",
            "Read more on Wikipedia",
            "https://en.wikipedia.org/wiki/Electronic_authentication"
    );

    @Override
    public ScanResultContent.ScanResultItem getDescription() {
        return description;
    }

    @Override
    protected void doTest() {
        ScanResultContent.Result result = ScanResultContent.Result.CONDITION_NOT_PRESENT;
        String details;

        /* TODO: update to rate the actual password */
        /* TODO: update to check for more than presence of password */

        String password = m_broker.name;

        if (password.isEmpty()) {
            result = ScanResultContent.Result.CONDITION_PRESENT;
            details = "This connection does not use password authentication.";
        }
        else {
            details = "This connection uses password authentication.";
        }

        m_reportReceiver.scanComplete(m_key, result, details);
    }
}
