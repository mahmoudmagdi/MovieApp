package khlafawi.com.movietest.data.model;

public class ProductionCompany {

    private int id;
    public static final String _id = "id";

    private String name;
    public static final String _name = "name";

    private String logo_path;
    public static final String _logo_path = "logo_path";

    private String origin_country;
    public static final String _origin_country = "origin_country";


    public ProductionCompany() {
    }

    public ProductionCompany(int id, String name, String logo_path, String origin_country) {
        this.id = id;
        this.name = name;
        this.logo_path = logo_path;
        this.origin_country = origin_country;
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

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public String getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(String origin_country) {
        this.origin_country = origin_country;
    }
}
