package edu.gvsu.cis.mqtt_sweeper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import edu.gvsu.cis.mqtt_sweeper.BrokerFragment.OnListFragmentInteractionListener;
import edu.gvsu.cis.mqtt_sweeper.DataStores.Broker;
import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent.BrokerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BrokerItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBrokerRecyclerViewAdapter extends RecyclerView.Adapter<MyBrokerRecyclerViewAdapter.ViewHolder> {

    private final List<Broker> broker;
    private final OnListFragmentInteractionListener mListener;

    public MyBrokerRecyclerViewAdapter(List<Broker> items, OnListFragmentInteractionListener listener) {
        this.broker = new ArrayList<Broker>();
        mListener = listener;
        reloadFrom(items);
    }

    public void reloadFrom(final List<Broker> data) {
        broker.clear();
        for(Broker b : data){
           broker.add(b);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_broker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Broker item;
        item = this.broker.get(position);
        holder.mItem = item;
        holder.mIdView.setText(broker.get(position).servername);
        holder.mContentView.setText(broker.get(position).url);

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

        return broker.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Broker mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.name);
            mContentView = (TextView) view.findViewById(R.id.description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
