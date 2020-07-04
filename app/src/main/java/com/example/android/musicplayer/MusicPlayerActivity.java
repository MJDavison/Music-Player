package com.example.android.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;


public class MusicPlayerActivity extends AppCompatActivity {
    private static final String LOG_TAG = MusicPlayerActivity.class.getSimpleName();
    final static MusicPlayerActivity musicPlayer = new MusicPlayerActivity();
    int nextSongID;
    int previousSongID;
    int loopToUse = LoopMode.LOOP_MODE_NONE;

    String artist;
    String album;
    String name;
    String length;
    int songDuration;
    int songDurationPlayed;
    int songAlbumArtID;
    ImageView btnPlayPause;
    ImageView btnLoop;

    boolean shuffle = false;
    private boolean songFinished = false;
    boolean playing = false;

    TextView activeSongNameLabel;
    TextView activeSongAlbumArtistLabel;
    TextView activeSongDurationLabel;
    TextView activeSongDurationTimerLabel;
    ImageView activeSongAlbumArt;
    SeekBar seekBar;

    public static MusicPlayerActivity getInstance() {
        return musicPlayer;
    }

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
        nextSongID = songintent.getIntExtra("nextSongID", 0);
        previousSongID = songintent.getIntExtra("previousSongID", 0);


        activeSongNameLabel = findViewById(R.id.activeSongNameLabel);
        activeSongAlbumArtistLabel = findViewById(R.id.activeSongAlbumArtistLabel);
        activeSongDurationLabel = findViewById(R.id.songDuration);
        seekBar = findViewById(R.id.seekBar);
        activeSongAlbumArt = findViewById(R.id.activeSongAlbumArt);

        btnPlayPause = findViewById(R.id.btn_playpause);
        btnLoop = findViewById(R.id.btn_loop);

        activeSongAlbumArtistLabel.setText(album + " - " + artist);
        activeSongNameLabel.setText(name);
        activeSongDurationLabel.setText(length);
        seekBar.setMax(songDuration);
        activeSongAlbumArt.setImageResource(songAlbumArtID);

        runTimer();


        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playing) {
                    playSong();

                } else if (playing) {
                    pauseSong();

                }
            }
        });


        btnLoop.setOnClickListener(new View.OnClickListener() {
            int timesClicked = 0;

            @Override
            public void onClick(View v) {
                if (loopToUse == LoopMode.LOOP_MODE_NONE)
                    loopToUse = LoopMode.LOOP_MODE_ONCE;
                else if (loopToUse == LoopMode.LOOP_MODE_ONCE)
                    loopToUse = LoopMode.LOOP_MODE_INFINITE;
                else if (loopToUse == LoopMode.LOOP_MODE_INFINITE)
                    loopToUse = LoopMode.LOOP_MODE_NONE;

                switch (loopToUse) {
                    case LoopMode.LOOP_MODE_NONE:
                        btnLoop.setImageResource(R.drawable.ic_loop_disabled);
                        timesClicked++;
                        break;

                    case LoopMode.LOOP_MODE_ONCE:
                        btnLoop.setImageResource(R.drawable.ic_loop_once);
                        timesClicked++;
                        break;

                    case LoopMode.LOOP_MODE_INFINITE:
                        btnLoop.setImageResource(R.drawable.ic_loop_infinite);
                        timesClicked = 0;
                        break;
                }
            }
        });

        final ImageView btnShuffle = findViewById(R.id.btn_shuffle);
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!shuffle) {
                    shuffle = true;
                    btnShuffle.setImageResource(R.drawable.ic_shuffle_enabled);
                    shuffleSongs();
                } else if (shuffle) {
                    shuffle = false;
                    btnShuffle.setImageResource(R.drawable.ic_shuffle_disabled);
                }
            }
        });

        final ImageView btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext.setImageResource(R.drawable.ic_next_pressed);
                nextSong();
                btnNext.setImageResource(R.drawable.ic_next);
            }
        });

        final ImageView btnPrevious = findViewById(R.id.btn_previous);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPrevious.setImageResource(R.drawable.ic_previous_pressed);
                previousSong();
                btnPrevious.setImageResource(R.drawable.ic_previous);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int currentProgress = seekBar.getProgress();
                    if ((currentProgress + progress) < currentProgress) {
                        songDurationPlayed -= progress;

                    } else if ((currentProgress + progress) > currentProgress) {
                        songDurationPlayed += progress;
                    } else {
                        songDurationPlayed = seekBar.getProgress();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                playing = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playing = true;
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
                int hours = seekBar.getProgress() / 3600;
                int minutes = (seekBar.getProgress() % 3600) / 60;
                int secs = seekBar.getProgress() % 60;

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
                    seekBar.setProgress(seekBar.getProgress() + 1);
                }

                if (seekBar.getProgress() == songDuration) {
                    pauseSong();
                    songFinished = true;
                    if (loopToUse == LoopMode.LOOP_MODE_NONE) {
                        nextSong();
                        //With a real song, it would reset the duration back to 0... but there aren't any songs, so...
                        seekBar.setProgress(0);
                        playSong();
                    } else if (loopToUse == LoopMode.LOOP_MODE_ONCE) {
                        loopOnce();
                    } else if (loopToUse == LoopMode.LOOP_MODE_INFINITE) {
                        loopInfinite();
                    }

                    if (shuffle)
                        Toast.makeText(MusicPlayerActivity.this, "Shuffle to random song", Toast.LENGTH_SHORT).show();
                }


                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
    }

    void shuffleSongs() {

    }

    void loopOnce() {
        if (loopToUse == LoopMode.LOOP_MODE_ONCE) {
            Toast.makeText(this, "Loop Once... The media player object has a loop function, so this would be handled by that.", Toast.LENGTH_SHORT).show();
            btnLoop.setImageResource(R.drawable.ic_loop_disabled);
            loopToUse = LoopMode.LOOP_MODE_NONE;
        }
    }

    void loopInfinite() {
        if (loopToUse == LoopMode.LOOP_MODE_INFINITE) {
            Toast.makeText(this, "Loop Infinitely... The media player object has a loop function, so this would be handled by that.", Toast.LENGTH_SHORT).show();
        }
        loopToUse = LoopMode.LOOP_MODE_NONE;
        btnLoop.setImageResource(R.drawable.ic_loop_disabled);
    }

    void pauseSong() {
        playing = false;
        btnPlayPause.setImageResource(R.drawable.ic_play);
    }

    void playSong() {

        playing = true;
        btnPlayPause.setImageResource(R.drawable.ic_pause);

        // Extra functionality later possibly.
    }

    public void nextSong() {
        Toast.makeText(this, "Next Song!", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Please read comment below this line", Toast.LENGTH_LONG).show();
        //This would be triggered both by skipping the song manually and the current song finishing.
        //I want to go to the next "song" on the list, but after several hours of googling and searching for an how-to, I've failed to find anything...
    }

    public void previousSong() {
        Toast.makeText(this, "Previous Song!", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Please read comment below this line", Toast.LENGTH_LONG).show();
        //I want to go to the previous "song" on the list, but after several hours of googling and searching for an how-to, I've failed to find anything...
    }

    //I read that using enums is bad practice, so here is this instead.
    //https://stackoverflow.com/questions/32032503/enums-and-android-annotation-intdef
    @IntDef({LoopMode.LOOP_MODE_NONE, LoopMode.LOOP_MODE_ONCE, LoopMode.LOOP_MODE_INFINITE})
    @Retention(RetentionPolicy.SOURCE)
    @interface LoopMode {
        int LOOP_MODE_NONE = 0;
        int LOOP_MODE_ONCE = 1;
        int LOOP_MODE_INFINITE = 2;
    }

}

