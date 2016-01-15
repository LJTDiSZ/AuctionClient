package com.jcc.auctionclient;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by juyuan on 1/8/2016.
 */
public class HomeListener implements View.OnClickListener{
    private Activity activity;
    public HomeListener(Activity activity)
    {
        this.activity = activity;
    }
    @Override
    public void onClick(View source)
    {
        Intent i = new Intent(activity , AuctionClientActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(i);
    }
}
