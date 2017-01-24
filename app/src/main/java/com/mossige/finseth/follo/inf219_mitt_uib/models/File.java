package com.mossige.finseth.follo.inf219_mitt_uib.models;

import com.google.gson.annotations.SerializedName;

import hirondelle.date4j.DateTime;

/**
 * Created by andre on 09.01.17.
 */

public class File {

    private int id;
    @SerializedName("folder_id")
    private int folderId;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("filename")
    private String fileName;
    private String url;
    private long size;
    @SerializedName("created_at")
    private DateTime createdAt;
    @SerializedName("updated_at")
    private DateTime updatedAt;
    @SerializedName("modified_at")
    private DateTime modifiedAt;
    private boolean hidden;
    @SerializedName("content-type")
    private String contentType;
    @SerializedName("mime_class")
    private String mimeClass;

    public File(int id, int folderId, String displayName, String fileName, String url, long size, DateTime createdAt, DateTime updatedAt, DateTime modifiedAt, boolean hidden, String contentType, String mimeClass) {
        this.id = id;
        this.folderId = folderId;
        this.displayName = displayName;
        this.fileName = fileName;
        this.url = url;
        this.size = size;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.modifiedAt = modifiedAt;
        this.hidden = hidden;
        this.contentType = contentType;
        this.mimeClass = mimeClass;
    }

    public int getId() {
        return id;
    }

    public int getFolderId() {
        return folderId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public long getSize() {
        return size;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public DateTime getModifiedAt() {
        return modifiedAt;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getContentType() {
        return contentType;
    }

    public String getMimeClass() {
        return mimeClass;
    }
}
