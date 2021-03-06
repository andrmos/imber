package no.mofifo.imber.models;


import com.google.gson.annotations.SerializedName;

import hirondelle.date4j.DateTime;

/**
 * Created by PatrickFinseth on 23.02.16.
 */
public class CalendarEvent implements Comparable<CalendarEvent> {

    private static final String TAG = "CalendarEvent";

    private String id;
    private String title;
    @SerializedName("location_name")
    private String location;
    @SerializedName("start_at")
    private DateTime startDate;
    @SerializedName("end_at")
    private DateTime endDate;
    private boolean hidden;

    public CalendarEvent(String id, String title, String location, DateTime startDate, DateTime endDate, boolean hidden) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hidden = hidden;
    }


    public boolean isHidden() {
        return hidden;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getTrimmedTitle() {
        return trimEventName(title);
    }

    public String getTrimmedLocation() {
        return trimLocation(location);
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Remove brackets and unnecessary title info
     *
     * @param name
     * @return
     */
    private String trimEventName(String name) {
        String[] splitArray = name.split(" ");
        String trimedName = "";

        if (splitArray[0].startsWith("[")) {
            String course_code = "";
            for (int i = 1; i < splitArray[0].length(); i++) {
                if (splitArray[0].charAt(i) != ']') {
                    course_code += splitArray[0].charAt(i);
                }

            }
            trimedName += course_code;
            trimedName += " " + splitArray[1];
        } else {
            trimedName = name;
        }

        return trimedName;
    }

    private String trimLocation(String location) {
        if (location != null) {

            String trimmedLocation = location.trim();

            if (trimmedLocation.startsWith("(")) {
                trimmedLocation = trimmedLocation.substring(1, trimmedLocation.length());

                if (location.endsWith(")")) {
                    trimmedLocation = trimmedLocation.substring(0, trimmedLocation.length() - 1);
                    return trimmedLocation;
                }
            }


            return location.trim();
        } else {
            return "";
        }
    }

    @Override
    public int compareTo(CalendarEvent event) {
        if (this.getStartDate().equals(event.getStartDate())) {
            return 0;
        } else if (this.getStartDate().gt(event.getStartDate())) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        CalendarEvent that = (CalendarEvent) o;
        return id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", hidden=" + hidden +
                '}';
    }
}