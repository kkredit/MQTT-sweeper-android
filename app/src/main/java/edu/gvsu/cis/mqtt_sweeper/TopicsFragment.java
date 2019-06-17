package edu.gvsu.cis.mqtt_sweeper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.gvsu.cis.mqtt_sweeper.DataStores.Topic;
import edu.gvsu.cis.mqtt_sweeper.DataStores.TopicContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.TopicContent.TopicItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TopicsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Topic> allTopics;
    private MytopicsRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TopicsFragment() {
        allTopics = new ArrayList<Topic>();
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TopicsFragment newInstance(int columnCount) {
        TopicsFragment fragment = new TopicsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            FirebaseDatabase dbRef = FirebaseDatabase.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            DatabaseReference userRef = dbRef.getReference(user.getUid());
            userRef.addChildEventListener(chEvListener);
            userRef.addValueEventListener(valEvListener);
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MytopicsRecyclerViewAdapter(allTopics,mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Topic item);
    }

    private ValueEventListener valEvListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            adapter.reloadFrom(allTopics);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private ChildEventListener chEvListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Topic entry = (Topic) dataSnapshot.getValue(Topic.class);
            entry._key = dataSnapshot.getKey();
            allTopics.add(entry);
//
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Topic entry = (Topic) dataSnapshot.getValue(Topic.class);
            List<Topic> newTopic = new ArrayList<Topic>();
            for (Topic p : allTopics) {
                if (!p._key.equals(dataSnapshot.getKey())) {
                    newTopic.add(p);
                }
            }
            allTopics= newTopic;
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
