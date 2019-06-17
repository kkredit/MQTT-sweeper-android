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
    public static final List<TopicItem> ITEMS = new ArrayList<TopicItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, TopicItem> ITEM_MAP = new HashMap<String, TopicItem>();


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
    public static class TopicItem {
        public final String id;
        public final String topic;
        public final String message;
        public  String _key;

        public TopicItem(String id, String content, String details) {
            this.id = id;
            this.topic = content;
            this.message = details;
            _key = null;
        }

        @Override
        public String toString() {
            return topic;
        }
    }
}
