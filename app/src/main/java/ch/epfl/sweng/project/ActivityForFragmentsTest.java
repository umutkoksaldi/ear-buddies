package ch.epfl.sweng.project; /**
 * Created by Dusan Viktor on 2016-11-04.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import ch.epfl.sweng.project.Fragment.UsersFragment;

/*
* This activity is created in order to be able to
* test the UsersFragment.
*
* */

public  class ActivityForFragmentsTest extends FragmentActivity {
    public UsersFragment usersFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        usersFrag = new UsersFragment();
        fragmentTransaction.add(R.id.activity_main,usersFrag);
        fragmentTransaction.commit();


    }
}
