package cn.globalph.passport.service;

import cn.globalph.common.config.domain.ModuleConfiguration;
import cn.globalph.common.config.service.ModuleProvider;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.service.exception.AddressVerificationException;

import java.util.List;

/**
 * Allows for pluggable address validators.
 * 
 * @author Kelly Tisdell
 *
 */
public interface AddressVerificationProvider extends ModuleProvider {

    public List<Address> validateAddress(Address address, ModuleConfiguration config) throws AddressVerificationException;

}
