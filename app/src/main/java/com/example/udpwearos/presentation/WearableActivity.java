package com.example.udpwearos.presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.MotionEvent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import de.nitri.gauge.Gauge;
import de.nitri.gauge.GaugeCustom;
import uk.co.jackhurst.seven_segment_display.SevenSegmentView;
import com.example.udpwearos.R;
import android.util.Log;

import java.io.IOException;

public class WearableActivity extends AppCompatActivity {

    private UDPClient udpClient;
    public Thread udpThread;

    private TextView textViewPing, textViewRPM, textViewTemp, textViewVoltage, textViewEncoderPosition, localEncoderPosition;
//    private SeekBar seekBarEn;
    public ImageButton bStart, buttonSettings;

    private ImageView watertemp, ic_batt;// = findViewById(R.id.myIcon);

    private int currentEn, currentEnlast, currentBt = 0, currentGT = 40, currentGC = 180, currentV1 = 0, currentV2 = 0, checkprogress = 0;

    private static final String TAG = "WearableActivity";

    private boolean isUserInteracting = false;
    private volatile boolean isRunning = true;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private SevenSegmentView segmentDigits, dot, hour, min, sec;
    public com.devadvance.circularseekbar.CircularSeekBar circularSeekBar;

    private final Runnable sendBtRunnable = new Runnable() {
        @Override
        public void run() {
            sendIfChanged();
            handler.postDelayed(this, 10); // gửi mỗi 100ms
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable);

        udpClient = new UDPClient();


        final Gauge gauge = findViewById(R.id.gauge);
        final GaugeCustom gaugeTemp = findViewById(R.id.gaugeTemp);
        final GaugeCustom gaugeBatt = findViewById(R.id.gaugeBatt);
        watertemp = findViewById(R.id.watertemp);
        ic_batt = findViewById(R.id.ic_batt);
        segmentDigits = findViewById(R.id.segment_digits);
        dot = findViewById(R.id.odd);
        hour = findViewById(R.id.hour);
        min = findViewById(R.id.min);
        sec = findViewById(R.id.sec);
        textViewRPM = findViewById(R.id.textViewRPM);
        textViewTemp = findViewById(R.id.textViewTemp);
        textViewVoltage = findViewById(R.id.textViewVoltage);
        textViewEncoderPosition = findViewById(R.id.textViewEncoderPosition);
        localEncoderPosition = findViewById(R.id.localEncoderPosition);
        textViewPing = findViewById(R.id.textViewPing); // TextView để hiển thị pinglocal

        circularSeekBar = findViewById(R.id.cirSeekGas);
//        seekBarEn = findViewById(R.id.seekBarEn);
        bStart = findViewById(R.id.bStart);
        buttonSettings = findViewById(R.id.buttonOpenSettings);

        SharedPreferences prefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        currentGT = prefs.getInt("GT", 40);
        currentGC = prefs.getInt("GC", 180);
        currentV1 = prefs.getInt("v1", 0);
        currentV2 = prefs.getInt("v2", 0);




//        seekBarEn.setMax(currentGC - currentGT); // giới hạn từ 0 đến (GC - GT)
//        seekBarEn.setProgress(currentEn - currentGT); // chuyển currentEn về khoảng tương ứng
        circularSeekBar.setMax(currentGC - currentGT); // giới hạn từ 0 đến (GC - GT)
        circularSeekBar.setProgress(currentEn - currentGT); // chuyển currentEn về khoảng tương ứng
        localEncoderPosition.setText(getString(R.string.local_encoder_value, currentEn + currentGT));

        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(WearableActivity.this, SettingsActivity.class);
            startActivity(intent);
        });







        // Gửi liên tục khi giữ nút Bt
        bStart.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.performClick();
                    currentBt = 2;
                    handler.post(sendBtRunnable);
                    return true;
                case MotionEvent.ACTION_UP:
//                    currentBt = 0;
//                    sendIfChanged(); // gửi lần cuối
//                    handler.removeCallbacks(sendBtRunnable);
//                    return true;
                case MotionEvent.ACTION_CANCEL:
                    currentBt = 0;
                    sendIfChanged(); // gửi lần cuối
                    sendIfChanged(); // gửi lần cuối
                    sendIfChanged(); // gửi lần cuối
                    handler.removeCallbacks(sendBtRunnable);
                    return true;
            }
            return false;
        });



        // Nhận dữ liệu UDP
        udpThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
//                String data = udpClient.receiveData();
                Pair<String, Long> result = udpClient.receiveData();  // Lấy cả receivedData và pinglocal
                String data = result.first;  // Dữ liệu nhận được từ server
                long ping = result.second;  // Giá trị pinglocal
                if (ping != -1 && data != null) {
                    runOnUiThread(() -> {
                        Log.d(TAG, "Received Data: " + data);
                        DataParser.parse(data);
                        textViewRPM.setText(getString(R.string.rpm_value, DataStore.getRPM()));
                        textViewTemp.setText(getString(R.string.temp_value, DataStore.getTemp()));
                        textViewVoltage.setText(getString(R.string.voltage_value, DataStore.getVoltage()));
                        textViewEncoderPosition.setText(getString(R.string.encoder_value, DataStore.getEncoderPosition()));
                        localEncoderPosition.setText(getString(R.string.local_encoder_value, currentEn));
                        textViewPing.setText(ping + " ms");


                        gauge.moveToValue(DataStore.getRPM());
                        gaugeTemp.moveToValue(DataStore.getTemp());
                        gaugeBatt.moveToValue(DataStore.getVoltage());

                        float temp = DataStore.getTemp();

                        if (temp < 50) {
                            watertemp.setColorFilter(Color.GREEN);
                        } else if (temp < 70) {
                            watertemp.setColorFilter(Color.YELLOW);
                        } else if (temp < 90) {
                            int orange = ContextCompat.getColor(this, R.color.debug_ORANGE);

                            watertemp.setColorFilter(orange);
                        } else {
                            watertemp.setColorFilter(Color.RED);
                        }

                        float vol = DataStore.getVoltage();

                        if (vol < 12.2) {
                            ic_batt.setColorFilter(Color.RED);
                        } else if (vol < 12.3) {
                            int orange = ContextCompat.getColor(this, R.color.debug_ORANGE);
                            ic_batt.setColorFilter(orange);
                        } else if (vol < 12.5) {
                            ic_batt.setColorFilter(Color.YELLOW);

                        } else {
                            ic_batt.setColorFilter(Color.GREEN);
                        }

                        int rpm = DataStore.getRPM();

                        if (rpm > 100) {
                            bStart.setImageResource(R.drawable.istop); // ảnh khi rpm thấp
                        } else {
                            bStart.setImageResource(R.drawable.istart); // ảnh khi rpm cao
                        }

                        segmentDigits.setDigits(DataStore.combinedValueHrs); // Đặt giá trị hiển thị
                        hour.setDigits(DataStore.getValueHrs11()); // Đặt giá trị hiển thị
                        min.setDigits(DataStore.getValueMinute1()); // Đặt giá trị hiển thị
                        sec.setDigits(DataStore.getValueSecnd1()); // Đặt giá trị hiển thị
                        dot.setDigits(DataStore.value_odd); // Đặt giá trị hiển thị

                        currentEn = DataStore.getEncoderPosition();
                        if (currentEn != currentEnlast ){
                            currentEnlast = currentEn;
                            circularSeekBar.setProgress(currentEn - currentGT);
                        }
                        //Log.d("curBT", "currentBt: " + currentBt);
                        if (checkprogress == 0) {
//                            seekBarEn.setProgress(DataStore.getEncoderPosition());
                            currentEn = DataStore.getEncoderPosition();
                            circularSeekBar.setProgress(currentEn - currentGT);

                            checkprogress = 1;
                        }
                    });
                }
            }
        });
        udpThread.start();


        circularSeekBar.setOnSeekBarChangeListener(new com.devadvance.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(com.devadvance.circularseekbar.CircularSeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    currentEn = progress + currentGT; // cộng lại để chuyển về khoảng thực tế
//                    seekBarEn.setProgress(currentEn);
                    localEncoderPosition.setText(getString(R.string.local_encoder_value, progress + currentGT));
                    sendIfChanged();
                }
            }

            @Override
            public void onStartTrackingTouch(com.devadvance.circularseekbar.CircularSeekBar seekBar) {
                isUserInteracting = true;
            }

            @Override
            public void onStopTrackingTouch(com.devadvance.circularseekbar.CircularSeekBar seekBar) {
                isUserInteracting = false;
            }
        });


        // SeekBar En
//        seekBarEn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
////                int actualValue = progress + currentGT;
////                if (actualValue != currentEn) {
////                    currentEn = actualValue;
////                    sendIfChanged();
////                }
//                if (fromUser || isUserInteracting) {  // Chỉ gửi khi người dùng thao tác
//                    currentEn = progress;
//                    sendIfChanged();
//
//                    circularSeekBar.setProgress(currentEn);
//
//                }
//            }
//
//            @Override public void onStartTrackingTouch(SeekBar seekBar) {
//                isUserInteracting = true;
//
//            }
//            @Override public void onStopTrackingTouch(SeekBar seekBar) {
//                isUserInteracting = true;
//            }
//        });
    }



    private void sendIfChanged() {
        String data = String.format("Bt:%d En:%d GT:%d GC:%d v0:%d v1:%d",
                currentBt, currentEn, currentGT, currentGC, currentV1, currentV2);
        sendDataInBackground(data);

    }

    private void sendDataInBackground(final String data) {
        new Thread(() -> udpClient.sendData(data)).start();
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (udpClient != null) udpClient.close();
        if (udpThread != null && udpThread.isAlive()) {
            udpThread.interrupt(); // dừng thread ngay lập tức
        }
        handler.removeCallbacks(sendBtRunnable); // dọn dẹp handler khi thoát
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Lấy lại giá trị từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        currentGT = prefs.getInt("GT", 40);
        currentGC = prefs.getInt("GC", 180);
        currentV1 = prefs.getInt("v1", 0);
        currentV2 = prefs.getInt("v2", 0);

        // Cập nhật SeekBar En
        circularSeekBar.setMax(currentGC - currentGT); // giới hạn từ 0 đến (GC - GT)
        if (currentEn >= currentGC){
            currentEn = currentGC;
            circularSeekBar.setProgress(currentEn - currentGT); // chuyển currentEn về khoảng tương ứng
            sendIfChanged();
        } else {
            circularSeekBar.setProgress(currentEn - currentGT); // chuyển currentEn về khoảng tương ứng
        }
    }


}

