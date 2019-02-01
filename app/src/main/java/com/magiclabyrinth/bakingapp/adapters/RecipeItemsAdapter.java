package com.magiclabyrinth.bakingapp.adapters;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.magiclabyrinth.bakingapp.R;
import com.magiclabyrinth.bakingapp.RecipeDetailActivity;
import com.magiclabyrinth.bakingapp.RecipeDetailFragment;
import com.magiclabyrinth.bakingapp.RecipeStepFragment;
import com.magiclabyrinth.bakingapp.model.Recipe;
import com.magiclabyrinth.bakingapp.widget.BakingAppWidgetProvider;

import java.util.List;

public class RecipeItemsAdapter
            extends RecyclerView.Adapter<RecipeItemsAdapter.ViewHolder> {

        private Context mContext;
        private List<Recipe.RecipeItem> mValues;
        private static final String RECIPE_ITEM = "recipe_item";
        private Recipe.RecipeItem item;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = (Recipe.RecipeItem) view.getTag();
                if (mTwoPane) {
                    Context context = view.getContext();
                    Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, item.getId());
                    intent.putExtra(RecipeDetailFragment.ARG_TWO_PANEL, mTwoPane);
                    intent.putExtra(RecipeStepFragment.ARG_RECIPE_ID, item.getId());
                    intent.putExtra(RecipeStepFragment.ARG_RECIPE_STEP_ITEM_ID, "0");
                    intent.putExtra(RecipeStepFragment.ARG_TWO_PANEL, mTwoPane);

                    context.startActivity(intent);
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, item.getId());
                    intent.putExtra(RecipeDetailFragment.ARG_TWO_PANEL, mTwoPane);

                    context.startActivity(intent);
                }

                //updating widget with
                sendWidgetRefreshBroadcast();
            }
        };

        public RecipeItemsAdapter(Context context,
                           List<Recipe.RecipeItem> items,
                           boolean twoPane) {
            mContext = context;
            mValues = items;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mRecipeIcon.setImageResource(R.drawable.food_icon_2_2x);
            holder.mContentView.setText(mValues.get(position).getName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView mRecipeIcon;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mRecipeIcon = (ImageView) view.findViewById(R.id.iv_recipe_icon);
                mContentView = (TextView) view.findViewById(R.id.name);
            }
        }

        public void setRecipes(List<Recipe.RecipeItem> recipes) {
            mValues = recipes;
            notifyDataSetChanged();
        }

        public void clear(){
            mValues = null;
        }

    private void sendWidgetRefreshBroadcast(){
        Intent intent = new Intent(mContext, BakingAppWidgetProvider.class);
        intent.putExtra(RECIPE_ITEM, item);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        mContext.sendBroadcast(intent);
    }

    }