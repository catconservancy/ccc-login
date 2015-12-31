package org.rmcc.ccc.model;

public class ErrorInfo {
    private String message;
    private String url;

    public ErrorInfo(String message, String url) {
        this.message = message;
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }
}
