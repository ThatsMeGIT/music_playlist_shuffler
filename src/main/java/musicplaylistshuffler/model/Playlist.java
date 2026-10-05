package musicplaylistshuffler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Playlist {
    private final String name;
    private String description;
    private final List<Song> songs;
    private List<Song> shuffledSongs;
    private Mode mode;
    private Loop loop;

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
        this.mode = Mode.NORMAL;
    }

    public double averageSongLength() {
        if (songs.isEmpty()) {
            return 0;
        }

        return (double) playlistTimeLength() / songs.size();
    }

    public int playlistTimeLength() {
        int playlistLength = 0;

        for (Song song : songs) {
            playlistLength += song.getDuration();
        }

        return playlistLength;
    }

    public String topGenre() {
        if (songs.isEmpty()) {
            return null;
        }

        Map<String, Integer> genreCounts = new HashMap<>();

        for (Song song : songs) {
            String genre = song.getGenre();
            genreCounts.put(genre, genreCounts.getOrDefault(genre, 0) + 1);
        }

        String mostCommonGenre = null;
        int highestCount = 0;

        for (Map.Entry<String, Integer> entry : genreCounts.entrySet()) {
            if (entry.getValue() > highestCount) {
                highestCount = entry.getValue();
                mostCommonGenre = entry.getKey();
            }
        }

        return mostCommonGenre;
    }

    public void shuffleOn() {
        mode = Mode.SHUFFLED;
        shuffledSongs = new ArrayList<>(songs);
        Collections.shuffle(shuffledSongs);
    }

    public void shuffleOff() {
        mode = Mode.NORMAL;
        shuffledSongs = null;
    }

    public void repeatOn() {
        loop = Loop.REPEAT_ON;
    }

    public void repeatOff() {
        loop = Loop.REPEAT_OFF;
    }

    @JsonIgnore
    public boolean isShuffled() {
        return mode == Mode.SHUFFLED;
    }

    @JsonIgnore
    public boolean isOnRepeat() {
        return loop == Loop.REPEAT_ON;
    }

    @JsonIgnore
    public List<Song> getCurrentList() {
        if (mode == Mode.SHUFFLED && shuffledSongs != null) {
            return shuffledSongs;
        }

        return songs;
    }

    public Song getSpecificSong(int index) {
        return getCurrentList().get(index);
    }

    public void addSong(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("song must not be null");
        }

        songs.add(song);

        if (mode == Mode.SHUFFLED) {
            shuffleOn();
        }
    }

    public void removeSong(Song song) {
        songs.remove(song);

        if (mode == Mode.SHUFFLED) {
            shuffleOn();
        }
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    @JsonIgnore
    public Mode getMode() {
        return mode;
    }
}