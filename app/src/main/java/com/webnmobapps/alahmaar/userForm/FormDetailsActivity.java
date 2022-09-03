package com.webnmobapps.alahmaar.userForm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.webnmobapps.alahmaar.R;

public class FormDetailsActivity extends AppCompatActivity {

    WebView web;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_details2);


        url = getIntent().getStringExtra("url");

        Log.e("url;",url);



        web = findViewById(R.id.webview);


        web.setWebViewClient(new WebViewClient());

        web.getSettings().setLoadsImagesAutomatically(true);

        web.getSettings().setJavaScriptEnabled(true);

        web.getSettings().setBuiltInZoomControls(true);

     //   web.getSettings().setSupportZoom(true);
        web.getSettings().setLoadWithOverviewMode(true);

        web.getSettings().setUseWideViewPort(true);

        web.getSettings().setAllowContentAccess(true);

        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient());


    }


}