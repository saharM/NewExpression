package com.example.newexpressions;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ExpressionsDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_COMMENT };

  public ExpressionsDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Expression createExpression(String expression) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_COMMENT, expression);
    long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Expression newExpression = cursorToExpression(cursor);
    cursor.close();
    return newExpression;
  }

  public void deleteExpression(Expression expression) {
    long id = expression.getId();
    System.out.println("Expression deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<Expression> getAllExpressions() {
    List<Expression> expressions = new ArrayList<Expression>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Expression expression = cursorToExpression(cursor);
      expressions.add(expression);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return expressions;
  }
  
  public List<Expression> getLikeExpressions(String exp) {
	    List<Expression> expressions = new ArrayList<Expression>();

	    String whereClause = "expression LIKE ?";
	    String[] whereArgs = new String[] {
	    		"%"+exp+"%"
	    };
	    Cursor cursor = database.query("Expression", null, whereClause, whereArgs, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Expression expression = cursorToExpression(cursor);
	      expressions.add(expression);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return expressions;
	  }

  private Expression cursorToExpression(Cursor cursor) {
    Expression expression = new Expression();
    expression.setId(cursor.getLong(0));
    expression.setExpression(cursor.getString(1));
    return expression;
  }
} 