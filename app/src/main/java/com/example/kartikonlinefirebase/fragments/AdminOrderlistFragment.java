package com.example.kartikonlinefirebase.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.adapters.OrderListViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class AdminOrderlistFragment extends Fragment {

    TextView adminVisTv, adminLiveTv, adminTotalTv;

    
    //  @BindView(R.id.admin_ol_tab)
    //TabItem adminOlTab;
    //@BindView(R.id.admin_co_tab)
    //TabItem adminCoTab;
    TabLayout tabLayout;
    ViewPager vPager;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        adminVisTv = (TextView) view.findViewById(R.id.admin_vis_tv);
        adminLiveTv = (TextView) view.findViewById(R.id.admin_live_tv);
        adminTotalTv = (TextView) view.findViewById(R.id.admin_total_tv);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        vPager = (ViewPager) view.findViewById(R.id.v_pager);

        initViewPager();

        return  view;

    }

    private void initViewPager() {
        ArrayList<Fragment> fragments =new ArrayList<>();
        fragments.add(new ConfirmOrderFragment());
        fragments.add(new AdminOrderlistFragment());

        OrderListViewPagerAdapter pagerAdapter = new OrderListViewPagerAdapter(getActivity().getSupportFragmentManager(),getContext(), fragments);
        vPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(vPager);
        tabLayout.getTabAt(0).setText("OrderList");
        tabLayout.getTabAt(1).setText("Confirm Order");
    }

}
