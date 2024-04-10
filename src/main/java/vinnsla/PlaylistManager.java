package vinnsla;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    public static final String PLAYLISTSFOLDER = "playlists";

    /*
    aðferð sem vistar playlistana í möppuni fyrir playlist en ef notandi er ekki
    búin að keyra forritið áður þá er búið til nýja möppu
     */
    public static void savePlaylists(List<Playlist> playlists) {
        File directory = new File(PLAYLISTSFOLDER);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (Playlist playlist : playlists) {
            String fileName = playlist.getName() + ".ser";
            String filePath = PLAYLISTSFOLDER + File.separator + fileName;
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
                outputStream.writeObject(playlist);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //hin aðferðin vistar og þessi hleður niður "loadar"
    @SuppressWarnings("unchecked")
    public static List<Playlist> loadPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        File directory = new File(PLAYLISTSFOLDER);
        File[] playlistFiles = directory.listFiles();
        if (playlistFiles != null) {
            for (File file : playlistFiles) {
                if (file.isFile() && file.getName().endsWith(".ser")) {
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

    //notað fyrir aðferðina í playlistController til að eyða playlista
    public static void removePlaylist(Playlist playlist) {
        List<Playlist> playlists = loadPlaylists();
        playlists.remove(playlist);
        savePlaylists(playlists);
    }

}
