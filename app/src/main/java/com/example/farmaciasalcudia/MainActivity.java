package com.example.farmaciasalcudia;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.ComponentActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends ComponentActivity {
    private OkHttpClient client = new OkHttpClient();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        fetchData();
    }

    private void fetchData() {
        new FetchDataTask().execute();
    }

    private class FetchDataTask extends AsyncTask<Void, Void, List<Farmacia>> {

        @Override
        protected List<Farmacia> doInBackground(Void... voids) {
            String apiKey = "c71469b44f584c20bec26290dcba5a18";
            String url = "https://api.scraperapi.com?api_key=" + apiKey + "&url=https://www.lalcudia.com/web/farmacies_nw.php";
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Document document = Jsoup.parse(body);
                    return parseData(document);
                }
            } catch (IOException e) {
                Log.e("Error", "Error fetching data", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Farmacia> farmacias) {
            super.onPostExecute(farmacias);
            if (farmacias != null) {
                // Mostrar los datos en un ListView
                FarmaciaAdapter adapter = new FarmaciaAdapter(MainActivity.this, farmacias);
                listView.setAdapter(adapter);
            } else {
                // Manejar el caso de error o datos vacíos
                Log.e("Error", "Failed to fetch data");
            }
        }

        private List<Farmacia> parseData(Document document) {
            List<Farmacia> results = new ArrayList<>();
            Map<String, String> ubicaciones = new HashMap<>();

            // Regex para extraer guardias y reforzos
            String guardiaRegex = "<strong><br>([^<]+)</strong><font\\s+color=\"green\"><br>GUÀRDIA---&gt; </font>([^<]+)<font\\s+color=\"red\">\\s*<br> REFORÇ---&gt; </font>([^<]*)<br>";
            // Regex para extraer ubicaciones
            String ubicacionRegex = "<strong>\\s*(.+?)</strong><br>\\s*(.+?)\\s*-\\s*Tel.\\s*(\\d+\\s*\\d+\\s*\\d+)\\s*-\\s*<a href=\"(.+?)\"";

            // Extraer guardias y reforzos
            Elements tdElements = document.select("td.Estilo18");
            for (Element element : tdElements) {
                String htmlContent = element.html();
                if (htmlContent != null) {
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(guardiaRegex);
                    java.util.regex.Matcher matcher = pattern.matcher(htmlContent);
                    while (matcher.find()) {
                        String date = matcher.group(1).trim();
                        String guardia = matcher.group(2).trim();
                        String reforc = matcher.group(3).trim();
                        results.add(new Farmacia(date, guardia, reforc));
                    }
                }
            }

            // Extraer ubicaciones
            for (Element element : tdElements) {
                String htmlContent = element.html();
                if (htmlContent != null) {
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(ubicacionRegex);
                    java.util.regex.Matcher matcher = pattern.matcher(htmlContent);
                    while (matcher.find()) {
                        String name = matcher.group(1).trim();
                        String address = matcher.group(2).trim();
                        ubicaciones.put(name, address);
                    }
                }
            }

            // Añadir ubicaciones a los resultados
            for (Farmacia farmacia : results) {
                farmacia.setGuardiaLocation(ubicaciones.getOrDefault(farmacia.getGuardia(), "N/A"));
                farmacia.setReforcLocation(ubicaciones.getOrDefault(farmacia.getReforc(), "N/A"));
            }

            return results;
        }
    }
}