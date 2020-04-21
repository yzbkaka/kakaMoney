package com.example.yzbkaka.kakamoney.home;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.yzbkaka.kakamoney.R;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private Toolbar toolbar;

    private ViewPager viewPager;

    private List<String> titleList = new ArrayList<>();

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        tabLayout = (TabLayout)findViewById(R.id.add_tablayout);
        toolbar = (Toolbar)findViewById(R.id.add_toolbar);
        viewPager = (ViewPager)findViewById(R.id.add_viewpager);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView(){
        titleList.add("流出");
        titleList.add("流入");
        for(int i = 0;i < titleList.size();i++) tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
        fragmentList.add(new AddOutFragment());
        fragmentList.add(new AddInFragment());
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(fragmentAdapter);
    }

    /**
     * viewPager适配器
     */
    class FragmentAdapter extends FragmentStatePagerAdapter{
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
