package vinnsla;

import vinnsla.Playlist;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class PlaylistManager {
    private static final String FILE_NAME = "playlists.ser";


    public static void savePlaylists(List<Playlist> playlists) {
        for (Playlist playlist : playlists) {
            String fileName = playlist.getName() + ".dat"; // Use playlist name and ".dat" extension
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
                outputStream.writeObject(playlist);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @SuppressWarnings("unchecked")
    public static List<Playlist> loadPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        File directory = new File("."); // Change this if playlists are stored in a specific directory
        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".dat")) { // Check for ".dat" extension
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
        return playlists;
    }

}