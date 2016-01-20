package cn.globalph.b2c.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ErrorWrap{

    /*
     * This is in case the client prefers to return a 200 with with this error, but 
     * wants to represent the error code, say a 500, here.
     */
    @XmlElement
    protected Integer httpStatusCode;

    @XmlElementWrapper(name = "messages")
    @XmlElement(name = "message")
    protected List<ErrorMessageWrap> messages;

    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public List<ErrorMessageWrap> getMessages() {
        if (this.messages == null) {
            this.messages = new ArrayList<ErrorMessageWrap>();
        }
        return this.messages;
    }

    public void setMessages(List<ErrorMessageWrap> messages) {
        this.messages = messages;
    }

}
