package com.example.udpwearos.presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.udpwearos.R;

public class SettingsActivity extends AppCompatActivity {

    private EditText editGT, editGC, editV1, editV2;
    private Button buttonSave;
    private SharedPreferences prefs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
    }
}
