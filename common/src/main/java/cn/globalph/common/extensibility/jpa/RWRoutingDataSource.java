package cn.globalph.common.extensibility.jpa;

import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import cn.globalph.common.web.WebRequestContext;

public class RWRoutingDataSource extends AbstractRoutingDataSource implements ApplicationListener<BeforeBeginWriteTransactionApplicationEvent>{
	private static final String MASTER_DB_KEY = "globalphDSMaster";
	private static final String SLAVE_DB_KEY = "globalphDSSlave";
	private static final String IS_EXIST_WRITE_IN_REQUEST_KEY = "IS_EXIST_WRITE_IN_REQUEST";
	
	@Override
	protected Object determineCurrentLookupKey() { 
		Object isExistWriteTransaction = WebRequestContext.getWebRequestContext().getAdditionalProperties().get(IS_EXIST_WRITE_IN_REQUEST_KEY);
		
		if( isExistWriteTransaction!=null && (Boolean)isExistWriteTransaction )
			return MASTER_DB_KEY;
		else
			return SLAVE_DB_KEY;
	}

	@Override
	public void onApplicationEvent( BeforeBeginWriteTransactionApplicationEvent event) {
		WebRequestContext.getWebRequestContext().getAdditionalProperties().put(IS_EXIST_WRITE_IN_REQUEST_KEY, true );
		
	}
}
