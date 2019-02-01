package com.magiclabyrinth.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class RecipeDetailActivity extends AppCompatActivity {
    private FloatingActionButton fabNext;
    private FloatingActionButton fabPrevious;
    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        fabNext = (FloatingActionButton) findViewById(R.id.fabNext);
        fabPrevious = (FloatingActionButton) findViewById(R.id.fabPrevious);
        fabNext.setVisibility(View.GONE);
        fabPrevious.setVisibility(View.GONE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            launchDetailsFragment();

            if (mTwoPane) {
                launchStepsFragment();
            }
        }
    }

    private void launchDetailsFragment(){
        Bundle arguments = new Bundle();
        arguments.putString(RecipeDetailFragment.ARG_ITEM_ID,
                getIntent().getStringExtra(RecipeDetailFragment.ARG_ITEM_ID));
        mTwoPane = getIntent().getBooleanExtra(RecipeDetailFragment.ARG_TWO_PANEL, false);
        arguments.putBoolean(RecipeDetailFragment.ARG_TWO_PANEL,
                mTwoPane);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_detail_container, fragment)
                .commit();
    }

    private void launchStepsFragment(){
        Bundle args = new Bundle();
        args.putString(RecipeStepFragment.ARG_RECIPE_STEP_ITEM_ID,
                getIntent().getStringExtra(RecipeStepFragment.ARG_RECIPE_STEP_ITEM_ID));
        args.putString(RecipeStepFragment.ARG_RECIPE_ID,
                getIntent().getStringExtra(RecipeStepFragment.ARG_RECIPE_ID));
        args.putBoolean(RecipeStepFragment.ARG_TWO_PANEL,
                getIntent().getBooleanExtra(RecipeStepFragment.ARG_TWO_PANEL, false));

        RecipeStepFragment frg = new RecipeStepFragment();
        frg.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_steps_container, frg)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getInt("fragment") >= 0){
            launchDetailsFragment();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
