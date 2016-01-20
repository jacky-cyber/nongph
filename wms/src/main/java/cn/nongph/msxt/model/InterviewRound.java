package com.felix.msxt.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.felix.nsf.Need2JSON;
import java.util.Iterator;

@Entity
@Table(name="interview_round")
public class InterviewRound {
	public static enum STATE{WAITING, PENDING, GOING, PASS, REJECT, ABSENT, CANCEL}
	public static enum TYPE{EXAM, VIEW}
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	@Need2JSON
	private String id;
	
	@Version
	private Integer version;
	
	@ManyToOne
	@JoinColumn(name="interview_id")
	@Need2JSON
	private Interview interview;
	
	@OneToMany(mappedBy = "interviewRound", cascade = CascadeType.REMOVE)
	@Need2JSON
	private List<InterviewRoundExamination> interviewRoundExaminations;
	
	@OneToMany(mappedBy="interviewRound", cascade = CascadeType.REMOVE)
   @Need2JSON
	private List<InterviewRoundUser> interviewRoundUsers;
	
	@Column(name="name")
    @Need2JSON
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	@Need2JSON
	private STATE state; 
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="plan_start_time")
    @Need2JSON
	private Date planStartTime;
	
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="actual_start_time")
    @Need2JSON
	private Date actualStartTime;
    
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="actual_finish_time")
	private Date actualFinishTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_on")
	private Date createOn; 
	
	@Column(name="create_by")
	private String createBy;
    
    @Need2JSON
	@Enumerated(EnumType.STRING)
	@Column(name="type")
	private TYPE type; 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Interview getInterview() {
		return interview;
	}

	public void setInterview(Interview interview) {
		this.interview = interview;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public STATE getState() {
		return state;
	}

	public void setState(STATE state) {
		this.state = state;
	}

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Date getActualFinishTime() {
        return actualFinishTime;
    }

    public void setActualFinishTime(Date actualFinishTime) {
        this.actualFinishTime = actualFinishTime;
    }

	public Date getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

    public List<InterviewRoundUser> getInterviewRoundUsers() {
        return interviewRoundUsers;
    }

    public void setInterviewRoundUsers(List<InterviewRoundUser> interviewRoundUsers) {
        this.interviewRoundUsers = interviewRoundUsers;
    }

    public List<InterviewRoundExamination> getInterviewRoundExaminations() {
        return interviewRoundExaminations;
    }

    public void setInterviewRoundExaminations(List<InterviewRoundExamination> interviewRoundExaminations) {
        this.interviewRoundExaminations = interviewRoundExaminations;
    }

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	} 
    
    public InterviewRound getNextRound(){
        InterviewRound next = null;
        Iterator<InterviewRound> it = this.getInterview().getRounds().iterator();
        while( it.hasNext() ) {
            if( it.next()==this ) {
                if( it.hasNext() )
                    next = it.next();
                break;
            }
        }
        return next;
    }
    
   public boolean isDone(){
	    if( this.getState() == STATE.WAITING
	    		|| this.getState() == STATE.PENDING
	    		|| this.getState() == STATE.GOING )
	    	return false;
	    else
	    	return true;
    }
}
