package me.nhu_duong.blockchaindemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileNotFoundException;
import java.io.IOException;

import me.nhu_duong.blockchaindemo.helpers.Purchase;
import me.nhu_duong.blockchaindemo.helpers.QRDecoder;
import me.nhu_duong.blockchaindemo.helpers.Util;

public class HomeActivity extends AppCompatActivity {

    protected Button btnScan;
    protected Button btnPurchase;
    protected Button btnLoad;
    protected Activity activity;
    public static final String TAG = "BCA";
    public static final int REQUEST_CODE_SCANNING = 1;
    public static final int REQUEST_CODE_REQUEST_WRITE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnScan = this.findViewById(R.id.btn_scan);
        btnLoad = this.findViewById(R.id.btn_load);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        activity = this;
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setCaptureActivity(CustomerScannerActivity.class);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.setRequestCode(REQUEST_CODE_SCANNING);
                integrator.initiateScan();
            }
        });

        btnPurchase = this.findViewById(R.id.btn_purchase);
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Util u = new Util();
                    u.showMessage(activity, "Your external storage is not mounted. Please check your SD card and continue");
                    return;
                }
                if (checkWriteExternalStoragePermission()) {
                    new DoPayment().execute();
                }
            }
        });
    }

    public class DoPayment extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Purchase purchase = new Purchase();

            try {
                String image = purchase.doPayment(activity);
                return image;
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
//            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Util u = new Util();
            u.showMessage(activity, "Your QR Code is downloaded in Gallery under file name: " + s);
        }
    }

    protected boolean checkWriteExternalStoragePermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_REQUEST_WRITE_PERMISSION);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_REQUEST_WRITE_PERMISSION && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            new DoPayment().execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SCANNING) {
            handleActivityScanningResult(resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void handleActivityScanningResult(int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        if (result == null) {
            return;
        }
        if (result.getContents() == null) {
            Toast.makeText(this, "You closed the scan", Toast.LENGTH_LONG);
            Log.d("BCA", "You closed the scan");
            return;
        }
        Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG);
        Intent intent = new Intent(activity, CheckingActivity.class);
        intent.putExtra("code_result", result.getContents());
        startActivity(intent);
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }
}
