package com.magiclabyrinth.bakingapp.retrofit;

import com.magiclabyrinth.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeApiInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe.RecipeItem>> getRecipeItems();
}
