package me.nhu_duong.blockchaindemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import me.nhu_duong.blockchaindemo.helpers.Util;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    protected String filePath = Environment.getDataDirectory().getPath() + "/qrcode.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = this.findViewById(R.id.btn_login);
        final Activity activity = this;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtUsername = activity.findViewById(R.id.user_name);
                Util.username = txtUsername.getText().toString();
                Intent intent = new Intent(activity, HomeActivity.class);
                startActivity(intent);
            }
        });

//        new DownloadFileFromURL().execute("https://upload.wikimedia.org/wikipedia/commons/thumb/3/30/Superqr.svg/525px-Superqr.svg.png");
//        new DownloadFileFromURL().execute("http://192.168.1.7/img/qrcode.png");
//        this.scanLocalFile(activity);
        Log.d("TEST", Environment.getExternalStorageState());
    }

    protected void scanLocalFile(Activity activity) {

//        Uri uri = Uri.parse(filePath);
//        File file = new File(filePath);
//        if (file.exists()) {
//            Log.d("TEST", "can read");
//        } else {
//            Log.d("TEST", "can't read");
//        }

    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            Log.e("TEST", "Downloading file. " + f_url[0]);
            try {
                URL url = new URL(f_url[0]);
                Log.e("TEST", "1");
                URLConnection conection = url.openConnection();
                Log.e("TEST", "2");
                conection.connect();
                Log.e("TEST", "connected");
                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
//                Log.d("TEST", Environment
//                        .getExternalStorageDirectory().toString());
//                OutputStream output = new FileOutputStream(Environment
//                        .getExternalStorageDirectory().toString()
//                        + "/frame4.png");
                OutputStream output = openFileOutput("qrcode.png", Context.MODE_PRIVATE);

                byte data[] = new byte[1024];

                long total = 0;
Log.d("TEST", "start reading");
                while ((count = input.read(data)) != -1) {
                    Log.d("TEST", "read some data");
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                Log.e("TEST", "finish");
                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
//            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
//            dismissDialog(progress_bar_type);

        }

    }
}
