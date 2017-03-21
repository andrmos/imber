package no.mofifo.imber.models;

/**
 * Created by Follo on 22.02.2016.
 */
public class Participant {

    private int id;
    private String name;

    public Participant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        if (id != that.id) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }
}
