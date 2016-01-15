package com.jcc.auctionclient;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by juyuan on 1/14/2016.
 */
public class KindArrayAdapter extends BaseAdapter {
    private JSONArray kindArray;
    private Context ctx;
    public KindArrayAdapter(JSONArray kindArray, Context ctx){
        this.kindArray = kindArray;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return kindArray.length();
    }

    @Override
    public Object getItem(int i) {
        return kindArray.optJSONObject(i);
    }

    @Override
    public long getItemId(int i) {
        try{
            return ((JSONObject)getItem(i)).getInt("id");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout container = new LinearLayout(ctx);
        container.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout linear = new LinearLayout(ctx);
        linear.setOrientation(LinearLayout.VERTICAL);

        ImageView iv = new ImageView(ctx);
        iv.setPadding(10, 0, 20, 0);
        iv.setImageResource(R.drawable.item);
        linear.addView(iv);

        TextView tv = new TextView(ctx);
        try{
            String kindName = ((JSONObject)getItem(i)).getString("kindName");
            tv.setText(kindName);
        } catch (JSONException e){
            e.printStackTrace();
        }
        tv.setTextSize(20);
        linear.addView(tv);

        container.addView(linear);

        TextView descView = new TextView(ctx);
        descView.setPadding(30, 0, 0, 0);
        try{
            String kindDesc = ((JSONObject)getItem(i)).getString("kindDesc");
            descView.setText(kindDesc);
        } catch (JSONException e){
            e.printStackTrace();
        }
        descView.setTextSize(16);
        container.addView(descView);
        return container;
    }
}
