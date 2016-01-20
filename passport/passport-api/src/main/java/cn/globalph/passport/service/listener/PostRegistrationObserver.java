package cn.globalph.passport.service.listener;

import cn.globalph.passport.domain.Customer;

public interface PostRegistrationObserver {

    public void processRegistrationEvent(Customer customer);
}
