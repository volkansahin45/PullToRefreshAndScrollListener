package com.vsahin.pulltorefreshandscrolllistener;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeContainer;
    ListView listView;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        listView = (ListView) findViewById(R.id.listview);
        items = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page) {
                loadDataForList(page);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataForList(0);
            }
        });

        //load first page of data
        loadDataForList(0);
    }

    public void loadDataForList(final int page) {
        Log.d("MainActivity", "loadDataForList Page : " + page);
        if(page == 0){
            resetList();
        }

        //you have to make a small latency here. I wrote 200 just for example
        //While fetching data from server it already takes time. You dont need to use Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int startingPoint = page * 10;
                int endingPoint = startingPoint + 10;
                for (int i = startingPoint; i < endingPoint; i++) {
                    items.add("item " + i);
                }
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        }, 200);
    }

    private void resetList(){
        items.clear();
        adapter.clear();
        adapter.notifyDataSetChanged();
    }
}
