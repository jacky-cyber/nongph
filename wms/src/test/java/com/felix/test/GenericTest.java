package com.felix.test;

import java.lang.reflect.TypeVariable;

import com.felix.exam.model.QuestionType;

public class GenericTest {

	public static void main( String[] args ) {
		QuestionTypeDao dao = new QuestionTypeDao();
		dao.showGenericType();
	}
	
	
}

class CommandDao<T>{
	public void showGenericType(){
		TypeVariable[] tvs = this.getClass().getSuperclass().getTypeParameters();
		System.out.println(tvs[0].getClass());
	}
}

class QuestionTypeDao extends CommandDao<QuestionType>{
	
}