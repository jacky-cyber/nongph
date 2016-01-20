package cn.globalph.logistics.hd;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @author steven
 * @since 7/11/15
 */
@XStreamAlias("OrderResponse")
public class OrderResponse {
    @XStreamImplicit
    private List<Result> resultList;

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }

    @XStreamAlias("Result")
    public static class Result {

        @XStreamAlias("DoIds")
        private String doIds;
        @XStreamAlias("BillNo")
        private String billNo;
        @XStreamAlias("Flag")
        private Integer flag;
        @XStreamAlias("Remark")
        private String remark;

        public String getDoIds() {
            return doIds;
        }

        public void setDoIds(String doIds) {
            this.doIds = doIds;
        }

        public String getBillNo() {
            return billNo;
        }

        public void setBillNo(String billNo) {
            this.billNo = billNo;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

}

