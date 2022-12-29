package com.streetrod.toolkit.stats;

import java.util.ArrayList;
import java.util.List;

public class SpritePosition {

	public static List<SpritePosition> entries;
	static {
		entries = new ArrayList<SpritePosition>(Car.NUM_CARS);
	}

	/*
	 * byte offset_x_rear_tire
	 * byte offset_x_front_tire
	 * byte offset_x_driver
	 * byte offset_y_driver
	 * byte offset_x_chopped_roof
	 * byte offset_x_air_scoop
	 * byte offset_y_air_scoop
	 * byte offset_y_rear_bumper
	 * byte offset_x_front_bumper
	 * byte offset_y_front_bumper
	 * byte offset_x_decal
	 * byte offset_y_decal
	 * 
	 * note: offset x starts from left side of chassis sprite
	 * note: offset y starts from top of chassis sprite
	 * note: offset x rear bumper is always 0
	 * note: offset y chopped roof is always 0
	 */
	private int[] values;

	public SpritePosition(byte[] data) {
		values = new int[data.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = data[i] & 0xFF;
		}
	}

	public static void addEntry(byte[] data) {
		entries.add(new SpritePosition(data));
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < values.length; i++) {
			s += values[i] + "\t";
		} 
		return s;
	}
}
