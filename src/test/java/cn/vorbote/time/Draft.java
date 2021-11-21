package cn.vorbote.time;

import org.junit.Test;

import java.util.Calendar;

public class Draft {

    @Test
    public void test01() {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        System.out.println(calendar.getTimeInMillis() / 1000);
    }

    @Test
    public void test02() {
        var now = new DateTime(2021, 12, 11);
        System.out.println(now);
    }

    @Test
    public void test03() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("calendar.getTimeInMillis() = " + calendar.getTimeInMillis());
    }
}
