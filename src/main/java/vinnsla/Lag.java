package vinnsla;

public class Lag {
    private final String song;
    private final String name;

    private final int length;

    private final String image;


    public Lag(String path, String image, String name, int length) {
        song = path;
        this.name = name;
        this.length = length;
        this.image = image;
    }
}
