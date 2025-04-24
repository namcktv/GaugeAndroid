package com.example.udpwearos.presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.widget.BoxInsetLayout;

import com.example.udpwearos.R;

import java.io.IOException;
import java.io.InputStream;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private BoxInsetLayout main_layout;  // Layout bạn muốn đổi nền
    private EditText editGT, editGC, editV1, editV2;
    private Button buttonSave, buttonChangeBackground;
    private SharedPreferences prefs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        main_layout = findViewById(R.id.main_layout); // layout chứa toàn bộ, đặt id cho nó trong XML
        buttonChangeBackground = findViewById(R.id.buttonChangeBackground);

        editGT = findViewById(R.id.editGT);
        editGC = findViewById(R.id.editGC);
        editV1 = findViewById(R.id.editV1);
        editV2 = findViewById(R.id.editV2);
        buttonSave = findViewById(R.id.buttonSettings);


        prefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);

        // Load
        editGT.setText(String.valueOf(prefs.getInt("GT", 40)));
        editGC.setText(String.valueOf(prefs.getInt("GC", 180)));
        editV1.setText(String.valueOf(prefs.getInt("V1", 0)));
        editV2.setText(String.valueOf(prefs.getInt("V2", 0)));

        buttonSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("GT", Integer.parseInt(editGT.getText().toString()));
            editor.putInt("GC", Integer.parseInt(editGC.getText().toString()));
            editor.putInt("V1", Integer.parseInt(editV1.getText().toString()));
            editor.putInt("V2", Integer.parseInt(editV2.getText().toString()));
            editor.apply();
            finish();
        });


        buttonChangeBackground.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, 101);

        });

    }

    // Bắt sự kiện chọn ảnh



    // Nhận ảnh và set làm background
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            // Ghi nhớ quyền truy cập ảnh
            getContentResolver().takePersistableUriPermission(
                    imageUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                // Đặt background cho layout
                main_layout.setBackground(drawable);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
