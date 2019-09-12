package khlafawi.com.movietest.data.model;

public class ProductionCountry {

    private String name;
    public static final String _name = "name";

    private String iso_3166_1;
    public static final String _iso_3166_1 = "iso_3166_1";


    public ProductionCountry() {
    }

    public ProductionCountry(String name, String iso_3166_1) {
        this.name = name;
        this.iso_3166_1 = iso_3166_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }
}

