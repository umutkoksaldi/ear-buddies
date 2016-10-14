package ch.epfl.sweng.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Login extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageButton facebook = (ImageButton) findViewById(R.id.loginButton);
        setTitle("Love at First Song");
        facebook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition( R.anim.slide_in_up, R.anim.silde_up_out );
    }
}