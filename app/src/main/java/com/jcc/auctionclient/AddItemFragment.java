package com.jcc.auctionclient;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jcc.auctionclient.util.DialogUtil;
import com.jcc.auctionclient.util.HttpCallbackListener;
import com.jcc.auctionclient.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemFragment extends Fragment {
    EditText itemName, itemDesc, itemRemark, initPrice;
    Spinner itemKind, availTime;
    Button bnAdd, bnCancel;

    public AddItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);
        itemName = (EditText) rootView.findViewById(R.id.itemName);
        itemDesc = (EditText) rootView.findViewById(R.id.itemDesc);
        itemRemark = (EditText) rootView.findViewById(R.id.itemRemark);
        initPrice = (EditText) rootView.findViewById(R.id.initPrice);
        itemKind = (Spinner) rootView.findViewById(R.id.itemKind);
        availTime = (Spinner) rootView.findViewById(R.id.availTime);
        bnAdd = (Button) rootView.findViewById(R.id.bnAdd);
        bnCancel = (Button) rootView.findViewById(R.id.bnCancel);

        String url = HttpUtil.BASE_URL + "viewKind.jsp";
        HttpUtil.sendHttpRequest(url, "GBK", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    final JSONArray jsonArray = new JSONArray(response);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            itemKind.setAdapter(new JSONArrayAdapter(getActivity(), jsonArray, "kindName", false));
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        bnCancel.setOnClickListener(new HomeListener(getActivity()));
        bnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    String name = itemName.getText().toString();
                    String desc = itemDesc.getText().toString();
                    String remark = itemRemark.getText().toString();
                    String price = initPrice.getText().toString();
                    JSONObject kind = (JSONObject)itemKind.getSelectedItem();
                    int avail = availTime.getSelectedItemPosition();
                    switch (avail){
                        case 5:
                            avail = 7;
                            break;
                        case 6:
                            avail = 30;
                            break;
                        default:
                            avail += 1;
                            break;
                    }
                    try {
                        addItem(name, desc, remark, price, kind.getInt("id"), avail);
                    }catch (JSONException e){
                        e.printStackTrace();
                        DialogUtil.showDialog(getActivity(), "错误" + e.getMessage(), false);
                    }
                }
            }
        });
        return rootView;
    }

    private boolean validate(){
        String name = itemName.getText().toString().trim();
        if (name.equals("")){
            DialogUtil.showDialog(getActivity(), "物品名称是必填项！", false);
            return false;
        }
        String price = initPrice.getText().toString().trim();
        if (price.equals("")){
            DialogUtil.showDialog(getActivity(), "起拍价格是必填项！", false);
            return false;
        }
        try{
            Double.parseDouble(price);
        } catch (NumberFormatException e){
            DialogUtil.showDialog(getActivity(), "起拍价格是数值！", false);
            return false;
        }
        return true;
    }

    private void addItem(String name, String desc, String remark, String initPrice, int kindId, int availTime){
        Map<String, String> map = new HashMap<String, String>();
        map.put("itemName", name);
        map.put("itemDesc", desc);
        map.put("itemRemark", remark);
        map.put("initPrice", initPrice);
        map.put("kindId", kindId + "");
        map.put("availTime", availTime + "");
        String url = HttpUtil.BASE_URL + "addItem.jsp";
        HttpUtil.submitPostData(url, map, "GBK", new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showDialog(getActivity(), response, false);
                        if (AuctionClientActivity.mTwoPane) {
                            getActivity().getFragmentManager().popBackStack();
                        } else {
                            getActivity().finish();
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showDialog(getActivity(), e.getMessage(), false);
                    }
                });
            }
        });
    }

}
