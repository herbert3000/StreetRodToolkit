package com.streetrod.toolkit.sprites;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class LibWriter {

	public static void writeLib(File file, List<Sprite> sprites) throws IOException {
		
		FileOutputStream out;
		out = new FileOutputStream(file);
		
		// write numSprites
		out.write((byte)(sprites.size() % 256));
		out.write((byte)(sprites.size() / 256));
		
		// update header offsets
		int offset = 0;
		for (Sprite s : sprites) {
			s.setOffset(offset);
			offset += s.getSizeEncoded();
			// write header entry
			out.write(s.getHeader());
		}
		
		// write encoded bitmaps
		for (Sprite s : sprites) {
			out.write(s.getBitmapEncoded());
		}
		
		out.write((byte)Sprite.IDENTIFIER);
		
		out.close();
	}
}
