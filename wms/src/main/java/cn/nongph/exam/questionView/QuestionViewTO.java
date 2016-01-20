package com.felix.exam.questionView;

import com.felix.nsf.Need2JSON;

public class QuestionViewTO {
	@Need2JSON
	private String questionType;
	@Need2JSON
	private String position;
	@Need2JSON
	private String name;
	@Need2JSON
	private String content;
	@Need2JSON
	private String rightAnswer;
	@Need2JSON
	private String choiceItems;
	
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRightAnswer() {
		return rightAnswer;
	}
	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer = rightAnswer;
	}
	public String getChoiceItems() {
		return choiceItems;
	}
	public void setChoiceItems(String choiceItems) {
		this.choiceItems = choiceItems;
	}
}
