package org.example.moviesplatform.error.model;

public class WatchHistoryNotFoundException extends RuntimeException {
    public WatchHistoryNotFoundException(String message) {
        super(message);
    }
}