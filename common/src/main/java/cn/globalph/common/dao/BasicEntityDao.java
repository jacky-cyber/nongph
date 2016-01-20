package cn.globalph.common.dao;

public interface BasicEntityDao<T> {

    public Class<? extends T> getImplClass();
    
	public T get( Long id );
	
	public T persist( T object );
	
	public void delete( T o );
	
	public void detach( T o );
}
