package no.mofifo.imber.models;

/**
 * User model
 * https://canvas.instructure.com/doc/api/users.html
 *
 * @author Øystein Follo
 * @date 23.02.2016
 */
public class User {

    private int id;
    private String name;
    private String primary_email;
    private String login_id;
    private String sortable_name;

    public User(int id, String name, String primary_email, String login_id, String sortable_name) {
        this.id = id;
        this.login_id = login_id;
        this.primary_email = primary_email;
        this.name = name;
        this.sortable_name = sortable_name;
    }

    public String getPrimary_email() {
        return primary_email;
    }

    public String getLoginId() {
        return login_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSortable_name() {
        return sortable_name;
    }
}
