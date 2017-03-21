package no.mofifo.imber.models;

/**
 * Created by Follo on 20.03.2016.
 */
public class Recipient {

    private String id;
    private String name;
    private String group;
    private boolean checked;

    public Recipient(String id, String name) {
        this.id = id;
        this.name = name;
        this.group = null;
        this.checked = false;
    }

    public Recipient(String id, String name, String group) {
        this.id = id;
        this.name = name;
        this.group = group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean getChecked() {
        return checked;
    }

    public boolean equals(Recipient that) {
        if (!this.getName().equals(that.getName())) {
            return false;
        }

        return this.getId().equals(that.getId());

    }
}
