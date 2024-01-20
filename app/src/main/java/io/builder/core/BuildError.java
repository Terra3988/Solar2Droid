package io.builder.core;

import io.solar2d.log.Logger;

public class BuildError extends RuntimeException {
	
	public BuildError(String msg) {
		super(msg);
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
		Logger.getInstance().log("ERROR", getMessage());
	}

}