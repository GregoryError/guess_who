package com.kaleidoscope.guesscelebrities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    private Button button_0, button_1, button_2, button_3;
    private final String urlStr = "https://www.usmagazine.com/celebrities/a/";
    private String contentStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_0 = findViewById(R.id.button0);
        button_1 = findViewById(R.id.button1);
        button_2 = findViewById(R.id.button2);
        button_3 = findViewById(R.id.button3);
        
        UrlTaskWorker urlWorker = new UrlTaskWorker();
        try {
            contentStr = urlWorker.execute(urlStr).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }

    private static class UrlTaskWorker extends AsyncTask<String, Void, String> {
        URL url = null;
        HttpsURLConnection httpsUrlConnection = null;
        InputStream in = null;
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder strBld = new StringBuilder();
            try {
                url = new URL(strings[0]);
                httpsUrlConnection = (HttpsURLConnection) url.openConnection();
                in = httpsUrlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in, Charset.forName("UTF-8"));
                int data = reader.read();
                while (data != -1) {
                    strBld.append((char)data);
                    data = reader.read();
                }
                reader.close();

            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if (httpsUrlConnection != null)
                    httpsUrlConnection.disconnect();
            }
            return strBld.toString();
        }
    }
}