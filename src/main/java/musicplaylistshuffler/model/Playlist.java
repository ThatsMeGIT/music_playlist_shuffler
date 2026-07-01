package musicplaylistshuffler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import musicplaylistshuffler.model.Mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Playlist {
    private String name;
    private List<Song> songs;
    private Mode mode;

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

    // SHUFFLE FINALLY (hopefully i guess)public void sortMitarbeiter() {
    //
    //        Mitarbeiter tmp;
    //
    //        for (int i = 0; i < pv.getSize() - 1; i++) {
    //            for (int j = 0; j < pv.getSize() - 1 - i; j++) {
    //
    //                Mitarbeiter m1 = pv.get(j);
    //                Mitarbeiter m2 = pv.get(j + 1);
    //
    //                if (m1.istKleiner(m2)) {
    //
    //                } else {
    //                    tmp = m1;
    //                    pv.set(j + 1, m1);
    //                    pv.set(j, m2);
    //                }
    //
    //            }
    //        }
    //    }

    public List<Song> Shuffle() {

        List<Song> shuffledSongs = new ArrayList<>();
        Song tmp;

        for (Song song : songs) {
            for (int i = 0; i < songs.toArray().length - 2; i++) {
                for (int j = 0; j < songs.toArray().length - i - 1; j++) {

                    Song shu1 = songs.get(j);
                    Song shu2 = songs.get(j + 1);

                } if (mode == Mode.SHUFFLED) {

                }
            }

        }
        return shuffledSongs;
    }

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
