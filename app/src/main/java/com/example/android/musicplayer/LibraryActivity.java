package com.example.android.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {
    private static final LibraryActivity instance = new LibraryActivity();
    ListView listView;

    public static LibraryActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        listView = findViewById(R.id.list);
        //ArrayList for Songs
        ArrayList<Song> songList = new ArrayList<>();

        /*
        Manually generating song details to fill the app. Wouldn't be included in a real app, since the library would have actual songs in it.
        The song duration would usually be set via seconds being converted to minutes, but this is purely a structure app, rather than a functioning one,
        so no actual songs are here to get info from.
        Songs that don't have a album are singles. I made a seperate constructor for those in the Song class and manually set the album to "Single"
         */
        //By Source, Fair use, https://en.wikipedia.org/w/index.php?curid=63679726
        //By Source, Fair use, https://en.wikipedia.org/w/index.php?curid=63575330
        //By Source, Fair use, https://en.wikipedia.org/w/index.php?curid=63845949

        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0)
                songList.add(new Song("Breaking Me", "2:47", "Topic ft A7S", R.drawable.topic_and_a7s_breaking_me));
            else if (i % 2 == 0)
                songList.add(new Song("Rain On Me", "3:09", "Lady Gaga & Ariana Grande", "Chromatica", R.drawable.lady_gaga_chromatica));
            else
                songList.add(new Song("Rockstar", "3:01", "DaBaby ft. Roddy Ricch", "Blame It on Baby", R.drawable.dababy_blame_it_on_baby));

        }
        SongAdapter itemsAdapter = new SongAdapter(this, songList);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int songID, long id) {
                playSong(songID);
            }
        });
    }

    public final void playSong(int songID) {
        Intent playSongIntent = new Intent(LibraryActivity.this, MusicPlayerActivity.class);

        Song currentSong = (Song) listView.getItemAtPosition(songID);
        int nextSong = 0;
        int previousSong = 0;

        if (listView.getItemAtPosition(songID + 1) != null)
            nextSong = songID + 1;
        if (listView.getItemAtPosition(songID - 1) != null)
            previousSong = songID - 1;


        playSongIntent.putExtra("songName", currentSong.getName());
        playSongIntent.putExtra("songArtist", currentSong.getArtist());
        playSongIntent.putExtra("songAlbum", currentSong.getAlbum());
        playSongIntent.putExtra("songLengthString", currentSong.getSongLengthString());
        playSongIntent.putExtra("songLengthInt", currentSong.getSongLengthTime());
        playSongIntent.putExtra("songAlbumArt", currentSong.getAlbumArtID());
        playSongIntent.putExtra("nextSongID", nextSong);
        playSongIntent.putExtra("previousSongID", previousSong);


        startActivity(playSongIntent);
    }
}