package com.magiclabyrinth.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.magiclabyrinth.bakingapp.R;
import com.magiclabyrinth.bakingapp.model.Recipe;

public class BakingAppWidgetProvider extends AppWidgetProvider {
    private static final String RECIPE_ITEM = "recipe_item";
    public static Recipe.RecipeItem recipeItem;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews remoteViews = getRecipeListRemoteView(context);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BakingAppGetRecipeService.startActionGetRecipe(context);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
                for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static RemoteViews getRecipeListRemoteView(Context context){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bakingapp_appwidget);

        Intent intent = new Intent(context, BakingAppUpdateWidgetService.class);

        if (recipeItem != null) {
            views.setTextViewText(R.id.tv_widget_title, recipeItem.getName() + " Recipe");
        }

        views.setRemoteAdapter(R.id.lv_widget_recipe_ingredients_list, intent);

        return views;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String ACTION = intent.getAction();

        if (ACTION.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE) && intent.hasExtra(RECIPE_ITEM)) {

            recipeItem = (Recipe.RecipeItem) intent.getParcelableExtra(RECIPE_ITEM);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName widget = new ComponentName(context,
                    BakingAppWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(widget),
                    R.id.lv_widget_recipe_ingredients_list);
        }

        super.onReceive(context, intent);
    }


}
