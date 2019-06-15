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
        ScanResultItem result = mValues.get(position);
        holder.mItem = result;
        holder.mName.setText(result.name);
        holder.mDescr.setText(result.details);
        int imageId = getImageFromSeverity(result.result, result.severity);
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

    private int getImageFromSeverity(ScanResultContent.Result result, ScanResultContent.Severity severity) {
        int image = R.drawable.ic_excl_point_gray_24dp;;
        switch (result) {
            case CONDITION_PRESENT:
                /* nested switches aren't very nice... */
                switch (severity) {
                    case MINOR:
                        image = R.drawable.ic_excl_triangle_yellow_24dp;
                        break;
                    case MODERATE:
                        image = R.drawable.ic_excl_outline_red_24dp;
                        break;
                    case SEVERE:
                        image = R.drawable.ic_excl_filled_red_24dp;
                        break;
                }
                break;
            case CONDITION_NOT_PRESENT:
                image = R.drawable.ic_check_green_24dp;
                break;
            case ERROR_WHILE_RUNNING:
                image = R.drawable.ic_excl_point_gray_24dp;
                break;
        }
        return image;
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
