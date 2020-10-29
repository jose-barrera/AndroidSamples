package com.jlbc.sounddemokotlin

import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sound_demo.*
import java.io.IOException

class SoundDemo : AppCompatActivity(), View.OnClickListener {

    private enum class AudioSystems {
        SoundPool, MediaPlayer
    }

    private var mediaPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private var assetFD1: AssetFileDescriptor? = null
    private var assetFD2: AssetFileDescriptor? = null
    private var assetFD3: AssetFileDescriptor? = null
    private var idSong1 = -1
    private var idSong2 = -1
    private var idSong3 = -1
    private var nowPlaying = -1
    private var volume = 0.3f
    private var selectedAudioSystem = AudioSystems.SoundPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_demo)

        // SoundPool class changed in Lollipop version, so it is
        // necessary to identify the version to use the correct implementation.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // The new way of using a SoundPool object is with an AudioAttributes
            // object to set the attributes of the pool of sound wanted.
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            // Initializes the SoundPool object
            // https://developer.android.com/reference/android/media/SoundPool.Builder
            soundPool = SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attributes)
                .build()
        } else {
            // This is the older way for SoundPool
            // https://developer.android.com/reference/android/media/SoundPool#SoundPool(int,%20int,%20int)
            soundPool = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
        }

        // MediaPlayer class changed in Oreo version, so it is
        // necessary to identify the version to use the correct implementation.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            // Initializes the MediaPlayer object
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioAttributes(attributes)
        } else {
            // This is the older way for MediaPlayer
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
        mediaPlayer?.setVolume(volume, volume)

        // Loads the songs into memory and gets the id of the
        // corresponding asset for each song.
        try {
            val manager = assets
            assetFD1 = manager.openFd("Balls to the Wall.mp3")
            assetFD2 = manager.openFd("Transylvania.mp3")
            assetFD3 = manager.openFd("Warriors of the World united.mp3")
            idSong1 = soundPool!!.load(assetFD1, 0)
            idSong2 = soundPool!!.load(assetFD2, 0)
            idSong3 = soundPool!!.load(assetFD3, 0)
        } catch (e: IOException) {
            Toast.makeText(this, "Failed to load song files!", Toast.LENGTH_LONG).show()
            buttonSong1.isEnabled = false
            buttonSong2.isEnabled = false
            buttonSong3.isEnabled = false
            buttonStop.isEnabled = false
        }

        // Sets listeners
        buttonSong1.setOnClickListener(this)
        buttonSong2.setOnClickListener(this)
        buttonSong3.setOnClickListener(this)
        buttonStop.setOnClickListener(this)

        // The SeekBar object controls the volume of the music.
        // Calculates the volume based on SeekBar's progress and
        // sets the volume on SoundPool.
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                volume = progress / 10f
                soundPool?.setVolume(nowPlaying, volume, volume)
                mediaPlayer?.setVolume(volume, volume)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // This Spinner object changes the audio system.
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                // Sets the selected audio system
                when (position) {
                    0 -> selectedAudioSystem = AudioSystems.SoundPool
                    1 -> selectedAudioSystem = AudioSystems.MediaPlayer
                }
                // Stops any current reproduction
                soundPool?.stop(nowPlaying)
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer?.stop()
                    mediaPlayer?.reset()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // This method frees all resources associated with
    // audio systems.
    override fun onDestroy() {
        super.onDestroy()
        soundPool!!.release()
        soundPool = null
        mediaPlayer!!.release()
        mediaPlayer = null
    }

    // This method is the listener associated to buttons
    // used to play or stop the songs.
    // https://developer.android.com/reference/android/media/SoundPool#play(int,%20float,%20float,%20int,%20int,%20float)
    override fun onClick(view: View) {
        if (selectedAudioSystem == AudioSystems.SoundPool) soundPoolActions(view)
        if (selectedAudioSystem == AudioSystems.MediaPlayer) mediaPlayerActions(view)
    }

    // This method is the specific actions for SoundPool audio system.
    private fun soundPoolActions(view: View) {
        when (view.id) {
            R.id.buttonSong1 -> {
                // Stops the current song and play the first one
                soundPool!!.stop(nowPlaying)
                nowPlaying = soundPool!!.play(idSong1, volume, volume, 0, 0, 1f)
            }
            R.id.buttonSong2 -> {
                // Stops the current song and play the second one
                soundPool!!.stop(nowPlaying)
                nowPlaying = soundPool!!.play(idSong2, volume, volume, 0, 0, 1f)
            }
            R.id.buttonSong3 -> {
                // Stops the current song and play the third one
                soundPool!!.stop(nowPlaying)
                nowPlaying = soundPool!!.play(idSong3, volume, volume, 0, 0, 1f)
            }
            R.id.buttonStop ->
                // Stops the current song
                soundPool!!.stop(nowPlaying)
        }
    }

    // This method is the specific actions for MediaPlayer audio system.
    private fun mediaPlayerActions(view: View) {
        try {
            when (view.id) {
                R.id.buttonSong1 -> {
                    // Stops the current song and play the first one
                    if (mediaPlayer!!.isPlaying) {
                        mediaPlayer!!.stop()
                        mediaPlayer!!.reset()
                    }
                    mediaPlayer!!.setDataSource(assetFD1!!.fileDescriptor, assetFD1!!.startOffset, assetFD1!!.length)
                    mediaPlayer!!.prepare()
                    mediaPlayer!!.start()
                }
                R.id.buttonSong2 -> {
                    // Stops the current song and play the second one
                    if (mediaPlayer!!.isPlaying) {
                        mediaPlayer!!.stop()
                        mediaPlayer!!.reset()
                    }
                    mediaPlayer!!.setDataSource(assetFD2!!.fileDescriptor, assetFD2!!.startOffset, assetFD2!!.length)
                    mediaPlayer!!.prepare()
                    mediaPlayer!!.start()
                }
                R.id.buttonSong3 -> {
                    // Stops the current song and play the third one
                    if (mediaPlayer!!.isPlaying) {
                        mediaPlayer!!.stop()
                        mediaPlayer!!.reset()
                    }
                    mediaPlayer!!.setDataSource(assetFD3!!.fileDescriptor, assetFD3!!.startOffset, assetFD3!!.length)
                    mediaPlayer!!.prepare()
                    mediaPlayer!!.start()
                }
                R.id.buttonStop ->
                    // Stops the current song
                    if (mediaPlayer!!.isPlaying) {
                        mediaPlayer!!.stop()
                        mediaPlayer!!.reset()
                    }
            }
        } catch (e: IOException) {
            Toast.makeText(this, "Failed to play song files!", Toast.LENGTH_LONG).show()
        }
    }
}

/*
 *  Reference: Horton (2018). Android Programming for Beginners
 */