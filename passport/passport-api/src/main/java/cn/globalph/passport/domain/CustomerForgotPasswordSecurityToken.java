package cn.globalph.passport.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author bpolster
 *
 */
public interface CustomerForgotPasswordSecurityToken extends Serializable {
    
    /**
     * Returns the security token.
     * @return
     */
    public String getToken();
    
    /**
     * Sets the security token.
     * @return
     */
    public void setToken(String token);

    /**
     * Date the token was created
     * @return
     */
    public Date getCreateDate();

    /**
     * Set the generation date for the token.
     * @return
     */
    public void setCreateDate(Date date);

    /**
     * Date the token was used to reset the password.
     * @return
     */
    public Date getTokenUsedDate();

    /**
     * Set the date the token was used to reset the password.
     * @return
     */
    public void setTokenUsedDate(Date date);

    /**
     * Id associated with this forgot password token.
     * @return
     */
    public Long getCustomerId();

    /**
     * Id associated with this forgot password token.
     * @return
     */
    public void setCustomerId(Long customerId);

    /**
     * Returns true if the token has already been used.
     */
    public boolean isTokenUsedFlag();

    /**
     * Sets the token used flag. 
     */
    public void setTokenUsedFlag(boolean tokenUsed);
}
