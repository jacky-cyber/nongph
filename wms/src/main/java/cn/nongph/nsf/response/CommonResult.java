package com.felix.nsf.response;

import com.felix.nsf.Need2JSON;

public class CommonResult {
	public static enum STATE{SUCCESS, FAILURE}
	
	public static class ErrorDesc{
		@Need2JSON
		private String code;
		@Need2JSON
		private String desc;
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	@Need2JSON
	private STATE state;
	
	@Need2JSON
	private ErrorDesc errorDesc;

	public STATE getState() {
		return state;
	}

	public void setState(STATE state) {
		this.state = state;
	}

	public ErrorDesc getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(ErrorDesc errorDesc) {
		this.errorDesc = errorDesc;
	}
	
	public static CommonResult createSuccessResult(){
		CommonResult cr = new CommonResult();
		cr.setState( STATE.SUCCESS );
		return cr;
	}
	
	public static CommonResult createFailureResult(){
		CommonResult cr = new CommonResult();
		cr.setState( STATE.FAILURE );
		return cr;
	}
	
	public static CommonResult createFailureResult(String code, String desc){
		CommonResult cr = new CommonResult();
		cr.setState( STATE.FAILURE );
		
		ErrorDesc ed = new ErrorDesc();
		ed.setCode( code );
		ed.setDesc( desc );
		
		cr.setErrorDesc( ed );
		
		return cr;
	}
}

