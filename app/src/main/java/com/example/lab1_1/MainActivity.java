package com.example.laba1_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.net.Uri;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private Button buttonCapture;
    private ImageView imageView;
    private Button buttonTelegram;
    private Button buttonCall;
    private EditText editTextPhoneNumber;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.capturedImage);
        buttonCapture = findViewById(R.id.buttonCapture);
        buttonTelegram = findViewById(R.id.buttonTelegram);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonCall = findViewById(R.id.buttonCall);
        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://old.education.cchgeu.ru/");

        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent button_capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(button_capture, 100);
            }
        });
        buttonTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTelegram();
            }
        });
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editTextPhoneNumber.getText().toString();
                if (!phoneNumber.isEmpty()) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        makeCall(phoneNumber);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(photo);
    }
    private void launchTelegram() {
        Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://t.me/laysan_d"));
        startActivity(telegram);
    }
    private void makeCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// Разрешение получено, выполните звонок
            String phoneNumber = editTextPhoneNumber.getText().toString();
            makeCall(phoneNumber);
        } else {
            Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
        }
    }

}