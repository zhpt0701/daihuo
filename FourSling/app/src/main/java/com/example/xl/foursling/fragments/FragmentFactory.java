package com.example.xl.foursling.fragments;






import com.example.xl.foursling.fragments.message.FragmentDeposit;
import com.example.xl.foursling.fragments.message.FreightFragment;

import java.util.HashMap;

/**
 * fragment工厂类
 * Created by xl on 2016/12/15.
 */
public class FragmentFactory {
    private static HashMap<Integer, Fragment> savedFragment = new HashMap<Integer, Fragment>();

    public static Fragment getFragment(int position) {
        Fragment fragment = savedFragment.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new MessageFragment();
                    break;
                case 1:
                    fragment = new UserEnterFragment();
                    break;
                case 2:
                    fragment = new RoderFragment();
                    break;
                case 3:
                    fragment = new MoneyFragment();
                    break;
                case 4:
                    fragment = new RouteFragment();
                    break;
                case 5:
                    fragment = new SettingFragment();
                    break;
                case 6:
                    fragment = new FreightFragment();
                    break;
                case 7:
                    fragment = new FragmentDeposit();
                    break;
            }
            savedFragment.put(position, fragment);
        }
        return fragment;
    }
}
