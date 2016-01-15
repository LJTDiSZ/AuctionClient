package com.jcc.auctionclient;

import android.app.Fragment;

/**
 * Created by juyuan on 1/14/2016.
 */
public class AddItem extends FragmentActivity {
    @Override
    protected Fragment getFragment() {
        return new AddItemFragment();
    }
}
