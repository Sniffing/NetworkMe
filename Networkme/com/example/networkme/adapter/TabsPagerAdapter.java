package example.networkme.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import example.networkme.fragments.AppMapFragment;
import example.networkme.fragments.ListFragment;
import example.networkme.fragments.MashFragment;
import example.networkme.fragments.PicsFragment;

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
                return new MashFragment();
            case 1:
                return new AppMapFragment();
            case 2:
                return new ListFragment();
            case 3:
                return new PicsFragment();
        }

        return null;
    }
    


    @Override
    public int getCount() {
        //number of tabs
        return 4;
    }

}
