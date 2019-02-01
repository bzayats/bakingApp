package com.magiclabyrinth.bakingapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.magiclabyrinth.bakingapp.R;
import com.magiclabyrinth.bakingapp.RecipeStepFragment;
import com.magiclabyrinth.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

public class RecipeStepsAdapter
        extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private Context mContext;
    private Recipe.RecipeItem mRecipeItem;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Recipe.RecipeStep item = (Recipe.RecipeStep) view.getTag();

            Bundle arguments = new Bundle();
            arguments.putString(RecipeStepFragment.ARG_RECIPE_STEP_ITEM_ID, item.getId());
            arguments.putString(RecipeStepFragment.ARG_RECIPE_ID, mRecipeItem.getId());

            if (mTwoPane) {
                arguments.putBoolean(RecipeStepFragment.ARG_TWO_PANEL, mTwoPane);
                RecipeStepFragment fragment = new RecipeStepFragment();
                fragment.setArguments(arguments);
                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_steps_container, fragment)
                        .commit();
            } else {
                RecipeStepFragment fragment = new RecipeStepFragment();
                fragment.setArguments(arguments);
                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_detail_container, fragment)
                        .commit();
            }
        }
    };

    public RecipeStepsAdapter(Context context,
                              Recipe.RecipeItem item,
                              boolean twoPane) {
        mContext = context;
        mRecipeItem = item;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_details_list_steps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mShortDescription.setText(mRecipeItem.getStepsList().get(position).getShortDescription());

        if (!mRecipeItem.getStepsList().get(position).getThumbnailURL().isEmpty()) {
            Picasso.with(mContext)
                    .load(mRecipeItem.getStepsList().get(position).getThumbnailURL())
                    //TODO: change this image to a custom made step image (this is for the error)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.mThumbnail);
        } else {
            Picasso.with(mContext)
                    .load(R.drawable.play_icon_black)
                    .into(holder.mThumbnail);
        }

        holder.itemView.setTag(mRecipeItem.getStepsList().get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mRecipeItem.getStepsList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mShortDescription;
        final ImageView mThumbnail;

        ViewHolder(View view) {
            super(view);
            mShortDescription = (TextView) view.findViewById(R.id.tv_short_description);
            mThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
        }
    }

    public void setRecipe(Recipe.RecipeItem mRecipeItem) {
        this.mRecipeItem = mRecipeItem;
        notifyDataSetChanged();
    }

    public void clear() {
        mRecipeItem = null;
    }
}