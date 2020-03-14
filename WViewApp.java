package net.sytes.vision.WViewApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;

public class WViewApp extends Activity
{
    public WebView webview;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
		webview.addJavascriptInterface(new WViewBridge(this), "android");
        webview.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        setContentView(webview);

        //webview.setWebViewClient(new WebViewClient());
		webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("file:///android_asset/main.html");
    }

    @Override
    public boolean onKeyDown(int code, KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN
            && code == KeyEvent.KEYCODE_BACK
            && webview.canGoBack() == true){
            webview.goBack();
            return true;
        }
        return super.onKeyDown(code, event);
    }
}
