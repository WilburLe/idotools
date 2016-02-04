package com.toolbox.entity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class BannerConfigEntity {
    private String id;
    private String elementId;
    private String previewImageUrl;
    private long   createDate;

    private FileSize  fileSize;
    private ActionUrl actionUrl;
    private String[]  tags;

    public class FileSize {
        private long ldpi;

        public long getLdpi() {
            return ldpi;
        }

        public void setLdpi(long ldpi) {
            this.ldpi = ldpi;
        }

    }

    public class ActionUrl {
        private String hdpi;
        private String mdpi;
        private String ldpi;

        public String getHdpi() {
            return hdpi;
        }

        public void setHdpi(String hdpi) {
            this.hdpi = hdpi;
        }

        public String getMdpi() {
            return mdpi;
        }

        public void setMdpi(String mdpi) {
            this.mdpi = mdpi;
        }

        public String getLdpi() {
            return ldpi;
        }

        public void setLdpi(String ldpi) {
            this.ldpi = ldpi;
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }

    public FileSize getFileSize() {
        return fileSize;
    }

    public void setFileSize(FileSize fileSize) {
        this.fileSize = fileSize;
    }

    public ActionUrl getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(ActionUrl actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

}
