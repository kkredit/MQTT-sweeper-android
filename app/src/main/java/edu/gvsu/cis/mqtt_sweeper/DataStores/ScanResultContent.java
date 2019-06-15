package edu.gvsu.cis.mqtt_sweeper.DataStores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.Result;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ScanResultContent {

    public enum Severity {
        MINOR,
        MODERATE,
        SEVERE,
    }

    public enum Result {
        HAVE_NOT_RUN,
        ERROR_WHILE_RUNNING,
        CONDITION_PRESENT,
        CONDITION_NOT_PRESENT,
    }

    public static final List<ScanResultItem> ITEMS = new ArrayList<ScanResultItem>();
    public static final Map<String, ScanResultItem> ITEM_MAP = new HashMap<String, ScanResultItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(ScanResultItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ScanResultItem createDummyItem(int position) {
        return new ScanResultItem(String.valueOf(position), "Item " + position,
                                  makeDetails(position), Severity.MINOR, "More info.");
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class ScanResultItem {
        public String id;
        public final String name;
        public final String details;
        public final Severity severity;
        public final String moreInfo;
        public Result result = Result.HAVE_NOT_RUN;
        public String resultDetails = "";

        public ScanResultItem(String id, String name, String details, Severity severity, String moreInfo) {
            this.id = id;
            this.name = name;
            this.details = details;
            this.severity = severity;
            this.moreInfo = moreInfo;
        }

        public ScanResultItem(String name, String details, Severity severity, String moreInfo) {
            this.name = name;
            this.details = details;
            this.severity = severity;
            this.moreInfo = moreInfo;
        }

        public ScanResultItem(ScanResultItem orig) {
            this.id = orig.id;
            this.name = orig.name;
            this.details = orig.details;
            this.severity = orig.severity;
            this.moreInfo = orig.moreInfo;
        }

        public void setResult(Result result, String details) {
            this.result = result;
            this.resultDetails = details;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
