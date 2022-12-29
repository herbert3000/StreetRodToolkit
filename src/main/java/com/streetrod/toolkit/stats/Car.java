package com.streetrod.toolkit.stats;

import java.util.ArrayList;
import java.util.List;

public class Car {

	public static final int NUM_CARS = 26;
	public static List<Car> cars;
	static {
		cars = new ArrayList<Car>(NUM_CARS);
	}

	private short price;
	private byte unknown2;
	private byte next;
	private byte transmission;
	private byte tires;
	private byte brand;
	private byte carb;
	private byte manifold;
	private byte engine;
	private short stringOffsetDescription;
	private short spriteIndexChassis;
	
	private byte[] val;

	public Car(byte[] data) {
		price = (short) (data[0] & 0xFF | data[1] << 8);
		unknown2 = data[2];
		next = data[3];
		
		transmission = (byte) ((data[4] & 0B11000000) >> 6);
		tires =        (byte) ((data[4] & 0B00110000) >> 4);
		brand =        (byte) ((data[4] & 0B00000011));
		carb =         (byte) ((data[5] & 0B11000000) >> 6);
		manifold =     (byte) ((data[5] & 0B00111000) >> 3);
		engine =       (byte) ((data[5] & 0B00000011));
		
		stringOffsetDescription = (short) (data[6] & 0xFF | data[7] << 8);
		spriteIndexChassis = (short) (data[8] & 0xFF | data[9] << 8);
		
		val = new byte[8];
		val[0] = (byte) ((data[4] & 0B11000000) >> 6);
		val[1] = (byte) ((data[4] & 0B00110000) >> 4);
		val[2] = (byte) ((data[4] & 0B00001100) >> 2);
		val[3] = (byte) ((data[4] & 0B00000011));
		val[4] = (byte) ((data[5] & 0B11000000) >> 6);
		val[5] = (byte) ((data[5] & 0B00110000) >> 4);
		val[6] = (byte) ((data[5] & 0B00001100) >> 2);
		val[7] = (byte) ((data[5] & 0B00000011));
	}

	public static void addCar(byte[] data) {
		cars.add(new Car(data));
	}

	public String getVals() {
		return String.format("%d, %d, %d, %d, %d, %d, %d, %d",
				val[0], val[1], val[2], val[3], val[4], val[5], val[6], val[7]);
	}

	public String toString() {
		return String.format("%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d",
				price, unknown2, next, transmission, tires, brand, carb,
				manifold, engine, stringOffsetDescription, spriteIndexChassis);
	}
}
