package com.felix.msxt.position;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import javax.inject.Inject;
import javax.inject.Named;

import com.felix.msxt.model.Position;
import com.felix.nsf.Pagination;

@RequestScoped
@Named
public class PositionAction {
	
	@Inject
	private PositionService positionService;
	
	private int start;
	
	private int limit;
	
	private String id;
	
	private String name;
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Pagination search(){
		Pagination page = new Pagination();
		page.setStart( start );
		page.setLimit( limit );
		if( name==null || name.isEmpty() ) {
			name = "%";
		} else {
			name = "%" + name + "%";
		}
		positionService.search( page, name );
		return page;
	}
	
	public List<Position> getAll(){
		return positionService.getAll();
	}
	
	public Position get(){
		return positionService.get(id);
	}
	/*
	
	private  selectedPosition;
	
    @Begin
    public void selectPosition(final String id) {
    	conversation.setTimeout(600000); //10 * 60 * 1000 (10 minutes)
    	selectedPosition = em.find(Position.class, id);
    }
    
    public void deletePosition(final String id) {
    	Position p = em.find( Position.class, id);
    	Position previousP = getPreviousPosition(p);
    	
    	if( previousP==null ) {
    		CriteriaBuilder cb = em.getCriteriaBuilder();
    		CriteriaQuery<Examination> cq = cb.createQuery( Examination.class );
    		Root<Examination> rp = cq.from( Examination.class );
    		cq.select( rp ).where( cb.equal( rp.get( Examination_.position), p ) );
    		
    		List<Examination> ees = em.createQuery( cq ).getResultList();
    		if( ees.size()>0 ) {
    			messages.error( new DefaultBundleKey("msxt_position_delete_failure2") )
    			        .params( p.getName(), ees.get(0).getName() );
    		} else {
	    		em.remove( p );
	    		messages.info( new DefaultBundleKey("msxt_position_delete_success") )
		        		.params( p.getName() );
    		}
    	} else {
    		messages.error( new DefaultBundleKey("msxt_position_delete_failure1") )
                    .params( p.getName(), previousP.getName() );
    	}
    	
    }
    
    @Produces
    @RequestScoped
    @Named
    public Position getSelectedPosition() {
        return selectedPosition;
    }
    
    private Position getPreviousPosition( Position p ) {
   	 	CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Position> cquery = builder.createQuery(Position.class);
        Root<Position> rp = cquery.from(Position.class);

        cquery.select( rp ).where( builder.equal( rp.get(Position_.nextPosition), p) );
        List<Position> results = em.createQuery(cquery).getResultList();
	   	if( results!=null && results.size()>0 )
	   		return results.get(0);
	   	else
	   		return null;
   }
   */
}
