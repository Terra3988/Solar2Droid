package io.builder.core;

import java.util.ArrayList;
import java.util.List;

public class TaskExecutor {
	private List<Task> taskList;
	
	public TaskExecutor()
	{
		taskList = new ArrayList<Task>();
	}
	
	public void execute()
	{
		for( Task task : taskList ) {
			try {
				task.execute();
			} catch(BuildError error) {
				error.printStackTrace();
				break;
			}
		}
	}
	
	public void add(Task task)
	{
		taskList.add(task);
	}
	
	public void pop(Task task)
	{
		taskList.remove(task);
	}
	
	public void pop(int index)
	{
		taskList.remove(index);
	}
}