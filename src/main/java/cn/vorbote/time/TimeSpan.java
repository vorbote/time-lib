package cn.vorbote.time;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TimeSpan {

    private int year;   // 年份(0-9999)
    private int month;  // 月份(1-12)
    private int date;   // 日期(1-根据情况而定)
    private int hour;   // 小时(0-23)
    private int minute; // 分钟(0-59)
    private int second; // 秒数(0-59)
    private int mills;  // 毫秒(0-999)
}
