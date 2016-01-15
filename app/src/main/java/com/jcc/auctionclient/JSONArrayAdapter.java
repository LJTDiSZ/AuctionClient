package com.jcc.auctionclient;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by juyuan on 1/13/2016.
 */
public class JSONArrayAdapter extends BaseAdapter {
    private Context ctx;
    private JSONArray jsonArray;
    private String property;
    private boolean hasIcon;

    public JSONArrayAdapter(Context ctx, JSONArray jsonArray, String property, boolean hasIcon){
        this.ctx = ctx;
        this.jsonArray = jsonArray;
        this.property = property;
        this.hasIcon = hasIcon;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int i) {
        return jsonArray.optJSONObject(i);
    }

    @Override
    public long getItemId(int i) {
        try {
            return ((JSONObject) getItem(i)).getInt("id");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout linear = new LinearLayout(ctx);
        linear.setOrientation(LinearLayout.HORIZONTAL);

        ImageView iv = new ImageView(ctx);
        iv.setPadding(10, 0, 20, 0);
        iv.setImageResource(R.drawable.item);
        linear.addView(iv);

        TextView tv = new TextView(ctx);
        try{
            String itemName = ((JSONObject)getItem(i)).getString(property);
            tv.setText(itemName);
        } catch (JSONException e){
            e.printStackTrace();
        }
        tv.setTextSize(20);
        if (hasIcon){
            linear.addView(tv);
            return linear;
        } else {
            tv.setTextColor(Color.BLACK);
            return tv;
        }
    }
}
