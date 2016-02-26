package com.zjianhao.bean;

/**
 * Created by zjianhao on 15-12-6.
 */
public class App {
    private int versionId;
    private String versionName;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String description;

    public String getVersionName() {
        return versionName;
    }



    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
