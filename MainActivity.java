package com.rainbowgpt.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

public class MainActivity extends Activity {
  WebView web;
  @Override public void onCreate(Bundle b) { super.onCreate(b); web=new WebView(this); web.getSettings().setJavaScriptEnabled(true); web.getSettings().setDomStorageEnabled(true); web.addJavascriptInterface(new Bridge(),"Android"); web.setWebViewClient(new WebViewClient()); web.setWebChromeClient(new WebChromeClient()); setContentView(web); web.loadUrl("file:///android_asset/index.html"); }
  class Bridge {
    @JavascriptInterface public String getKey(){ return getPreferences(0).getString("key",""); }
    @JavascriptInterface public void saveKey(String key){ getPreferences(0).edit().putString("key",key).apply(); }
    @JavascriptInterface public void ask(String messagesJson){ new Thread(()->{
      String result;
      try {
        String key=getPreferences(0).getString("key","");
        if(key.isEmpty()) throw new Exception("Enter your OpenAI API key in Settings first.");
        URL url=new URL("https://api.openai.com/v1/chat/completions");
        HttpURLConnection c=(HttpURLConnection)url.openConnection(); c.setRequestMethod("POST"); c.setConnectTimeout(20000); c.setReadTimeout(60000); c.setDoOutput(true);
        c.setRequestProperty("Authorization","Bearer "+key); c.setRequestProperty("Content-Type","application/json");
        JSONObject payload=new JSONObject(); payload.put("model","gpt-4o-mini"); payload.put("messages",new JSONArray(messagesJson));
        try(OutputStream o=c.getOutputStream()){o.write(payload.toString().getBytes("UTF-8"));}
        InputStream stream=(c.getResponseCode()<400)?c.getInputStream():c.getErrorStream();
        ByteArrayOutputStream bytes=new ByteArrayOutputStream(); byte[] buffer=new byte[4096]; int n; while((n=stream.read(buffer))!=-1)bytes.write(buffer,0,n);
        JSONObject response=new JSONObject(bytes.toString("UTF-8"));
        if(c.getResponseCode()>=400) throw new Exception(response.optJSONObject("error")!=null?response.getJSONObject("error").optString("message","API error"):"HTTP "+c.getResponseCode());
        result=response.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
      } catch(Exception e){result="Error: "+e.getMessage();}
      final String message=result; runOnUiThread(()->web.evaluateJavascript("receive("+JSONObject.quote(message)+")",null));
    }).start(); }
  }
}
