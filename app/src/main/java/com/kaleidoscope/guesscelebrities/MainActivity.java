package com.kaleidoscope.guesscelebrities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    private Button button_0, button_1, button_2, button_3;
    private ImageView imageView;
    private final String urlStr = "https://www.usmagazine.com/celebrities/a/";
    private String contentStr = "";
    HashMap<String, String> personMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_0 = findViewById(R.id.button0);
        button_1 = findViewById(R.id.button1);
        button_2 = findViewById(R.id.button2);
        button_3 = findViewById(R.id.button3);
        imageView = findViewById(R.id.imageView);

        UrlTaskWorker urlWorker = new UrlTaskWorker();
        try {
            contentStr = urlWorker.execute(urlStr).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        personMap = new HashMap<>();

        parseToMap(contentStr, personMap);

        LinkedList<String> names = new LinkedList<>(personMap.keySet());



        String personX = names.get((int) ((names.size() + 1) * Math.random()));
        //imageView.setImageBitmap();

        switch ((int) (Math.random() * 4)) {
            case 0:
                button_0.setText(personX);
                break;
            case 1:
                button_1.setText(personX);
                break;
            case 2:
                button_2.setText(personX);
                break;
            case 3:
                button_3.setText(personX);
                break;
        }


    }

    private void parseToMap(String content, HashMap<String, String> map) {
        LinkedList<String> images = new LinkedList<>();
        LinkedList<String> names = new LinkedList<>();

        Pattern namePattern = Pattern.compile("\"celebrity-name\">(.*?)</span>");
        Matcher nameMatcher = namePattern.matcher(content);
        while (nameMatcher.find()) {
            names.add(nameMatcher.group(1));
        }

        Pattern imgPattern = Pattern.compile("data-src=\"https://(.*?)jpg");
        Matcher imgMatcher = imgPattern.matcher(content);
        while (imgMatcher.find()) {
            images.add(imgMatcher.group(1) + "jpg");
        }

        while (!names.isEmpty()) {
            map.put(names.pop(), images.pop());
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