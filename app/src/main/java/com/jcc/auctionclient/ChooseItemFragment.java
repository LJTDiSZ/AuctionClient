package com.jcc.auctionclient;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.telecom.Call;
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
public class ChooseItemFragment extends Fragment {
    public static final int ADD_BID = 0x1009;
    Button bnHome;
    ListView succList;
    TextView viewTitle;
    Callbacks mCallbacks;

    public ChooseItemFragment() {
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
        long kindId = getArguments().getLong("kindId");
        String url = HttpUtil.BASE_URL + "itemList.jsp?kindId=" + kindId;
        HttpUtil.sendHttpRequest(url, "GBK", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    final JSONArray jsonArray = new JSONArray(response);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            succList.setAdapter(new JSONArrayAdapter(getActivity(), jsonArray, "name", true));
                        }
                    });
                } catch (Exception e) {
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

        viewTitle.setText(R.string.item_list);
        succList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject jsonObject = (JSONObject) succList.getAdapter().getItem(i);
                Bundle bundle = new Bundle();
                try {
                    bundle.putInt("itemId", jsonObject.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mCallbacks.onItemSelected(ADD_BID, bundle);
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof Activity)){
            throw new IllegalStateException("ChooseKindFragment所在的Activity必须实现Callbacks接口!");
        }
        Activity a = (Activity)context;
        if (!(a instanceof Callbacks)){
            throw new IllegalStateException("ChooseKindFragment所在的Activity必须实现Callbacks接口!");
        }
        mCallbacks = (Callbacks) a;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)){
            throw new IllegalStateException("ChooseKindFragment所在的Activity必须实现Callbacks接口!");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
