package edu.gvsu.cis.mqtt_sweeper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.gvsu.cis.mqtt_sweeper.ScanResultFragment.OnListFragmentInteractionListener;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.ScanResultItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ScanResultItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyScanResultRecyclerViewAdapter extends RecyclerView.Adapter<MyScanResultRecyclerViewAdapter.ViewHolder> {

    private List<ScanResultItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyScanResultRecyclerViewAdapter(List<ScanResultItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void updateList(List<ScanResultItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_scanresult, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).id);
        holder.mDescr.setText(mValues.get(position).details);
        int imageId = getImageFromSeverity(mValues.get(position).severity);
        holder.mLogo.setImageResource(imageId);

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

    private int getImageFromSeverity(ScanResultContent.Severity severity) {
        if (ScanResultContent.Severity.MINOR == severity)
            return R.drawable.ic_info_outline_black_24dp;
        if (ScanResultContent.Severity.MODERATE == severity)
            return R.drawable.ic_error_outline_black_24dp;
        if (ScanResultContent.Severity.SEVERE == severity)
            return R.drawable.ic_error_black_24dp;
        return R.drawable.ic_info_outline_black_24dp;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mDescr;
        public final ImageView mLogo;
        public ScanResultItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.name);
            mDescr = (TextView) view.findViewById(R.id.description);
            mLogo = (ImageView) view.findViewById(R.id.severity_logo);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescr.getText() + "'";
        }
    }
}
