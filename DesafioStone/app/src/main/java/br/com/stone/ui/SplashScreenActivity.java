package br.com.stone.ui;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import br.com.stone.R;

@EActivity(R.layout.activity_splash_screen)
public class SplashScreenActivity extends AppCompatActivity implements Runnable{
    private final int TIME_OF_SPLASHSCREEN = 3 * 1000; //3 Segundos
    private boolean isRunning;
    private Handler handler;

    @AfterViews
    void onInitSplashScreen(){
        isRunning = true;
        handler = new Handler();
        handler.postDelayed(this, TIME_OF_SPLASHSCREEN);
    }

    @Override
    public void run() {
        if(isRunning)
            MainActivity_.intent(this).start();

        finish();
    }

    @Override
    protected void onPause() {
        isRunning = false;
        super.onPause();
    }
}
