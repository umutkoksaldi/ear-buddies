package ch.epfl.sweng.project;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Your app's main activity.
 */
public final class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private final int NUMBER_OF_FRAG = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.pagerMain);
        Pager adapter = new Pager(getSupportFragmentManager(), NUMBER_OF_FRAG);

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
    }
}