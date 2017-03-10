package org.rmcc.ccc.exception;

public class InvalidImageTypeException extends Exception {


    private String filePath;
    private boolean isThumb;

    public InvalidImageTypeException(String path, boolean isThumb) {
        this.filePath = path;
        this.isThumb = isThumb;
    }
    public InvalidImageTypeException(String path) {
        this.filePath = path;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isThumb() {
        return isThumb;
    }

    public void setThumb(boolean thumb) {
        isThumb = thumb;
    }
}
