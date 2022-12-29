package com.streetrod.toolkit.stats;

import java.util.ArrayList;
import java.util.List;

public class Driver {

	public static final int NUM_DRIVERS = 22;
	public static List<Driver> drivers;
	static {
		drivers = new ArrayList<Driver>(NUM_DRIVERS);
	}

	private byte unknown0;
	private byte unknown1;
	private byte unknown2;
	private byte unknown3;
	private short spriteIndexPicture;
	private short stringOffsetName;
	private byte preferredCar;
	private byte unknown9;
	private byte unknown10;
	private byte unknown14;

	public Driver(byte[] data) {
		unknown0 = data[0];
		unknown1 = data[1];
		unknown2 = data[2];
		unknown3 = data[3];
		spriteIndexPicture = (short) (data[4] & 0xFF | data[5] << 8);
		stringOffsetName   = (short) (data[6] & 0xFF | data[7] << 8);
		preferredCar = data[8];
		unknown9  = data[9];
		unknown10 = data[10];
		unknown14 = data[14];
	}

	public static void addDriver(byte[] data) {
		drivers.add(new Driver(data));
	}

	public String toString() {
		return String.format("%d, %d, %d, %d, %d, %d, %d, %d, %d, %d",
				unknown0, unknown1, unknown2, unknown3, spriteIndexPicture, stringOffsetName,
				preferredCar, unknown9, unknown10, unknown14);
	}
}
