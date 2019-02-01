package com.magiclabyrinth.bakingapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.magiclabyrinth.bakingapp.adapters.RecipeItemsAdapter;
import com.magiclabyrinth.bakingapp.idling_resource.SimpleIdlingResource;
import com.magiclabyrinth.bakingapp.model.Recipe;
import com.magiclabyrinth.bakingapp.sync.RecipeItemLoader;

import java.util.ArrayList;
import java.util.List;

import android.support.test.espresso.IdlingResource;

public class RecipeListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {
    private static final String TAG = RecipeListActivity.class.getSimpleName();

    private boolean mTwoPane;
    private static final int LOADER_ITEMS_ID = 22;
    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private RecipeItemsAdapter mRecipeItemsAdapter;
    private static final String RECIPES_KEY = "recipesKey";

    @Nullable
    private SimpleIdlingResource simpleIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mProgressDialog = new ProgressDialog(this);
        getIdlingResource();

        if (findViewById(R.id.gl_landscape) != null) {
            mTwoPane = true;
        }

        if (savedInstanceState == null){
            if (!Recipe.RECIPE_ITEMS.isEmpty()) {
                setupRecyclerView();
            } else {
                initApiService();
            }
        } else {
            setupRecyclerView();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!Recipe.RECIPE_ITEMS.isEmpty()) {
            outState.putParcelableArrayList(RECIPES_KEY, (ArrayList<? extends Parcelable>) Recipe.RECIPE_ITEMS);
        }
        super.onSaveInstanceState(outState);
    }

    private void initApiService(){
        getSupportLoaderManager().initLoader(LOADER_ITEMS_ID, null, this);
    }

    private void setupRecyclerView() {
        Log.i(TAG, "Total size of recipes: " + Recipe.RECIPE_ITEMS.size());
        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_list);
        Log.i(TAG, "value of mTwoPane: " + mTwoPane);
        mRecipeItemsAdapter = new RecipeItemsAdapter(this, Recipe.RECIPE_ITEMS, mTwoPane);
        assert mRecyclerView != null;
        mRecyclerView.setAdapter(mRecipeItemsAdapter);
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle){
        mProgressDialog.show();

        return new RecipeItemLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object recipes) {
        mProgressDialog.dismiss();

        int id = loader.getId();

        if (id == LOADER_ITEMS_ID) {
            if (recipes != null) {
                Recipe.RECIPE_ITEMS.addAll((List<Recipe.RecipeItem>) recipes);
                Log.i(TAG, "Current size of recipes items: " + Recipe.RECIPE_ITEMS.size());
                setupRecyclerView();
                mRecipeItemsAdapter.notifyDataSetChanged();
            } else {
                Log.i(TAG, "Recipes already exist");
            }
        }

        if (simpleIdlingResource != null) {
            simpleIdlingResource.setIdleState(true);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource(){
        if (simpleIdlingResource == null){
            simpleIdlingResource = new SimpleIdlingResource();
        }

        return simpleIdlingResource;
    }
}
