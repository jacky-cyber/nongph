package com.felix.exam.question.type;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.exam.model.QuestionType;

@RequestScoped
@Named
public class QuestionTypeAction  {
	
	@Inject
	private QuestionTypeService questionTypeService;
	
	public List<QuestionType> search(){
		return questionTypeService.search();
	}
}
