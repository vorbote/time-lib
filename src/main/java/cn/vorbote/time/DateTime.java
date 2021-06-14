package cn.vorbote.time;

import cn.vorbote.commons.MathUtil;
import cn.vorbote.commons.StringUtil;
import cn.vorbote.time.exceptions.TimeOutRangeException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateTime class supports useful methods to times.
 *
 * @author vorbote thills@vorbote.cn
 */
public class DateTime implements
        Comparable<DateTime>, Serializable {

    private long timestamp = System.currentTimeMillis();
    private String pattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * Generate a new DateTime instance of {@code current} time.
     */
    public DateTime() {
    }

    /**
     * Generate a specified {@code DateTime} instance of the date.
     *
     * @param year  The year (1 through 9999).
     * @param month The month (1 through 12).
     * @param date  The day (1 through the number of days in month).
     */
    @SuppressWarnings("all")
    public DateTime(int year, int month, int date) {
        // Get the instance of calendar.
        var calendar = Calendar.getInstance();

        // The month number should between 1 ~ 12
        if (MathUtil.IsNotBetween(month, 1, 12)) {
            throw new TimeOutRangeException(StringUtil.Format("The month: {} is out of range of (1 ~ 12).",
                    month));
        }

        var dayInTheMonth = 0;

        // The situation of month is not bewteen 1 to 12 is handled. Therefore
        // no need for a default block.
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                dayInTheMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                dayInTheMonth = 30;
                break;
            case 2:
                dayInTheMonth = ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ? 29 : 28;
                break;
        }

        // The month number should between 1 ~ the days in the month
        if (MathUtil.IsNotBetween(date, 1, dayInTheMonth)) {
            throw new TimeOutRangeException(StringUtil.Format("The date: {} is out of range of (1 ~ {}).",
                    date, dayInTheMonth));
        }

        calendar.set(year, month - 1, date);
        timestamp = calendar.getTimeInMillis();
    }

    /**
     * Generate a specified {@code DateTime} instance of the date.
     *
     * @param year   The year (1 through 9999).
     * @param month  The month (1 through 12).
     * @param date   The day (1 through the number of days in month).
     * @param hour   The hour (0 through 23).
     * @param minute The minute (0 through 59).
     * @param second The second (0 through 59).
     */
    public DateTime(int year, int month, int date, int hour, int minute, int second) {
        this(year, month, date);
        // Check the hour
        if (MathUtil.IsNotBetween(hour, 0, 23)) {
            throw new TimeOutRangeException(StringUtil.Format("The hour: {} is out of range of (0 ~ 23)", hour));
        }
        // Add the hours to the timestamp
        timestamp += 3600L * hour * 1000L;

        // Check the minute
        if (MathUtil.IsNotBetween(minute, 0, 59)) {
            throw new TimeOutRangeException(StringUtil.Format("The minute: {} is out of range of (0 ~ 59)", minute));
        }
        // Add the minutes to the timestamp
        timestamp += 60L * minute * 1000L;

        // Check the second
        if (MathUtil.IsNotBetween(minute, 0, 59)) {
            throw new TimeOutRangeException(StringUtil.Format("The second: {} is out of range of (0 ~ 59)", second));
        }
        // Add the second to the timestamp
        timestamp += second * 1000L;
    }

    /**
     * Generate a specified {@code DateTime} instance of the date.
     *
     * @param year   The year (1 through 9999).
     * @param month  The month (1 through 12).
     * @param date   The day (1 through the number of days in month).
     * @param hour   The hour (0 through 23).
     * @param minute The minute (0 through 59).
     * @param second The second (0 through 59).
     * @param mills  The mills (0 through 999).
     */
    public DateTime(int year, int month, int date, int hour, int minute, int second, int mills) {
        this(year, month, date, hour, minute, second);

        // Check the mills
        if (MathUtil.IsNotBetween(minute, 0, 999)) {
            throw new TimeOutRangeException(StringUtil.Format("The mills: {} is out of range of (0 ~ 59)", hour));
        }
        // Add the minutes to the timestamp
        timestamp += mills;
    }

    /**
     * Get the Unix Timestamp of this current time.
     *
     * @return The Unix Timestamp of this {@code DateTime} instance.
     */
    public long Unix() {
        return timestamp / 1000;
    }

    /**
     * Add the specific time to the {@code DateTime} instance.
     *
     * @param ts Time Span.
     * @return The time after added this {@code TimeSpan}.
     */
    public DateTime Add(TimeSpan ts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        calendar.add(Calendar.YEAR, ts.getYear());
        calendar.add(Calendar.MONTH, ts.getMonth());
        calendar.add(Calendar.DATE, ts.getDate());
        calendar.add(Calendar.HOUR, ts.getHour());
        calendar.add(Calendar.MINUTE, ts.getMinute());
        calendar.add(Calendar.SECOND, ts.getSecond());
        calendar.add(Calendar.MILLISECOND, ts.getMills());
        this.timestamp = calendar.getTimeInMillis();
        return this;
    }

    /**
     * Minus the specific time to the {@code DateTime} instance.
     *
     * @param ts Time Span.
     * @return The time after added this {@code TimeSpan}.
     */
    public DateTime Minus(TimeSpan ts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        calendar.add(Calendar.YEAR, -ts.getYear());
        calendar.add(Calendar.MONTH, -ts.getMonth());
        calendar.add(Calendar.DATE, -ts.getDate());
        calendar.add(Calendar.HOUR, -ts.getHour());
        calendar.add(Calendar.MINUTE, -ts.getMinute());
        calendar.add(Calendar.SECOND, -ts.getSecond());
        calendar.add(Calendar.MILLISECOND, -ts.getMills());
        this.timestamp = calendar.getTimeInMillis();
        return this;
    }

    /**
     * Set pattern for this datetime.
     *
     * @param pattern The formatted String.
     */
    public void Pattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Get pattern for this datetime.
     *
     * @return The pattern String.
     */
    public String Pattern() {
        return this.pattern;
    }

    /**
     * Set the timestamp.
     *
     * @param timestamp The timestamp.
     */
    public void Timestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the timestamp.
     *
     * @return The timestamp.
     */
    public long Timestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        final var formatter = new SimpleDateFormat(pattern);
        final var date = new Date(timestamp);
        return formatter.format(date);
    }

    public String ToString() {
        final var formatter = new SimpleDateFormat(pattern);
        final var date = new Date(timestamp);
        return formatter.format(date);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(DateTime o) {
        return 0;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    public int CompareTo(DateTime o) {
        return (int) (this.Timestamp() - o.Timestamp());
    }

    /**
     * Get the current Date and Time.
     *
     * @return The current Date and Time.
     */
    public static DateTime Now() {
        return new DateTime();
    }
}
