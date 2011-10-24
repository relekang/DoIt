package com.rolflekang.doit;

import java.util.ArrayList;
import java.util.Date;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class DoneListActivity extends ListActivity implements OnItemLongClickListener, OnItemClickListener {
	private DataHelper dHelper;
	private TodoAdapter adapter;
	private ArrayList<Todo> todoList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dHelper = new DataHelper(getApplicationContext());
		todoList = dHelper.selectAll(DataHelper.KEY_DONE+"=1");
		adapter = new TodoAdapter(this, R.layout.row, todoList);
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setOnItemLongClickListener(this);
		lv.setOnItemClickListener(this);
	}
	private void updateList() {
		todoList.clear();
		for(Todo t : dHelper.selectAll(DataHelper.KEY_DONE+"=1")){
			todoList.add(t);
		}
		adapter.notifyDataSetChanged();
	}
	public void onRowCheckboxClick(View v){
		Todo t = null;
		for (int i = 0; i < getListView().getCount(); i++) {
			if(v.getParent().equals(getListView().getChildAt(i))) t = (Todo) getListView().getItemAtPosition(i);
			
		}
		t.setDone(false);
		dHelper.updateTodo(t);
		updateList();
		Toast.makeText(getApplicationContext(), t.getTitle() + " has been marked as not done. ", Toast.LENGTH_SHORT).show();
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg, View v, int pos, long arg3) {
		Todo t = todoList.get(pos);
		if(dHelper.deleteTodo(t.getId())) Toast.makeText(getApplicationContext(), "Deleted " + t.getTitle(), Toast.LENGTH_SHORT).show();
		updateList();
		return true;
	}
	@Override
	public void onItemClick(AdapterView<?> arg, View v, int pos, long arg3) {
//		showEditDialog(todoList.get(pos));
		Todo t = todoList.get(pos);
		t.setDone(false);
		dHelper.updateTodo(t);
		updateList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch(item.getItemId()){
		case R.id.om_settings:
			i = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(i);
			i = null;
			break;
		case R.id.om_donelist:
			i = new Intent(getApplicationContext(), DoneListActivity.class);
			startActivity(i);
			i = null;
			break;
		}
		return true;
	}
	@Override
	protected void onResume() {
		super.onResume();
		updateList();
	}
	@SuppressWarnings("unused")
	private void creatDummyContent(int nr) {

		dHelper.deleteAll();
		for(int i = 0; i <= nr; i++){
			dHelper.insertTodo(new Todo(i, "Test "+ i, null, "jeej"));
		}
	}
	
	@SuppressWarnings("unused")
	private Date parseDateString(String string) {
		if(string.length() <= 0 || string == null) return null;
		String[] d = string.split("-");
		Date date = new Date(Integer.parseInt(d[0])-1900 , Integer.parseInt(d[1])-1, Integer.parseInt(d[2]));
		return date;
	}

}