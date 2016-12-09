package ch.epfl.sweng.project;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.Music;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.media.MusicInfoService;

import static android.content.Intent.CATEGORY_APP_MUSIC;

//import ch.epfl.sweng.project.media.MusicInfoService;


public final class MainActivity extends AppCompatActivity {

    private static final int USERS_AROUND_FRAGMENT = 0;
    private static final int MAP_FRAGMENT = 1;
    private static final int PROFILE_FRAGMENT = 2;
    final int DELAY_MATCH_CALL = 10000;
    final int NOTIFICATION_ID = 0;
    ModelApplication modelApplication = ModelApplication.getModelApplication();
    private TabLayout mTabLayout = null;
    private ViewPager mViewPager = null;
    private boolean expandedMusicHistory = false;
    private Handler mHandler = new Handler();
    private boolean matchDisplayed = false;
    private long lastIDMatched = 0;
    private final Runnable matchRequest = new Runnable() {
        @Override
        public void run() {
            matchSearch();
            mHandler.postDelayed(this, DELAY_MATCH_CALL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        createTabLayout();
        createViewPager();

        // Starts service to be aware if the device is playing music, and gets music information
        Intent musicInfo = new Intent(this, MusicInfoService.class);
        startService(musicInfo);

        mHandler.postDelayed(matchRequest, DELAY_MATCH_CALL);
    }

    private void createTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayoutMain);

        //Adding the tabs
        mTabLayout.addTab(mTabLayout.newTab().setText("Users"));
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

    private void createViewPager() {
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

    // When the user click on the "play" button, he will be redirected to his music player.
    public void launchMusicPlayer(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(CATEGORY_APP_MUSIC);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), R.string.no_music_player_installed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == USERS_AROUND_FRAGMENT) {
            mViewPager.setCurrentItem(MAP_FRAGMENT);
        }
        // TODO implement back stack animation for user music history
        else if (mViewPager.getCurrentItem() == PROFILE_FRAGMENT) {
            mViewPager.setCurrentItem(MAP_FRAGMENT);
        } else if (mViewPager.getCurrentItem() == MAP_FRAGMENT) {
            // Leave the app properly without going back to the welcome activity
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        } else {
            super.onBackPressed();
        }
    }

    public void setExpendedMusicHistory() {
        expandedMusicHistory = true;
    }

    private void matchSearch() {
        User[] otherUsers = ModelApplication.getModelApplication().getOtherUsers();

        if (otherUsers != null) {
            for (int i = 0; i < otherUsers.length; i++) {
                long musicID = otherUsers[i].getCurrentMusicId();
                long ourID = 0;
                Music music = ModelApplication.getModelApplication().getMusic();
                if (music != null && music.getId() != null) {
                    ourID = Long.parseLong(music.getId());
                }

                if (musicID == ourID) {

                    if (musicID != lastIDMatched) {
                        matchDisplayed = false;
                    }

                    if (!matchDisplayed) {
                        lastIDMatched = musicID;
                        displayMatch(i);
                        break;
                    }
                }
            }
        }
    }

    private void displayMatch(int userIndex) {
        Log.i("Match", "It's a match");
        User match = ModelApplication.getModelApplication().getOtherUsers()[userIndex];
        matchDisplayed = true;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(match.getFirstname()+" is listening to the same music!")
                        .setContentText("Tap to learn more.")
                        .setAutoCancel(true);
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public boolean gotMatch() {
        return matchDisplayed;
    }
}