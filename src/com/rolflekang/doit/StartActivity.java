package com.rolflekang.doit;

import java.util.ArrayList;
import java.util.Date;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class StartActivity extends ListActivity implements OnClickListener, OnItemLongClickListener, OnItemClickListener {
	private DataHelper dHelper;
	private TodoAdapter adapter;
	private ArrayList<Todo> todoList;
	private EditText addtodofield;
	private Button addtodobtn;
	private Dialog editDialog;
	private EditText editTitleField;
	private EditText editDescField;
	private EditText editDueDate;
	private Button editSaveBtn;
	private long editId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		dHelper = new DataHelper(getApplicationContext());
		todoList = dHelper.selectAll(DataHelper.KEY_DONE+"=0");
		adapter = new TodoAdapter(this, R.layout.row, todoList);
		setListAdapter(adapter);
		addtodofield = (EditText) findViewById(R.id.addtodofield);
		addtodobtn = (Button) findViewById(R.id.addtodobtn);
		addtodobtn.setOnClickListener(this);
		ListView lv = getListView();
		lv.setOnItemLongClickListener(this);
		lv.setOnItemClickListener(this);
	}
	private void updateList() {
		todoList.clear();
		for(Todo t : dHelper.selectAll(DataHelper.KEY_DONE+"=0")){
			todoList.add(t);
		}
		adapter.notifyDataSetChanged();
		updateWidget();
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.addtodobtn){
			if(addtodofield.getText().toString() != ""){
				try{
					dHelper.insertTodo(new Todo(addtodofield.getText().toString()));
					updateList();
					addtodofield.setText("");
				} catch (IllegalArgumentException e) {
					Toast.makeText(getApplicationContext(), "You have to enter a title", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "You have to enter a title", Toast.LENGTH_SHORT).show();
			}
		} else if (v.getId() == R.id.edit_savebtn){
			Date date = parseDateString(editDueDate.getText().toString());
			dHelper.updateTodo(new Todo(editId, editTitleField.getText().toString(), date, editDescField.getText().toString()));
			updateList();
			editDialog.dismiss();
		}

	}
	public void onRowCheckboxClick(View v){
		Todo t = null;
		for (int i = 0; i < getListView().getCount(); i++) {
			if(v.getParent().equals(getListView().getChildAt(i))) t = (Todo) getListView().getItemAtPosition(i);

		}
		t.setDone(true);
		dHelper.updateTodo(t);
		updateList();
		Toast.makeText(getApplicationContext(), t.getTitle() + " has been archived", Toast.LENGTH_SHORT).show();
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg, View v, int pos, long arg3) {
		//		Todo t = todoList.get(pos);
		//		if(dHelper.deleteTodo(t.getId())) Toast.makeText(getApplicationContext(), "Deleted " + t.getTitle(), Toast.LENGTH_SHORT).show();
		//		updateList();
		showEditDialog(todoList.get(pos));
		return true;
	}
	@Override
	public void onItemClick(AdapterView<?> arg, View v, int pos, long arg3) {
		Todo t = todoList.get(pos);
		if(t.isDone()){ 
			t.setDone(false);
		} else {
			t.setDone(true);
		}
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
	private void showEditDialog(Todo todo) {
		editId = todo.getId();
		editDialog = new Dialog(this);
		editDialog.setContentView(R.layout.edittododialog);
		editDialog.setTitle(todo.getTitle());
		editTitleField = (EditText) editDialog.findViewById(R.id.edit_titlefield);
		editDescField = (EditText) editDialog.findViewById(R.id.edit_descfield);
		editDueDate = (EditText) editDialog.findViewById(R.id.edit_datefield);
		editSaveBtn = (Button) editDialog.findViewById(R.id.edit_savebtn);
		editSaveBtn.setOnClickListener(this);
		editTitleField.setText(todo.getTitle());
		editDescField.setText(todo.getDescription());
		if(todo.getDueDate() != null) editDueDate.setText(todo.getDueDateString());

		editDialog.show();

	}
	@SuppressWarnings("unused")
	private void creatDummyContent(int nr) {

		dHelper.deleteAll();
		for(int i = 0; i <= nr; i++){
			dHelper.insertTodo(new Todo(i, "Test "+ i, null, "jeej"));
		}
	}
	private Date parseDateString(String string) {
		if(string.length() <= 0 || string == null) return null;
		String[] d = string.split("-");
		Date date = new Date(Integer.parseInt(d[0])-1900 , Integer.parseInt(d[1])-1, Integer.parseInt(d[2]));
		return date;
	}
	protected void updateWidget() {
		Intent i = new Intent();
		i.setAction(WidgetProvider.ACTION_WIDGET_RECEIVER);
        getApplicationContext().sendBroadcast(i);
	}
}