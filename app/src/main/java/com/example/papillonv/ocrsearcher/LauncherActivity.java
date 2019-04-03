package com.example.papillonv.ocrsearcher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class LauncherActivity extends AppCompatActivity {

    public final static int CAMERA_PERMISSION = 100;
    /*public final static int EXTERNAL_READ_PERMISSION = 101;
    public final static int EXTERNAL_WRITE_PERMISSION = 102;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION);
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(),SearcherActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

          if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
          {
              if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
              {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                   if (checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)
                   {
                       Toast.makeText(this, "İzin Verildi!", Toast.LENGTH_SHORT).show();

                       Intent intent = new Intent(getApplicationContext(),SearcherActivity.class);
                       startActivity(intent);
                       finish();
                   }
                   else
                   {
                       requestPermissions(new String[]{Manifest.permission.INTERNET},3);
                   }
                }
                else
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                }
              }
              else
              {
                  requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
              }
          }
          else
          {
              requestPermissions(new String[]{Manifest.permission.CAMERA},0);
              Toast.makeText(this, "İzin Verilmedi!", Toast.LENGTH_SHORT).show();
              finish();
          }

        }
}
