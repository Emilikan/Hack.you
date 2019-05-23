package com.example.app_for_rightech_iot_cloud;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
public class Swipe extends FragmentStatePagerAdapter {
    private String tabTitles[] = new String[] { "Состояние", "История"};
    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Swipe(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem

    public Fragment getItem( int  position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Now now = new Now();
                return now;
            case 1:
                Hisrory hist = new Hisrory();
                return hist;
            default:
                return null;
        }
    }
    @Override public CharSequence getPageTitle(int position) {
        // генерируем заголовок в зависимости от позиции
        return tabTitles[position];
    }
    //Overriden method getCount to get the number of tabsd
    @Override
    public int getCount() {
        return tabCount;
    }
}
