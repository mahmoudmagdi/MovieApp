package khlafawi.com.movietest.data.model;

public class SpokenLanguage {

    private String name;
    public static final String _name = "name";

    private String iso_639_1;
    public static final String _iso_3166_1 = "iso_3166_1";

    public SpokenLanguage() {
    }

    public SpokenLanguage(String name, String iso_639_1) {
        this.name = name;
        this.iso_639_1 = iso_639_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }
}
