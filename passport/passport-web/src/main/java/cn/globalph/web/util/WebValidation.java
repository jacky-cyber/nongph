package cn.globalph.web.util;

public class WebValidation {
	private static String phoneEL = "^(((13[0-9])|(15([0-3]|[5-9]))|(17([0-3]|[5-9]))|(18([0-3]|[5-9])))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
	private static String emailEL ="^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"; 
	private static String validationCodeEl = "^(\\d{6})$";
	
	//手机号码验证
	public static boolean isPhone(String s){
		if(s == null || s.equals("")){
			return false;
		}
		return s.matches(phoneEL);
	}
	
	//手机验证码验证
	public static boolean isValidationCode(String s){
		if(s == null || s.equals("")){
			return false;
		}
		return s.matches(validationCodeEl);
	}
	
	//邮箱验证
	public static boolean isEmail(String s){
		if(s == null || s.equals("")){
			return false;
		}
		return s.matches(emailEL);
	}
	
	public static void main(String[] args){
		System.out.println(isPhone("1111"));
		System.out.println(isPhone("18901547402"));
		System.out.println(isEmail("1111"));
		System.out.println(isEmail("1111@qq.com"));
		System.out.println(isValidationCode("111111"));
		System.out.println(isValidationCode("11d111"));
	}
}
