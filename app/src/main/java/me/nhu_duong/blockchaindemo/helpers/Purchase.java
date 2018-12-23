package me.nhu_duong.blockchaindemo.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import me.nhu_duong.blockchaindemo.R;

public class Purchase {

    public String doPayment(Activity context) throws IOException {
        Downloader d = new Downloader();
        Util u = new Util();
        Long currentTime = u.getCurrentTimeStamp();
        String target = "code_" + currentTime.toString() + ".png";
        String url = context.getResources().getString(R.string.domain) + "/products/purchase?username=" + Util.username;

        byte[] result = d.getBitmapFromURL(url);
//        FileOutputStream os = context.openFileOutput(target, Context.MODE_PRIVATE);
        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/BCA";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        String filePath = folderPath + "/" + target;
        FileOutputStream os = new FileOutputStream(filePath);

        try {
            os.write(result);
            os.flush();
            os.close();

            galleryAddPic(context, new File(filePath));
            Log.e("BCA", "added to gallery");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("BCA", e.getMessage());
            return "do payment error: " + e.getMessage();
        }

        return target;
    }

    private void galleryAddPic(Activity context, File f) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        Log.e("BCA", f.getAbsolutePath());
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
