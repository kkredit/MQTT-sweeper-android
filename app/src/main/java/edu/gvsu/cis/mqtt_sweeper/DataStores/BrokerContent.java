package edu.gvsu.cis.mqtt_sweeper.DataStores;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample topic for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class BrokerContent {

    public static  List<BrokerItem> ITEMS = new ArrayList<BrokerItem>();
    public static  Map<String, BrokerItem> ITEM_MAP = new HashMap<String, BrokerItem>();

    public static class BrokerItem {
        public String id;
        public String name;
        public String url;
        public String scanSummary;
        private List<ScanResultContent.ScanResultItem> scanResults;
        private Integer nextId = 0;

        public BrokerItem(String id, String name, String url, String scanSummary, List<ScanResultContent.ScanResultItem> scanResults) {
            this.id = id;
            this.name = name;
            this.url = url;
            this.scanSummary = scanSummary;
            this.scanResults = scanResults;
        }


        @Override
        public String toString() {
            return name;
        }

        public void addScanResultItem(ScanResultContent.ScanResultItem item) {
            item.setId(nextId.toString());
            nextId++;
            scanResults.add(item);
        }

        public List<ScanResultContent.ScanResultItem> getScanResults() {
            return scanResults;
        }

    }
}
