package edu.gvsu.cis.mqtt_sweeper.DataStores;

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
public class TopicContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<topicItem> ITEMS = new ArrayList<topicItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, topicItem> ITEM_MAP = new HashMap<String, topicItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createTopicItem(i));
        }
    }

    private static void addItem(topicItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static topicItem createTopicItem(int position) {
        return new topicItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore message information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of topic.
     */
    public static class topicItem {
        public final String id;
        public final String topic;
        public final String message;

        public topicItem(String id, String content, String details) {
            this.id = id;
            this.topic = content;
            this.message = details;
        }

        @Override
        public String toString() {
            return topic;
        }
    }
}
