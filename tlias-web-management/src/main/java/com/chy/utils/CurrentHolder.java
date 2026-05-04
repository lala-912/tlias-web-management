package com.chy.utils;

public class CurrentHolder {

    private static final ThreadLocal<Integer> CURRENT_ID = new ThreadLocal<>();

    public static void setCurrentId(Integer employeeId) {
        CURRENT_ID.set(employeeId);
    }

    public static Integer getCurrentId() {
        return CURRENT_ID.get();
    }

    public static void remove() {
        CURRENT_ID.remove();
    }
}