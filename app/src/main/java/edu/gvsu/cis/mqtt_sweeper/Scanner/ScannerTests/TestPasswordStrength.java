package edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTests;

import java.text.DecimalFormat;

import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.R;
import edu.gvsu.cis.mqtt_sweeper.Scanner.ScannerTest;
import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.scoring.Result;

public class TestPasswordStrength extends ScannerTest {

    private static final ScanResultContent.ScanResultItem description = new ScanResultContent.ScanResultItem(
            "Password strength",
            "Tests password strength.",
            ScanResultContent.Severity.MINOR,
            "TODO More info."
    );

    @Override
    public ScanResultContent.ScanResultItem getDescription() {
        return description;
    }

    /* See http://world.std.com/~reinhold/diceware.html for some commentary on entropy */
    private static final Double OK_PW_THRESHOLD_MIN = 20.0;
    private static final Double GOOD_PW_THRESHOLD_MIN = 40.0;
    private static final Double STRONG_PW_THRESHOLD_MIN = 60.0;

    @Override
    protected void doTest() {
        Nbvcxz strengthRater = new Nbvcxz();

//        void runStrengthTests(strengthRater);

        ScanResultContent.Result result = ScanResultContent.Result.CONDITION_NOT_PRESENT;
        String details;

        /* TODO: update to rate the actual password */
        Result passwordResult = strengthRater.estimate(m_broker.name);
        Double entropy = passwordResult.getEntropy();
        DecimalFormat f = new DecimalFormat("#");
        String entropyStr = f.format(entropy);

        if (entropy < OK_PW_THRESHOLD_MIN) {
            result = ScanResultContent.Result.CONDITION_PRESENT;
            details = "Password strength is extremely poor (" + entropyStr + " bits of entropy).";
        }
        else if (entropy < GOOD_PW_THRESHOLD_MIN) {
            result = ScanResultContent.Result.CONDITION_PRESENT;
            details = "Password strength is poor (" + entropyStr + " bits of entropy).";
        }
        else if (entropy < STRONG_PW_THRESHOLD_MIN) {
            details = "Password strength is good (" + entropyStr + " bits of entropy).";
        }
        else {
            details = "Password strength is very good (" + entropyStr + " bits of entropy).";
        }

        m_reportReceiver.scanComplete(m_key, result, details);
    }

    @SuppressWarnings("unused")
    private void runStrengthTests(Nbvcxz strengthRater) {
        Result passwordResult = strengthRater.estimate("password");
        int score = passwordResult.getBasicScore(); // 0
        Double entropy = passwordResult.getEntropy(); // 1.0
        passwordResult = strengthRater.estimate("P@ssw0rd");
        score = passwordResult.getBasicScore(); // 1
        entropy = passwordResult.getEntropy(); // 11.0
        passwordResult = strengthRater.estimate("34$-3football");
        score = passwordResult.getBasicScore(); // 2
        entropy = passwordResult.getEntropy(); // 22.1
        passwordResult = strengthRater.estimate("Alabast3r0812");
        score = passwordResult.getBasicScore(); // 3
        entropy = passwordResult.getEntropy(); // 28.5
        passwordResult = strengthRater.estimate("Pass34Moon");
        score = passwordResult.getBasicScore(); // 4
        entropy = passwordResult.getEntropy(); // 35.7
        passwordResult = strengthRater.estimate("34$$as8_52%");
        score = passwordResult.getBasicScore(); // 4
        entropy = passwordResult.getEntropy(); // 46.2
        passwordResult = strengthRater.estimate("bbYuI@+6iR__9b}FkaJ?");
        score = passwordResult.getBasicScore(); // 4
        entropy = passwordResult.getEntropy(); // 93.3
        passwordResult = strengthRater.estimate("bbYuI@+6iR__9b}FkaJ?cL<Cg~GWB:");
        score = passwordResult.getBasicScore(); // 4
        entropy = passwordResult.getEntropy(); // 141.4
    }
}
