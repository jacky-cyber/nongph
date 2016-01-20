package cn.globalph.logistics.hd;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author steven
 * @since 7/11/15
 */
@XStreamAlias("TrackRequest")
public class TrackRequest {
    @XStreamAlias("Partners")
    private String partners;
    @XStreamAlias("Md5Key")
    private String md5Key;

    public String getPartners() {
        return partners;
    }

    public void setPartners(String partners) {
        this.partners = partners;
    }

    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }
}
