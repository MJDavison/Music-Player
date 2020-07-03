package com.example.android.musicplayer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SongAdapter extends ArrayAdapter<Song> {


    public SongAdapter(@NonNull Activity context, ArrayList<Song> songList) {
        super(context, 0, songList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
        }

        Song currentSong = getItem(position);

        TextView songNameLabelTextView = listItemView.findViewById(R.id.tvSongNameLabel);
        TextView songAlbumArtistLabelTextView = listItemView.findViewById(R.id.tvSongAlbumArtistLabel);

        if (currentSong != null) {
            songNameLabelTextView.setText(currentSong.getName());
            if (currentSong.getAlbum() != null)
                songAlbumArtistLabelTextView.setText(currentSong.getAlbum() + " - " + currentSong.getArtist());
            else
                songAlbumArtistLabelTextView.setText("Single - " + currentSong.getArtist());
        }

        return listItemView;
    }


}
