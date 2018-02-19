package com.example.polcel.popular_movies_part1.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.polcel.popular_movies_part1.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by polcel on 10/02/2018.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/";

    private final static String QUERY_POPULAR_PARAM = "popular";
    private final static String QUERY_TOP_RATED_PARAM = "top_rated";
    private final static String PATH_API_KEY = "api_key";

    public static URL buildUrl(boolean getOnlyPopular) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(getOnlyPopular ? QUERY_POPULAR_PARAM : QUERY_TOP_RATED_PARAM)
                .appendQueryParameter(PATH_API_KEY, BuildConfig.MOVIES_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /*
     * Seen on: https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     */

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}