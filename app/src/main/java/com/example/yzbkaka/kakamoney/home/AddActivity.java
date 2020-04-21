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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.R;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private Toolbar toolbar;

    private ViewPager viewPager;

    private List<String> titleList = new ArrayList<>();

    private List<Fragment> fragmentList = new ArrayList<>();

    private int count = 0;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                count++;
                if(count == 2){
                    finish();
                }else{
                    Toast.makeText(this, "别忘了点击右上角进行存储哦", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.save:
                Fragment fragment = getCurrentFragment();
                if(fragment instanceof AddOutFragment){
                    ((AddOutFragment) fragment).saveOut();
                    Toast.makeText(this, "存储成功", Toast.LENGTH_SHORT).show();
                }else{

                }
                finish();
                break;

        }
        return false;
    }

    /**
     * 获取当前展示的是哪一个fragment
     */
    public Fragment getCurrentFragment(){
        FragmentManager fragmentManager = AddActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    /**
     * 对返回按钮进行监听
     */
    @Override
    public void onBackPressed() {
        count++;
        if(count == 2){
            finish();
        }else{
            Toast.makeText(this, "别忘了点击右上角进行存储哦", Toast.LENGTH_SHORT).show();
        }
    }
}
