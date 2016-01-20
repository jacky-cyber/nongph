package cn.globalph.logistics.hd;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @author steven
 * @since 7/11/15
 */
@XStreamAlias("PushTrackResponse")
public class TrackResponse {
    @XStreamImplicit
    private List<OrderTrack> orderTrackList;

    public List<OrderTrack> getOrderTrackList() {
        return orderTrackList;
    }

    public void setOrderTrackList(List<OrderTrack> orderTrackList) {
        this.orderTrackList = orderTrackList;
    }

    @XStreamAlias("OrderTrack")
    public static class OrderTrack {
        private String tmsServiceCode;
        private String operator;
        private String operatorDate;
        private String orderCode;
        private String orderNo;
        private String status;
        private String scanSite;
        private String station;
        private String ctrName;
        private String content;
        private String remark;

        public String getTmsServiceCode() {
            return tmsServiceCode;
        }

        public void setTmsServiceCode(String tmsServiceCode) {
            this.tmsServiceCode = tmsServiceCode;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getOperatorDate() {
            return operatorDate;
        }

        public void setOperatorDate(String operatorDate) {
            this.operatorDate = operatorDate;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getScanSite() {
            return scanSite;
        }

        public void setScanSite(String scanSite) {
            this.scanSite = scanSite;
        }

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public String getCtrName() {
            return ctrName;
        }

        public void setCtrName(String ctrName) {
            this.ctrName = ctrName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
