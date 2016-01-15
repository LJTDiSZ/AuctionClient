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

import com.jcc.auctionclient.util.DialogUtil;
import com.jcc.auctionclient.util.HttpCallbackListener;
import com.jcc.auctionclient.util.HttpUtil;

import org.json.JSONArray;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseKindFragment extends Fragment {
    public static final int CHOOSE_ITEM = 0x1008;
    Callbacks mCallbacks;
    Button bnHome;
    ListView kindList;

    public ChooseKindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_choose_kind, container, false);
        bnHome = (Button) rootView.findViewById(R.id.bn_home);
        kindList = (ListView) rootView.findViewById(R.id.kindList);
        bnHome.setOnClickListener(new HomeListener(getActivity()));
        kindList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putLong("kindId", l);
                mCallbacks.onItemSelected(CHOOSE_ITEM, bundle);
            }
        });

        HttpUtil.sendHttpRequest(HttpUtil.BASE_URL + "viewKind.jsp", "GBK", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    final JSONArray jsonArray = new JSONArray(response);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            kindList.setAdapter(new KindArrayAdapter(jsonArray, getActivity()));
                            kindList.invalidate();
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

        return  rootView;
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
