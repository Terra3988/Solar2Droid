package io.git.api;

import io.builder.core.BuildError;
import io.builder.util.NetworkUtil;
import io.solar2d.log.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class CloneCommand implements Command {
	
	private File saveDir;
	
	private String repoOwner;
	private String repoName;
	
	private String gitToken;
	
	private String copyDir = "";
	
	private ProgressMonitor progressMonitor;
	
	public void setDirectory(File dir) {
		this.saveDir = dir;
	}
	
	public void setRepo(String owner, String name) {
		this.repoOwner = owner;
		this.repoName = name;
	}
	
	public void setGitToken(String tok) {
		this.gitToken = tok;
	}
	
	public void setCopyDir(String dir) {
		this.copyDir = dir;
	}
	
	public void setProgressMonitor(ProgressMonitor m) {
		this.progressMonitor = m;
	}
	
	private int downloadsNum = 0;
	private int downloaded = 0;
	
	private List<JSONObject> dirs;
	private List<JSONObject> files;
	
	@Override
	public void call() {
		new Thread() {
			@Override
			public void run() {
				prepare();
				cloneRepo();
			}
		}.start();
	}
	
	private void cloneRepo() {
		if(progressMonitor != null)
			progressMonitor.onStart();
		
		if(!saveDir.exists())
			saveDir.mkdir();
		
		Logger.getInstance().log("note", "" + files.size());
		
		for(JSONObject jDir: dirs) {
			try {
				String path = jDir.getString("path");
				File dir = new File(saveDir, path);
				Logger.getInstance().log("note", path);
				dir.mkdir();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		int procent = 0;
		
		for(JSONObject jFile: files) {
			try {
				String path = jFile.getString("path");
				path = path.replace("plugins", "");
				String url = jFile.getString("download_url");
				File file = new File(saveDir, path);
				NetworkUtil.downloadFile(url, file);
				downloaded++;
				if(progressMonitor != null) {
					int newP = Math.round((downloaded / files.size()) * 100);
					if(newP > procent) {
						progressMonitor.onUpdate(newP);
						procent = newP;
					}
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		if(progressMonitor != null)
			progressMonitor.onFinish();
	}
	
	private void prepare() {
		dirs = new ArrayList<>();
		files = new ArrayList<>();
		try {
			prepare_dir(copyDir);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void prepare_dir(String path) throws Exception {
		JSONArray filess = getFilesList(path);
		for(int i = 0; i < filess.length(); i++) {
			JSONObject element = filess.getJSONObject(i);
		//	Logger.getInstance().log("prepare", element.getString("type"));
			if(element.getString("type") == "dir") {
				//Logger.getInstance().log("note", element.toString());
				dirs.add(element);
				prepare_dir(element.getString("path"));//path + "/" + element.getString("name"));
			} else {
				downloadsNum++;
			//	Logger.getInstance().log("note", path);
				files.add(element);
			}
		}
	}
	
	private JSONArray getFilesList(String folder) throws Exception {
		String apiUrl = String.format("https://api.github.com/repos/%s/%s/contents/%s", repoOwner, repoName, folder);
		OkHttpClient httpClient = new OkHttpClient();
		Request request = new Request.Builder()
				.addHeader("Authorization", "Bearer " + gitToken)
				.url(apiUrl)
				.build();
		try(Response response = httpClient.newCall(request).execute()) {
			String res = response.body().string();
			return new JSONArray(res);
		}
	}
}