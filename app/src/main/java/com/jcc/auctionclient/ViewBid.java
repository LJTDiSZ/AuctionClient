package com.jcc.auctionclient;

import android.app.Fragment;

/**
 * Created by juyuan on 1/15/2016.
 */
public class ViewBid extends FragmentActivity {
    @Override
    protected Fragment getFragment() {
        return new ViewBidFragment();
    }
}
