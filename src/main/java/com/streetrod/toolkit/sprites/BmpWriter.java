package com.streetrod.toolkit.sprites;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BmpWriter {

	public static byte[] getMsBitmap(byte[][] bitmap, int type) {
		int width = bitmap[0].length;
		int height = bitmap.length;
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		try {
			stream.write(getHeader(width, height));
			stream.write(getPalette(type));
			stream.write(getData(width, height, bitmap));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stream.toByteArray();
	}

	private static byte[] getHeader(int width, int height) {
		int headerSize = 0x36;
		int palSize = 0x40;
		
		int width2 = (int) (Math.ceil(width / 8.0) * 4);
		int dataSize = width2 * height;
		
		int totalSize = headerSize + palSize + dataSize;
		
		ByteBuffer buffer = ByteBuffer.allocate(headerSize);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		
		buffer.put((byte) 'B');
		buffer.put((byte) 'M');
		
		buffer.putInt(totalSize);
		buffer.putInt(0);
		buffer.putInt(headerSize + palSize);
		buffer.putInt(0x28);
		buffer.putInt(width);
		buffer.putInt(height);
		buffer.putShort((short) 1);
		buffer.putShort((short) 4);
		
		return buffer.array();
	}

	private static byte[] getPalette(int type) {
		byte[] palette = {
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
			(byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x00, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x00, 
			(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x00, 
			(byte) 0xFF, (byte) 0x55, (byte) 0x55, (byte) 0x00, (byte) 0xAA, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
			(byte) 0xAA, (byte) 0xAA, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xAA, (byte) 0x00, (byte) 0x00, 
			(byte) 0x00, (byte) 0x00, (byte) 0xAA, (byte) 0x00, (byte) 0x55, (byte) 0x55, (byte) 0xFF, (byte) 0x00, 
			(byte) 0xAA, (byte) 0x00, (byte) 0xAA, (byte) 0x00, (byte) 0x55, (byte) 0xFF, (byte) 0x55, (byte) 0x00, 
			(byte) 0x00, (byte) 0x55, (byte) 0xAA, (byte) 0x00, (byte) 0x55, (byte) 0xFF, (byte) 0xFF, (byte) 0x00
		};
		if (type == 1) {
			palette[2] = (byte) 0xFF; // set background to red
			
			palette[12] = (byte) 0xFF;
			palette[13] = (byte) 0x55;
			palette[14] = (byte) 0xFF;
		} else if (type == 2) {
			palette[0] = palette[1] = palette[2] = (byte) 0xFF;
		}
		return palette;
	}

	private static byte[] getData(int width, int height, byte[][] bitmap) {
		int width2 = (int) (Math.ceil(width / 8.0) * 4);
		byte[] data = new byte[width2 * height];
		
		for (int h = 0; h < height; h++) {
			int h2 = height - h - 1;
			
			for (int w = 0; w < width / 2; w++) {
				int value = ((bitmap[h][w*2] % 16) << 4) | (bitmap[h][w*2 + 1] % 16);
				data[h2 * width2 + w] = (byte) value;
			}
			if (width % 2 != 0) {
				data[h2 * width2 + width / 2] = (byte) ((bitmap[h][width - 1] % 16) << 4);
			}
		}
		
		return data;
	}
}