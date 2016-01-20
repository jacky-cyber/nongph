package cn.globalph.passport.service.handler;

import cn.globalph.common.security.util.PasswordReset;
import cn.globalph.passport.domain.Customer;

public interface PasswordUpdatedHandler {

    public void passwordChanged(PasswordReset passwordReset, Customer customer, String newPassword);

}
