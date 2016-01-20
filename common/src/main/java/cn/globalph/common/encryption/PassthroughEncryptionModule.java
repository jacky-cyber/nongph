package cn.globalph.common.encryption;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The default encryption module simply passes through the decrypt and encrypt text.
 * A real implementation should adhere to PCI compliance, which requires robust key
 * management, including regular key rotation. An excellent solution would be a module
 * for interacting with the StrongKey solution. Refer to this discussion:
 * 
 * http://www.strongauth.com/forum/index.php?topic=44.0
 *
 */
public class PassthroughEncryptionModule implements EncryptionModule {
    protected static final Logger LOG = LogManager.getLogger(PassthroughEncryptionModule.class);
    
    
    public PassthroughEncryptionModule() {
    	LOG.warn("This passthrough encryption module provides NO ENCRYPTION and should NOT be used in production.");
    }

    public String decrypt(String cipherText) {
        return cipherText;
    }

    public String encrypt(String plainText) {
        return plainText;
    }
}