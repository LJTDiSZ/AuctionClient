package com.jcc.auctionclient;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * Created by juyuan on 1/14/2016.
 */
public abstract class FragmentActivity extends Activity {
    private static final int ROOT_CONTAINER_ID = 0x9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        setContentView(layout);
        layout.setId(ROOT_CONTAINER_ID);
        getFragmentManager().beginTransaction().replace(ROOT_CONTAINER_ID, getFragment()).commit();
    }

    protected abstract Fragment getFragment();
}
