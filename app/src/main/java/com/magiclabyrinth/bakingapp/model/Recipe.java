package com.magiclabyrinth.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    public static final List<RecipeItem> RECIPE_ITEMS = new ArrayList<RecipeItem>();
    public static final List<RecipeIngredient> RECIPE_INGREDIENTS = new ArrayList<>();
    public static final List<RecipeStep> RECIPE_STEPS = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(RECIPE_ITEMS);
        dest.writeTypedList(RECIPE_INGREDIENTS);
        dest.writeTypedList(RECIPE_STEPS);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in){
        in.readTypedList(RECIPE_ITEMS, Recipe.RecipeItem.CREATOR);
        in.readTypedList(RECIPE_INGREDIENTS, RecipeIngredient.CREATOR);
        in.readTypedList(RECIPE_STEPS, RecipeStep.CREATOR);
    }

    /**
     * Recipe item representing recipe main data.
     */
    public static class RecipeItem implements Parcelable{
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        private String details;
        @SerializedName("servings")
        private String servings;
        @SerializedName("image")
        private String imageURL;

        @SerializedName("ingredients")
        private ArrayList<RecipeIngredient> ingredientsList;
        @SerializedName("steps")
        private ArrayList<RecipeStep> stepsList;

        public RecipeItem(String id, String name, String details, String servings, String imageURL) {
            this.id = id;
            this.name = name;
            this.details = details;
            this.servings = servings;
            this.imageURL = imageURL;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getId(){
            return id;
        }

        public String getName(){
            return name;
        }

        public String getDetails(){
            return details;
        }

        public String getServings(){
            return servings;
        }

        public String getImageURL(){
            return imageURL;
        }

        public List<RecipeIngredient> getIngredientList(){
            return ingredientsList;
        }

        public List<RecipeStep> getStepsList(){
            return stepsList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(details);
            dest.writeString(servings);
            dest.writeString(imageURL);
        }

        public static final Creator<RecipeItem> CREATOR = new Creator<RecipeItem>() {
            @Override
            public RecipeItem createFromParcel(Parcel source) {
                return new RecipeItem(source);
            }

            @Override
            public RecipeItem[] newArray(int size) {
                return new RecipeItem[size];
            }
        };

        private RecipeItem(Parcel in){
            id = in.readString();
            name = in.readString();
            details = in.readString();
            servings = in.readString();
            imageURL = in.readString();
        }
    }

    /**
     * Recipe item representing recipe ingredient data.
     */
    public static class RecipeIngredient implements Parcelable{
        private final String quantity;
        private final String measure;
        private final String ingredient;

        public RecipeIngredient(String quantity, String measure, String ingredient) {
            this.quantity = quantity;
            this.measure = measure;
            this.ingredient = ingredient;
        }

        @Override
        public String toString() {
            return ingredient;
        }

        public String getQuantity(){
            return quantity;
        }

        public String getMeasure(){
            return measure;
        }

        public String getIngredient(){
            return ingredient;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(quantity);
            dest.writeString(measure);
            dest.writeString(ingredient);
        }

        public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>() {
            @Override
            public RecipeIngredient createFromParcel(Parcel source) {
                return new RecipeIngredient(source);
            }

            @Override
            public RecipeIngredient[] newArray(int size) {
                return new RecipeIngredient[size];
            }
        };

        private RecipeIngredient(Parcel in){
            quantity = in.readString();
            measure = in.readString();
            ingredient = in.readString();
        }
    }

    /**
     * Recipe item representing recipe ingredient data.
     */
    public static class RecipeStep implements Parcelable{
        public final String id;
        public final String shortDescription;
        public final String description;
        public final String videoURL;
        public final String thumbnailURL;


        public RecipeStep(String id, String shortDescription, String description, String videoURL, String thumbnailURL) {
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoURL = videoURL;
            this.thumbnailURL = thumbnailURL;
        }

        @Override
        public String toString() {
            return shortDescription;
        }

        public String getId(){
            return id;
        }

        public String getShortDescription(){
            return shortDescription;
        }

        public String getDescription(){
            return description;
        }

        public String getVideoURL(){
            return videoURL;
        }

        public String getThumbnailURL(){
            return thumbnailURL;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(shortDescription);
            dest.writeString(description);
            dest.writeString(videoURL);
            dest.writeString(thumbnailURL);
        }

        public static final Creator<RecipeStep> CREATOR = new Creator<RecipeStep>() {
            @Override
            public RecipeStep createFromParcel(Parcel source) {
                return new RecipeStep(source);
            }

            @Override
            public RecipeStep[] newArray(int size) {
                return new RecipeStep[size];
            }
        };

        private RecipeStep(Parcel in){
            id = in.readString();
            shortDescription = in.readString();
            description = in.readString();
            videoURL = in.readString();
            thumbnailURL = in.readString();
        }
    }
}
