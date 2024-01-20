package io.builder.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBuilder {
	private List<String> args;
	
	public CommandBuilder() {
		args = new ArrayList<String>();
	}
	
	public void addArg(String arg) {
		args.add(arg);
	}
	
	public void addArgs(String... arg) {
		args.addAll(Arrays.asList(arg));
	}
	
	public String build() {
		String comm = "";
		for(String a : args) {
			comm += a;
			comm += " ";
		}
		
		return comm;
	}
}