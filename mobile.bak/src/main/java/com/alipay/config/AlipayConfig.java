package com.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {

    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public static String partner = "2088911727927812";

    // 收款支付宝账号，以2088开头由16位纯数字组成的字符串
    public static String seller_id = partner;
    // 商户的私钥
    public static String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANeyYZaXe1IcLfFK" +
            "OecHZSkUjQ5IEt079U5HFzfW6rdiGizOXePCcwoNFFUBkf/X7n7haUQh5nX+jhml" +
            "dmjl1ztV6RvOC+Ej17qAKqazWesYpbfvgrvUm5PZKiYrYzgH83hSf1u6XqPW5Tod" +
            "3mSzWgiFa30JZElyypNZh7ZY+MDpAgMBAAECgYAT2/akONo/2xxXxO/D16h2uc+q" +
            "6vPhrq2NQXpfx8fgjoW+blmyqKqS2FVw5i4dEjKBOBvLDBv97SwELHnUeqTwRFlJ" +
            "73OJA7FGFdDZzL6Bjk7Wl2pdUp5FOVaT4ScnsiVvXY8lHvX7rwTOnHLlL5i9nDP+" +
            "hkRGrwVDZ9iYJVjNSQJBAOsNTNmf0ACUGY4j5vZvhHq3hR1UeRk5B/i8k7kPrPW5" +
            "c1IDrravzCyDqur1vmpT5SzmqshTSFDm4hS8HiWOrisCQQDq6357sxGYQ3fqWZTR" +
            "3HjpF9KsIFBERPCHQabSlwzBZine3B/QuYGq3pAUNo9E9ESRYwTmaPTGFM3RJC5O" +
            "slc7AkAkg8Nth2Mjw94Yc8FchUR6X+og/U92uKJhZMI6HJJM9gtRMVtB7Bt2ytmR" +
            "eK2lDPVsPUVIq49vaf6zI+o3AWxTAkEA5BZ3/QSVDcBcGqaVjtlAcBjYTJUnOdGx" +
            "s7Fzlr5f/RsDhkU1gsPKeSuUuntqyWlddcetR0Uae4RRfUFi0LfpZwJBAOgNomBY" +
            "f6Hsu1hN3h3pvumbjdp5NZ/8zWylJqze5rLsU9LLulTPHUQKhqeLdIzSs1rrXoKn" +
            "+S9RWXettnK/bG0=";

    // 支付宝的公钥，无需修改该值
    public static String ali_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBg" +
            "QCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKM" +
            "iFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFX" +
            "fFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    // 调试用，创建TXT日志文件夹路径
    public static String log_path = "\\globalph_www\\log";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String input_charset = "utf-8";

    // 签名方式 不需修改
    public static String sign_type = "RSA";

}
