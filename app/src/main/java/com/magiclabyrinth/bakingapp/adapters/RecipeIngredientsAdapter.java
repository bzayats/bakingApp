package com.magiclabyrinth.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magiclabyrinth.bakingapp.R;
import com.magiclabyrinth.bakingapp.model.Recipe;

public class RecipeIngredientsAdapter
            extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {

        private Context mContext;
        private Recipe.RecipeItem mRecipeItem;

        public RecipeIngredientsAdapter(Context context,
                                        Recipe.RecipeItem item) {
            mContext = context;
            mRecipeItem = item;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_details_list_ingredients, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIngredient.setText(mRecipeItem.getIngredientList().get(position).getIngredient());
            holder.mQuantity.setText(mRecipeItem.getIngredientList().get(position).getQuantity());
            holder.mMeasure.setText(mRecipeItem.getIngredientList().get(position).getMeasure());
        }

        @Override
        public int getItemCount() {
            return mRecipeItem.getIngredientList().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIngredient;
            final TextView mQuantity;
            final TextView mMeasure;

            ViewHolder(View view) {
                super(view);
                mIngredient = (TextView) view.findViewById(R.id.tv_ingredient);
                mQuantity = (TextView) view.findViewById(R.id.tv_quantity);
                mMeasure = (TextView) view.findViewById(R.id.tv_measure);
            }
        }

        public void setRecipe(Recipe.RecipeItem mRecipeItem) {
            this.mRecipeItem = mRecipeItem;
            notifyDataSetChanged();
        }

        public void clear(){
            mRecipeItem = null;
        }
    }