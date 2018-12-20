package me.nhu_duong.blockchaindemo.helpers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class QRDecoder {

    public String Decode(Activity context, Uri uri) throws IOException, NotFoundException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        return Decode(bitmap);
    }

    public String Decode(Bitmap bitmap) throws NotFoundException {
        int width = bitmap.getWidth(), height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        Log.d("TEST", "" + width + " " + height);
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.recycle();
        bitmap = null;
        RGBLuminanceSource rgbSource = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(rgbSource));
        MultiFormatReader reader = new MultiFormatReader();

        Result result = reader.decode(bBitmap);
        Log.e("TEST", "decode result: " + result.getText());
//                Toast.makeText(this, result.getText(), Toast.LENGTH_LONG);
        return result.getText();
    }

    public String Decode(Activity context, String source) throws NotFoundException, FileNotFoundException {
        Log.e("TEST", "start sscaning");
        FileInputStream inputStream = context.openFileInput("qrcode.png");
//            InputStream inputStream = activity.getContentResolver().openInputStream(uri);

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        if (bitmap == null)
        {
            Log.e("TEST", "uri is not a bitmap,");
            return null;
        }
        return Decode(bitmap);
    }
}
