package ch.hslu.appe.reminder.genius.DB.Converter;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateConverter {

    @TypeConverter
    public static LocalDate fromTimestamp(Long value) {
        return value == null
                ? null
                : new Date(value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @TypeConverter
    public static Long dateToTimestamp(LocalDate date) {
        return date == null
                ? null
                : Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime();
    }
}
