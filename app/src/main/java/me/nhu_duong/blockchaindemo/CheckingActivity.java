package me.nhu_duong.blockchaindemo;

import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import me.nhu_duong.blockchaindemo.helpers.Util;

public class CheckingActivity extends AppCompatActivity {

    protected LinearLayout scanningBox;
    protected TextView txtMessage;
    protected String message;
    protected int color;
    protected boolean isFlashing;
    protected CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);
        scanningBox = findViewById(R.id.check_activity_scanning_box);
        txtMessage = findViewById(R.id.text_view_message);
        setStatusScanning("Scanning in progress");
        startAnimation();

        String code = getIntent().getStringExtra("code_result");
        String url = this.getResources().getString(R.string.domain) + "/products/activate?code=" + code + "&username=" + Util.username;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean result = response.getBoolean("result");
                            String message = response.getString("message");
                            if (result) {
                                setStatusSuccess(message);
                            } else {
                                setStatusFailed(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                requestQueue.add(jsonObjectRequest);
            }
        }, 2000);

    }

    protected void startAnimation() {
        countDownTimer = new CountDownTimer(1000000, 500) {

            public void onTick(long millisUntilFinished) {
                if (!isFlashing || ((millisUntilFinished % 1000) > 500)) {
                    scanningBox.setBackgroundColor(color);
//                    txtMessage.setText(message);
                    return;
                } else {
                    scanningBox.setBackgroundColor(0xffffffff);
                }

            }

            public void onFinish() {

//                scanner.setVisibility(View.INVISIBLE);

            }
        }.start();
    }

    @Override
    protected void onPause() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onPause();
    }

    protected void setStatusScanning(String message) {
//        scanningBox.setBackgroundColor();
        color = 0xFFFF8300;
        isFlashing = true;
        txtMessage.setText(message);
        txtMessage.setTextColor(color);
    }
    protected void setStatusSuccess(String message) {
//        scanningBox.setBackgroundColor();
        txtMessage.setText(message);
        color = 0xFF00FF00;
        isFlashing = false;
        txtMessage.setTextColor(color);
    }
    protected void setStatusFailed(String message) {
//        scanningBox.setBackgroundColor();
        txtMessage.setText(message);
        color = 0xFFFF0000;
        isFlashing = true;
        txtMessage.setTextColor(color);
    }
}
