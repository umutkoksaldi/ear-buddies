package ch.epfl.sweng.project;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public final class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createTabLayout();
        createViewPager();



    }

    private void createTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayoutMain);

        //Adding the tabs
        mTabLayout.addTab(mTabLayout.newTab().setText("Blank"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Map"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Profile"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //noinspection ConstantConditions
        mTabLayout.getTabAt(1).select();
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void createViewPager(){
        mViewPager = (ViewPager) findViewById(R.id.pagerMain);
        Pager adapter = new Pager(getSupportFragmentManager(), mTabLayout.getTabCount());

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position >= 0 && position < mTabLayout.getTabCount()) {
                    //noinspection ConstantConditions
                    mTabLayout.getTabAt(position).select();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(1);
    }
}