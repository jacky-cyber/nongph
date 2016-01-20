package cn.globalph.common.encryption;

public interface EncryptionModule {

    public String encrypt(String plainText);

    public String decrypt(String cipherText);

}
