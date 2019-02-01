package com.magiclabyrinth.bakingapp;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magiclabyrinth.bakingapp.adapters.RecipeIngredientsAdapter;
import com.magiclabyrinth.bakingapp.adapters.RecipeStepsAdapter;
import com.magiclabyrinth.bakingapp.model.Recipe;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class RecipeDetailFragment extends Fragment {
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private FloatingActionButton fab;
    private FloatingActionButton fab2;

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_TWO_PANEL = "two_panel";

    private Recipe.RecipeItem mRecipeItem;
    private TextView mIngredients;
    private RecyclerView mIngredientsRecyclerView;
    private RecipeIngredientsAdapter mIngredientsAdapter;
    private boolean mTwoPanel;
    private RecyclerView mStepsRecyclerView;
    private RecipeStepsAdapter mRecipeStepsAdapter;

    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mRecipeItem = Recipe.RECIPE_ITEMS.get(Integer.valueOf(getArguments().getString(ARG_ITEM_ID)) - 1);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipeItem.getName());
            }
        }

        if (getArguments().containsKey(ARG_TWO_PANEL)){
            mTwoPanel = getArguments().getBoolean(ARG_TWO_PANEL);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setVisibility(View.GONE);
        fab2.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        if (mRecipeItem != null) {

            mIngredients = (TextView) rootView.findViewById(R.id.tv_details_ingredients);
            mIngredients.setText(getTextIngredients(mRecipeItem));

            mRecipeStepsAdapter = new RecipeStepsAdapter(getActivity(), mRecipeItem, mTwoPanel);
            mStepsRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recipe_steps_list);
            mStepsRecyclerView.setAdapter(mRecipeStepsAdapter);

            DividerItemDecoration stepsDecoration = new DividerItemDecoration(getContext(), VERTICAL);
            mStepsRecyclerView.addItemDecoration(stepsDecoration);

            fab = (FloatingActionButton) getActivity().getWindow().findViewById(R.id.fabNext);
            fab2 = (FloatingActionButton) getActivity().getWindow().findViewById(R.id.fabPrevious);
        }

        return rootView;
    }

    private String getTextIngredients(Recipe.RecipeItem mRecipeItem){
        String allIngredients = "";

        for (int i = 0; i < mRecipeItem.getIngredientList().size(); ++i){
            allIngredients = allIngredients.concat(mRecipeItem.getIngredientList().
                    get(i).getIngredient()).concat(" | ").concat(mRecipeItem.getIngredientList().
                            get(i).getQuantity()).concat(" | ").
                    concat(mRecipeItem.getIngredientList().get(i).getMeasure() + "\n");
        }

        return allIngredients;
    }
}
