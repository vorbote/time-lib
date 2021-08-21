package cn.vorbote.time;

import lombok.*;

/**
 * Represents a time interval. We use this class to
 * show diffs from 2 {@code DateTime} instances.
 *
 * @author vorbote thills@vorbote.cn
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TimeSpan {

    private int days;           // 日期(1-根据情况而定)
    private int hours;          // 小时(0-23)
    private int minutes;        // 分钟(0-59)
    private int seconds;        // 秒数(0-59)
    private int milliseconds;   // 毫秒(0-999)

    @Override
    public String toString() {
        return String.format("%d.%02d:%02d:%02d.%03d",
                days, hours, minutes, seconds, milliseconds);
    }

    /**
     * Get the total seconds in this {@code TimeSpan}.
     *
     * @return The total seconds in this {@code TimeSpan}.
     */
    public long TotalSeconds() {
        return days * 86400L + hours * 3600L + minutes * 60L + seconds;
    }

    /**
     * Get the total milliseconds in this {@code TimeSpan}.
     *
     * @return The total milliseconds in this {@code TimeSpan}.
     */
    public long TotalMilliseconds() {
        return TotalSeconds() * 1000L + milliseconds;
    }

    /**
     * Get the total hours in this {@code TimeSpan}.
     *
     * @return The total milliseconds in this {@code TimeSpan}.
     */
    public double TotalHours() {
        return TotalSeconds() / 60. / 60.;
    }
}
