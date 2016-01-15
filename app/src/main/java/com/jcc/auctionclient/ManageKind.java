package com.jcc.auctionclient;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by juyuan on 1/14/2016.
 */
public class ManageKind  extends FragmentActivity implements Callbacks{
    @Override
    public void onItemSelected(Integer id, Bundle bundle) {
        Intent i = new Intent(this, AddKind.class);
        startActivity(i);
    }

    @Override
    protected Fragment getFragment() {
        return new ManageKindFragment();
    }
}
