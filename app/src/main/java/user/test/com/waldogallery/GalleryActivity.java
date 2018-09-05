package user.test.com.waldogallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import user.test.com.waldogallery.http.NetworkCalls;

public class GalleryActivity extends AppCompatActivity implements IGallery {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RecyclerView.LayoutManager layoutManager;

    private ImageViewAdapter adapter;

    private int page_number = 1;
    private int item_count = 10;

    private boolean isLoading;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;
    private int view_threshold = 10;
    private List<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.RecyclerView);
        layoutManager = new GridLayoutManager(this,3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        progressBar.setVisibility(View.VISIBLE);

        GalleryPresenter galleryPresenter = new GalleryPresenter(this);

        galleryPresenter.getToken(getApplicationContext());
        galleryPresenter.getAlbum();

    }


    @Override
    public void setAlbumList(List<GetAlbumByID.Record> records) {
        urls = records.stream().map(item -> item.urls().get(0).url()).collect(Collectors.toList());
        Log.e(getClass().getName(), new Gson().toJson(urls));



        adapter = new ImageViewAdapter(urls, getApplicationContext());



        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);


                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisibleItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                        if (dy > 0) {
                            if(isLoading){
                                if(totalItemCount > previousTotal){
                                    isLoading = false;
                                    previousTotal = totalItemCount;
                                }
                            }
                            if (!isLoading) {
                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount
                                        && pastVisibleItems >= 0) {
                                    page_number++;
                                    performPagination();
                                    isLoading = true;
                                }
                            }

                        }

                    }
                });

            }
        });
    }


    //Since API does not have a page number or and offset to retrieve records.
    // I am just storing urls og pics and adding it agaain and agin to perform endless pagination.
    private void performPagination(){
        progressBar.setVisibility(View.VISIBLE);
        String[] test = {"https://s3.amazonaws.com/waldo-thumbs-dev/medium/5fb81262-1830-514f-bfad-8ecabb26b107.jpg","https://s3.amazonaws.com/waldo-thumbs-dev/medium/253f3dcd-450b-5842-8dc0-6b55db1621ae.jpg","https://s3.amazonaws.com/waldo-thumbs-dev/medium/eab0bb35-844e-5a86-a57a-87574f9f6fd3.jpg","https://s3.amazonaws.com/waldo-thumbs-dev/medium/03338ab4-1e7f-5280-9a29-7311e2a86077.jpg","https://s3.amazonaws.com/waldo-thumbs-dev/medium/7ddc9ffd-92c4-5164-8476-728b5df17395.jpg","https://s3.amazonaws.com/waldo-thumbs-dev/medium/6c41edf8-4497-56a0-88ce-66b6857bd382.jpg","https://s3.amazonaws.com/waldo-thumbs-dev/medium/234ea375-2fc5-588b-bfa7-3a9759efca72.jpg","https://s3.amazonaws.com/waldo-thumbs-dev/medium/4eb455f9-5b2d-5d3b-bebd-25c6fde247f0.jpg","https://s3.amazonaws.com/waldo-thumbs-dev/medium/d7a1ffda-3e90-51a1-9fe6-9db755cefb7d.jpg","https://s3.amazonaws.com/waldo-thumbs-dev/medium/ce789205-2307-5d44-b233-e3b4a78491d9.jpg"};
        List<String> moreRecords = Arrays.asList(test);
        urls.addAll(moreRecords);
        adapter.addImages(moreRecords);
        progressBar.setVisibility(View.GONE);
    }
}
