package edu.gvsu.cis.mqtt_sweeper.DataStores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class BrokerContent {

    public static  List<BrokerItem> ITEMS = new ArrayList<BrokerItem>();
    public static  Map<String, BrokerItem> ITEM_MAP = new HashMap<String, BrokerItem>();

    public static class BrokerItem {
        public final String id;
        public final String name;
        public final String url;
        public String scanSummary;
        public final BrokerScanMetadata scanMetadata;
        private final List<ScanResultContent.ScanResultItem> scanResults;
        private Integer nextId = 0;

        public BrokerItem(String id, String name, String url) {
            this.id = id;
            this.name = name;
            this.url = url;
            this.scanMetadata = new BrokerScanMetadata();
            this.scanResults = new ArrayList<>();
            updateSummary();
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getScanSummary() {
            return scanSummary;
        }


        @Override
        public String toString() {
            return name;
        }

        public void addScanResultItem(ScanResultContent.ScanResultItem item) {
            item.setId(nextId.toString());
            nextId++;
            scanResults.add(item);
            updateMetadata(item);
            updateSummary();
        }

        public List<ScanResultContent.ScanResultItem> getScanResults() {
            return scanResults;
        }

        public void clearScanResults() {
            scanResults.clear();
            scanMetadata.clear();
            updateSummary();
        }

        private void updateMetadata(ScanResultContent.ScanResultItem item) {
            switch (item.result) {
                case CONDITION_PRESENT:
                    scanMetadata.numFailsTotal++;
                    switch (item.severity) {
                        case MINOR:
                            scanMetadata.numFailsMinor++;
                            break;
                        case MODERATE:
                            scanMetadata.numFailsModerate++;
                            break;
                        case SEVERE:
                            scanMetadata.numFailsSevere++;
                            break;
                    }
                    break;
                case CONDITION_NOT_PRESENT:
                    scanMetadata.numPasses++;
                    break;
                case ERROR_WHILE_RUNNING:
                    scanMetadata.numErrors++;
                    break;
            }
        }

        private void updateSummary() {
            if (0 == scanMetadata.numPasses + scanMetadata.numFailsTotal + scanMetadata.numErrors) {
                scanSummary = "No tests to report";
            }
            else {
                StringBuilder builder = new StringBuilder();
                addSummaryPart(builder, scanMetadata.numPasses, "Pass", "Passes");
                addSummaryPart(builder, scanMetadata.numFailsTotal, "Fail", "Fails");
                addSummaryPart(builder, scanMetadata.numErrors, "Error", "Errors");
                scanSummary = builder.toString();
            }
        }

        private void addSummaryPart(StringBuilder builder, int num, String singular, String plural) {
            if (0 < num) {
                if (0 < builder.length()) {
                    builder.append(", ");
                }
                builder.append(num).append(" ");
                if (1 == num) {
                    builder.append(singular);
                }
                else {
                    builder.append(plural);
                }
            }
        }

        class BrokerScanMetadata {
            int numPasses = 0;
            int numFailsTotal = 0;
            int numFailsMinor = 0;
            int numFailsModerate = 0;
            int numFailsSevere = 0;
            int numErrors = 0;

            BrokerScanMetadata() {
            }

            void clear() {
                numPasses = 0;
                numFailsTotal = 0;
                numFailsMinor = 0;
                numFailsModerate = 0;
                numFailsSevere = 0;
                numErrors = 0;
            }
        }
    }
}
