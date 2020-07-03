package com.example.android.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        //ArrayList for Songs
        ArrayList<Song> songList = new ArrayList<>();

        /*
        Manually generating song details to fill the app. Wouldn't be included in a real app, since the library would have actual songs in it.
        The song duration would usually be set via seconds being converted to minutes, but this is purely a structure app, rather than a functioning one,
        so no actual songs are here to get info from.
        Songs that don't have a album are singles. I made a seperate constructor for those in the Song class and manually set the album to "Single"
         */
        songList.add(new Song("Rockstar", "3:01", "DaBaby ft. Roddy Ricch", "Blame It on Baby"));
        songList.add(new Song("Rain On Me", "3:09", "Lady Gaga & Ariana Grande", "Chromatica"));
        songList.add(new Song("Breaking Me", "2:47", "Topic ft A7S"));
        songList.add(new Song("Rockstar", "3:01", "DaBaby ft. Roddy Ricch", "Blame It on Baby"));
        songList.add(new Song("Rain On Me", "3:09", "Lady Gaga & Ariana Grande", "Chromatica"));
        songList.add(new Song("Breaking Me", "2:47", "Topic ft A7S"));
        songList.add(new Song("Rockstar", "3:01", "DaBaby ft. Roddy Ricch", "Blame It on Baby"));
        songList.add(new Song("Rain On Me", "3:09", "Lady Gaga & Ariana Grande", "Chromatica"));
        songList.add(new Song("Breaking Me", "2:47", "Topic ft A7S"));

        //By Source, Fair use, https://en.wikipedia.org/w/index.php?curid=63679726
        songList.get(0).setAlbumArtID(R.drawable.dababy_blame_it_on_baby);

        //By Source, Fair use, https://en.wikipedia.org/w/index.php?curid=63575330
        songList.get(1).setAlbumArtID(R.drawable.lady_gaga_chromatica);

        //By Source, Fair use, https://en.wikipedia.org/w/index.php?curid=63845949
        songList.get(2).setAlbumArtID(R.drawable.topic_and_a7s_breaking_me);

        //Same images repeated so I have 9 "tracks" total.
        songList.get(3).setAlbumArtID(R.drawable.dababy_blame_it_on_baby);
        songList.get(4).setAlbumArtID(R.drawable.lady_gaga_chromatica);
        songList.get(5).setAlbumArtID(R.drawable.topic_and_a7s_breaking_me);
        songList.get(6).setAlbumArtID(R.drawable.dababy_blame_it_on_baby);
        songList.get(7).setAlbumArtID(R.drawable.lady_gaga_chromatica);
        songList.get(8).setAlbumArtID(R.drawable.topic_and_a7s_breaking_me);

        SongAdapter itemsAdapter = new SongAdapter(this, songList);
        final ListView listView = findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent playSongIntent = new Intent(LibraryActivity.this, MusicPlayerActivity.class);
                Song currentSong = (Song) listView.getItemAtPosition(position);
                playSongIntent.putExtra("songName", currentSong.getName());
                playSongIntent.putExtra("songArtist", currentSong.getArtist());
                playSongIntent.putExtra("songAlbum", currentSong.getAlbum());
                playSongIntent.putExtra("songLengthString", currentSong.getSongLengthString());
                playSongIntent.putExtra("songLengthInt", currentSong.getSongLengthTime());
                playSongIntent.putExtra("songAlbumArt", currentSong.getAlbumArtID());

                startActivity(playSongIntent);
            }
        });
    }
}