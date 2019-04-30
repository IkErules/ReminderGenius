package ch.hslu.appe.reminder.genius.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.hslu.appe.reminder.genius.Activity.AddContactActivity;
import ch.hslu.appe.reminder.genius.Activity.SearchContactResultActivity;
import ch.hslu.appe.reminder.genius.Model.SearchContact;
import ch.hslu.appe.reminder.genius.Parser.SearchContactXmlParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import android.content.Intent;

import static ch.hslu.appe.reminder.genius.Activity.AddContactActivity.PICK_CONTACT_REQUEST;

public class LoadContactDataAsync extends AsyncTask<String, Void, ArrayList<SearchContact>> {

    public static final String SEARCH_CONTACTS_RESULT = "search.contacts.result";
    private static final String BASE_SEARCH_URL = "https://tel.search.ch/api/";
    private static final String API_KEY = "&key=9fe1be1f621383bf4dcdd496a4bf6c0d";

    private HttpLoggingInterceptor httpLogger;
    private OkHttpClient httpClient;
    private ListView listView;
    private Context context;
    private AddContactActivity activity;

    public LoadContactDataAsync(Context context, AddContactActivity activity) {
        this.context = context;
        this.activity = activity;
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
                    .url(BASE_SEARCH_URL + searchParam + API_KEY)
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
        return String.format("?q=%s", searchParam);
    }

    @Override
    protected void onPostExecute(ArrayList<SearchContact> searchContacts) {
        searchContacts.forEach(searchContact -> {
            Log.d("LoadContactDataAsync", String.format("Found contact from https request: %s", searchContact.toString()));
        });

        Intent intent = new Intent(context, SearchContactResultActivity.class);
        intent.putParcelableArrayListExtra(SEARCH_CONTACTS_RESULT, searchContacts);
        activity.startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }
}
