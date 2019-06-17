package edu.gvsu.cis.mqtt_sweeper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.gvsu.cis.mqtt_sweeper.DataStores.Broker;
import edu.gvsu.cis.mqtt_sweeper.DataStores.Topic;
import edu.gvsu.cis.mqtt_sweeper.DataStores.TopicContent;
import edu.gvsu.cis.mqtt_sweeper.TopicsFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TopicContent.TopicItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MytopicsRecyclerViewAdapter extends RecyclerView.Adapter<MytopicsRecyclerViewAdapter.ViewHolder> {

    private final List<Topic> topics;
    private final OnListFragmentInteractionListener mListener;

    public MytopicsRecyclerViewAdapter(List<Topic> items, OnListFragmentInteractionListener listener) {
        this.topics = new ArrayList<Topic>();
        mListener = listener;
        reloadFrom(items);
    }
    public void reloadFrom(final List<Topic> data) {
        topics.clear();
        for(Topic p : data){
            topics.add(p);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_topics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Topic item;
        item = this.topics.get(position);
        holder.mItem = item;
        holder.mIdView.setText(topics.get(position).topic);
        holder.mContentView.setText(topics.get(position).message);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Topic mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
