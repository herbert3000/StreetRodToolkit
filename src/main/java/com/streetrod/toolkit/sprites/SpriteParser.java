package com.streetrod.toolkit.sprites;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SpriteParser {

	// hard-coded in SR.EXE / SRSE.EXE @ 0x3CA23
	private static final int[] COUNT = new int[]{ 1, 1, 2, 3, 4, 2, 3, 5, 6, 6, 4, 7, 7, 5, 8, 8 };
	private static final byte[] VALUE = new byte[]{ 0x00, (byte)0xFF, 0x00, 0x00, 0x00, (byte)0xFF, (byte)0xFF, 0x00, (byte)0xFF, 0x00, (byte)0xFF, (byte)0xFF, 0x00, (byte)0xFF, (byte)0xFF, 0x00 };

	public static void parse(Sprite sprite, byte[] data, String filename) {
		int p = 0;
		
		int magicWord = data[p++] & 0xFF;
		if (magicWord != Sprite.IDENTIFIER) {
			System.out.println("unknown compression");
			return;
		}
		
		int width = sprite.getWidth() / 8;
		int height = sprite.getHeight();
		
		int depth = 4;
		
		if (sprite.getType() == 2) {
			depth = 1;
		}
		
		byte[][] bitmap = new byte[height * depth][width];
		
		int pos = 0;
		
		while (pos < width * height * depth) {
			
			byte b = data[p++];
			
			boolean found = false;
			byte[] directory = sprite.getDirectory();
			
			for (int i = 0; i < directory.length; i++) {
				if (directory[i] == 0) {
					break;
				}
				if (b == directory[i]) {
					for (int c = 0; c < COUNT[i]; c++) {
						bitmap[pos / width][pos % width] = VALUE[i];
						pos++;
					}
					found = true;
					break;
				}
			}
			
			if (found) continue;
			
			if (b == 0) {
				int c = data[p++] & 0xFF;
				pos += c + 1;
			} else if (b == (byte)0xFF) {
				int c = data[p++] & 0xFF;
				for (int i = 0; i <= c; i++) {
					bitmap[pos / width][pos % width] = (byte) 0xFF;
					pos++;
				}
			} else {
				bitmap[pos / width][pos % width] = b;
				pos++;
			}
		}
		
		// construct 256-colors image
		int width2 = sprite.getWidth();
		byte[][] bitmap2 = new byte[height][width2];
		
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				for (int i = 0; i < 8; i++) {
					// byte to binary conversion
					// byte 255 in bitmap becomes byte 1,1,1,1,1,1,1,1 in bitmap2
					bitmap2[h][w * 8 + (7 - i)] = (byte) ((bitmap[h][w] & 1 << i) >> i);

					if (depth == 4) {
						bitmap2[h][w * 8 + (7 - i)] |= (byte) (
								(bitmap[h + height][w] & 1 << i) >> i << 1 |	  // 255 -> 2,2,2,2,2,2,2,2
								(bitmap[h + height * 2][w] & 1 << i) >> i << 2 |  // 255 -> 4,4,4,4,4,4,4,4
								(bitmap[h + height * 3][w] & 1 << i) >> i << 3) ; // 255 -> 8,8,8,8,8,8,8,8
					}
				}
			}
		}
		
		// create bitmap
		// write bmp header
		// write bmp data
		
		byte[] bmp = BmpWriter.getMsBitmap(bitmap2, sprite.getType());
		
		sprite.setBitmapEncoded(data);
		sprite.setBitmapDecoded(bmp);
		
		if (filename != null) {
			FileOutputStream out;
			try {
				out = new FileOutputStream(filename);
				out.write(bmp);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
