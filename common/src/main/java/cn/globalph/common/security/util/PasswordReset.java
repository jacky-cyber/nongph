package cn.globalph.common.security.util;

import java.io.Serializable;

/**
 * 
 * @felix.wu
 *
 */
public class PasswordReset implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private boolean passwordChangeRequired = false;
    private int passwordLength = 22;
    private boolean sendResetEmailReliableAsync = false;

    public PasswordReset() {
    }

    public PasswordReset(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public boolean isSendResetEmailReliableAsync() {
        return sendResetEmailReliableAsync;
    }

    public void setSendResetEmailReliableAsync(boolean sendResetEmailReliableAsync) {
        this.sendResetEmailReliableAsync = sendResetEmailReliableAsync;
    }
}
