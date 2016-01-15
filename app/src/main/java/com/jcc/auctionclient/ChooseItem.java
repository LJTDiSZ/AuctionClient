package com.jcc.auctionclient;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by juyuan on 1/15/2016.
 */
public class ChooseItem extends FragmentActivity implements Callbacks {

    @Override
    public void onItemSelected(Integer id, Bundle bundle) {
        Intent intent = new Intent(this, AddBid.class);
        intent.putExtra("itemId", bundle.getInt("itemId"));
        startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        ChooseItemFragment fragment = new ChooseItemFragment();
        Bundle arg = new Bundle();
        arg.putLong("kindId", getIntent().getLongExtra("kindId", -1));
        fragment.setArguments(arg);
        return fragment;
    }
}
