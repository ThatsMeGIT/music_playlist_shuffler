package musicplaylistshuffler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Playlist {
    private String name;
    private List<Song> songs;
    private List<Song> shuffledSongs;
    private Mode mode; // from ENUM Mode

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

    public int playlistTimeLength() {
        int playlistlength = 0;
        if (songs != null) {
            for (Song song : songs) {
                playlistlength += song.getDuration();
            }
            return playlistlength;
        } else return 0;
    }

    public String topGenre() {
        if (songs.isEmpty()) {
            return null;
        }

        Map<String, Integer> countGenre = new HashMap<>();

        for (Song song : songs) {
            String genre = song.getGenre();
            countGenre.put(genre, countGenre.getOrDefault(genre, 0) + 1);
        }

        String topGenre = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : countGenre.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                topGenre = entry.getKey();
            }
        }
        return topGenre;
    }

    public void shuffleON() {
        mode = Mode.SHUFFLED;

        shuffledSongs = new ArrayList<>(songs);
        Random rand = new Random();

        for (int i = 0; i < shuffledSongs.size(); i++) {
            int randomIndex = i + rand.nextInt(shuffledSongs.size() - i);

            Song tmp = shuffledSongs.get(i);
            shuffledSongs.set(i, shuffledSongs.get(randomIndex));
            shuffledSongs.set(randomIndex, tmp);
        }
    }

    public void shuffleOFF() {
        mode = Mode.NORMAL;
    }

    public List<Song> getCurrentList() {
        if (mode == Mode.SHUFFLED && shuffledSongs != null) {
            return shuffledSongs;
        } else return songs;
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongs() {
        return new ArrayList<Song>(songs);
    }

    public Song getSpecificSong(int index) {
        return getCurrentList().get(index);
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }
}
