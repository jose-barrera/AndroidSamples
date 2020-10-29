package com.jlbc.sounddemojava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.IOException;

public class SoundDemo extends AppCompatActivity implements View.OnClickListener {

    private enum AudioSystems { SoundPool, MediaPlayer }

    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private AssetFileDescriptor assetFD1;
    private AssetFileDescriptor assetFD2;
    private AssetFileDescriptor assetFD3;
    private int idSong1 = -1;
    private int idSong2 = -1;
    private int idSong3 = -1;
    private int nowPlaying = -1;
    private float volume = 0.3f;
    private AudioSystems selectedAudioSystem = AudioSystems.SoundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sound_demo);

        // Get all buttons references and sets listeners
        Button buttonSong1 = findViewById(R.id.buttonSong1);
        Button buttonSong2 = findViewById(R.id.buttonSong2);
        Button buttonSong3 = findViewById(R.id.buttonSong3);
        Button buttonStop = findViewById(R.id.buttonStop);

        // SoundPool class changed in Lollipop version, so it is
        // necessary to identify the version to use the correct implementation.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // The new way of using a SoundPool object is with an AudioAttributes
            // object to set the attributes of the pool of sound wanted.
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            // Initializes the SoundPool object
            // https://developer.android.com/reference/android/media/SoundPool.Builder
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            // This is the older way for SoundPool
            // https://developer.android.com/reference/android/media/SoundPool#SoundPool(int,%20int,%20int)
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        // MediaPlayer class changed in Oreo version, so it is
        // necessary to identify the version to use the correct implementation.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            // Initializes the MediaPlayer object
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(attributes);
        } else {
            // This is the older way for MediaPlayer
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        mediaPlayer.setVolume(volume, volume);

        // Loads the songs into memory and gets the id of the
        // corresponding asset for each song.
        try {
            AssetManager manager = getAssets();
            assetFD1 = manager.openFd("Balls to the Wall.mp3");
            assetFD2 = manager.openFd("Transylvania.mp3");
            assetFD3 = manager.openFd("Warriors of the World united.mp3");
            idSong1 = soundPool.load(assetFD1, 0);
            idSong2 = soundPool.load(assetFD2, 0);
            idSong3 = soundPool.load(assetFD3, 0);
        } catch(IOException e) {
            Toast.makeText(this, "Failed to load song files!", Toast.LENGTH_LONG).show();
            buttonSong1.setEnabled(false);
            buttonSong2.setEnabled(false);
            buttonSong3.setEnabled(false);
            buttonStop.setEnabled(false);
        }

        // Sets all listeners
        buttonSong1.setOnClickListener(this);
        buttonSong2.setOnClickListener(this);
        buttonSong3.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

        // The SeekBar object controls the volume of the music.
        // Calculates the volume based on SeekBar's progress and
        // sets the volume on SoundPool.
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress / 10f;
                soundPool.setVolume(nowPlaying, volume, volume);
                mediaPlayer.setVolume(volume, volume);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // This Spinner object changes the audio system.
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Sets the selected audio system
                switch(position) {
                    case 0: selectedAudioSystem = AudioSystems.SoundPool; break;
                    case 1: selectedAudioSystem = AudioSystems.MediaPlayer; break;
                }
                // Stops any current reproduction
                soundPool.stop(nowPlaying);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // This method frees all resources associated with
    // audio systems.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
        mediaPlayer.release();
        mediaPlayer = null;
    }

    // This method is the listener associated to buttons
    // used to play or stop the songs.
    // https://developer.android.com/reference/android/media/SoundPool#play(int,%20float,%20float,%20int,%20int,%20float)
    @Override
    public void onClick(View view) {
        if (selectedAudioSystem == AudioSystems.SoundPool) soundPoolActions(view);
        if (selectedAudioSystem == AudioSystems.MediaPlayer) mediaPlayerActions(view);
    }

    // This method is the specific actions for SoundPool audio system.
    private void soundPoolActions(View view) {
        switch (view.getId()) {
            case R.id.buttonSong1:
                // Stops the current song and play the first one
                soundPool.stop(nowPlaying);
                nowPlaying = soundPool.play(idSong1, volume, volume, 0, 0, 1);
                break;
            case R.id.buttonSong2:
                // Stops the current song and play the second one
                soundPool.stop(nowPlaying);
                nowPlaying = soundPool.play(idSong2, volume, volume, 0, 0, 1);
                break;
            case R.id.buttonSong3:
                // Stops the current song and play the third one
                soundPool.stop(nowPlaying);
                nowPlaying = soundPool.play(idSong3, volume, volume, 0, 0, 1);
                break;
            case R.id.buttonStop:
                // Stops the current song
                soundPool.stop(nowPlaying);
                break;
        }
    }

    // This method is the specific actions for MediaPlayer audio system.
    private void mediaPlayerActions(View view) {
        try {
            switch (view.getId()) {
                case R.id.buttonSong1:
                    // Stops the current song and play the first one
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                    mediaPlayer.setDataSource(assetFD1.getFileDescriptor(), assetFD1.getStartOffset(), assetFD1.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    break;
                case R.id.buttonSong2:
                    // Stops the current song and play the second one
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                    mediaPlayer.setDataSource(assetFD2.getFileDescriptor(), assetFD2.getStartOffset(), assetFD2.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    break;
                case R.id.buttonSong3:
                    // Stops the current song and play the third one
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                    mediaPlayer.setDataSource(assetFD3.getFileDescriptor(), assetFD3.getStartOffset(), assetFD3.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    break;
                case R.id.buttonStop:
                    // Stops the current song
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                    break;
            }
        } catch(IOException e) {
            Toast.makeText(this, "Failed to play song files!", Toast.LENGTH_LONG).show();
        }
    }
}

/*
 *  Reference: Horton (2018). Android Programming for Beginners
 */