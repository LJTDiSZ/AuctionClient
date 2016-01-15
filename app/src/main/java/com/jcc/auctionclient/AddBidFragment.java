package com.jcc.auctionclient;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jcc.auctionclient.util.DialogUtil;
import com.jcc.auctionclient.util.HttpCallbackListener;
import com.jcc.auctionclient.util.HttpUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBidFragment extends Fragment {
    TextView itemName, itemDesc, itemRemark, itemKind, initPrice, maxPrice, endTime;
    EditText bidPrice;
    Button bnAdd, bnCancel;
    JSONObject jsonObject;

    public AddBidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_add_bid, container, false);
        itemName = (TextView) rootView.findViewById(R.id.itemName);
        itemDesc = (TextView) rootView.findViewById(R.id.itemDesc);
        itemRemark = (TextView) rootView.findViewById(R.id.itemRemark);
        itemKind = (TextView) rootView.findViewById(R.id.itemKind);
        initPrice = (TextView) rootView.findViewById(R.id.initPrice);
        maxPrice = (TextView) rootView.findViewById(R.id.maxPrice);
        endTime = (TextView) rootView.findViewById(R.id.endTime);
        bidPrice = (EditText) rootView.findViewById(R.id.bidPrice);
        bnAdd = (Button) rootView.findViewById(R.id.bnAdd);
        bnCancel = (Button) rootView.findViewById(R.id.bnCancel);
        bnCancel.setOnClickListener(new HomeListener(getActivity()));

//        bnAdd.setEnabled(false);
        String url = HttpUtil.BASE_URL + "getItem.jsp?itemId=" + getArguments().getInt("itemId");
        HttpUtil.sendHttpRequest(url, "GBK", new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonObject = new JSONObject(response);
                            itemName.setText(jsonObject.getString("name"));
                            itemDesc.setText(jsonObject.getString("desc"));
                            itemRemark.setText(jsonObject.getString("remark"));
                            itemKind.setText(jsonObject.getString("kind"));
                            initPrice.setText(jsonObject.getString("initPrice"));
                            maxPrice.setText(jsonObject.getString("maxPrice"));
                            endTime.setText(jsonObject.getString("endTime"));
//                            bnAdd.setEnabled(true);
                        }catch (Exception e){
                            final String exMsg = e.getMessage();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DialogUtil.showDialog(getActivity(), "数据错误！" + exMsg, false);
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                });
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

        bnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    double curPrice = Double.parseDouble(bidPrice.getText().toString());
                    if (curPrice < jsonObject.getDouble("maxPrice")) {
                        DialogUtil.showDialog(getActivity(), "你输入的竞价必须高于当前竞价！", false);
                    } else {
                        addBid(jsonObject.getString("id"), curPrice + "");
                    }
                } catch (NumberFormatException ne) {
                    DialogUtil.showDialog(getActivity(), "你输入的竞价必须是数值！", false);
                } catch (Exception e){
                    e.printStackTrace();
                    DialogUtil.showDialog(getActivity(), "服务器响应异常，请稍后再试！" + e.getMessage(), false);
                }
            }
        });
        return rootView;
    }

    private void addBid(String itemId, String bidPrice){
        Map<String, String> map = new HashMap<String, String>();
        map.put("itemId", itemId);
        map.put("bidPrice", bidPrice);
        Log.d("AuctioinC", itemId + "=" + bidPrice);
        HttpUtil.submitPostData(HttpUtil.BASE_URL + "addBid.jsp", map, "GBK", new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showDialog(getActivity(), response, true);
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showDialog(getActivity(), "服务器响应异常，请稍后再试！" + e.getMessage(), false);
                    }
                });
                e.printStackTrace();
            }
        });
    }
}
