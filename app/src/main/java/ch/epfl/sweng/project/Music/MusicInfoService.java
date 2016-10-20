package ch.epfl.sweng.project.Music;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class MusicInfoService extends Service {
    String status = null;

    @Override
    public void onCreate(){
        Log.i("MusicInfoService", "Service started");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
