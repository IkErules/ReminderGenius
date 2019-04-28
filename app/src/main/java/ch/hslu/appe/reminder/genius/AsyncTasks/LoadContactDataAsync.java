package ch.hslu.appe.reminder.genius.AsyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.hslu.appe.reminder.genius.Activities.SearchContactResultActivity;
import ch.hslu.appe.reminder.genius.Models.SearchContact;
import ch.hslu.appe.reminder.genius.Parser.SearchContactXmlParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import android.content.Intent;

public class LoadContactDataAsync extends AsyncTask<String, Void, ArrayList<SearchContact>> {

    private static final String BASE_SEARCH_URL = "https://tel.search.ch/api/";
    private HttpLoggingInterceptor httpLogger;
    private OkHttpClient httpClient;
    private ListView listView;
    private Context context;

    public LoadContactDataAsync(Context context) {
        this.context = context;
        this.listView = listView;
        // HttpClient erzeugen und konfigurieren
        httpLogger = new HttpLoggingInterceptor();
        httpLogger.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder().addInterceptor(httpLogger).build();
    }

    @Override
    protected ArrayList<SearchContact> doInBackground(String... param) {
            String searchParam = getSearchParam(param[0]);

            Request request = new Request.Builder()
                    .url(BASE_SEARCH_URL + searchParam)
                    .build();

            Response response = null;
            try {
                response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    try {
                        SearchContactXmlParser searchContactXmlParser = new SearchContactXmlParser();
                        return searchContactXmlParser.parse(response.body().byteStream());
                    } catch (XmlPullParserException ex) {
                        String error = String.format("ERROR: Failed to parse response", ex.getMessage());
                        Log.e("HttpService", error);
                    }
                }

                String error = String.format("ERROR: Request failed with %s %s",
                        response.code(), response.message());
                Log.e("HttpService", error);
            } catch (IOException error) {
                Log.e("HttpService", "Exception while using ");
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return new ArrayList<>();
    }

    private String getSearchParam(String param) {
        List<String> params = Arrays.asList(param.trim().split(" ", 0));
        String searchParam = String.join("+", params);
        return String.format("?was=%s", searchParam);
    }

    @Override
    protected void onPostExecute(ArrayList<SearchContact> searchContacts) {
        searchContacts.forEach(searchContact -> {
            Log.i("LoadContactDataAsync", searchContact.toString());
        });

        Intent intent = new Intent(context, SearchContactResultActivity.class);
        intent.putParcelableArrayListExtra("contacts", searchContacts);
        context.startActivity(intent);
    }
}
