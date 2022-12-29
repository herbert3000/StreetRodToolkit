package com.streetrod.toolkit.sprites;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SpriteEncoder {

	public byte[] encode(Sprite sprite, byte[][] bitmap) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		stream.write(Sprite.IDENTIFIER);
		
		// RLE encoding of 00 and FF
		
		byte lastByte = 0x7F; // just a random value
		int counter = 0;
		
		for (int h = 0; h < bitmap.length; h++) {
			for (int w = 0; w < bitmap[0].length; w++) {
				
				if (counter == 256) {
					stream.write(new byte[] {lastByte, (byte) (counter - 1)});
					lastByte = 0x7F;
					counter = 0;
				}
				
				byte b = bitmap[h][w];
				
				if (b == 0) {
					if (lastByte == (byte)0xFF) {
						stream.write(new byte[] {(byte)0xFF, (byte) (counter - 1)});
						counter = 0;
					}
					lastByte = b;
					counter++;
				} else if (b == (byte)0xFF) {
					if (lastByte == 0) {
						stream.write(new byte[] {0, (byte) (counter - 1)});
						counter = 0;
					}
					lastByte = b;
					counter++;
				} else {
					if (counter != 0) {
						stream.write(new byte[] {lastByte, (byte) (counter - 1)});
						lastByte = 0x7F;
						counter = 0;
					}
					stream.write(b);
				}
			}
		}
		
		if (counter != 0) {
			stream.write(new byte[] {lastByte, (byte) (counter - 1)});
		}
		
		return stream.toByteArray();
	}

}
