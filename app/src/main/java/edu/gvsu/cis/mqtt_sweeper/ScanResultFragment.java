package edu.gvsu.cis.mqtt_sweeper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.gvsu.cis.mqtt_sweeper.DataStores.BrokerContent;
import edu.gvsu.cis.mqtt_sweeper.DataStores.ScanResultContent.ScanResultItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ScanResultFragment extends Fragment
        implements DataUpdateListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_BROKER_KEY = "broker-key";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private BrokerContent.BrokerItem m_broker;
    private OnListFragmentInteractionListener mListener;

    private RecyclerView m_view;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScanResultFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ScanResultFragment newInstance(int columnCount, String brokerKey) {
        ScanResultFragment fragment = new ScanResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_BROKER_KEY, brokerKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            m_broker = BrokerContent.ITEM_MAP.get(getArguments().getString(ARG_BROKER_KEY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanresult_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            m_view = (RecyclerView) view;
            if (mColumnCount <= 1) {
                m_view.setLayoutManager(new LinearLayoutManager(context));
            } else {
                m_view.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            DividerItemDecoration did = new DividerItemDecoration(m_view.getContext(),
                    DividerItemDecoration.VERTICAL);
            m_view.addItemDecoration(did);
            if (null != m_broker) {
                m_view.setAdapter(new MyScanResultRecyclerViewAdapter(m_broker.getScanResults(), mListener));
            }
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((ScanActivity) getActivity()).registerDataUpdateListener(this);
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
        ((ScanActivity) getActivity()).unregisterDataUpdateListener(this);
        mListener = null;
    }

    @Override
    public void onDataUpdate() {
        MyScanResultRecyclerViewAdapter adapter = (MyScanResultRecyclerViewAdapter) m_view.getAdapter();
        if (null != adapter && null != m_broker) {
            adapter.notifyDataSetChanged();
        }
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
        void onListFragmentInteraction(ScanResultItem item);
    }
}
