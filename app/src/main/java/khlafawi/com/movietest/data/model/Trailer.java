package khlafawi.com.movietest.data.model;

public class Trailer {

    private String id;
    public static final String _id = "id";

    private String key;
    public static final String _key = "key";

    public Trailer() {
    }

    public Trailer(String id, String key) {
        this.id = id;
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}