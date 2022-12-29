package com.streetrod.toolkit.sprites;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BmpReader {

	public static Sprite readSprite(String filename, int type) throws IOException {
		LittleEndianRandomAccessFile raf = new LittleEndianRandomAccessFile(filename, "r");
		
		byte[] bmp = Files.readAllBytes(Paths.get(filename));
		
		// read header
		raf.skipBytes(0x12);
		int width  = raf.readInt(); // must be a multiple of 8
		int height = raf.readInt();
		raf.skipBytes(2);
		int depth = raf.readShort();
		
		if (type == 2) {
			depth = 1;
		}
		
		Sprite sprite = new Sprite(width, height, type, bmp);
		
		// TODO: read palette
		raf.skipBytes(0x58); // for now, the bitmap must use the 16 colors from the extracted sprites
		
		byte bitmap[][] = new byte[height * depth][width / 8];
		
		for (int h = height - 1; h >= 0; h--) {
			for (int w = 0; w < width / 8; w++) {
				byte b[] = raf.readBytes(4);
				if (depth == 4) {
					bitmap[h + height * 3][w] = (byte) ((b[0] & 0x80) << 0 | (b[0] & 8) << 3 | (b[1] & 0x80) >> 2 | (b[1] & 8) << 1 | (b[2] & 0x80) >> 4 | (b[2] & 8) >> 1 | (b[3] & 0x80) >> 6 | (b[3] & 8) >> 3);
					bitmap[h + height * 2][w] = (byte) ((b[0] & 0x40) << 1 | (b[0] & 4) << 4 | (b[1] & 0x40) >> 1 | (b[1] & 4) << 2 | (b[2] & 0x40) >> 3 | (b[2] & 4) >> 0 | (b[3] & 0x40) >> 5 | (b[3] & 4) >> 2);
					bitmap[h + height]    [w] = (byte) ((b[0] & 0x20) << 2 | (b[0] & 2) << 5 | (b[1] & 0x20) >> 0 | (b[1] & 2) << 3 | (b[2] & 0x20) >> 2 | (b[2] & 2) << 1 | (b[3] & 0x20) >> 4 | (b[3] & 2) >> 1);
				}
				bitmap[h][w] = (byte) ((b[0] & 0x10) << 3 | (b[0] & 1) << 6 | (b[1] & 0x10) << 1 | (b[1] & 1) << 4 | (b[2] & 0x10) >> 1 | (b[2] & 1) << 2 | (b[3] & 0x10) >> 3 | (b[3] & 1) >> 0);
			}
		}
		raf.close();
		
		SpriteEncoder spriteEncoder = new SpriteEncoder();
		byte[] encodedBitmap = spriteEncoder.encode(sprite, bitmap);
		sprite.setBitmapEncoded(encodedBitmap);
		
		return sprite;
	}
}
