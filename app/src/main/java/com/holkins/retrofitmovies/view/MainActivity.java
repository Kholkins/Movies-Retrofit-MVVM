package com.holkins.retrofitmovies.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.Configuration;
import android.os.Bundle;

import com.holkins.retrofitmovies.R;
import com.holkins.retrofitmovies.adapter.ResultAdapter;
import com.holkins.retrofitmovies.databinding.ActivityMainBinding;
import com.holkins.retrofitmovies.model.MovieApiResponse;
import com.holkins.retrofitmovies.model.Result;
import com.holkins.retrofitmovies.service.MovieApiService;
import com.holkins.retrofitmovies.service.RetrofitInstance;
import com.holkins.retrofitmovies.viewmodel.MainActivityViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private PagedList<Result> results;
    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);

        mainActivityViewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(MainActivityViewModel.class);

        getPopularMovies();

        swipeRefreshLayout = activityMainBinding.swiperefresh;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        getPopularMovies();

                    }
                });
    }

    public void getPopularMovies() {

//        mainActivityViewModel.getAllMovieData().observe(this,
//                new Observer<List<Result>>() {
//                    @Override
//                    public void onChanged(List<Result> resultList) {
//                        results = (ArrayList<Result>) resultList;
//                        fillRecyclerView();
//                    }
//                });

        mainActivityViewModel.getPagedListLiveData().observe(this,
                new Observer<PagedList<Result>>() {
                    @Override
                    public void onChanged(PagedList<Result> resultList) {

                        results = resultList;
                        fillRecyclerView();

                    }
                });

    }

    private void fillRecyclerView() {

        recyclerView = activityMainBinding.recyclerView;
        adapter = new ResultAdapter(this);
        adapter.submitList(results);

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(
                    new GridLayoutManager(this, 2));

        } else {

            recyclerView.setLayoutManager(
                    new GridLayoutManager(this, 4));

        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
