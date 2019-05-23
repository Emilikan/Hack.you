package com.example.app_for_rightech_iot_cloud;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.widget.Button;


public class Main extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        Button notify = view.findViewById(R.id.btn_notific);
        Button neyro = view.findViewById(R.id.btn_neyro);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new Notifications();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
        neyro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new Neuronet();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);

        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));


        tabLayout.setTabTextColors(Color.parseColor("#469232"), Color.parseColor("#B71C1C"));

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);


        Swipe adapter = new Swipe(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        //установка adapter to pager
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //метод прослушивания свайпа по view
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

}
