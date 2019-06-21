package com.example.app_for_rightech_iot_cloud;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment {
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public int currentIndex;

        @Override
        public Fragment getItem(int position)
        {
            if(position == currentIndex){
                return new Now();
            } else {
                return new History();
            }
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        void addFragment(Fragment fragment, String name) {
            fragmentList.add(fragment);
            fragmentTitles.add(name);
        }
    }

    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        tabLayout = rootView.findViewById(R.id.tabs);
        if (Objects.equals(preferences.getString("theme", "light"), "dark")){
            rootView.findViewById(R.id.tabs).setBackgroundColor(Color.parseColor("#18191D"));
            tabLayout.setTabTextColors(Color.parseColor("#E9E9E9"),Color.parseColor("#E9E9E9"));
        }
        else{
            tabLayout.setTabTextColors(Color.parseColor("#000000"),Color.parseColor("#000000"));
            rootView.findViewById(R.id.tabs).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        viewPager = rootView.findViewById(R.id.viewpager);


        viewPager.setCurrentItem(0);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        viewPagerAdapter.currentIndex = 0;

        viewPagerAdapter.addFragment(new Now(), "Состояние");
        viewPagerAdapter.addFragment(new History(), "История");

        viewPager.setAdapter(viewPagerAdapter);
    }
}
