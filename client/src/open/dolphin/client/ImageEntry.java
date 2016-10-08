package open.dolphin.client;

import java.io.Serializable;

import javax.swing.*;

/**
 * ImageEntry
 *
 * @author  Kazushi Minagawa, Digital globe, Inc.
 */
public class ImageEntry implements Serializable {

    private String confirmDate;
    private String title;
    private String medicalRole;
    private String contentType;
    private ImageIcon imageIcon;
    private long id;
    private String url;
    private String fileName;
    private String path;
    private int numImages = 1;
    private int width;
    private int height;

    /** Creates a new instance of ImageEntry */
    public ImageEntry() {
    }

    /**
     *
     * @return
     */
    public String getConfirmDate() {
        return confirmDate;
    }

    /**
     *
     * @param val
     */
    public void setConfirmDate(String val) {
        confirmDate = val;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param val
     */
    public void setTitle(String val) {
        title = val;
    }

    /**
     *
     * @return
     */
    public String getMedicalRole() {
        return medicalRole;
    }

    /**
     *
     * @param val
     */
    public void setMedicalRole(String val) {
        medicalRole = val;
    }

    /**
     *
     * @return
     */
    public String getContentType() {
        return contentType;
    }

    /**
     *
     * @param val
     */
    public void setContentType(String val) {
        contentType = val;
    }

    /**
     *
     * @return
     */
    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    /**
     * 
     * @param val
     */
    public void setImageIcon(ImageIcon val) {
        imageIcon = val;
    }

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param val
     */
    public void setId(long val) {
        id = val;
    }

    /**
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     * @return
     */
    public int getNumImages() {
        return numImages;
    }

    /**
     *
     * @param numImages
     */
    public void setNumImages(int numImages) {
        this.numImages = numImages;
    }

    /**
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
