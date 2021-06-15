package cn.vorbote.time;

import cn.vorbote.commons.StringUtil;
import lombok.*;

/**
 * Represents a time interval.
 *
 * @author vorbote thills@vorbote.cn
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TimeSpan {

    private int days;           // 日期(1-根据情况而定)
    private int hours;          // 小时(0-23)
    private int minutes;        // 分钟(0-59)
    private int seconds;        // 秒数(0-59)
    private int milliseconds;   // 毫秒(0-999)

    /**
     * Getter for field {@code days}.
     *
     * @return The value of {@code days}.
     */
    public int getDays() {
        return days;
    }

    /**
     * Getter for field {@code hours}.
     *
     * @return The value of {@code hours}.
     */
    public int getHours() {
        return hours;
    }

    /**
     * Getter for field {@code minutes}.
     *
     * @return The value of {@code minutes}.
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Getter for field {@code seconds}.
     *
     * @return The value of {@code seconds}.
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Getter for field {@code milliseconds}.
     *
     * @return The value of {@code milliseconds}.
     */
    public int getMilliseconds() {
        return milliseconds;
    }

    /**
     * Setter for field {@code days}.
     *
     * @param days The value of {@code days}.
     */
    protected void setDays(int days) {
        this.days = days;
    }

    /**
     * Setter for field {@code hours}.
     *
     * @param hours The value of {@code hours}.
     */
    protected void setHours(int hours) {
        this.hours = hours;
    }

    /**
     * Setter for field {@code minutes}.
     *
     * @param minutes The value of {@code minutes}.
     */
    protected void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /**
     * Setter for field {@code seconds}.
     *
     * @param seconds The value of {@code seconds}.
     */
    protected void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Setter for field {@code milliseconds}.
     *
     * @param milliseconds The value of {@code milliseconds}.
     */
    protected void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    @Override
    public String toString() {
        return StringUtil.Format("{}.{}:{}:{}.{}",
                days,
                String.format("%02d", hours),
                String.format("%02d", minutes),
                String.format("%02d", seconds),
                String.format("%03d", milliseconds));
    }

    /**
     * Get the total seconds in this {@code TimeSpan}.
     * @return The total seconds in this {@code TimeSpan}.
     */
    public long TotalSeconds() {
        return days * 86400L + hours * 3600L + minutes * 60L + seconds;
    }

    /**
     * Get the total milliseconds in this {@code TimeSpan}.
     * @return The total milliseconds in this {@code TimeSpan}.
     */
    public long TotalMilliseconds() {
        return TotalSeconds() * 1000L + milliseconds;
    }
}
