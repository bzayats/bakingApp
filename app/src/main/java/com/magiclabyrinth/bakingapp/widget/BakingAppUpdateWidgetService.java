package com.magiclabyrinth.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.magiclabyrinth.bakingapp.R;
import com.magiclabyrinth.bakingapp.model.Recipe;

public class BakingAppUpdateWidgetService extends RemoteViewsService {
    private static final String TAG = BakingAppUpdateWidgetService.class.getSimpleName();
    Recipe.RecipeItem recipe;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext());
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        Context mContext;

        public ListRemoteViewsFactory(Context context){
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (BakingAppWidgetProvider.recipeItem != null){
                recipe = Recipe.RECIPE_ITEMS.get(Integer.valueOf(BakingAppWidgetProvider.recipeItem.getId()) - 1);
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (recipe == null){
                return 0;
            }

            return recipe.getIngredientList().size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (recipe == null){
                return null;
            }

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item_ingredients);
            remoteViews.setTextViewText(R.id.tv_widget_ingredient, recipe.getIngredientList().get(position).getIngredient().concat(":"));
            remoteViews.setTextViewText(R.id.tv_widget_quantity, recipe.getIngredientList().get(position).getQuantity());
            remoteViews.setTextViewText(R.id.tv_widget_measure, recipe.getIngredientList().get(position).getMeasure());

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
