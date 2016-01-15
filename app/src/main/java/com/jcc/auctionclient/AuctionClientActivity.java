package com.jcc.auctionclient;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AuctionClientActivity extends AppCompatActivity implements Callbacks {

    public static boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_client);

        if (findViewById(R.id.auction_detail_container) != null){
            mTwoPane = true;
            ((AuctionListFragment)getFragmentManager().findFragmentById(R.id.auction_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(Integer id, Bundle bundle) {
        if (mTwoPane){
            Fragment fragment = null;
            switch ((int)id){
                case 0:
                    fragment = new ViewItemFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString("action", "viewSucc.jsp");
                    fragment.setArguments(arguments);
                    break;
                case 1:
                    fragment = new ViewItemFragment();
                    Bundle arguments2 = new Bundle();
                    arguments2.putString("action", "viewFail.jsp");
                    fragment.setArguments(arguments2);
                    break;
                case 2:
                    fragment = new ManageKindFragment();
                    break;
                case 3:
                    fragment = new ManageItemFragment();
                    break;
                case 4:
                    fragment = new ChooseKindFragment();
                    break;
                case 5:
                    fragment = new ViewBidFragment();
                    break;
                case ManageItemFragment.ADD_ITEM:
                    fragment = new AddItemFragment();
                    break;
                case ManageKindFragment.ADD_KIND:
                    fragment = new AddKindFragment();
                    break;
                case ChooseKindFragment.CHOOSE_ITEM:
                    fragment = new ChooseItemFragment();
                    Bundle args = new Bundle();
                    args.putLong("kindId", bundle.getLong("kindId"));
                    fragment.setArguments(args);
                    break;
                case ChooseItemFragment.ADD_BID:
                    fragment = new AddBidFragment();
                    Bundle args2 = new Bundle();
                    args2.putInt("itemId", bundle.getInt("itemId"));
                    fragment.setArguments(args2);
                    break;
            }
            getFragmentManager().beginTransaction().replace(R.id.auction_detail_container, fragment)
                    .addToBackStack(null).commit();
        } else {
            Intent intent = null;
            switch ((int)id){
                case 0:
                    intent = new Intent(this, ViewItem.class);
                    intent.putExtra("action", "viewSucc.jsp");
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(this, ViewItem.class);
                    intent.putExtra("action", "viewFail.jsp");
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(this, ManageKind.class);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(this, ManageItem.class);
                    startActivity(intent);
                    break;
                case 4:
                    intent = new Intent(this, ChooseKind.class);
                    startActivity(intent);
                    break;
                case 5:
                    intent = new Intent(this, ViewBid.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
