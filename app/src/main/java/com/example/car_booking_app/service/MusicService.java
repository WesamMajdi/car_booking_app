package com.example.car_booking_app.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.car_booking_app.R;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize MediaPlayer
        // Note: Replace with actual raw resource like R.raw.background_music
        // Or use a URL for testing:
        try {
            // This is just a placeholder. In a real app, you'd have a file in res/raw
            // mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
            Log.d(TAG, "Service Created");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing MediaPlayer", e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service Started");
        
        // Start playing music
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        // Return START_STICKY to restart service if killed by system
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service Destroyed");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
