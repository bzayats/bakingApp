package com.magiclabyrinth.bakingapp.sync;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.magiclabyrinth.bakingapp.model.Recipe;
import com.magiclabyrinth.bakingapp.retrofit.RecipeApiClient;
import com.magiclabyrinth.bakingapp.retrofit.RecipeApiInterface;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;

public class RecipeItemLoader extends AsyncTaskLoader<List<Recipe.RecipeItem>> {
    private static final String TAG = RecipeItemLoader.class.getSimpleName();
    private List<Recipe.RecipeItem> recipeItems;

    public RecipeItemLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading(){
        if (recipeItems == null){
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<Recipe.RecipeItem> loadInBackground() {
        RecipeApiInterface apiService = RecipeApiClient.getRetrofit().create(RecipeApiInterface.class);
        Call<List<Recipe.RecipeItem>> call = apiService.getRecipeItems();
        List<Recipe.RecipeItem> result = new LinkedList<>();

        try {
            result = call.execute().body();
        } catch (IOException ioe){
            Log.e(TAG, ioe.toString());
        }

        return result;
    }

    @Override
    public void deliverResult(List<Recipe.RecipeItem> recipes){
        recipeItems = recipes;
        super.deliverResult(recipes);
    }
}
