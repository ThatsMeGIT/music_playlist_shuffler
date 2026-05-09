package musicplaylistshuffler.model;

public class Song {
    private String title;
    private String artist;
    private int duration;
    private String genre;

    public Song(String title, String artist, int duration, String genre) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be null or blank");
        }
        if (artist == null || artist.isBlank()) {
            throw new IllegalArgumentException("artist must not be null or blank");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("duration must be greater than 0");
        }
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("genre must not be null or blank");
        }

        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

}
