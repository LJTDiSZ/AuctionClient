package com.jcc.auctionclient;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by juyuan on 1/15/2016.
 */
public class AddBid extends FragmentActivity {
    @Override
    protected Fragment getFragment() {
        AddBidFragment fragment = new AddBidFragment();
        Bundle args = new Bundle();
        args.putInt("itemId", getIntent()
                .getIntExtra("itemId", -1));
        fragment.setArguments(args);
        return fragment;
    }
}
