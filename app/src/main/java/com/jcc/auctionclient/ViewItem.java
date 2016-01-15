package com.jcc.auctionclient;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by juyuan on 1/14/2016.
 */
public class ViewItem extends FragmentActivity {
    @Override
    protected Fragment getFragment() {
        ViewItemFragment fragment = new ViewItemFragment();
        Bundle arguments = new Bundle();
        arguments.putString("action", getIntent().getStringExtra("action"));
        fragment.setArguments(arguments);
        return fragment;
    }
}
