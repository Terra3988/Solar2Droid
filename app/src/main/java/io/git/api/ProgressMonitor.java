package io.git.api;

public interface ProgressMonitor {
	public void onStart();
	public void onUpdate(int completed);
	public void onFinish();
}