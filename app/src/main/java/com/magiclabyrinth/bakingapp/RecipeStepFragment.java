package com.magiclabyrinth.bakingapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.magiclabyrinth.bakingapp.model.Recipe;

public class RecipeStepFragment extends Fragment implements ExoPlayer.EventListener{
    public static final String ARG_RECIPE_STEP_ITEM_ID = "step_id";
    public static final String ARG_RECIPE_ID = "recipe_id";
    public static final String ARG_TWO_PANEL = "two_panel";

    private static final String BUNDLE_PLAYER_POSITION_KEY = "player_position";
    private static final String BUNDLE_PLAYER_PLAY_WHEN_READY_KEY = "player_play_when_ready";
    private static final String BUNDLE_PLAYER_CURRENT_WINDOW_INDEX_KEY = "player_current_window_index";

    private final String TAG = RecipeStepFragment.class.getSimpleName();
    private Recipe.RecipeItem mRecipeItem;
    private Recipe.RecipeIngredient mRecipeIngredient;
    private Recipe.RecipeStep mRecipeStep;
    private TextView shortDescription;
    private TextView description;
    private FloatingActionButton fabNext;
    private FloatingActionButton fabPrevious;
    private static long playbackPosition;
    private static int currentWindow;
    private static boolean playWhenReady;
    private int playerState;
    private int currentRecipeStep;
    private boolean mTwoPanel;

    //new additions
    private PlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    public RecipeStepFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_RECIPE_ID)) {
            mRecipeItem = Recipe.RECIPE_ITEMS.get(Integer.valueOf(getArguments().getString(ARG_RECIPE_ID))-1);
            mRecipeStep = mRecipeItem.getStepsList().get(Integer.valueOf(getArguments().getString(ARG_RECIPE_STEP_ITEM_ID)));

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_content, container, false);

        if (savedInstanceState != null){
            playbackPosition = savedInstanceState.getLong(BUNDLE_PLAYER_POSITION_KEY);
            currentWindow = savedInstanceState.getInt(BUNDLE_PLAYER_CURRENT_WINDOW_INDEX_KEY);
            playWhenReady = savedInstanceState.getBoolean(BUNDLE_PLAYER_PLAY_WHEN_READY_KEY);
        }

        if (mRecipeStep != null) {
            shortDescription = (TextView) rootView.findViewById(R.id.tv_step_short_description);
            description = (TextView) rootView.findViewById(R.id.tv_step_description);
            simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);
            int totalRecipeSteps = mRecipeItem.getStepsList().size();
            currentRecipeStep = Integer.valueOf(getArguments().getString(ARG_RECIPE_STEP_ITEM_ID));
            handleFabs(totalRecipeSteps);
            initUI();
        }

        return rootView;
    }

    private Uri getContentUri(){
        Uri videoUri = Uri.parse(mRecipeStep.getVideoURL());
        Log.i("URL: ", videoUri.toString());

        return videoUri;
    }

    private void handleFabs(int totalRecipeSteps){
        if (!mTwoPanel){
            fabNext = (FloatingActionButton) getActivity().getWindow().findViewById(R.id.fabNext);
            fabPrevious = (FloatingActionButton) getActivity().getWindow().findViewById(R.id.fabPrevious);
            fabNext.setVisibility(View.VISIBLE);
            fabPrevious.setVisibility(View.VISIBLE);

            fabNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentRecipeStep < totalRecipeSteps - 1) {
                        mRecipeStep = mRecipeItem.getStepsList().get(currentRecipeStep + 1);
                        currentRecipeStep++;
                        initUI();
                        releasePlayer();
                        initPlayer(getContentUri());
                    }
                }
            });

            fabPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentRecipeStep > 0) {
                        mRecipeStep = mRecipeItem.getStepsList().get(currentRecipeStep - 1);
                        currentRecipeStep--;
                        initUI();
                        releasePlayer();
                        initPlayer(getContentUri());
                    }
                }
            });
        }
    }

    private void initUI(){
        shortDescription.setText(mRecipeStep.getShortDescription());
        description.setText(mRecipeStep.getDescription());
    }

    private void initPlayer(Uri uri){
        if (player == null){
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                            new DefaultTrackSelector(),
                            new DefaultLoadControl());

            player.seekTo(currentWindow, playbackPosition);
            player.setPlayWhenReady(playWhenReady);
            simpleExoPlayerView.setPlayer(player);
        }

        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri contentUri){

        String userAgent = Util.getUserAgent(getContext(), "Baking App");

        //for mp4 only
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(contentUri);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(BUNDLE_PLAYER_POSITION_KEY, playbackPosition);
        outState.putBoolean(BUNDLE_PLAYER_PLAY_WHEN_READY_KEY, playWhenReady);
        outState.putInt(BUNDLE_PLAYER_CURRENT_WINDOW_INDEX_KEY, currentWindow);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23){
            initPlayer(getContentUri());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Util.SDK_INT <= 23 || player == null){
            initPlayer(getContentUri());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        playbackPosition = 0;
        playWhenReady = false;
        currentWindow = 0;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentOrientation = getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            makeVideoFullScreen();
        } else {
            exitFullScreen();
        }
    }

    private void makeVideoFullScreen(){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
        params.width = params.MATCH_PARENT;
        params.height = params.MATCH_PARENT;

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        }

        shortDescription.setVisibility(LinearLayout.GONE);
        description.setVisibility(LinearLayout.GONE);

        if (!mTwoPanel) {
            fabNext.setVisibility(View.GONE);
            fabPrevious.setVisibility(View.GONE);
        }
        simpleExoPlayerView.setLayoutParams(params);
        /*((ViewGroup)simpleExoPlayerView.getParent())*/getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
    }

    private void exitFullScreen(){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
        params.width = params.MATCH_PARENT;
        params.height = params.MATCH_PARENT;

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        }

        shortDescription.setVisibility(LinearLayout.VISIBLE);
        description.setVisibility(LinearLayout.VISIBLE);

        if (!mTwoPanel) {
            fabNext.setVisibility(View.VISIBLE);
            fabPrevious.setVisibility(View.VISIBLE);
        }
        simpleExoPlayerView.setLayoutParams(params);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
    }

    private void releasePlayer(){
        if (player != null){
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.stop();
            player.release();
            player = null;
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
            getFragmentManager().popBackStackImmediate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState){
            case ExoPlayer.STATE_BUFFERING:
                playerState = PlaybackState.STATE_BUFFERING;
                break;

            case ExoPlayer.STATE_ENDED:
                playerState = PlaybackState.STATE_STOPPED;
                break;

            case ExoPlayer.STATE_IDLE:
                playerState = PlaybackState.STATE_NONE;
                break;

            case ExoPlayer.STATE_READY:
                playerState = playWhenReady ? PlaybackState.STATE_PLAYING : PlaybackState.STATE_PAUSED;
                break;

            default:
                playerState = PlaybackState.STATE_NONE;
                break;
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
