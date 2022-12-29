package com.streetrod.toolkit.stats;

import java.util.ArrayList;
import java.util.List;

public class SpriteOffset {

	public static List<SpriteOffset> entries;
	static {
		entries = new ArrayList<SpriteOffset>(Car.NUM_CARS);
	}

	private short indexCarShape; // back of car during race
	private short spriteIndexChoppedRoof;
	private short spriteIndexAirScoop;
	private short spriteIndexRearBumper;
	private short spriteIndexFrontBumper;

	public SpriteOffset(byte[] data) {
		indexCarShape          = (short) (data[0] & 0xFF | data[1] << 8);
		spriteIndexChoppedRoof = (short) (data[2] & 0xFF | data[3] << 8);
		spriteIndexAirScoop    = (short) (data[4] & 0xFF | data[5] << 8);
		spriteIndexRearBumper  = (short) (data[6] & 0xFF | data[7] << 8);
		spriteIndexFrontBumper = (short) (data[8] & 0xFF | data[9] << 8);
	}

	public static void addEntry(byte[] data) {
		entries.add(new SpriteOffset(data));
	}

	public String toString() {
		return String.format("%d, %d, %d, %d, %d",
				indexCarShape, spriteIndexChoppedRoof, spriteIndexAirScoop,
				spriteIndexRearBumper, spriteIndexFrontBumper);
	}
}
