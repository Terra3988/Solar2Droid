package io.builder.lib;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CoronaArchiver
{
	private final byte[] _MAGIC_NUMBER_HEADER = {
		(byte) 0x72, // r
		(byte) 0x61, // a
		(byte) 0x63, // c
		(byte) 0x01  // .
	};
	
	private final byte[] _MAGIC_NUMBER_END = {
		(byte) 0xff,
		(byte) 0xff,
		(byte) 0xff,
		(byte) 0xff
	};
	
	private final int _MAGIC_NUMBER_INDEX = 1;
	
	
	private RandomAccessFile stream;
	
	// Packing
	private String _input_dir;
	private File[] _input_files;
	private Map<String, Integer> metadata;
	private Map<String, Integer> indexes;
	
	public CoronaArchiver()
	{
		metadata = new HashMap<>();
		indexes = new HashMap<>();
	}
	
	public void pack(String input_dir, String output_file)
	{
		this._input_dir = input_dir;
		try {
			this.stream = new RandomAccessFile(output_file, "rw");
			this._input_files = new File(input_dir).listFiles();
			Arrays.sort(_input_files, new Comparator<File>() {
				
				@Override
				public int compare(File arg0, File arg1) {
					return arg0.getName().compareTo(arg1.getName());
				}

			});
			this.metadata.put("length", this._input_files.length);
			
			// Writing metadata
			this.stream.write(this._MAGIC_NUMBER_HEADER);
			this.stream.write(Struct.pack("i", 1));
			this.stream.write(Struct.pack("i", 0));
			this.stream.write(Struct.pack("i", this.metadata.get("length")));
			
			for (File file : this._input_files) {
				int padding_length = this._padding_length(file.getName().length(), "index");
				int offset = 488;
				this.stream.write(Struct.pack("iii", this._MAGIC_NUMBER_INDEX, offset, file.getName().length()));
				this.stream.write(file.getName().getBytes(StandardCharsets.UTF_8));
				this._write_padding(padding_length);
			}
			
			long tell = this.stream.getFilePointer();
			long data_offset_start = this.stream.getFilePointer() - 12;
			this.stream.seek(8);
			this.stream.write(Struct.pack("i", (int) data_offset_start));
			this.stream.seek(tell);
			
			for (File file : this._input_files) {
				FileInputStream fis = new FileInputStream(file);
				int length = (int) file.length();
				int padding_length = this._padding_length(length, "data");
				int nxt = length + 4 + padding_length;
				
				byte[] content = new byte[length];
				fis.read(content);
				
				this.indexes.put(file.getName(), (int) this.stream.getFilePointer() );
				
				this.stream.write(Struct.pack("iii", 2, nxt, length));
				this.stream.write(content);
				this._write_padding(padding_length);
			}
			
			this.stream.write(this._MAGIC_NUMBER_END);
			this.stream.write(Struct.pack("i", 0));
			
			this._write_finalize();
			
			this.stream.close();
			} catch (IOException exception) {
			System.out.println(exception.toString());
			//        Log.w("ARCHIVER", exception.getMessage());
		}
	}
	
	private void _write_finalize() throws IOException
	{
		this.stream.seek(16);
		for(File file : this._input_files) {
			int padding_length = this._padding_length(file.getName().length(), "index");
			
			int dtype = this.stream.readInt();
			
			this.stream.seek(this.stream.getFilePointer());
			this.stream.write(Struct 
			.pack("i", indexes.get(file.getName())));
			this.stream.seek(this.stream.getFilePointer());
			
			int length = this.stream.readInt();
			
			byte[] temp = new byte[length + padding_length];
			this.stream.read(temp);
		}
	}
	
	private void _write_padding(int padding) throws IOException
	{
		for (int i = 0; i < padding; i++) {
			this.stream.write(0);
		}
	}
	
	private int _padding_length(int length, String type)
	{
		int padding;
		
		padding = (length + (4 - length % 4)) - length;
		if (type == "data") {
			padding = padding < 4 ? padding : 0;
		}
		
		return padding;
	}
}