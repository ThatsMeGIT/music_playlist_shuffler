package musicplaylistshuffler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String name;
    private List<Song> songs;

    @JsonCreator
    public Playlist(@JsonProperty("name") String name,
                    @JsonProperty("songs") List<Song> songs) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be null or blank");
        }
        if (songs == null) {
            throw new IllegalArgumentException("songs must not be null");
        }

        this.name = name;
        this.songs = new ArrayList<>(songs);
    }

    public double averageSongLength() {
        double averageLength = 0;
        if (songs != null) {
            for (Song song : songs) {
                averageLength += song.getDuration();
            }
            return averageLength / songs.size();
        } else return 0;
    }

    public int playlistLength() {
        int playlistlength = 0;
        if (songs != null) {
            for (Song song : songs) {
                playlistlength += song.getDuration();
            }
            return playlistlength;
        } else return 0;
    }

    // most genre implementation through maps

    // SHUFFLE FINALLY (hopefully i guess)

    public String getName() {
        return name;
    }

    public List<Song> getSongs() {
        return new ArrayList<Song>(songs);
    }

    public Song getSpecificSong(int index) {
        return songs.get(index);
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }
}
