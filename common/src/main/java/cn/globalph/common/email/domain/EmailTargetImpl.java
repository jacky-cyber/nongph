package cn.globalph.common.email.domain;

import java.util.Arrays;

public class EmailTargetImpl implements EmailTarget {

    private static final long serialVersionUID = 1L;

    protected String[] bccAddresses;
    protected String[] ccAddresses;
    protected String emailAddress;

    /*
     * (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTarget#getBCCAddresses()
     */

    public String[] getBCCAddresses() {
        return bccAddresses;
    }

    /*
     * (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTarget#getCCAddresses()
     */

    public String[] getCCAddresses() {
        return ccAddresses;
    }

    /*
     * (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTarget#getEmailAddress()
     */

    public String getEmailAddress() {
        return emailAddress;
    }

    /*
     * (non-Javadoc)
     * @see
     * cn.globalph.common.email.domain.EmailTarget#setBCCAddresses(java.lang
     * .String[])
     */

    public void setBCCAddresses(String[] bccAddresses) {
        this.bccAddresses = bccAddresses;
    }

    /*
     * (non-Javadoc)
     * @see
     * cn.globalph.common.email.domain.EmailTarget#setCCAddresses(java.lang
     * .String[])
     */

    public void setCCAddresses(String[] ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    /*
     * (non-Javadoc)
     * @see
     * cn.globalph.common.email.domain.EmailTarget#setEmailAddress(java.lang
     * .String)
     */

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(bccAddresses);
        result = prime * result + Arrays.hashCode(ccAddresses);
        result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmailTargetImpl other = (EmailTargetImpl) obj;
        if (!Arrays.equals(bccAddresses, other.bccAddresses))
            return false;
        if (!Arrays.equals(ccAddresses, other.ccAddresses))
            return false;
        if (emailAddress == null) {
            if (other.emailAddress != null)
                return false;
        } else if (!emailAddress.equals(other.emailAddress))
            return false;
        return true;
    }

}
