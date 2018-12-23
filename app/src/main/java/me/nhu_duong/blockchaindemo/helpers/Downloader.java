package me.nhu_duong.blockchaindemo.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Downloader {
    public void download(Activity context, String source, String target) {
        int count;
        Log.e("TEST", "Downloading file. " + source);
        try {
            URL url = new URL(source);
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
            OutputStream output = context.openFileOutput(target, Context.MODE_PRIVATE);

            byte data[] = new byte[1024];

            long total = 0;
            Log.d("TEST", "start reading");
            while ((count = input.read(data)) != -1) {
                Log.d("TEST", "read some data");
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
//                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

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
            e.printStackTrace();
            Log.e("TEST", "Error: " + e.getMessage());
        }

        return;
    }

    public byte[] downloadImage(String url, String imgName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.out.println("URL ["+url+"] - Name ["+imgName+"]");

            HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            con.getOutputStream().write( ("name=" + imgName).getBytes());

            InputStream is = con.getInputStream();
            byte[] b = new byte[1024];

            while ( is.read(b) != -1)
                baos.write(b);

            con.disconnect();
        }
        catch(Throwable t) {
            Log.e("TEST", t.getMessage());
            t.printStackTrace();
        }

        return baos.toByteArray();
    }

    public byte[] getBitmapFromURL(String src) throws IOException {
//        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            return readBytes(input);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
    }
    public byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }
}
