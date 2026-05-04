package com.chy.utils;

public class CurrentHolder {

    private static final ThreadLocal<Integer> CURRENT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_ROLE = new ThreadLocal<>();

    public static void setCurrentId(Integer employeeId) {
        CURRENT_ID.set(employeeId);
    }

    public static Integer getCurrentId() {
        return CURRENT_ID.get();
    }

    public static void setCurrentRole(String role) {
        CURRENT_ROLE.set(role);
    }

    public static String getCurrentRole() {
        return CURRENT_ROLE.get();
    }

    public static void remove() {
        CURRENT_ID.remove();
        CURRENT_ROLE.remove();
    }
}