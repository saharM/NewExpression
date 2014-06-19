package com.example.newexpressions;

import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class TestDatabaseActivity extends ListActivity {
  private ExpressionsDataSource datasource;
  public EditText newTxt;
  FilteredArrayAdapter adapter = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    datasource = new ExpressionsDataSource(this);
    datasource.open();

    List<Expression> values = datasource.getAllExpressions();

    // use the SimpleCursorAdapter to show the
    // elements in a ListView
    //ArrayAdapter<Expression> adapter = new ArrayAdapter<Expression>(this,
    //    android.R.layout.simple_list_item_1, values);    
    adapter = new FilteredArrayAdapter (this, 0, values, datasource);
    setListAdapter(adapter);
    
    newTxt = (EditText) findViewById(R.id.text);
    newTxt.addTextChangedListener(filterTextWatcher);    
  }
  
  private TextWatcher filterTextWatcher = new TextWatcher() {
	  
	  public void afterTextChanged(Editable s) {
		  //Do your stuff		 
	  }
	   
	  public void beforeTextChanged(CharSequence s, int start, int count,
	  int after) {
	  // do your stuff
	  }
	   
	  public void onTextChanged(CharSequence s, int start, int before,
	  int count) {
	  // do your stuff		  
		  if(s!=null) {
			if (adapter != null) {
				adapter.getFilter().filter(s);
			}			  
		  }
	  }
	   
  };

  // Will be called via the onClick attribute
  // of the buttons in main.xml
  public void onClick(View view) {
    @SuppressWarnings("unchecked")
    ArrayAdapter<Expression> adapter = (ArrayAdapter<Expression>) getListAdapter();
    Expression expression = null;
    switch (view.getId()) {
    case R.id.add:
      //String[] expressions = new String[] { "Cool", "Very nice", "Hate it" };
      //int nextInt = new Random().nextInt(3);
      // save the new comment to the database
    	newTxt = (EditText) findViewById(R.id.text);
    	expression = datasource.createExpression(newTxt.getText().toString());
    	adapter.add(expression);
    	break;
    case R.id.delete:
	    if (getListAdapter().getCount() > 0) {
	    	expression = (Expression) getListAdapter().getItem(0);
	        datasource.deleteExpression(expression);
	        adapter.remove(expression);
	    }
	    break;
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  protected void onResume() {
    datasource.open();
    super.onResume();
  }

  @Override
  protected void onPause() {
    datasource.close();
    super.onPause();
  }

} 