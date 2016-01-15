package com.jcc.auctionclient;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuctionListFragment extends Fragment {
    ListView auctionList;
    private Callbacks mCallbacks;

    public AuctionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_auction_list, container, false);
        auctionList = (ListView)rootView.findViewById(R.id.auction_list);
        auctionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("AuctionC", "onItemClicked " + i);
                Log.d("AuctionC", mCallbacks == null ? "mCallbacks = null" : "mCallbacks != null");
                mCallbacks.onItemSelected(i, null);
            }
        });
        Log.d("AuctionC", "onCreateView");
        return rootView;
    }


    @Override
    public void onAttach(Context context) { //This is only for API 23 or greater, it will not be called in Android 4.4.2
        Log.d("AuctionC", "onAttach0");
        super.onAttach(context);
        Log.d("AuctionC", "onAttach1");

        if (!(context instanceof Activity)){
            throw new IllegalStateException("AuctionListFragment所在的Activity必须实现Callbacks接口!");
        }
        Activity a = (Activity)context;
        if (!(a instanceof Callbacks)){
            throw new IllegalStateException("AuctionListFragment所在的Activity必须实现Callbacks接口!");
        }
        mCallbacks = (Callbacks) a;
        Log.d("AuctionC", "onAttach");
    }

    @Override
    public void onAttach(Activity activity) { //This will be called for ealier then API32, like Android 4.4.2
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)){
            throw new IllegalStateException("AuctionListFragment所在的Activity必须实现Callbacks接口!");
        }
        mCallbacks = (Callbacks) activity;
        Log.d("AuctionC", "onAttach Activity");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void setActivateOnItemClick(boolean activateOnItemClick){
        auctionList.setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }
}
