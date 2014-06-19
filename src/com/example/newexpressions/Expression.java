package com.example.newexpressions;

public class Expression {
	  private long id;
	  private String expression;
	
	  public long getId() {
	    return id;
	  }
	
	  public void setId(long id) {
	    this.id = id;
	  }
	
	  public String getExpression() {
	    return expression;
	  }
	
	  public void setExpression(String expression) {
	    this.expression = expression;
	  }
	
	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return expression;
	  }
}
