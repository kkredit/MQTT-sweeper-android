package edu.gvsu.cis.mqtt_sweeper.DataStores;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper class for providing sample topic for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class BrokerContent {

    public static final String NULL_BROKER_ID = "";

    public static  Map<String, BrokerItem> ITEM_MAP = new HashMap<String, BrokerItem>();

    private static String mNextBrokerId = "0";
    private static FirebaseDatabase mDbRef = null;
    private static FirebaseAuth mAuth;
    private static DatabaseReference mTopRef;

    private static ChildEventListener chEvListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Broker entry = (Broker) dataSnapshot.getValue(Broker.class);
            if (getBrokerIdByBroker(entry).equals(NULL_BROKER_ID)) {
                entry._key = dataSnapshot.getKey();
                addBrokerNoDbUpdate(entry);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Broker entry = (Broker) dataSnapshot.getValue(Broker.class);
            String brokerId = getBrokerIdByBroker(entry);
            if (!brokerId.equals(NULL_BROKER_ID)) {
//                entry._key = dataSnapshot.getKey();
                updateBrokerNoDbUpdate(getBrokerIdByBroker(entry), entry);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Broker entry = (Broker) dataSnapshot.getValue(Broker.class);
            String brokerId = getBrokerIdByBroker(entry);
            if (!brokerId.equals(NULL_BROKER_ID)) {
                delBrokerNoDbUpdate(brokerId);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    public static void initDb() {
        FirebaseDatabase mDbRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        String uid = mUser.getUid();
        mTopRef = mDbRef.getReference(uid);
        mTopRef.addChildEventListener(chEvListener);
    }

    public static void addBroker(Broker b) {
        mTopRef.push().setValue(b);
    }

    private static void addBrokerNoDbUpdate(Broker b) {
        BrokerItem newBroker = new BrokerItem(mNextBrokerId, b);
        ITEM_MAP.put(mNextBrokerId, newBroker);
        mNextBrokerId = Integer.toString(Integer.parseInt(mNextBrokerId) + 1);
    }

    public static void delBroker(String id) {
        BrokerItem broker = getBroker(id);
        mTopRef.child(broker.broker._key).removeValue();
    }

    private static void delBrokerNoDbUpdate(String id) {
        ITEM_MAP.remove(id);
    }

    public static void updateBroker(String id, Broker b) {
        BrokerItem broker = getBroker(id);
        b._key = broker.broker._key;
        broker.broker = b;
        mTopRef.child(broker.broker._key).setValue(b);
    }

    private static void updateBrokerNoDbUpdate(String id, Broker b) {
        BrokerItem broker = getBroker(id);
        broker.broker = b;
    }

    public static BrokerItem getBroker(String id) {
        return ITEM_MAP.get(id);
    }

    public static String getBrokerIdByBroker(Broker broker) {
        for (BrokerItem bi : ITEM_MAP.values()) {
            if (bi.broker._key.equals(broker._key)) {
                return bi.id;
            }
        }
        return NULL_BROKER_ID;
    }

    public static class BrokerItem {
        public final String id;
        public Broker broker;
        public String scanSummary;
        public final BrokerScanMetadata scanMetadata;
        private final List<ScanResultContent.ScanResultItem> scanResults;
        private Integer nextScanId = 0;

        public BrokerItem(String id, Broker b) {
            this.id = id;
            this.broker = b;
            this.scanMetadata = new BrokerScanMetadata();
            this.scanResults = new ArrayList<>();
            updateSummary();
        }

        public String getScanSummary() {
            return scanSummary;
        }

        @Override
        public String toString() {
            return broker.toString();
        }

        public void addScanResultItem(ScanResultContent.ScanResultItem item) {
            item.setId(nextScanId.toString());
            nextScanId++;
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
