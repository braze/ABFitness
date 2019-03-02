package udacity.example.com.abfitness.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import udacity.example.com.abfitness.R;

public class WorkoutFragment extends Fragment implements Player.EventListener {

    private static final String ARG_WORK_OUT_VID_URL = "work_out_video_url";
    private static final String ARG_WORK_OUT_VIDEO_DESCRIPTION = "work_out_video_description";
    private static final String INTENT_STRING_URL = "intent_string_url_value";
    private static final String PLAY_WHEN_READY = "PLAY_WHEN_READY";
    private static final String CURRENT_WINDOW = "CURRENT_WINDOW";
    private static final String PLAYBACK_POSITION = "PLAYBACK_POSITION";

    private String mWorkOutVideoUrl;
    private ArrayList<String> mWorkOutDescription;
    private WorkoutFragment.OnFragmentInteractionListener mListener;

    private SimpleExoPlayer mPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private Unbinder mUnBinder;
    private PlayerView mPlayerView;

    private boolean playWhenReady = false;
    private int currentWindow;
    private long playBackPosition;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    public static WorkoutFragment newInstance(String videoUrlString, ArrayList<String> description) {
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_WORK_OUT_VID_URL, videoUrlString);
        args.putStringArrayList(ARG_WORK_OUT_VIDEO_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWorkOutVideoUrl = getArguments().getString(ARG_WORK_OUT_VID_URL);
            mWorkOutDescription = getArguments().getStringArrayList(ARG_WORK_OUT_VIDEO_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            playBackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
        }

        final View rootView = inflater.inflate(R.layout.master_fragment_exercises, container, false);
        mPlayerView = rootView.findViewById(R.id.playerView);

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                R.layout.exercises_list_view_item, mWorkOutDescription);
        ListView listView = rootView.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        mUnBinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WorkoutFragment.OnFragmentInteractionListener) {
            mListener = (WorkoutFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        int display_mode = getResources().getConfiguration().orientation;
        if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
        if (Util.SDK_INT > 23) {
            initPrepareAndPlay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int display_mode = getResources().getConfiguration().orientation;
        if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initPrepareAndPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        outState.putInt(CURRENT_WINDOW, currentWindow);
        outState.putLong(PLAYBACK_POSITION, playBackPosition);
        outState.putString(INTENT_STRING_URL, mWorkOutVideoUrl);
    }

    private void initPrepareAndPlay() {
        initializeMediaSession();
        initPlayer(Uri.parse(mWorkOutVideoUrl));

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Expand video, hide mDescription, hide system UI
            expandVideoView(mPlayerView);
            hideSystemUI();
        }
        playBackPosition = mPlayer.getCurrentPosition();
    }

    public void initPlayer(Uri videoURL) {
        if (mPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mPlayerView.setPlayer(mPlayer);
            mPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), "ABFitness");
            MediaSource mediaSource = new ExtractorMediaSource(videoURL,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            mPlayer.prepare(mediaSource);
            mPlayer.setPlayWhenReady(playWhenReady);
            mPlayer.seekTo(playBackPosition);
        }
    }

    private void expandVideoView (PlayerView exoPlayer) {
        exoPlayer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        exoPlayer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            playBackPosition = mPlayer.getCurrentPosition();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    private void hideSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), "WarmUpFragment");

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                mPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                mPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                mPlayer.seekTo(playBackPosition);
            }
        });
        mediaSession.setActive(true);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

}