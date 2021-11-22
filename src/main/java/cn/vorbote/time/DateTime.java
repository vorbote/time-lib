package cn.vorbote.time;

import cn.vorbote.commons.except.TimeOutRangeException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * {@code DateTime} class supports useful methods to times.
 * The processing of date and time makes us particularly
 * troubled in Java, so we developed the {@code DateTime}
 * class and {@code TimeSpan} class that refer to the Date
 * processing mechanism of the <b>Dotnet</b> platform.
 * These classes are developed based on Java timestamp and
 * the Calendar class of the Java platform, and it is
 * convenient to use by encapsulating some convenient
 * methods.
 *
 * @author vorbote thills@vorbote.cn
 */
public class DateTime implements
        Comparable<DateTime>, Serializable {

    /**
     * Check whether the year, month and the date is in the
     * correct range.
     *
     * @param year   The year (1 through 9999).
     * @param month  The month (1 through 12).
     * @param date   The day (1 through the number of days in month).
     * @param hour   The hour (0 through 23).
     * @param minute The minute (0 through 59).
     * @param second The second (0 through 59).
     * @throws TimeOutRangeException If the time is set out the correct
     *                               range, the exception will be throw
     *                               out.
     */
    private static void check(int year, int month, int date, int hour, int minute, int second) {
        // The month number should between 1 ~ 12
        if (month <= 0 || month > 12) {
            throw new TimeOutRangeException(String.format("The month: %d is out of range of (1 ~ 12).",
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
        if (date < 1 || date > dayInTheMonth) {
            throw new TimeOutRangeException(String.format("The date: %d is out of range of (1 ~ %d).",
                    date, dayInTheMonth));
        }

        // Check the hour
        if (hour < 0 || hour > 23) {
            throw new TimeOutRangeException(String.format("The hour: %d is out of range of (0 ~ 23)", hour));
        }

        // Check the minute
        if (minute < 0 || minute > 59) {
            throw new TimeOutRangeException(String.format("The minute: %d is out of range of (0 ~ 59)", minute));
        }

        // Check the second
        if (second < 0 || second > 59) {
            throw new TimeOutRangeException(String.format("The second: %d is out of range of (0 ~ 59)", second));
        }
    }

    /**
     * Timestamp, use the form of unix time.
     */
    private long timestamp = System.currentTimeMillis() / 1000L;

    /**
     * Specify the way to format the time.
     */
    private String pattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * Getter for timestamp, returns a unix timestamp.
     *
     * @return Unix timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Generate a new {@code DateTime} instance of {@code current} time.
     */
    public DateTime() {
    }

    /**
     * Build a {@code DateTime} instance by java timestamp or
     * unix timestamp.
     *
     * @param timestamp Unix Timestamp.
     */
    public DateTime(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Generate a new DateTime instance of {@code current} time.
     *
     * @param date A {@code Date} instance.
     */
    public DateTime(Date date) {
        this.timestamp = date.getTime() / 1000L;
    }

    /**
     * Generate a new DateTime instance of {@code current} time.
     *
     * @param calendar A {@code Calendar} instance.
     */
    public DateTime(Calendar calendar) {
        this.timestamp = calendar.getTimeInMillis() / 1000L;
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

        check(year, month, date, 0, 0, 0);

        calendar.set(year, month - 1, date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        timestamp = calendar.getTimeInMillis() / 1000L;
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
        check(year, month, date, hour, minute, second);

        var calendar = Calendar.getInstance();

        // Set the year, month and date
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, date);

        // Add the hours to the timestamp
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        // Add the minutes to the timestamp
        calendar.set(Calendar.MINUTE, minute);

        // Add the second to the timestamp
        calendar.set(Calendar.SECOND, second);

        calendar.set(Calendar.MILLISECOND, 0);

        this.timestamp = calendar.getTimeInMillis() / 1000L;
    }

    /**
     * Get the Unix Timestamp of this current time.
     *
     * @return The Unix Timestamp of this {@code DateTime} instance.
     */
    public long Unix() {
        return timestamp;
    }

    /**
     * Get the Java Timestamp of this (@code DateTime} instance.
     *
     * @return The Java Timestamp of this {@code DateTime} instance, but will lose the millisecond.
     */
    public long Java() {
        return timestamp * 1000L;
    }

    /**
     * Add the specific time to the {@code DateTime} instance.
     *
     * @param ts Time Span.
     * @return The time after added this {@code TimeSpan}.
     */
    public DateTime Add(TimeSpan ts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp * 1000L));
        calendar.add(Calendar.DATE, ts.getDays());
        calendar.add(Calendar.HOUR, ts.getHours());
        calendar.add(Calendar.MINUTE, ts.getMinutes());
        calendar.add(Calendar.SECOND, ts.getSeconds());
        this.timestamp = calendar.getTimeInMillis() / 1000L;
        return this;
    }

    /**
     * Returns a new {@code DateTime} that adds the specified number
     * of days to the value of this instance.
     *
     * @param days A number of whole and fractional days. The value
     *             parameter can be negative or positive.
     * @return An object whose value is the sum of the date and
     * time represented by this instance and the number of
     * days represented by value.
     */
    public DateTime AddDays(double days) {
        // Set the current time to the time of the current instance.
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000L);

        // Calculate the times.
        var seconds = (int) (days * 24 * 60 * 60);

        // Add time
        calendar.add(Calendar.SECOND, seconds);
        timestamp = calendar.getTimeInMillis() / 1000L;
        return this;
    }

    /**
     * Returns a new {@code DateTime} that adds the specified number
     * of hours to the value of this instance.
     *
     * @param hours A number of whole and fractional hours. The
     *              value parameter can be negative or positive.
     * @return An object whose value is the sum of the date and time
     * represented by this instance and the number of hours
     * represented by value.
     */
    public DateTime AddHours(double hours) {
        // Set the current time to the time of the current instance.
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000L);

        // Calculate the seconds to be added.
        var seconds = (int) (hours * 60 * 60);

        // Add to the calendar instance.
        calendar.add(Calendar.SECOND, seconds);
        timestamp = calendar.getTimeInMillis() / 1000L;
        return this;
    }


    /**
     * Returns a new {@code DateTime} that adds the specified number
     * of hours to the value of this instance.
     *
     * @param minutes A number of whole and fractional minutes. The
     *                value parameter can be negative or positive.
     * @return An object whose value is the sum of the date and time
     * represented by this instance and the number of minutes
     * represented by value.
     */
    public DateTime AddMinutes(double minutes) {
        // Set the current time to the time of the current instance.
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000L);

        var seconds = (int) (minutes * 60);

        // Add to the calendar instance.
        calendar.add(Calendar.SECOND, seconds);
        timestamp = calendar.getTimeInMillis() / 1000L;
        return this;
    }

    /**
     * Returns a new {@code DateTime} that adds the specified number
     * of months to the value of this instance.
     *
     * @param months A number of months. The months parameter can be
     *               negative or positive.
     * @return An object whose value is the sum of the date and time
     * represented by this instance and months.
     */
    public DateTime AddMonths(int months) {
        // Set the current time to the time of the current instance.
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000L);

        // Add to the calendar instance.
        calendar.add(Calendar.MONTH, months);
        timestamp = calendar.getTimeInMillis() / 1000L;
        return this;
    }

    /**
     * Returns a new {@code DateTime} that adds the specified number
     * of seconds to the value of this instance.
     *
     * @param seconds A number of whole and fractional seconds. The
     *                value parameter can be negative or positive.
     * @return An object whose value is the sum of the date and time
     * represented by this instance and the number of seconds
     * represented by value.
     */
    public DateTime AddSeconds(int seconds) {
        // Set the current time to the time of the current instance.
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000L);

        // Add to the calendar instance.
        calendar.add(Calendar.SECOND, seconds);
        timestamp = calendar.getTimeInMillis() / 1000L;
        return this;
    }

    /**
     * Returns a new {@code DateTime} that adds the specified number
     * of years to the value of this instance.
     *
     * @param years A number of years. The value parameter can be
     *              negative or positive.
     * @return An object whose value is the sum of the date and time
     * represented by this instance and the number of years
     * represented by value.
     */
    public DateTime AddYears(int years) {
        // Set the current time to the time of the current instance.
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000L);

        // Add to the calendar instance.
        calendar.add(Calendar.YEAR, years);
        timestamp = calendar.getTimeInMillis() / 1000L;
        return this;
    }

    /**
     * Minus the specific time to the {@code DateTime} instance.
     *
     * @param ts Time Span.
     * @return The time after added this {@code TimeSpan}.
     */
    public DateTime Minus(TimeSpan ts) {
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000L);
        calendar.add(Calendar.DATE, -ts.getDays());
        calendar.add(Calendar.HOUR, -ts.getHours());
        calendar.add(Calendar.MINUTE, -ts.getMinutes());
        calendar.add(Calendar.SECOND, -ts.getSeconds());
        this.timestamp = calendar.getTimeInMillis() / 1000L;
        return this;
    }

    /**
     * A {@code DateTime} instance minus another instance will
     * return a {@code TimeSpan} instance, this
     * {@code TimeSpan} instance will tell you how many days,
     * hours, minutes, seconds and milliseconds between them.
     *
     * @param time Another {@code DateTime} instance
     * @return A {@code TimeSpan} instance which will tell
     * you how many days, hours, minutes, seconds and
     * milliseconds between them.
     */
    public TimeSpan Minus(DateTime time) {
        var span = new TimeSpan();

        var seconds = this.timestamp - time.timestamp;
        span.setSeconds((int) seconds);

        return span;
    }

    /**
     * Set pattern for this datetime.
     *
     * @param pattern The formatted String.
     */
    public DateTime Pattern(String pattern) {
        this.pattern = pattern;
        return this;
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
    public DateTime Timestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Get the timestamp.
     *
     * @return The timestamp.
     * @see #Unix()
     * @see #getTimestamp()
     */
    public long Timestamp() {
        return this.timestamp;
    }

    /**
     * This method {@code o.toString()} will convert the timestamp
     * to a string time expression in the specified format.
     *
     * @return A string time expression.
     * @see #ToString()
     */
    @Override
    public String toString() {
        final var formatter = new SimpleDateFormat(pattern);
        final var date = new Date(timestamp * 1000L);
        return formatter.format(date);
    }

    /**
     * This method {@code o.toString()} will convert the timestamp
     * to a string time expression in the specified format.
     *
     * @return A string time expression.
     */
    public String ToString() {
        final var formatter = new SimpleDateFormat(pattern);
        final var date = new Date(timestamp * 1000L);
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
        return (int) (this.Timestamp() - o.Timestamp());
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
     * Returns a value indicating whether the value of this instance is equal
     * to the value of the specified {@code DateTime} instance.
     *
     * @param o The object to compare to this instance.
     * @return True if the value parameter equals the value of this instance;
     * Otherwise, false.
     */
    public boolean Equals(DateTime o) {
        return this.Timestamp() == o.Timestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTime dateTime = (DateTime) o;
        return timestamp == dateTime.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp);
    }

    /**
     * Get the current Date and Time.
     *
     * @return The current Date and Time.
     * @see #DateTime()
     */
    public static DateTime Now() {
        return new DateTime();
    }

    /**
     * Returns an indication whether the specified year is a leap year.
     *
     * @return An indication whether the specified year is a leap year.
     */
    public boolean IsLeapYear() {
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Timestamp() * 1000L);
        var year = calendar.get(Calendar.YEAR);
        return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
    }

    /**
     * Convert {@code DateTime} instance to {@code Date} instance.
     *
     * @return {@code Date} instance.
     */
    public Date ToDate() {
        return new Date(this.Timestamp() * 1000L);
    }

    /**
     * Convert {@code DateTime} instance to {@code Calendar} instance.
     *
     * @return {@code Calendar} instance.
     */
    public Calendar ToCalendar() {
        var instance = Calendar.getInstance();
        instance.setTime(this.ToDate());
        return instance;
    }
}
