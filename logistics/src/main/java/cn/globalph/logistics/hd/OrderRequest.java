package cn.globalph.logistics.hd;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * @author steven
 * @since 7/11/15
 */
@XStreamAlias("OrderRequest")
public class OrderRequest {
    @XStreamAlias("Partners")
    private String partners;
    @XStreamAlias("Md5Key")
    private String md5Key;
    @XStreamAlias("Orders")
    private List<Order> orders;

    public Order newOrder() {
        return new Order();
    }

    public SenderReceiver newSenderReceiver() {
        return new SenderReceiver();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }

    public String getPartners() {
        return partners;
    }

    public void setPartners(String partners) {
        this.partners = partners;
    }

    @XStreamAlias("order")
    public static class Order {
        @XStreamAlias("DoIds")
        private String doIds;
        @XStreamAlias("BillNo")
        private String billNo;
        @XStreamAlias("ChildsNo")
        private String childsNo;
        @XStreamAlias("Product")
        private String product;
        @XStreamAlias("Count")
        private Integer count;
        @XStreamAlias("Weight")
        private Float weight;
        @XStreamAlias("Value")
        private Float value;
        @XStreamAlias("CodAmt")
        private Float codAmt;
        @XStreamAlias("TopaymentAmt")
        private Float topaymentAmt;
        @XStreamAlias("ReceiptNo")
        private String receiptNo;
        @XStreamAlias("Remark")
        private String remark;
        @XStreamAlias("paymentType")
        private Integer paymentType = 1;
        @XStreamAlias("Sender")
        private SenderReceiver sender;
        @XStreamAlias("Receiver")
        private SenderReceiver receiver;
        @XStreamAlias("ExtendField1")
        private String extendField1;
        @XStreamAlias("ExtendField2")
        private String extendField2;
        @XStreamAlias("ExtendField3")
        private String extendField3;

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

        public String getChildsNo() {
            return childsNo;
        }

        public void setChildsNo(String childsNo) {
            this.childsNo = childsNo;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Float getWeight() {
            return weight;
        }

        public void setWeight(Float weight) {
            this.weight = weight;
        }

        public Float getValue() {
            return value;
        }

        public void setValue(Float value) {
            this.value = value;
        }

        public Float getCodAmt() {
            return codAmt;
        }

        public void setCodAmt(Float codAmt) {
            this.codAmt = codAmt;
        }

        public Float getTopaymentAmt() {
            return topaymentAmt;
        }

        public void setTopaymentAmt(Float topaymentAmt) {
            this.topaymentAmt = topaymentAmt;
        }

        public String getReceiptNo() {
            return receiptNo;
        }

        public void setReceiptNo(String receiptNo) {
            this.receiptNo = receiptNo;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Integer getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(Integer paymentType) {
            this.paymentType = paymentType;
        }

        public SenderReceiver getSender() {
            return sender;
        }

        public void setSender(SenderReceiver sender) {
            this.sender = sender;
        }

        public SenderReceiver getReceiver() {
            return receiver;
        }

        public void setReceiver(SenderReceiver receiver) {
            this.receiver = receiver;
        }

        public String getExtendField1() {
            return extendField1;
        }

        public void setExtendField1(String extendField1) {
            this.extendField1 = extendField1;
        }

        public String getExtendField2() {
            return extendField2;
        }

        public void setExtendField2(String extendField2) {
            this.extendField2 = extendField2;
        }

        public String getExtendField3() {
            return extendField3;
        }

        public void setExtendField3(String extendField3) {
            this.extendField3 = extendField3;
        }
    }

    public static class SenderReceiver {
        @XStreamAlias("Name")
        private String name;
        @XStreamAlias("Tel")
        private String tel;
        @XStreamAlias("Company")
        private String company;
        @XStreamAlias("Addr")
        private String addr;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }
    }
}

