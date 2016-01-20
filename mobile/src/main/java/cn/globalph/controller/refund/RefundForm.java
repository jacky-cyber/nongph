package cn.globalph.controller.refund;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class RefundForm implements Serializable {
    protected static final long serialVersionUID = 1L;

    protected String refundDesc;
    protected MultipartFile refundImg;

    public String getRefundDesc() {
        return refundDesc;
    }
    
    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }
    
    public MultipartFile getRefundImg() {
        return refundImg;
    }
    
    public void setRefundImg(MultipartFile refundImg) {
        this.refundImg = refundImg;
    }    
}
