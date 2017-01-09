package com.mossige.finseth.follo.inf219_mitt_uib.models;

import com.google.gson.annotations.SerializedName;

import hirondelle.date4j.DateTime;

/**
 * Created by andre on 09.01.17.
 */

public class Folder {

    private int id;
    @SerializedName("context_id")
    private int contextId;
    @SerializedName("context_type")
    private String contextType;
    @SerializedName("folders_url")
    private int position;
    private String foldersUrl;
    @SerializedName("files_url")
    private String filesUrl;
    @SerializedName("files_count")
    private int filesCount;
    @SerializedName("folders_count")
    private int foldersCount;
    private String name;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("parent_folder_id")
    private int parentFolderId;
    @SerializedName("created_at")
    private DateTime createdAt;
    @SerializedName("updated_at")
    private DateTime updatedAt;

    public Folder(int id, int contextId, String contextType, int position, String foldersUrl, String filesUrl, int filesCount, int foldersCount, String name, String fullName, int parentFolderId, DateTime createdAt, DateTime updatedAt) {
        this.id = id;
        this.contextId = contextId;
        this.contextType = contextType;
        this.position = position;
        this.foldersUrl = foldersUrl;
        this.filesUrl = filesUrl;
        this.filesCount = filesCount;
        this.foldersCount = foldersCount;
        this.name = name;
        this.fullName = fullName;
        this.parentFolderId = parentFolderId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public int getContextId() {
        return contextId;
    }

    public String getContextType() {
        return contextType;
    }

    public int getPosition() {
        return position;
    }

    public String getFoldersUrl() {
        return foldersUrl;
    }

    public String getFilesUrl() {
        return filesUrl;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public int getFoldersCount() {
        return foldersCount;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public int getParentFolderId() {
        return parentFolderId;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }
}
