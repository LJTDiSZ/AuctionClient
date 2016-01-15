package com.jcc.auctionclient;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.jcc.auctionclient.util.DialogUtil;
import com.jcc.auctionclient.util.HttpCallbackListener;
import com.jcc.auctionclient.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageKindFragment extends Fragment {
    public static final int ADD_KIND = 0x1007;

    Button bnHome, bnAdd;
    ListView kindList;
    Callbacks mCallbacks;

    public ManageKindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manage_kind, container, false);
        bnHome = (Button) rootView.findViewById(R.id.bn_home);
        bnAdd = (Button) rootView.findViewById(R.id.bnAdd);
        kindList = (ListView) rootView.findViewById(R.id.kindList);
        bnHome.setOnClickListener(new HomeListener(getActivity()));
        bnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onItemSelected(ADD_KIND, null);
            }
        });

        String url = HttpUtil.BASE_URL + "viewKind.jsp";
        HttpUtil.sendHttpRequest(url, "GBK", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    final JSONArray jsonArray = new JSONArray(response);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            kindList.setAdapter(new KindArrayAdapter(jsonArray, getActivity()));
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
}
