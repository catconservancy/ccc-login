package org.rmcc.ccc.exception;

public class InvalidImageTypeException extends Exception {


    private String filePath;

    public InvalidImageTypeException(String path) {
        this.filePath = path;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
