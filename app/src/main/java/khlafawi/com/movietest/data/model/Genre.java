package khlafawi.com.movietest.data.model;

public class Genre {

    private int id;
    public static final String _id = "id";

    private String name;
    public static final String _name = "name";

    public Genre() {
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
