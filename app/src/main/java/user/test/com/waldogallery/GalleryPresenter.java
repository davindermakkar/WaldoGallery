package user.test.com.waldogallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import user.test.com.waldogallery.http.NetworkCalls;

public class GalleryPresenter {
    private IGallery gallery;

    public GalleryPresenter(IGallery gallery) {
        this.gallery = gallery;
    }

    public void getToken(Context context){
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://auth.dev.waldo.photos").newBuilder();
        urlBuilder.addQueryParameter("username", "waldo-android");
        urlBuilder.addQueryParameter("password", "1234");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = NetworkCalls.getClient(context);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.d("Response code:", "===========" + response.code());

                Log.d("==Cookie==1===", "==="+response.request().headers().get("Cookie"));
                Log.d("==Cookie==2==", "==="+response.headers().get("Set-Cookie"));
                Log.d("==Content-Type====", "==="+response.headers().get("Content-Type"));
                SharedPreferences mcpPreferences = context.getSharedPreferences("WALDO_COOKIE", Context.MODE_PRIVATE);
                Log.e("==Cookie==12===", "==="+new Gson().toJson(mcpPreferences.getStringSet("cookies",new HashSet<String>())));
            }
        });




    }

    public void getAlbum() {

        NetworkCalls.getApolloClient().query(GetAlbumByID.builder().build()).enqueue(new ApolloCall.Callback<GetAlbumByID.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetAlbumByID.Data> response) {
                List<GetAlbumByID.Record> records = response.data().album().photos().records();
                gallery.setAlbumList(records);
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }
}
