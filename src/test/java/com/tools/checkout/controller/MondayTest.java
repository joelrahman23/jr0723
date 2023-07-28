package com.tools.checkout.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MondayTest {

    private static Calendar cacheCalendar;

    public static int getFirstMonday(int year, int month) {
        cacheCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cacheCalendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        cacheCalendar.set(Calendar.MONTH, month);
        cacheCalendar.set(Calendar.YEAR, year);
        return cacheCalendar.get(Calendar.DATE);
    }

    @BeforeEach
    public void setUp() {
        cacheCalendar = Calendar.getInstance();
        System.err.println("Using calendar " + cacheCalendar);
    }

    @Test
    public void test() {
        assertEquals(7, getFirstMonday(2015, Calendar.SEPTEMBER));
    }

}