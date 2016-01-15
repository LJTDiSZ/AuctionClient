package com.jcc.auctionclient;


import android.app.Fragment;
import android.os.Bundle;
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
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.cert.TrustAnchor;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewItemFragment extends Fragment {
    Button bnHome;
    ListView succList;
    TextView viewTitle;

    public ViewItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.view_item, container, false);
        bnHome = (Button) rootView.findViewById(R.id.bn_home);
        succList = (ListView) rootView.findViewById(R.id.succList);
        viewTitle = (TextView) rootView.findViewById(R.id.view_title);

        bnHome.setOnClickListener(new HomeListener(getActivity()));
        String action = getArguments().getString("action");
        String url = HttpUtil.BASE_URL + action;
        if (action.equals("viewFail.jsp")){
            viewTitle.setText(R.string.view_fail);
        }

        HttpUtil.sendHttpRequest(url, "GBK", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    final JSONArrayAdapter adapter = new JSONArrayAdapter(getActivity(), jsonArray, "name", true);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            succList.setAdapter(adapter);
                        }
                    });
                } catch (Exception e){
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

        succList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewItemDetail(i);
            }
        });
        return rootView;
    }

    private void viewItemDetail(int position){
        View detailView = getActivity().getLayoutInflater().inflate(R.layout.detail, null);
        TextView itemName = (TextView)detailView.findViewById(R.id.itemName);
        TextView itemKind = (TextView)detailView.findViewById(R.id.itemKind);
        TextView maxPrice = (TextView)detailView.findViewById(R.id.maxPrice);
        TextView itemRemark = (TextView)detailView.findViewById(R.id.itemRemark);
        JSONObject jsonObject = (JSONObject)succList.getAdapter().getItem(position);
        try {
            itemName.setText(jsonObject.getString("name"));
            itemKind.setText(jsonObject.getString("kind"));
            maxPrice.setText(jsonObject.getString("maxPrice"));
            itemRemark.setText(jsonObject.getString("desc"));
        }catch (Exception e){
            e.printStackTrace();
        }
        DialogUtil.showDialog(getActivity(), detailView);
    }
}
