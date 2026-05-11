package musicplaylistshuffler;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Song;

public class MusicPlaylistShuffler {
//    static void main(String[] args) {
//
//        Playlist pl_1 = new Playlist(
//                "Playlist_1",
//                List.of(
//                        new Song("Bohemian Rhapsody", "Queen", 355, "Rock"),
//                        new Song("Billie Jean", "Michael Jackson", 290, "Pop"),
//                        new Song("Stairway to Heaven", "Led Zeppelin", 475, "Rock")
//                )
//        );
//        Playlist pl_2 = new Playlist(
//                "Playlist_2",
//                List.of(
//                        new Song("unendlichkeit - Main Edit", "CRO", 216, "Hip-Hop"),
//                        new Song("1000x COOLER", "beastboy", 147, "Hip-Hop"),
//                        new Song("DARE", "Gorillaz", 245, "Hip-Hop")
//                )
//        );
//
//        Scanner selectPlaylist = new Scanner(System.in);
//
//        while (true) {
//            System.out.println("Welche Playlist willst du hören?: ");
//            String input = selectPlaylist.nextLine();
//
//            switch (input) {
//
//                case "Playlist_1":
//                    for (Song song : pl_1.getSongs()) {
//                        System.out.println(song.getTitle() + " | " + song.getArtist() + " | " + song.getDuration() + "s | " + song.getGenre());
//                    }
//                    break;
//
//                case "Playlist_2":
//                    for (Song song : pl_2.getSongs()) {
//                        System.out.println(song.getTitle() + " | " + song.getArtist() + " | " + song.getDuration() + "s | " + song.getGenre());
//                    }
//                    break;
//
//                default:
//                    System.out.println("Playlist mit diesem Namen existiert nicht.");
//                    continue;
//            }
//
//            break;
//        }
//        selectPlaylist.close();
//
//    }

    public static void main() {
        Player np = new Player();
        np.setPlayingSong(new Song("Bohemian Rhapsody", "Queen", 355, "Rock"));
        System.out.println();
        try {
            np.startPlaying();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
