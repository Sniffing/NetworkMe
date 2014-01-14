package example.networkme.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import example.networkme.fragments.MainFragment;
import example.networkme.fragments.MapFragment;
import example.networkme.fragments.TextFragment;
import example.networkme.fragments.PicsFragment;
import example.networkme.fragments.MashFragment;

/**
 * Created by Marcel on 11/9/13.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter{

    public TabsPagerAdapter (FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new MainFragment();
            case 1:
                return new MapFragment();
            case 2:
                return new MashFragment();
            case 3:
                return new TextFragment();
            case 4:
                return new PicsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        //number of tabs
        return 5;
    }

}
