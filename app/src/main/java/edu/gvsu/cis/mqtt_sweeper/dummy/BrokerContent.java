package edu.gvsu.cis.mqtt_sweeper.dummy;

import java.net.URL;
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

    public static final List<BrokerItem> ITEMS = new ArrayList<BrokerItem>();
    public static final Map<String, BrokerItem> ITEM_MAP = new HashMap<String, BrokerItem>();

    static {
        // Add a sample item.
        addItem(createDummyItem(0));
    }

    private static void addItem(BrokerItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static BrokerItem createDummyItem(int position) {
        return new BrokerItem(String.valueOf(position), "Broker " + position,
                "tcp://broker.mqttdashboard.com:1883");
    }

    public static class BrokerItem {
        public final String id;
        public final String name;
        public final String url;

        public BrokerItem(String id, String name, String url) {
            this.id = id;
            this.name = name;
            this.url = url;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
