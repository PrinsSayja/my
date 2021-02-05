package com.Educationinfo.educationinfo;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.EducationIfo.checkInternet.NetworkChangeListner;


public class MainActivity extends AppCompatActivity {
    private WebView mywebView;
    NetworkChangerListner networkChangerListner = new NetworkChangeListner();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mywebView = (WebView) findViewById(R.id.webview);
        mywebView.setWebViewClient(new WebViewClient());
        mywebView.loadUrl("http://educationinfo.net.s3-website.ap-south-1.amazonaws.com/");
        WebSettings webSettings = mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mywebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String useragent, String contentDisposition, String mimetype, long contentLength) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        DownloadDialog(url, useragent, contentDisposition, mimetype);
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
                else
                {
                    DownloadDialog(url, useragent, contentDisposition, mimetype);
                }
            }
        });
    }

    public void DownloadDialog(final String url, final String UserAgent, String contentdisposition, String mimetype)
                {
                  final String filename = URLUtil.guessFileName(url,contentdisposition,mimetype);
                    AlertDialog.Builder builder = new AlertDialog.Builder(  this);
                    builder.setTitle("Downloading...")
                            .setMessage("તમે પાઠપુસ્તક Download કરવા માંગો છો ?"+ ' '+" "+filename+" "+' ')
                            .setPositiveButton( "હા", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                    String cookie = CookieManager.getInstance().getCookie(url);
                                    request.addRequestHeader("Cookie", cookie);
                                    request.addRequestHeader("User-Agent", UserAgent);
                                    request.allowScanningByMediaScanner();

                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                                    DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                                    manager.enqueue(request);

                                }

                            } )
                          .setNegativeButton("ના",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface,int i){
                                    dialogInterface.cancel();
                                }
                            })
                          .show();

    }
    public class mywebClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            super.onPageStarted(view,url,favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            return true;
        }
    }
    @Override
    public void onBackPressed(){
        if(mywebView.canGoBack()) {
            mywebView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangerListner, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangerListner);
        super.onStop();
    }
}
}
