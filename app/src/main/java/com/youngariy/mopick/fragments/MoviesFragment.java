package com.youngariy.mopick.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import androidx.appcompat.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.youngariy.mopick.R;
import com.youngariy.mopick.activities.ViewAllMoviesActivity;
import com.youngariy.mopick.adapters.MovieBriefsLargeAdapter;
import com.youngariy.mopick.adapters.MovieBriefsSmallAdapter;
import com.youngariy.mopick.network.movies.Genre;
import com.youngariy.mopick.network.movies.MovieBrief;
import com.youngariy.mopick.utils.Constants;
import com.youngariy.mopick.utils.LocaleHelper;
import com.youngariy.mopick.utils.MovieGenres;
import com.youngariy.mopick.utils.NetworkConnection;
import com.youngariy.mopick.viewmodels.MoviesViewModel;

import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment {

    private ProgressBar mProgressBar;

    private FrameLayout mNowShowingLayout;
    private RecyclerView mNowShowingRecyclerView;
    private MovieBriefsLargeAdapter mNowShowingAdapter;
    private List<MovieBrief> mNowShowingMovies;

    private FrameLayout mPopularLayout;
    private RecyclerView mPopularRecyclerView;
    private MovieBriefsSmallAdapter mPopularAdapter;
    private List<MovieBrief> mPopularMovies;

    private FrameLayout mUpcomingLayout;
    private RecyclerView mUpcomingRecyclerView;
    private MovieBriefsLargeAdapter mUpcomingAdapter;
    private List<MovieBrief> mUpcomingMovies;

    private FrameLayout mTopRatedLayout;
    private RecyclerView mTopRatedRecyclerView;
    private MovieBriefsSmallAdapter mTopRatedAdapter;
    private List<MovieBrief> mTopRatedMovies;

    private MaterialButton mGenreButton;
    private List<Genre> mGenres;
    private Integer mSelectedGenreId = null;

    // 원본 데이터 저장
    private List<MovieBrief> mOriginalNowShowingMovies = new ArrayList<>();
    private List<MovieBrief> mOriginalPopularMovies = new ArrayList<>();
    private List<MovieBrief> mOriginalUpcomingMovies = new ArrayList<>();
    private List<MovieBrief> mOriginalTopRatedMovies = new ArrayList<>();

    private MoviesViewModel mMoviesViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        initViews(view);
        initAdapters();
        initClickListeners(view);

        mMoviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);

        if (NetworkConnection.isConnected(getContext())) {
            observeViewModel();
            String region = LocaleHelper.getRegionCode(getContext());
            mMoviesViewModel.loadAllMovies(getContext(), getString(R.string.MOVIE_DB_API_KEY), region);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void initViews(View view) {
        mProgressBar = view.findViewById(R.id.progress_bar);
        mNowShowingLayout = view.findViewById(R.id.layout_now_showing);
        mPopularLayout = view.findViewById(R.id.layout_popular);
        mUpcomingLayout = view.findViewById(R.id.layout_upcoming);
        mTopRatedLayout = view.findViewById(R.id.layout_top_rated);

        mNowShowingRecyclerView = view.findViewById(R.id.recycler_view_now_showing);
        new LinearSnapHelper().attachToRecyclerView(mNowShowingRecyclerView);
        mPopularRecyclerView = view.findViewById(R.id.recycler_view_popular);
        mUpcomingRecyclerView = view.findViewById(R.id.recycler_view_upcoming);
        new LinearSnapHelper().attachToRecyclerView(mUpcomingRecyclerView);
        mTopRatedRecyclerView = view.findViewById(R.id.recycler_view_top_rated);

        mGenreButton = view.findViewById(R.id.button_genre_filter);
    }

    private void initAdapters() {
        mNowShowingMovies = new ArrayList<>();
        mNowShowingAdapter = new MovieBriefsLargeAdapter(getContext(), mNowShowingMovies, true);
        mNowShowingRecyclerView.setAdapter(mNowShowingAdapter);
        mNowShowingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mPopularMovies = new ArrayList<>();
        mPopularAdapter = new MovieBriefsSmallAdapter(getContext(), mPopularMovies);
        mPopularRecyclerView.setAdapter(mPopularAdapter);
        mPopularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mUpcomingMovies = new ArrayList<>();
        mUpcomingAdapter = new MovieBriefsLargeAdapter(getContext(), mUpcomingMovies, false);
        mUpcomingRecyclerView.setAdapter(mUpcomingAdapter);
        mUpcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mTopRatedMovies = new ArrayList<>();
        mTopRatedAdapter = new MovieBriefsSmallAdapter(getContext(), mTopRatedMovies);
        mTopRatedRecyclerView.setAdapter(mTopRatedAdapter);
        mTopRatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void initClickListeners(View view) {
        view.findViewById(R.id.text_view_view_all_now_showing).setOnClickListener(v -> openViewAllMovies(Constants.NOW_SHOWING_MOVIES_TYPE));
        view.findViewById(R.id.text_view_view_all_popular).setOnClickListener(v -> openViewAllMovies(Constants.POPULAR_MOVIES_TYPE));
        view.findViewById(R.id.text_view_view_all_upcoming).setOnClickListener(v -> openViewAllMovies(Constants.UPCOMING_MOVIES_TYPE));
        view.findViewById(R.id.text_view_view_all_top_rated).setOnClickListener(v -> openViewAllMovies(Constants.TOP_RATED_MOVIES_TYPE));
    }

    private void openViewAllMovies(String viewAllType) {
        if (!NetworkConnection.isConnected(getContext())) {
            Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
        intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, viewAllType);
        if (mSelectedGenreId != null) {
            intent.putExtra(Constants.SELECTED_GENRE_ID, mSelectedGenreId);
        }
        startActivity(intent);
    }

    private void observeViewModel() {
        mMoviesViewModel.getGenresList().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null) {
                MovieGenres.loadGenresList(genres);
                mGenres = genres;
                setupGenreButton();
            }
        });

        mMoviesViewModel.getNowShowingMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                mOriginalNowShowingMovies.clear();
                mOriginalNowShowingMovies.addAll(movies);
                applyGenreFilter();
            }
        });

        mMoviesViewModel.getPopularMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                mOriginalPopularMovies.clear();
                mOriginalPopularMovies.addAll(movies);
                applyGenreFilter();
            }
        });

        mMoviesViewModel.getUpcomingMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                mOriginalUpcomingMovies.clear();
                mOriginalUpcomingMovies.addAll(movies);
                applyGenreFilter();
            }
        });

        mMoviesViewModel.getTopRatedMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                mOriginalTopRatedMovies.clear();
                mOriginalTopRatedMovies.addAll(movies);
                applyGenreFilter();
            }
        });

        mMoviesViewModel.getError().observe(getViewLifecycleOwner(), isError -> {
            if (isError) {
                Toast.makeText(getContext(), R.string.error_loading_movies, Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupGenreButton() {
        if (mGenres == null || mGenres.isEmpty()) return;

        // 기본 선택 없음 (전체 표시)
        mSelectedGenreId = null;
        mGenreButton.setText("카테고리");

        mGenreButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), mGenreButton);
            
            // 모든 장르 메뉴 아이템 추가
            for (int i = 0; i < mGenres.size(); i++) {
                Genre genre = mGenres.get(i);
                popupMenu.getMenu().add(0, i, 0, genre.getGenreName());
            }
            
            popupMenu.setOnMenuItemClickListener(item -> {
                int position = item.getItemId();
                mSelectedGenreId = mGenres.get(position).getId();
                mGenreButton.setText(mGenres.get(position).getGenreName());
                applyGenreFilter();
                return true;
            });
            
            popupMenu.show();
        });
    }

    private void applyGenreFilter() {
        filterMovies(mOriginalNowShowingMovies, mNowShowingMovies, mNowShowingAdapter, mNowShowingLayout, mNowShowingRecyclerView);
        filterMovies(mOriginalPopularMovies, mPopularMovies, mPopularAdapter, mPopularLayout, mPopularRecyclerView);
        filterMovies(mOriginalUpcomingMovies, mUpcomingMovies, mUpcomingAdapter, mUpcomingLayout, mUpcomingRecyclerView);
        filterMovies(mOriginalTopRatedMovies, mTopRatedMovies, mTopRatedAdapter, mTopRatedLayout, mTopRatedRecyclerView);
        mProgressBar.setVisibility(View.GONE);
    }

    private void filterMovies(List<MovieBrief> originalList, List<MovieBrief> filteredList, 
                             RecyclerView.Adapter adapter, FrameLayout layout, RecyclerView recyclerView) {
        filteredList.clear();
        
        if (mSelectedGenreId == null) {
            // 전체 선택 - 원본 데이터 모두 표시
            filteredList.addAll(originalList);
        } else {
            // 선택한 장르로 필터링
            for (MovieBrief movie : originalList) {
                if (movie.getGenreIds() != null && movie.getGenreIds().contains(mSelectedGenreId)) {
                    filteredList.add(movie);
                }
            }
        }

        adapter.notifyDataSetChanged();
        
        if (!filteredList.isEmpty()) {
            layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}




