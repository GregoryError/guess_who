package com.kaleidoscope.guesscelebrities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private int choice = -1;

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


        play();

    }


    private void play() {

        personMap = new HashMap<>();

        parseToMap(contentStr, personMap);

        LinkedList<String> names = new LinkedList<>(personMap.keySet());

        choice = (int) (Math.random() * 4);

        Collections.shuffle(names);

        button_0.setText(names.pop());
        button_1.setText(names.pop());
        button_2.setText(names.pop());
        button_3.setText(names.pop());

        String keyName = "";

        switch (choice) {
            case 0:
                keyName = button_0.getText().toString();
                break;
            case 1:
                keyName = button_1.getText().toString();
                break;
            case 2:
                keyName = button_2.getText().toString();
                break;
            case 3:
                keyName = button_3.getText().toString();
                break;
        }

        ImageDownloader downloader = new ImageDownloader();
        try {
            imageView.setImageBitmap(downloader.execute("https://" + personMap.get(keyName)).get());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public void onClickButton_0(View view) {
        if (choice == 0) {
            Toast.makeText(this, "RIGHT!", Toast.LENGTH_SHORT).show();
            play();
        }
        else
            Toast.makeText(this, "WRONG!", Toast.LENGTH_SHORT).show();
    }

    public void onClickButton_1(View view) {
        if (choice == 1) {
            Toast.makeText(this, "RIGHT!", Toast.LENGTH_SHORT).show();
            play();
        }
        else
            Toast.makeText(this, "WRONG!", Toast.LENGTH_SHORT).show();
    }

    public void onClickButton_2(View view) {
        if (choice == 2) {
            Toast.makeText(this, "RIGHT!", Toast.LENGTH_SHORT).show();
            play();
        }
        else
            Toast.makeText(this, "WRONG!", Toast.LENGTH_SHORT).show();
    }

    public void onClickButton_3(View view) {
        if (choice == 3) {
            Toast.makeText(this, "RIGHT!", Toast.LENGTH_SHORT).show();
            play();
        }
        else
            Toast.makeText(this, "WRONG!", Toast.LENGTH_SHORT).show();
    }

    private static class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        private URL url = null;
        private HttpsURLConnection httpsUrlConnections = null;
        private Bitmap bitmap;

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                url = new URL(strings[0]);
                httpsUrlConnections = (HttpsURLConnection) url.openConnection();
                InputStream in = httpsUrlConnections.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpsUrlConnections != null) {
                    httpsUrlConnections.disconnect();
                }
            }
            return bitmap;
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
                    strBld.append((char) data);
                    data = reader.read();
                }
                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpsUrlConnection != null)
                    httpsUrlConnection.disconnect();
            }
            return strBld.toString();
        }
    }
}