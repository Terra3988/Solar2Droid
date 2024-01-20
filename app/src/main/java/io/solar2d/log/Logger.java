package io.solar2d.log;

public class Logger {
	public interface OnLogListener {
		public void onNewLog(String tag, String log);
	}
	
	private OnLogListener listener;
	
	private static Logger INSTANCE;
	
	public Logger() {
		INSTANCE = this;
	}
	
	public static Logger getInstance() {
		return INSTANCE;
	}
	
	public void setOnLogListener(OnLogListener l) {
		listener = l;
	}
	
	public void log(String tag, String log) {
		if(listener != null) {
			listener.onNewLog(tag, log);
		}
	}
}