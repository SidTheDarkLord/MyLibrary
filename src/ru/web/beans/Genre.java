package ru.web.beans;


public class Genre {

    private long id;
    private String name;

    public Genre(String name, long id) {
        this.id = id;
        this.name = name;
    }

    public Genre() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
