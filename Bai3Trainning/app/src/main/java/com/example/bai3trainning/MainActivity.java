package com.example.bai3trainning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.MaskFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText edtUrl;
    private Button btnDownload;
    private ProgressBar progressBar;
    private TextView txtDownload;
    public static final int REQUEST_PERMISON = 10;
    private boolean accessPermision=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission1();
                if(accessPermision) {
                    String Url = edtUrl.getText().toString().trim();
                    Intent intent =new Intent(MainActivity.this,DownloadServices.class);
                    intent.putExtra("url",Url);
                    startService(intent);
                    visibiltyDownloading();
                }
            }
        });

    }


    private void initView() {
        edtUrl = findViewById(R.id.edt_url);
        btnDownload = findViewById(R.id.btn_download);
        progressBar = findViewById(R.id.pgDownload);
        txtDownload = findViewById(R.id.txt_downloading);
    }

    private void checkPermission1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permision = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permision, REQUEST_PERMISON);
            } else
                accessPermision = true ;
        } else
            accessPermision = true ;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISON) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessPermision = true;
            } else {
                accessPermision =false;
            }
        } else {
            accessPermision =false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    private void visibiltyDownloading() {
        txtDownload.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        txtDownload.setText("Downloading !!! Please wait");
    }

    private void goneDownloading() {
        txtDownload.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (DownloadServices.downloadID == id) {
                Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                goneDownloading();
            }
        }
    };
}
