package cn.globalph.logistics.ph;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author steven
 * @since 7/24/15
 */
@Entity
@Table(name = "ph_express")
public class Express {
    @Id
    @Column(name = "express_no")
    private String expressNo;
    @Column(name = "order_no")
    private String orderNo;
    @Column(name = "receiver")
    private String receiver;
    @Column(name = "receiver_phone")
    private String receiverPhone;
    @Column(name = "receiver_address")
    private String receiverAddress;
    @Column(name = "sender")
    private String sender;
    @Column(name = "sender_phone")
    private String senderPhone;
    @Column(name = "sender_address")
    private String senderAddress;
    @Column(name = "description")
    private String description;
    @Column(name = "date_created")
    private Date dateCreated;

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
