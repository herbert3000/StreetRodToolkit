package com.streetrod.toolkit.sprites;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpriteExtractor {

	public List<Sprite> extract(String pathIn, String pathOut) throws IOException {
		
		boolean extractSprites = true;
		if (pathOut == null) extractSprites = false;
		
		LittleEndianRandomAccessFile raf = new LittleEndianRandomAccessFile(pathIn, "r");
		
		int numSprites = raf.readShort();
		int start = 2 + numSprites * 30; // start of pixel data
		
		List<Sprite> sprites = new ArrayList<>(numSprites);
		
		for (int i = 0; i < numSprites; i++) {
			sprites.add(new Sprite(raf.readShort(), raf.readShort(), raf.readShort(), raf.readByte(), raf.readDirectory(), raf.readByte(), raf.readInt(), raf.readShort()));
		}
		
		if (extractSprites) {
			File directory = new File(pathOut);
		    if (!directory.exists()){
		        directory.mkdirs();
		    }
		}
		
		for (int i = 0; i < sprites.size(); i++) {
			
			Sprite sprite = sprites.get(i);
			//System.out.println(sprite);
			
			String fn = null;
			if (extractSprites) {
				fn = String.format("%s\\%03d_%d.bmp", pathOut, i, sprite.getType());
			}
			
			raf.seek(sprite.getOffset() + start);
			byte[] data = raf.readBytes(sprite.getSizeEncoded());
			SpriteParser.parse(sprite, data, fn);
		}
		
		raf.close();
		
		return sprites;

	}

	public static void main (String[] args) {
		
		try {
			SpriteExtractor extractor = new SpriteExtractor();
			if (args.length == 0) {
				System.out.println(String.format("Usage: java %s LIB_FILE [OUTPUT_DIRECTORY]", extractor.getClass().getSimpleName()));
				System.exit(1);
			}
			String pathIn = args[0];
			String pathOut;
			if (args.length >= 2) {
				pathOut = args[1];
			} else {
				pathOut = args[0] + ".out";
			}
			
			extractor.extract(pathIn, pathOut);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
