package ch.epfl.sweng.project.view.activity;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.controlers.UserDetailsControler;
import ch.epfl.sweng.project.controlers.UserSongControler;
import ch.epfl.sweng.project.medias.MusicInfoService;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.models.User;
import ch.epfl.sweng.project.view.util_view.Pager;

import static android.content.Intent.CATEGORY_APP_MUSIC;
import static ch.epfl.sweng.project.util_constant.GlobalSetting.FRAGMENT_MAP;
import static ch.epfl.sweng.project.util_constant.GlobalSetting.FRAGMENT_PROFILE;
import static ch.epfl.sweng.project.util_constant.GlobalSetting.FRAGMENT_USERS;

//import ch.epfl.sweng.project.media.MusicInfoService;


public final class MainActivity extends AppCompatActivity {

    private static FragmentManager fragmentManager = null;
    final int DELAY_MATCH_CALL = 10000;
    final int NOTIFICATION_ID = 0;
    ModelApplication modelApplication = ModelApplication.getModelApplication();
    private TabLayout mTabLayout = null;
    private boolean isActive = true;
    private ViewPager mViewPager = null;
    private Handler mHandler = new Handler();
    private final Runnable matchRequest = new Runnable() {
        @Override
        public void run() {
            matchSearch();
            mHandler.postDelayed(this, DELAY_MATCH_CALL);
        }
    };
    private boolean matchDisplayed = false;
    private long lastIDMatched = 0;
    private UserDetailsControler userDetailsControler = UserDetailsControler.getConnectionControler();

    public static FragmentManager getMainActivityFragmentManager() {
        return fragmentManager;
    }

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
        fragmentManager = getFragmentManager();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    private void createTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayoutMain);

        ColorStateList colors;
        if (Build.VERSION.SDK_INT >= 23) {
            colors = getResources().getColorStateList(R.color.tab_icon, getTheme());
        } else {
            colors = getResources().getColorStateList(R.color.tab_icon);
        }

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            assert tab != null;
            Drawable icon = tab.getIcon();

            if (icon != null) {
                icon = DrawableCompat.wrap(icon);
                DrawableCompat.setTintList(icon, colors);
            }
        }


        //noinspection ConstantConditions
        mTabLayout.getTabAt(1).select();
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                mViewPager.setCurrentItem(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if (FRAGMENT_USERS == pos) {
                    // Leave the detail fragment if it had been opened
                    if (UserDetailsControler.getConnectionControler().isOpenFromUserList()) {
                        getSupportFragmentManager().popBackStackImmediate();
                        userDetailsControler.setOpenFromUserList(false);
                    }
                }
                if (FRAGMENT_MAP == pos) {
                    // Leave the detail fragment if it had been opened
                    if (UserDetailsControler.getConnectionControler().isOpenFromMap()) {
                        getSupportFragmentManager().popBackStackImmediate();
                        userDetailsControler.setOpenFromMap(false);
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if (FRAGMENT_USERS == pos) {
                    // Leave the detail fragment if it had been opened
                    if (UserDetailsControler.getConnectionControler().isOpenFromUserList()) {
                        getSupportFragmentManager().popBackStackImmediate();
                        userDetailsControler.setOpenFromUserList(false);
                    }
                }
                if (FRAGMENT_MAP == pos) {
                    // Leave the detail fragment if it had been opened
                    if (UserDetailsControler.getConnectionControler().isOpenFromMap()) {
                        getSupportFragmentManager().popBackStackImmediate();
                        userDetailsControler.setOpenFromMap(false);
                    }
                }
            }
        });
    }

    private void createViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.pagerMain);
        Pager adapter = new Pager(getSupportFragmentManager(), mTabLayout.getTabCount());
        // Cause an issue with users fragment refreshing
        mViewPager.setOffscreenPageLimit(2);
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
        UserDetailsControler userDetailsControler = UserDetailsControler.getConnectionControler();

        if (mViewPager.getCurrentItem() == FRAGMENT_USERS) {
            if (userDetailsControler.isOpenFromUserList()) {
                // Check if we are on a user details fragment
                super.onBackPressed();
                userDetailsControler.setOpenFromUserList(false);
            } else {
                // Go from the people fragment to the map fragment
                mViewPager.setCurrentItem(FRAGMENT_MAP);
            }

        } else if (mViewPager.getCurrentItem() == FRAGMENT_PROFILE) {
            mViewPager.setCurrentItem(FRAGMENT_MAP);
        } else if (mViewPager.getCurrentItem() == FRAGMENT_MAP) {
            if (userDetailsControler.isOpenFromMap()) {
                // Check if we are on a user details fragment
                super.onBackPressed();
                userDetailsControler.setOpenFromMap(false);
            } else {
                // Leave the app properly without going back to the welcome activity
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        } else {
            super.onBackPressed();
        }
    }

    private void matchSearch() {

        ModelApplication ma = ModelApplication.getModelApplication();
        User[] otherUsers = ma.getOtherUsers();
        UserSongControler userSongControler = UserSongControler.getUserSongControler();
        User ourUser = ma.getUser();
        long ourMusicId = ourUser.getCurrentMusicId();
        if (ourMusicId != 0) {
            Music ourMusic = ma.getMusic();
            if (ourMusic != null) {
                checkStillSameMusic(ourMusic);
                if (otherUsers != null) {
                    for (int i = 0; i < otherUsers.length; i++) {
                        long musicId = otherUsers[i].getCurrentMusicId();

                        if (musicId != 0) {
                            Music otherMusic = userSongControler.getSongMap().get(otherUsers[i].getIdApiConnection());

                            if (otherMusic != null && otherMusic.getArtist().equals(ourMusic.getArtist()) && otherMusic
                                    .getName().equals(ourMusic.getName())) {

                                Music lastMatchedMusic = ma.getLastMatchedMusic();
                                if (lastMatchedMusic == null || !(ourMusic.getArtist().equals(lastMatchedMusic
                                        .getArtist()) && ourMusic.getName().equals(lastMatchedMusic.getName()))) {
                                    ma.setMatchDisplayed(false);
                                }

                                if (!ma.isMatchDisplayed()) {
                                    ma.setLastMatchedMusic(ourMusic);
                                    displayMatch(i);
                                    ma.setMatchDisplayed(true);
                                    ma.setZoomedOnMatch(false);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkStillSameMusic(Music ourMusic) {
        if (modelApplication.getLastMatchedMusic() != null) {
            //We are not listening to the same matched music
            if (!(modelApplication.getLastMatchedMusic().getArtist().equals(ourMusic.getArtist()) &&
                    modelApplication.getLastMatchedMusic().getArtist().equals(ourMusic.getArtist()))) {
                modelApplication.setMatchedUser(null);
            }
        }
    }

    private void displayMatch(int userIndex) {
        User match = ModelApplication.getModelApplication().getOtherUsers()[userIndex];
        ModelApplication.getModelApplication().setMatchedUser(match);
        Log.i("Match", "It's a match with " + match.getFirstname());

        //the isActive is commented because the service is not running background and we didn't have time to go
        // through now
        // if (!isActive) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_whatshot_black_24dp)
                        .setContentTitle(match.getFirstname() + " is listening to the same music!")
                        .setContentText("Tap to learn more.")
                        .setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        //   }
    }

    public boolean gotMatch() {
        return modelApplication.isMatchDisplayed();
    }
}