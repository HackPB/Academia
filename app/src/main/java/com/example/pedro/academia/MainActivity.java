package com.example.pedro.academia;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout; //swipe
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WebView myWebView = (WebView) findViewById(R.id.webview);

           //settings to site
           WebSettings webSettings = myWebView.getSettings();
           webSettings.setJavaScriptEnabled(true);
           webSettings.setDomStorageEnabled(true); //to open the content of livescoresmenu (DOM Storage)
           //myWebView.setWebViewClient(new WebViewClient());
           myWebView.setWebChromeClient(new WebChromeClient());
           //site to see
           myWebView.loadUrl("http://www.academiadasapostas.com");

           myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
           if (Build.VERSION.SDK_INT >= 19){
               myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
           }/*else {
               myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
           }*/

           final ImageView splash = (ImageView) findViewById(R.id.splash);
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   splash.setVisibility(View.INVISIBLE);
               }
           },2000);



        //myWebView.setWebViewClient(new NoGo());


        //swipe to refresh the actual page
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                myWebView.loadUrl("javascript:window.location.reload(true)");
                swipeRefreshLayout.setRefreshing(false);
                }

            });

           //Page off connection error
           myWebView.setWebViewClient(new WebViewClient() {
               @Override
               public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
               { myWebView.loadUrl("file:///android_asset/error.html");
                   swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                       @Override
                       public void onRefresh() {
                           myWebView.loadUrl("javascript:window.history.go(-1)");
                           swipeRefreshLayout.setRefreshing(false);
                       }

                   });

               }

           });






    }

    //back button with confirmation
    @Override
    public void onBackPressed()
    {
        WebView webView = (WebView) findViewById(R.id.webview);
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.Exit)
                    .setMessage(R.string.Exitmsg)
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.No, null)
                    .show();
        }
    }
    //Client definition and no go to other site
    private class NoGo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("http://www.academiadasapostas.com")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }


    }


}


