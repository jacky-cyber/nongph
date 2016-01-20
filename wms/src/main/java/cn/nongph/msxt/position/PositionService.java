package com.felix.msxt.position;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.exam.model.Question;
import com.felix.msxt.model.Position;
import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;
import com.felix.nsf.Pagination;

@ApplicationScoped
@Named
public class PositionService extends CommonService<Position>{
	
	@Inject
	PositionDao positionDao;
	
    public void search( Pagination p, String name ){
    	positionDao.doSearch( p, name );
    }
    
    public List<Position> getAll(){
    	return positionDao.getAll();
    }

	@Override
	public CommonDao<Position> getDao() {
		return positionDao;
	}
	
	public List<Position> getQuestionPosition( Question q) {
		return positionDao.getQuestionPosition(q);
	}
    
    /*
    private final Position newPosition = new Position();
    
    private String nextPostionId;
    
    public void create() {
        if (verifyPositonNameIsAvailable()) {
        	if( nextPostionId != null ) {
        		Position nextPosition = em.find( Position.class, nextPostionId);        	
        		newPosition.setNextPosition( nextPosition );
        	}
        	
            em.persist( newPosition );

            messages.info(new DefaultBundleKey("registration_registered"))
                    .defaults("You have been successfully create as the position {0}!")
                    .params(newPosition.getName());
        } 
    }


    @Produces
    @Named
    public Position getNewPosition() {
        return newPosition;
    }

    public UIInput getPositionNameInput() {
        return positionNameInput;
    }

    public void setPositionNameInput(final UIInput positionNameInput) {
        this.positionNameInput = positionNameInput;
    }

    private boolean verifyPositonNameIsAvailable() {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	
    	CriteriaQuery<Position> cq = cb.createQuery(Position.class);
    	Root<Position> fr = cq.from(Position.class);
    	cq.select( fr ).where( cb.equal(fr.get(Position_.name), newPosition.getName()) );
    	
    	TypedQuery<Position> q = em.createQuery( cq );
    	List<Position> existing = q.getResultList();
    	
        if ( existing != null && existing.size()>0 ) {
            messages.warn( new BundleKey("messages", "account_usernameTaken") )
                    .defaults( "The position name '{0}' is already taken. Please choose another position name.")
                    .params( newPosition.getName() )
                    .targets( positionNameInput.getClientId() );
            return false;
        }

        return true;
    }


	public List<Position> getExistingPositions() {
		@SuppressWarnings("unchecked")
		List<Position> existing = em.createQuery("select p from Position p").getResultList();
		return existing;
	}


	public String getNextPostionId() {
		return nextPostionId;
	}


	public void setNextPostionId(String nextPostionId) {
		this.nextPostionId = nextPostionId;
	}
	*/
}
