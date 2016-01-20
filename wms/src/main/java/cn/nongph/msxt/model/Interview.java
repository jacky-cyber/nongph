package com.felix.msxt.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import com.felix.nsf.Need2JSON;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Table(name="interview")
public class Interview {
	public static enum STATUS{
		PENDING, GOING, PASS, REJECT, ABSENT 
	}
	
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	@Need2JSON
	private String id;
	
	@Version
	private Integer version;
	
	@ManyToOne
	@JoinColumn(name="apply_position_id")
	@Need2JSON
	private Position applyPosition;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_on")
	@Need2JSON
	private Date createOn;
	
	@Column(name="create_by")
	@Need2JSON
	private String createBy;
	
    @Enumerated(EnumType.STRING)
	@Column(name="status")
	@Need2JSON
	private STATUS status; //等待面试，正在面试，面试完成，缺席面试'
	
	@ManyToOne
	@JoinColumn(name="interviewer_id")
	@Need2JSON
	private Candidate candidate;
	
	@Column(name="login_name")
	@Need2JSON
	private String loginName;
	
	@Column(name="login_password")
	@Need2JSON
	private String loginPassword;
	
	@OneToMany(mappedBy = "interview", cascade = CascadeType.REMOVE)
    @Need2JSON
	private List<InterviewRound> rounds;
	
	@Column(name="conversation_id")
	@Need2JSON
	private String conversationId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="plan_start_time")
    @Need2JSON
	private Date planStartTime;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Position getApplyPosition() {
		return applyPosition;
	}
	
	public void setApplyPosition(Position applyPosition) {
		this.applyPosition = applyPosition;
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
	
	public STATUS getStatus() {
		return status;
	}
	
	public void setStatus(STATUS status) {
		this.status = status;
	}
	
	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public String getLoginName() {
		return loginName;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getLoginPassword() {
		return loginPassword;
	}
	
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	
	public List<InterviewRound> getRounds() {
		return rounds;
	}

	public void setRounds(List<InterviewRound> rounds) {
		this.rounds = rounds;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		return version;
	}
	
	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }
    
    public InterviewRound getCurrentRound(){
        InterviewRound cr = null;
        for( InterviewRound ir : rounds ) {
            if(  ir.getState() == InterviewRound.STATE.WAITING
                    || ir.getState() == InterviewRound.STATE.PENDING
                    || ir.getState()== InterviewRound.STATE.GOING
                  ) {
                cr = ir;
                break;
            } 
        }
        return cr;
    }
    
   public boolean isDone(){
    	if( this.getStatus()== STATUS.PENDING
    		 || this.getStatus()== STATUS.GOING )
    		return false;
    	else
    		return true;
    }
}
