package com.logicut.studiopro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;
    private ValueCallback<Uri[]> filePathCallback;
    private static final int FILE_CHOOSER_RESULT_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(Color.parseColor("#07090E"));
        getWindow().setNavigationBarColor(Color.parseColor("#07090E"));

        webView = new WebView(this);
        webView.setBackgroundColor(Color.parseColor("#07090E"));

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (MainActivity.this.filePathCallback != null) {
                    MainActivity.this.filePathCallback.onReceiveValue(null);
                }
                MainActivity.this.filePathCallback = filePathCallback;
                try {
                    String mimeType = "*/*";
                    String chooserTitle = "Select Media from Gallery";
                    if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null) {
                        StringBuilder joined = new StringBuilder();
                        for (String t : fileChooserParams.getAcceptTypes()) {
                            if (t != null) joined.append(t.toLowerCase()).append(",");
                        }
                        String acceptStr = joined.toString();
                        boolean hasVideo = acceptStr.contains("video");
                        boolean hasImage = acceptStr.contains("image") || acceptStr.contains(".png") || acceptStr.contains(".jpg") || acceptStr.contains(".webp");
                        boolean hasAudio = acceptStr.contains("audio") || acceptStr.contains(".mp3") || acceptStr.contains(".wav");
                        if (hasVideo && !hasImage && !hasAudio) {
                            mimeType = "video/*";
                            chooserTitle = "Select Video from Phone Gallery";
                        } else if (hasImage && !hasVideo && !hasAudio) {
                            mimeType = "image/*";
                            chooserTitle = "Select Photo or PNG from Phone Gallery";
                        } else if (hasAudio && !hasVideo && !hasImage) {
                            mimeType = "audio/*";
                            chooserTitle = "Select Audio Track";
                        }
                    }
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType(mimeType);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, chooserTitle), FILE_CHOOSER_RESULT_CODE);
                } catch (Exception e) {
                    MainActivity.this.filePathCallback = null;
                    return false;
                }
                return true;
            }
        });

        setContentView(webView);
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (filePathCallback == null) return;
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK && data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    results = new Uri[count];
                    for (int i = 0; i < count; i++) {
                        results[i] = data.getClipData().getItemAt(i).getUri();
                    }
                } else if (data.getData() != null) {
                    results = new Uri[]{ data.getData() };
                }
            }
            filePathCallback.onReceiveValue(results);
            filePathCallback = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
