package com.example.bai3trainning;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import static android.os.SystemClock.elapsedRealtime;

public class DownloadServices extends Service {
    public static long downloadID = 0;
    private String URL = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startDownloadFile(intent);
        return START_STICKY;
    }

    public void startDownloadFile(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            URL = intent.getExtras().getString("url");
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("Download File >>>>>");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()));

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadID = downloadManager.enqueue(request);
        }

    }

}
