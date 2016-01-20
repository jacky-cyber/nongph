package cn.globalph.common.extensibility.jpa;

import org.springframework.context.ApplicationEvent;

public class BeforeBeginWriteTransactionApplicationEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	public BeforeBeginWriteTransactionApplicationEvent(Object transaction) {
		super(transaction);
	}

}
