package edu.coursera.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public class Test {
    public static void main(String[] args) {
        AtomicReference<Object> r = new AtomicReference<>();
        System.out.println(r.get());
    }
}
