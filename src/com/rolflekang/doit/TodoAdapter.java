package com.rolflekang.doit;

import java.text.MessageFormat;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TodoAdapter extends ArrayAdapter<Todo> {
	
	private LayoutInflater mInflater;
	private ArrayList<Todo> todoList;
	private String dueString;
	
	public TodoAdapter(Context context, int resourceId, ArrayList<Todo> todos) {
		super(context, resourceId, todos);
		this.todoList = todos;
		this.mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return todoList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row, parent, false);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.description = (TextView) convertView.findViewById(R.id.description);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.checkBtn = (ImageView) convertView.findViewById(R.id.checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Todo todo = todoList.get(position);
		dueString = "";
		
		if (todo.getTitle().length() > 35)
			holder.title.setText(todo.getTitle().substring(0, 32) + "...");
		else
			holder.title.setText(todo.getTitle());
		if (todo.getDescription().length() > 35)
			holder.description.setText(todo.getDescription().substring(0, 32) + "...");
		else
			holder.description.setText(todo.getDescription());
		
		if(todo.hasDueDate()){
			if(todo.getDaysToDue() >= 0 && todo.getHoursToDue() > 0){
				if(todo.getDaysToDue() == 0)
					dueString = MessageFormat.format(getContext().getResources().getString(R.string.row_dueinhours), todo.getHoursToDue());
				else
					dueString = MessageFormat.format(getContext().getResources().getString(R.string.row_dueindays), todo.getDaysToDue());
				
			}
			if(todo.getDaysToDue() <= 0 && todo.getHoursToDue() < 0){
				if(todo.getDaysToDue() == 0)
					dueString = MessageFormat.format(getContext().getResources().getString(R.string.row_wasduehours), todo.getHoursToDue());
				else
					dueString = MessageFormat.format(getContext().getResources().getString(R.string.row_wasduedays), todo.getHoursToDue());
			}
		}
		holder.date.setText(dueString);
		dueString = null;
		
		if(todo.isDone()) holder.checkBtn.setImageResource(R.drawable.check);
		else holder.checkBtn.setImageResource(R.drawable.empty);

		todo = null;
  		return convertView;
 	}

  	static class ViewHolder {
		public TextView title;
		public TextView description;
 		public TextView date;
 		public ImageView checkBtn;
 	}

}
