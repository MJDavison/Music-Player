package com.example.android.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MusicPlayerActivity extends AppCompatActivity {
    private static final String LOG_TAG = MusicPlayerActivity.class.getSimpleName();
    String artist;
    String album;
    String name;
    String length;
    int songDuration;
    int songDurationPlayed;
    int songAlbumArtID;

    boolean shuffle = false;
    boolean loop = false;
    boolean loopOnce = false;
    boolean playing = false;

    TextView activeSongNameLabel;
    TextView activeSongAlbumArtistLabel;
    TextView activeSongDurationLabel;
    TextView activeSongDurationTimerLabel;
    ImageView activeSongAlbumArt;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        Intent songintent = getIntent();

        artist = songintent.getStringExtra("songArtist");
        album = songintent.getStringExtra("songAlbum");
        name = songintent.getStringExtra("songName");
        length = songintent.getStringExtra("songLengthString");
        songDuration = songintent.getIntExtra("songLengthInt", 0);
        songAlbumArtID = songintent.getIntExtra("songAlbumArt", 0);

        activeSongNameLabel = findViewById(R.id.activeSongNameLabel);
        activeSongAlbumArtistLabel = findViewById(R.id.activeSongAlbumArtistLabel);
        activeSongDurationLabel = findViewById(R.id.songDuration);
        seekBar = findViewById(R.id.seekBar);
        activeSongAlbumArt = findViewById(R.id.activeSongAlbumArt);


        activeSongAlbumArtistLabel.setText(album + " - " + artist);
        activeSongNameLabel.setText(name);
        activeSongDurationLabel.setText(length);
        seekBar.setMax(songDuration);
        activeSongAlbumArt.setImageResource(songAlbumArtID);

        runTimer();

        final ImageView btnPlayPause = findViewById(R.id.btn_playpause);
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playing) {
                    playing = true;
                    btnPlayPause.setImageResource(R.drawable.ic_pause);
                } else if (playing) {
                    playing = false;
                    btnPlayPause.setImageResource(R.drawable.ic_play);
                }
            }
        });
    }

    private void runTimer() {
        //Timer info from here: https://www.geeksforgeeks.org/how-to-create-a-stopwatch-app-using-android-studio/
        activeSongDurationTimerLabel = findViewById(R.id.songDurationTimer);

        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = songDurationPlayed / 3600;
                int minutes = (songDurationPlayed % 3600) / 60;
                int secs = songDurationPlayed % 60;

                // Format the seconds into hours, minutes,
                // and seconds.
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);

                // Set the text view text.
                activeSongDurationTimerLabel.setText(time);

                // If running is true, increment the
                // seconds variable.
                if (playing) {
                    songDurationPlayed++;
                    seekBar.setProgress(songDurationPlayed);
                }

                if (songDurationPlayed == songDuration)
                    nextSong();

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
    }

    void playSong() {

    }

    void nextSong() {
        Toast.makeText(this, "Next Song!", Toast.LENGTH_SHORT).show();
    }

    void previousSong() {

    }

}