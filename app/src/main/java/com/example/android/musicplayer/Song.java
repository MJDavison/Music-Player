package com.example.android.musicplayer;

public class Song {
    private final String name;
    private final String songLengthString;
    private final int songLengthTime;
    private String artist;
    private String album;
    private int albumArtID;

    Song(String songName, String songLength, String songArtist, String songAlbum, int songAlbumArtID) {
        name = songName;
        songLengthString = songLength;
        album = songAlbum;
        artist = songArtist;
        albumArtID = songAlbumArtID;

        songLengthTime = timeCalculator();
    }

    Song(String songName, String songLength, String songArtist, int songAlbumArtID) {
        name = songName;
        songLengthString = songLength;
        artist = songArtist;
        album = "Single";
        albumArtID = songAlbumArtID;

        songLengthTime = timeCalculator();
    }

    public int getAlbumArtID() {
        return albumArtID;
    }

    public void setAlbumArtID(int albumArtID) {
        this.albumArtID = albumArtID;
    }

    public String getSongLengthString() {
        return songLengthString;
    }

    public int getSongLengthTime() {
        return songLengthTime;
    }

    private int timeCalculator() {
        int duration = 0;
        String[] time = songLengthString.split(":");
        int minutes = Integer.parseInt(time[0]);
        int seconds = Integer.parseInt(time[1]);
        int minsToSeconds = minutes * 60;
        duration = minsToSeconds + seconds;
        return duration;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getName() {
        return name;
    }


}
