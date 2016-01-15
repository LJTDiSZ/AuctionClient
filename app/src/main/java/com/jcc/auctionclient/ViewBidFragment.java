package com.jcc.auctionclient;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jcc.auctionclient.util.DialogUtil;
import com.jcc.auctionclient.util.HttpCallbackListener;
import com.jcc.auctionclient.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewBidFragment extends Fragment {
    Button bnHome;
    ListView bidList;

    public ViewBidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_bid, container, false);
        bnHome = (Button)rootView.findViewById(R.id.bn_home);
        bidList = (ListView)rootView.findViewById(R.id.bidList);

        bnHome.setOnClickListener(new HomeListener(getActivity()));
        String url = HttpUtil.BASE_URL + "viewBid.jsp";
        HttpUtil.sendHttpRequest(url, "GBK", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    final JSONArray jsonArray = new JSONArray(response);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bidList.setAdapter(new JSONArrayAdapter(getActivity(), jsonArray, "item", true));
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                    final String exMsg = e.getMessage();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtil.showDialog(getActivity(), "服务器响应异常，请稍后再试！" + exMsg, false);
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                final String exMsg = e.getMessage();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showDialog(getActivity(), "服务器响应异常，请稍后再试！" + exMsg, false);
                    }
                });
            }
        });

        bidList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewBidDetail(i);
            }
        });
        return rootView;
    }

    private void viewBidDetail(int position){
        View detailView = getActivity().getLayoutInflater().inflate(R.layout.bid_detail, null);
        TextView itemName = (TextView)detailView.findViewById(R.id.itemName);
        TextView bidPrice = (TextView)detailView.findViewById(R.id.bidPrice);
        TextView bidTime = (TextView)detailView.findViewById(R.id.bidTime);
        TextView bidUser = (TextView)detailView.findViewById(R.id.bidUser);
        JSONObject jsonObject = (JSONObject)bidList.getAdapter().getItem(position);
        try{
            itemName.setText(jsonObject.getString("item"));
            bidPrice.setText(jsonObject.getString("price"));
            bidTime.setText(jsonObject.getString("bidDate"));
            bidUser.setText(jsonObject.getString("user"));
        } catch (JSONException e){
            e.printStackTrace();
        }
        DialogUtil.showDialog(getActivity(), detailView);
    }
}
