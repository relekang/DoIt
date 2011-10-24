package com.rolflekang.doit;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataHelper{

	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESC = "description";
	public static final String KEY_DUEDATE = "duedate";
	public static final String KEY_DONE = "done";

	public static final String DB_NAME = "appdata";
	public static final String DB_TABLE = "todos";
	public static final int DB_VERSION = 4;
	private static final String DB_CREATE = "CREATE TABLE IF NOT EXISTS " + DB_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_TITLE + " text not null, " + KEY_DUEDATE + " text not null, " + KEY_DESC + " text not null, " + KEY_DONE +" int not null);";

	private DbHelper dbHelper;
	private Context context;
	private SQLiteDatabase db;

	public DataHelper(Context context) {
		this.context = context;
	}
	public DataHelper open() throws SQLException {
		dbHelper = new DbHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	public void close() {
		dbHelper.close();
	}

	public long insertTodo(Todo todo) {
		Log.i("DoIt", "Inserting "+todo.getTitle()+" into database");
		if(todo.getTitle().length() == 0) throw new IllegalArgumentException("No title");
		open();
		ContentValues iValues = new ContentValues();
		iValues.put(KEY_TITLE, todo.getTitle());
		iValues.put(KEY_DUEDATE, todo.getDueDateString());
		iValues.put(KEY_DESC,todo.getDescription());
		iValues.put(KEY_DONE, todo.isDoneInt());
		long status = db.insert(DB_TABLE, null, iValues);
		close();
		return status;
	}

	public boolean updateTodo(Todo todo) {
		Log.i("DoIt", "Updating "+todo.getTitle()+" in database");
		open();
		ContentValues uValues = new ContentValues();
		uValues.put(KEY_TITLE, todo.getTitle());
		uValues.put(KEY_DUEDATE, todo.getDueDateString());
		uValues.put(KEY_DESC, todo.getDescription());
		uValues.put(KEY_DONE, todo.isDoneInt());
		boolean status = db.update(DB_TABLE, uValues, KEY_ROWID+"="+todo.getId(),null) > 0;
		close();
		return status;

	}

	public boolean deleteAll() {
		open();
		int arg = db.delete(DB_TABLE, "1", null);
		close();
		return (arg > 0);
	}

	public boolean deleteTodo(long rowId) {
		String arg = KEY_ROWID +"="+ rowId;
		Log.i("DoIt", "Deleted "+ rowId);
		open();
		boolean s  = db.delete(DB_TABLE, arg, null) > 0;
		close();
		return s;
	}

	public ArrayList<Todo> selectAll() {
		ArrayList<Todo> todoList = new ArrayList<Todo>();
		open();
		Cursor c = db.query(DB_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_DUEDATE, KEY_DESC, KEY_DONE }, null, null, null, null, KEY_TITLE +" desc");
		if (c.moveToFirst()) {
			do {
				todoList.add(new Todo(c.getLong(0), c.getString(1) ,parseDateString(c.getString(2)),c.getString(3), c.getInt(4))); 
			} while (c.moveToNext());
		}
		if (c != null && !c.isClosed()) {
			c.close();
		}
		close();
		return todoList;
	}
	public ArrayList<Todo> selectAll(String filter) {
		ArrayList<Todo> todoList = new ArrayList<Todo>();
		open();
		Cursor c = db.query(DB_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_DUEDATE, KEY_DESC, KEY_DONE }, filter, null, null, null, KEY_TITLE +" desc");
		if (c.moveToFirst()) {
			do {
				todoList.add(new Todo(c.getLong(0), c.getString(1) ,parseDateString(c.getString(2)),c.getString(3), c.getInt(4))); 
			} while (c.moveToNext());
		}
		if (c != null && !c.isClosed()) {
			c.close();
		}
		close();
		return todoList;
	}
//	public ProgressBar selectBar(int id) throws NullPointerException{
//		Cursor cursor; ProgressBar bar = null;
//		open();
//		cursor = db.query(DB_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_DUEDATE, KEY_DESC }, KEY_ROWID+"="+Integer.toString(id), null, null, null, KEY_TITLE +" desc");
//		if (cursor.moveToFirst()) {
//			Date startDate = parseDateString(cursor.getString(2));
//			Date endDate = parseDateString(cursor.getString(3));
//			startDate.setYear(startDate.getYear()-1900);
//			endDate.setYear(endDate.getYear()-1900);
//			bar = new ProgressBar(cursor.getLong(0),startDate,endDate, cursor.getString(1)); 
//		}
//		if (cursor != null && !cursor.isClosed()) {
//			cursor.close();
//		}
//		close();
//		if(bar != null) return bar;
//		else throw new NullPointerException();
//	}
//	public ProgressBar selectNextBar(int id) {
//		ProgressBar bar = selectBar(id); int i = 0;
//		ArrayList<ProgressBar> list = selectAll();
//		Log.i("ProgressBars", Long.toString(bar.getId()));
//		Log.i("ProgressBars", "Fetching next bar");
//		try{
//			for(ProgressBar ib: list){
//				if(ib.getId() == bar.getId()) i = list.indexOf(ib);
//				else Log.w("ProgressBars", "Fail");
//			}
//			if(i + 1 >= list.size()) i = -1;
//			Log.i("ProgressBars", Integer.toString(i));
//			return list.get(i+1);
//		} catch (IndexOutOfBoundsException e ){ Log.e("ProgressBars", e.toString()); return list.get(0);}
//	}

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.w("DoIt", "Creating database");
			db.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("DoIt", "Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
			onCreate(db);
		}

	}
	private Date parseDateString(String string) {
		if(string.length() <= 0 || string == null) return null;
		String[] d = string.split("-");
		Date date = new Date(Integer.parseInt(d[0])-1900, Integer.parseInt(d[1])-1, Integer.parseInt(d[2]));
		return date;
	}

}