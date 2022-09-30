package com.ngo.alahmaar.basicAndroidFunction.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ngo.alahmaar.R;

public class AboutFragment extends Fragment {


    WebView web;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        web = view.findViewById(R.id.web);




        String url ="http://69.49.235.253:8004/ngo-abouts-page";

        Log.e("url;",url);


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

        return  view;
    }
}