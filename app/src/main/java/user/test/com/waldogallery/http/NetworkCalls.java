package user.test.com.waldogallery.http;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetworkCalls {
    private static String URL = "https://core-graphql.dev.waldo.photos/gql";
    private static ApolloClient waldoApolloClient;
    private static OkHttpClient client = new OkHttpClient();


    public static ApolloClient getApolloClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new BasicAuthenticationInterceptor()).addInterceptor(loggingInterceptor).build();
        waldoApolloClient = ApolloClient.builder().serverUrl(URL).okHttpClient(httpClient).build();
        return waldoApolloClient;
    }

    public static OkHttpClient getClient(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().
                connectTimeout(60 * 5, TimeUnit.SECONDS)
                .readTimeout(60 * 5, TimeUnit.SECONDS)
                .writeTimeout(60 * 5, TimeUnit.SECONDS).
                addInterceptor(loggingInterceptor).
                addInterceptor(new ReceivedCookiesInterceptor(context)).
                addInterceptor(new AddCookieInterceptor(context)).build();
        return httpClient;
    }

}

