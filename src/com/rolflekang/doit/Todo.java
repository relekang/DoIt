package com.rolflekang.doit;

import java.util.Date;

public class Todo {
	private final long id;
	private String title;
	private String description;
	private Date dueDate;
	private int priority;
	private boolean isdone;

	public Todo(String title) {
		id = 0;
		setTitle(title);
		setDescription("");
	}
	public Todo(long id, String title, Date dueDate, String description) {
		this.id = id;
		setDueDate(dueDate);
		setDescription(description);
		setTitle(title);
		setPriority(0);
		setDone(false);
	}


	public Todo(long id, String title, Date dueDate, String description,int done) {
		this.id = id;
		setDueDate(dueDate);
		setDescription(description);
		setTitle(title);
		setPriority(0);
		setDone(done);
	}

	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public void setDone(boolean isdone) {
		this.isdone = isdone;
	}
	/**
	 * Sets the todo done based on an int value
	 * @param done takes 0-false or 1-true
	 */
	private void setDone(int done) {
		if(done == 1) isdone = true;
		else isdone = false;
	}
	/**
	 * Sets the priority of current Todo
	 * @param priority has to be in the range [0,4]
	 */
	public void setPriority(int priority) {
		if(priority >= 0 && priority <= 4) this.priority = priority;
		else throw new IllegalArgumentException("Priority is not within right range");
	}

	/**
	 * Creates a string to save in the database
	 * @return a string on the format yyyy-mm-dd
	 */
	public String getDueDateString() {
		if(dueDate != null){
			return (dueDate.getYear()+1900)+"-"+(dueDate.getMonth()+1)+"-"+dueDate.getDate();
		}
		else return "";
	}
	/**
	 * Return days left to due
	 * @return
	 */
	public int getDaysToDue(){
		if(dueDate == null) return 0;
		Date now = new Date();
		long left = dueDate.getTime() - now.getTime();
		left = left/(1000*60*60*24);
		return (int)left;
		
	}
	public int getHoursToDue() {
		if(dueDate == null) return 0;
		Date now = new Date();
		long left = dueDate.getTime() - now.getTime();
		left = left/(1000*60*60);
		return (int)left;
	}
	
	public boolean hasDueDate() {
		return (dueDate != null);
	}
	public int isDoneInt() {
		if(isdone == true) return 1;
		else return 0;
	}
	/*
	 * Standard getters
	 */
	public long getId() 			{	return id;			}
	public String getTitle() 		{	return title;		}
	public String getDescription() 	{	return description; }
	public Date getDueDate()		{	return dueDate;		}
	public int getPriority()		{	return priority;	}
	public boolean isDone() 		{	return isdone;		}

	


}
