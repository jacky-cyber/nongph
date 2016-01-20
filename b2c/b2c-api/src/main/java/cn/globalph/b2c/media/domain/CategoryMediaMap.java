package cn.globalph.b2c.media.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "NPH_CATEGORY_MEDIA_MAP")
public class CategoryMediaMap implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    CategoryMediaMapPK categoryMediaMapPK;

    @Column(name = "KEY", nullable = false)
    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public CategoryMediaMapPK getCategoryMediaMapPK() {
        return categoryMediaMapPK;
    }

    public static class CategoryMediaMapPK implements Serializable {
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        @Column(name = "CATEGORY_ID", nullable = false)
        private Long categoryId;

        @Column(name = "MEDIA_ID", nullable = false)
        private Long mediaId;

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        public Long getMediaId() {
            return mediaId;
        }

        public void setMediaId(Long mediaId) {
            this.mediaId = mediaId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            else if (!(obj instanceof CategoryMediaMapPK)) return false;

            return categoryId.equals(((CategoryMediaMapPK) obj).getCategoryId()) &&
            mediaId.equals(((CategoryMediaMapPK) obj).getMediaId());
        }

        @Override
        public int hashCode() {
            return categoryId.hashCode() + mediaId.hashCode();
        }
    }
}
