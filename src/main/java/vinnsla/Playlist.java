package vinnsla;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Playlist implements Serializable {
    private String name;
    private List<String> songPaths;

    public Playlist(String name) {
        this.name = name;
        this.songPaths = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSongPaths() {
        return songPaths;
    }

    public void addSong(String songPath) {
        songPaths.add(songPath);
    }

    public void setSongPaths(List<String> songPaths) {
        this.songPaths = songPaths;
    }

}
