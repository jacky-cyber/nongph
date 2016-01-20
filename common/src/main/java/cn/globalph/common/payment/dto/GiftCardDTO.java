package cn.globalph.common.payment.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public class GiftCardDTO<T> {

    protected T parent;

    protected Map<String, Object> additionalFields;
    protected String giftCardNum;
    protected String giftCardMasked;

    public GiftCardDTO() {
        this.additionalFields = new HashMap<String, Object>();
    }

    public GiftCardDTO(T parent) {
        this.additionalFields = new HashMap<String, Object>();
        this.parent = parent;
    }

    public T done() {
        return parent;
    }

    public GiftCardDTO<T> additionalFields(String key, Object value) {
        additionalFields.put(key, value);
        return this;
    }

    public GiftCardDTO<T> giftCardNum(String giftCardNum) {
        this.giftCardNum = giftCardNum;
        return this;
    }

    public GiftCardDTO<T> giftCardMasked(String giftCardMasked) {
        this.giftCardMasked = giftCardMasked;
        return this;
    }
}
