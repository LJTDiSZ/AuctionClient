package com.jcc.auctionclient;


import android.app.Activity;
import android.content.Context;
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

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageItemFragment extends Fragment {
    public static final int ADD_ITEM = 0x1006;
    Button bnHome, bnAdd;
    ListView itemList;
    Callbacks mCallbacks;

    public ManageItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manage_item, container, false);
        bnHome = (Button) rootView.findViewById(R.id.bn_home);
        bnAdd = (Button)rootView.findViewById(R.id.bnAdd);
        itemList = (ListView) rootView.findViewById(R.id.itemList);
        bnHome.setOnClickListener(new HomeListener(getActivity()));
        bnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onItemSelected(ADD_ITEM, null);
            }
        });
        String url = HttpUtil.BASE_URL + "viewOwnerItem.jsp";
        HttpUtil.sendHttpRequest(url, "GBK", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    final JSONArray jsonArray = new JSONArray(response);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            itemList.setAdapter(new JSONArrayAdapter(getActivity(), jsonArray, "name", true));
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                    final String exMsg = e.getMessage();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtil.showDialog(getActivity(), "服务器响应错误！" + exMsg, false);
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                final String exMsg = e.getMessage();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showDialog(getActivity(), "服务器响应异常，请稍后再试！" + exMsg, false);
                    }
                });
                e.printStackTrace();
            }
        });

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewItemInBid(i);
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof Activity)){
            throw new IllegalStateException("ManageKindFragment所在的Activity必须实现Callbacks接口!");
        }
        Activity a = (Activity)context;
        if (!(a instanceof Callbacks)){
            throw new IllegalStateException("ManageKindFragment所在的Activity必须实现Callbacks接口!");
        }
        mCallbacks = (Callbacks) a;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)){
            throw new IllegalStateException("ManageKindFragment所在的Activity必须实现Callbacks接口!");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private void viewItemInBid(int position){
        View detailView = getActivity().getLayoutInflater().inflate(R.layout.detail_in_bid, null);
        TextView itemName = (TextView) detailView.findViewById(R.id.itemName);
        TextView itemKind = (TextView) detailView.findViewById(R.id.itemKind);
        TextView maxPrice = (TextView) detailView.findViewById(R.id.maxPrice);
        TextView initPrice = (TextView) detailView.findViewById(R.id.initPrice);
        TextView endTime = (TextView) detailView.findViewById(R.id.endTime);
        TextView itemRemark = (TextView) detailView.findViewById(R.id.itemRemark);

        JSONObject jsonObject = (JSONObject) itemList.getAdapter().getItem(position);
        try{
            itemName.setText(jsonObject.getString("name"));
            itemKind.setText(jsonObject.getString("kind"));
            maxPrice.setText(jsonObject.getString("maxPrice"));
            itemRemark.setText(jsonObject.getString("desc"));
            initPrice.setText(jsonObject.getString("initPrice"));
            endTime.setText(jsonObject.getString("endTime"));
        } catch (JSONException e){
            e.printStackTrace();
        }

        DialogUtil.showDialog(getActivity(), detailView);
    }
}
