package com.uamo.app;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.view.KeyEvent;
import android.content.Intent;
import android.net.Uri;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.getcapacitor.BridgeActivity;

import java.util.ArrayList;

public class MainActivity extends BridgeActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private String[] permissions = {
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.POST_NOTIFICATIONS
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Перевірка та запит дозволів
        checkPermissions();

        // Обробка Deep Links
        handleDeepLink(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleDeepLink(intent);
    }

    private void handleDeepLink(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            String path = data.getPath();
            
            if (path != null) {
                // Передаємо deep link у WebView
                String js = "window.handleDeepLink('" + data.toString() + "');";
                if (getBridge() != null && getBridge().getWebView() != null) {
                    getBridge().getWebView().loadUrl("javascript:" + js);
                }
            }
        }
    }

    private void checkPermissions() {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) 
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(new String[0]),
                REQUEST_CODE_PERMISSIONS
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // Дозвіл надано
                } else {
                    Toast.makeText(this, 
                        "Потрібно надати дозволи для роботи додатку", 
                        Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            WebView webView = getBridge().getWebView();
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
