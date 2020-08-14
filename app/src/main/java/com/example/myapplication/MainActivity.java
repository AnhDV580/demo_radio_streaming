package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar playSeekBar;
    private Button buttonPlay;
    private Button buttonStopPlay;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUIElements();
        initializeMediaPlayer();
    }

    private void initializeUIElements() {

        playSeekBar = (ProgressBar) findViewById(R.id.progressBar1);
        playSeekBar.setMax(100);
        playSeekBar.setVisibility(View.INVISIBLE);

        buttonPlay = (Button) findViewById(R.id.btnPlayRadio);
        buttonPlay.setOnClickListener(this);

        buttonStopPlay = (Button) findViewById(R.id.stop);
        buttonStopPlay.setEnabled(false);
        buttonStopPlay.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v == buttonPlay) {
            try {
                startPlaying();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (v == buttonStopPlay) {
            stopPlaying();
        }
    }

    private void startPlaying() throws IOException {
        buttonStopPlay.setEnabled(true);
        buttonPlay.setEnabled(false);

        playSeekBar.setVisibility(View.VISIBLE);


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.prepare();

    }

    private void stopPlaying() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            initializeMediaPlayer();
        }

        buttonPlay.setEnabled(true);
        buttonStopPlay.setEnabled(false);
        playSeekBar.setVisibility(View.INVISIBLE);
    }

    private void initializeMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        String radioUrl = "http://ice24.securenetsystems.net/WBGW";
        String audioMp3Url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
        try {
            mediaPlayer.setDataSource(radioUrl);
//            mediaPlayer.setDataSource(audioMp3Url);
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                playSeekBar.setSecondaryProgress(percent);
                Log.i("Buffering", "" + percent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}