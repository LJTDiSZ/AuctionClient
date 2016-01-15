package com.jcc.auctionclient;


import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jcc.auctionclient.util.DialogUtil;
import com.jcc.auctionclient.util.HttpCallbackListener;
import com.jcc.auctionclient.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddKindFragment extends Fragment {
    EditText kindName, kindDesc;
    Button bnAdd, bnCancel;

    public AddKindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_add_kind, container, false);
        kindName = (EditText)rootView.findViewById(R.id.kindName);
        kindDesc = (EditText)rootView.findViewById(R.id.kindDesc);
        bnAdd = (Button)rootView.findViewById(R.id.bnAdd);
        bnCancel = (Button)rootView.findViewById(R.id.bnCancel);

        bnCancel.setOnClickListener(new HomeListener(getActivity()));
        bnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    String name = kindName.getText().toString();
                    String desc = kindDesc.getText().toString();
                    addKind(name, desc);
                }
            }
        });
        return rootView;
    }

    private boolean validate(){
        String name = kindName.getText().toString().trim();
        if (name.equals("")){
            DialogUtil.showDialog(getActivity(), "种类名称是必填项！", false);
            return false;
        }
        return true;
    }

    private void addKind(String name, String desc){
        Map<String, String> map = new HashMap<String, String>();
        map.put("kindName", name);
        map.put("kindDesc", desc);
        String url = HttpUtil.BASE_URL + "addKind.jsp";
        HttpUtil.submitPostData(url, map, "GBK", new HttpCallbackListener() {
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
            public void onError(Exception e) {
                e.printStackTrace();
                final String exMsg = e.getMessage();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showDialog(getActivity(), "服务器响应异常，请稍后再试" + exMsg, false);
                    }
                });
            }
        });
    }
}
