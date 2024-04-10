package vinnsla;

import vinnsla.Playlist;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    public static final String PLAYLISTS_DIRECTORY = "playlists";

    public static void savePlaylists(List<Playlist> playlists) {
        File directory = new File(PLAYLISTS_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }

        for (Playlist playlist : playlists) {
            String fileName = playlist.getName() + ".ser"; // Use playlist name and ".ser" extension
            String filePath = PLAYLISTS_DIRECTORY + File.separator + fileName;
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
                outputStream.writeObject(playlist);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Playlist> loadPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        File directory = new File(PLAYLISTS_DIRECTORY);
        File[] playlistFiles = directory.listFiles();
        if (playlistFiles != null) {
            for (File file : playlistFiles) {
                if (file.isFile() && file.getName().endsWith(".ser")) { // Check for ".ser" extension
                    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                        Object obj = inputStream.readObject();
                        if (obj instanceof Playlist) {
                            playlists.add((Playlist) obj);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return playlists;
    }
}
