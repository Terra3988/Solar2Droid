package io.builder.lib;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class Struct {
	public static byte[] pack(String format, Object... values) {
		ByteBuffer buffer = ByteBuffer.allocate(format.length() * 4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int index = 0;
		for (char c : format.toCharArray()) {
			switch (c) {
			case 'b':
				buffer.put((byte) values[index]);
				index++;
				break;
			case 'h':
				buffer.putShort((short) values[index]);
				index++;
				break;
			case 'i':
				buffer.putInt((int) values[index]);
				index++;
				break;
			case 'l':
				buffer.putLong((long) values[index]);
				index++;
				break;
			case 'f':
				buffer.putFloat((float) values[index]);
				index++;
				break;
			case 'd':
				buffer.putDouble((double) values[index]);
				index++;
				break;
			default:
				throw new IllegalArgumentException("Invalid format character: " + c);
			}
		}
		
		return buffer.array();
	}
}