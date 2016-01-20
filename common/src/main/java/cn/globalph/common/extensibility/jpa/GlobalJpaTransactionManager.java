package cn.globalph.common.extensibility.jpa;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * 在JpaTransactionManager基础之上支持事件通知。
 * 为数据库读写分离提供保障。
 * @author felix.wu
 *
 */
public class GlobalJpaTransactionManager extends JpaTransactionManager implements ApplicationEventPublisherAware{
	private static final long serialVersionUID = 1L;
	
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		if( !definition.isReadOnly() )
			applicationEventPublisher.publishEvent( new BeforeBeginWriteTransactionApplicationEvent(transaction) );
		super.doBegin(transaction, definition);
	}
}
