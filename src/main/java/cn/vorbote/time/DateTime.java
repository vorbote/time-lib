package cn.vorbote.time;

import cn.vorbote.commons.MathUtil;
import cn.vorbote.commons.StringUtil;
import cn.vorbote.time.exceptions.TimeOutRangeException;

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
     * @param mills  The mills (0 through 999).
     */
    private static void check(int year, int month, int date, int hour, int minute, int second, int mills) {
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

        // Check the hour
        if (MathUtil.IsNotBetween(hour, 0, 23)) {
            throw new TimeOutRangeException(StringUtil.Format("The hour: {} is out of range of (0 ~ 23)", hour));
        }

        // Check the minute
        if (MathUtil.IsNotBetween(minute, 0, 59)) {
            throw new TimeOutRangeException(StringUtil.Format("The minute: {} is out of range of (0 ~ 59)", minute));
        }

        // Check the second
        if (MathUtil.IsNotBetween(second, 0, 59)) {
            throw new TimeOutRangeException(StringUtil.Format("The second: {} is out of range of (0 ~ 59)", second));
        }

        // Check the mills
        if (MathUtil.IsNotBetween(mills, 0, 999)) {
            throw new TimeOutRangeException(StringUtil.Format("The mills: {} is out of range of (0 ~ 59)", hour));
        }
    }

    private long timestamp = System.currentTimeMillis();
    private String pattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * Generate a new DateTime instance of {@code current} time.
     */
    public DateTime() {
    }

    /**
     * Build a {@code DateTime} instance by java timestamp or
     * unix timestamp.
     *
     * @param timestamp Unix Timestamp or Java Timestamp.
     */
    public DateTime(long timestamp) {
        if (String.valueOf(timestamp).matches("\\d{10}")) { // 对于Unix Timestamp
            this.timestamp = timestamp * 1000L;
        } else if (String.valueOf(timestamp).matches("\\d{13}")) { // 对于Java Timestamp
            this.timestamp = timestamp;
        }
    }

    /**
     * Generate a new DateTime instance of {@code current} time.
     *
     * @param date A {@code Date} instance.
     */
    public DateTime(Date date) {
        this.timestamp = date.getTime();
    }

    /**
     * Generate a new DateTime instance of {@code current} time.
     *
     * @param calendar A {@code Calendar} instance.
     */
    public DateTime(Calendar calendar) {
        this.timestamp = calendar.getTimeInMillis();
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

        check(year, month, date, 0, 0, 0, 0);

        calendar.set(year, month - 1, date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
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
        check(year, month, date, hour, minute, second, 0);

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

        this.timestamp = calendar.getTimeInMillis();
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
        check(year, month, date, hour, minute, second, mills);

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

        // Add the mills to the timestamp
        calendar.set(Calendar.MILLISECOND, mills);

        // Add the minutes to the timestamp
        timestamp = calendar.getTimeInMillis();
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
        calendar.add(Calendar.DATE, ts.getDays());
        calendar.add(Calendar.HOUR, ts.getHours());
        calendar.add(Calendar.MINUTE, ts.getMinutes());
        calendar.add(Calendar.SECOND, ts.getSeconds());
        calendar.add(Calendar.MILLISECOND, ts.getMilliseconds());
        this.timestamp = calendar.getTimeInMillis();
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
        calendar.setTimeInMillis(timestamp);

        // Calculate the times.
        var seconds = (int) (days * 24 * 60 * 60);

        // Add time
        calendar.add(Calendar.SECOND, seconds);
        timestamp = calendar.getTimeInMillis();
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
        calendar.setTimeInMillis(timestamp);

        // Calculate the seconds to be added.
        var seconds = (int) (hours * 60 * 60);

        // Add to the calendar instance.
        calendar.add(Calendar.SECOND, seconds);
        timestamp = calendar.getTimeInMillis();
        return this;
    }

    /**
     * Returns a new {@code DateTime} that adds the specified number
     * of hours to the value of this instance.
     *
     * @param milliseconds A number of whole and fractional
     *                     millisecond. The value parameter can be
     *                     negative or positive.
     * @return An object whose value is the sum of the date and time
     * represented by this instance and the number of
     * milliseconds represented by value.
     */
    public DateTime AddMilliseconds(int milliseconds) {
        // Set the current time to the time of the current instance.
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        // Add to the calendar instance.
        calendar.add(Calendar.MILLISECOND, milliseconds);
        timestamp = calendar.getTimeInMillis();
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
        calendar.setTimeInMillis(timestamp);

        var seconds = (int) (minutes * 60);

        // Add to the calendar instance.
        calendar.add(Calendar.SECOND, seconds);
        timestamp = calendar.getTimeInMillis();
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
        calendar.setTimeInMillis(timestamp);

        // Add to the calendar instance.
        calendar.add(Calendar.MONTH, months);
        timestamp = calendar.getTimeInMillis();
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
        calendar.setTimeInMillis(timestamp);

        // Add to the calendar instance.
        calendar.add(Calendar.SECOND, seconds);
        timestamp = calendar.getTimeInMillis();
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
        calendar.setTimeInMillis(timestamp);

        // Add to the calendar instance.
        calendar.add(Calendar.YEAR, years);
        timestamp = calendar.getTimeInMillis();
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
        calendar.add(Calendar.DATE, -ts.getDays());
        calendar.add(Calendar.HOUR, -ts.getHours());
        calendar.add(Calendar.MINUTE, -ts.getMinutes());
        calendar.add(Calendar.SECOND, -ts.getSeconds());
        calendar.add(Calendar.MILLISECOND, -ts.getMilliseconds());
        this.timestamp = calendar.getTimeInMillis();
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

        // There are no differences between them, then return
        // a zero-time span.
        if (this == time) {
            return span;
        }

        var ts = Timestamp() - time.Timestamp();

        // Set milliseconds.
        span.setMilliseconds((int) (ts % 1000));
        ts /= 1000;

        // Set days.
        span.setDays((int) (ts / 86400));
        ts %= 86400;

        // Set hours.
        span.setHours((int) (ts / 3600));
        ts %= 3600;

        // Set minutes.
        span.setMinutes((int) (ts / 60));
        ts %= 60;

        // Set seconds.
        span.setSeconds((int) ts);

        return span;
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
        final var date = new Date(timestamp);
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
        calendar.setTimeInMillis(Timestamp());
        var year = calendar.get(Calendar.YEAR);
        return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
    }
}
