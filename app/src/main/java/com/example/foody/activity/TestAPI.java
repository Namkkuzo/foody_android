package com.example.foody.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foody.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;


public class TestAPI extends AppCompatActivity {

    final String apiKey = "apiKey=828518d44e1a4a42bcc49b20e7b66167";
    static Logger logger = Logger.getLogger(TestAPI.class.getName());
    TextView textView;
    Button btnTestAPI;

    private static String rep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_api);
        getView();
        listenClick();
        api();
    }

    public void getView(){
        textView = findViewById(R.id.textAPI);
        btnTestAPI = findViewById(R.id.btnTestAPI);
    }

    void listenClick(){
        btnTestAPI.setOnClickListener(view -> {
            StringBuilder reponse = new StringBuilder();
            String url = "https://api.spoonacular.com/recipes/716429/information?includeNutrition=false&apiKey=828518d44e1a4a42bcc49b20e7b66167";
//            try {
//                reponse = getJsonStringFromAPI(url, "GET");
//                logger.info(reponse.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            api();
            stringToJson();
            logger.info(reponse.toString());
            textView.setText(rep);
        });
    }

    public StringBuilder getJsonStringFromAPI(String urlConnect, String method) throws Exception{
        URL url = new URL(urlConnect);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.trim().toUpperCase());
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; "
                + "Windows NT 5.1; en-US; rv:1.8.0.11) ");
        connection.setRequestProperty("Connection", "keep-alive");
        InputStream responseStream = connection.getInputStream();

        BufferedReader rd = new BufferedReader(new InputStreamReader(responseStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return response;
    }

    public void stringToJson(){
        try {

            JSONObject obj = new JSONObject(rep);

            logger.info("My App"+ obj.toString());

        } catch (Throwable t) {
            logger.info("My App"+ "Could not parse malformed JSON: \"" + rep + "\"");
        }
    }

    public void api(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.spoonacular.com/recipes/716429/information?includeNutrition=false&apiKey=828518d44e1a4a42bcc49b20e7b66167";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        textView.setText("Response is: "+ response.toString());
                        rep = response.toString();
                        logger.info("reponse: "+rep);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        textView.setText("That didn't work!");
                    }
                    });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


}
