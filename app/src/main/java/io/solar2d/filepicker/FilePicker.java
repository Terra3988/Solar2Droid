package io.solar2d.filepicker;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import io.solar2d.R;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class FilePicker extends Dialog {
	private Context mContext;
	
	public interface OnFilePickedListener {
		public void onFilePicked(File pickedFile);
	}
	public interface FileFilter {
		public boolean filter(File file);
	}
	
	private OnFilePickedListener onFilePickedListener;
	private FileFilter fileFilter;
	
	// FileManager data
	private File currentFile = new File("/sdcard");
	
	
	private LinearLayout wContainer;
	private LayoutInflater inflater;
	
	public FilePicker(Context pContext) {
		super(pContext);
		this.mContext = pContext;
		this.inflater = LayoutInflater.from(pContext);
	}
	
	public void setOnFilePickedListener(OnFilePickedListener l) {
		this.onFilePickedListener = l;
	}
	
	public void setFileFilter(FileFilter ff) {
		this.fileFilter = ff;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.file_picker);
		
		wContainer = findViewById(R.id.file_picker_container);
		refresh();
	}
	
	private void refresh() {
		wContainer.removeAllViews();
		
		File[] files = currentFile.listFiles();
		if (files == null) {
			Toast.makeText(mContext, "Нет доступа к директории", 0).show();
			currentFile = new File("/sdcard");
			refresh();
			return;
		}
		
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
			    return f1.getName().compareTo(f2.getName());
			}
		});
		
		for(File file: files) {
			if(fileFilter != null && fileFilter.filter(file)) {
				add(file);
			} else if (fileFilter == null){
				add(file);
			}
		}
	}
	
	private void add(File file) {
		View view = inflater.inflate(R.layout.file_row, wContainer, false);
		TextView wName = view.findViewById(R.id.file_row_name);
		wName.setText(file.getName());
		
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				currentFile = file;
				refresh();
				return;
			}
		});
		
		view.findViewById(R.id.file_row_select).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(onFilePickedListener != null) {
					onFilePickedListener.onFilePicked(file);
					dismiss();
				}
			}
		});
				
		wContainer.addView(view);
	}
	
	
	
	@Override
	public void onBackPressed() {
		if(currentFile.getAbsolutePath().equals(new File("/sdcard").getAbsolutePath())) {
			super.onBackPressed();
			return;
		}
		
		currentFile = currentFile.getParentFile();
		refresh();
		return;
	}
}