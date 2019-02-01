package com.magiclabyrinth.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.magiclabyrinth.bakingapp.model.Recipe;

public class BakingAppGetRecipeService extends IntentService {
    private static final String TAG = BakingAppGetRecipeService.class.getSimpleName();
    public static final String ACTION_GET_RECIPE = "com.magiclabyrinth.bakingapp.widget.action.get_recipe";

    public BakingAppGetRecipeService(){
        super("BakingAppGetRecipeService");
    }

    public static void startActionGetRecipe(Context context){
        Intent intent = new Intent(context, BakingAppGetRecipeService.class);
        intent.setAction(ACTION_GET_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null){
            final String action = intent.getAction();

            if (action.equals(ACTION_GET_RECIPE)){
                handleActionGetRecipe();
            }
        }
    }

    private void handleActionGetRecipe() {
        while (Recipe.RECIPE_ITEMS.isEmpty()) {
            Log.i(TAG, "in handleActionGetRecipe, total recipes size: " + Recipe.RECIPE_ITEMS.size());

            try {
                Thread.sleep(50);
                Log.i(TAG, "RecipeSize: " + Recipe.RECIPE_ITEMS.size());
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
        BakingAppWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds);
    }
}
