package no.mofifo.imber.retrofit;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * This deserializer will deserialize a date string in UTC time zone with the ISO 8601 format, into the date4j.DateTime type
 * with time zone GMT+1.
 * ISO 8601 from Canvas API: "YYYY-MM-DDTHH:MM:SSZ"
 *
 * Created by andre on 03.01.17.
 */
public class DateTimeDeserializer implements JsonDeserializer<DateTime> {
    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String raw = json.getAsString();
        raw = raw.replace('T', ' ');
        raw = raw.substring(0, raw.length() - 1);

        DateTime dateTime = new DateTime(raw);
        TimeZone oldTimeZone = TimeZone.getTimeZone("UTC");
        TimeZone newTimeZone = TimeZone.getTimeZone("Europe/Oslo");
        dateTime = dateTime.changeTimeZone(oldTimeZone, newTimeZone);
        return dateTime;
    }
}
