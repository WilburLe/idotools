package com.toolbox.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Document(collection = "wallpaper")
public class WallpaperEntity {
    @Id
    private String id;
    private String elementId;
    private String src;
    private String previewImageUrl;
    private long   createDate;

    private FileSize     fileSize;
    private ActionUrl    actionUrl;
    private List<String> tags;

    public class FileSize {
        private long hdpi;
        private long mdpi;
        private long ldpi;

        public long getLdpi() {
            return ldpi;
        }

        public void setLdpi(long ldpi) {
            this.ldpi = ldpi;
        }

        public long getHdpi() {
            return hdpi;
        }

        public void setHdpi(long hdpi) {
            this.hdpi = hdpi;
        }

        public long getMdpi() {
            return mdpi;
        }

        public void setMdpi(long mdpi) {
            this.mdpi = mdpi;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

}
