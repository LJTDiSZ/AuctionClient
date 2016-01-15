package com.jcc.auctionclient;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by juyuan on 1/15/2016.
 */
public class ChooseKind extends FragmentActivity implements Callbacks {
    @Override
    protected Fragment getFragment() {
        return new ChooseKindFragment();
    }

    @Override
    public void onItemSelected(Integer id, Bundle bundle) {
        Intent i = new Intent(this, ChooseItem.class);
        i.putExtra("kindId", bundle.getLong("kindId"));
        startActivity(i);
    }
}
