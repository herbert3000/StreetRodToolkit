package com.streetrod.toolkit.stats;

import java.io.IOException;

import com.streetrod.toolkit.sprites.LittleEndianRandomAccessFile;

public class DataReader {

	public void read(String pathIn, String pathOut) throws IOException {
		
		LittleEndianRandomAccessFile raf = new LittleEndianRandomAccessFile(pathIn, "r");
		
		for (int i = 0; i < Driver.NUM_DRIVERS; i++) {
			Driver.addDriver(raf.readBytes(18));
		}
		
		for (int i = 0; i < Car.NUM_CARS; i++) {
			Car.addCar(raf.readBytes(10));
		}
		
		for (int i = 0; i < Car.NUM_CARS; i++) {
			SpriteOffset.addEntry(raf.readBytes(10));
		}
		
		for (int i = 0; i < Car.NUM_CARS; i++) {
			SpritePosition.addEntry(raf.readBytes(12));
		}
		
		System.out.println("Drivers:");
		for (Driver d : Driver.drivers) {
			System.out.println(d);
		}
		System.out.println();
		
		System.out.println("Cars:");
		for (Car c : Car.cars) {
			System.out.println(c);
		}
		System.out.println();
		
		System.out.println("Sprite Offsets:");
		for (SpriteOffset e : SpriteOffset.entries) {
			System.out.println(e);
		}
		System.out.println();
		
		System.out.println("Sprite Positions:");
		System.out.println("x_rear_tire\tx_front_tire\tx_driver\ty_driver\tx_chopped_roof\tx_air_scoop\ty_air_scoop\ty_rear_bumper\tx_front_bumper\ty_tires\tx_decal\ty_decal\t");
		for (SpritePosition e : SpritePosition.entries) {
			System.out.println(e);
		}
		System.out.println();
		
		System.out.println("click boxes garage:");
		System.out.println("rear bumper\t\t\tchopped roof\t\t\ttransmission\t\t\tengine\t\t\t\tfront bumper");
		for (int i=0; i<5; i++) System.out.print("x\ty\tw\th\t"); System.out.println();
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 20; j++) {
				System.out.print((raf.readByte() & 0xFF) + "\t");
			}
			System.out.println();
		}
		System.out.println();
		
		System.out.println("click boxes gas station:");
		System.out.println("tank cap\trear windshield\tfront windshield");
		System.out.println("x\ty\tx\ty\tx\ty");
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 6; j++) {
				System.out.print((raf.readByte() & 0xFF) + "\t");
			}
			System.out.println();
		}
		System.out.println();
		
		raf.close();
	}

	public static void main(String[] args) {
		try {
			DataReader reader = new DataReader();
			
			if (args.length == 0) {
				System.out.println(String.format("Usage: java %s HOT_DATA_FILE [OUTPUT_DIRECTORY]", reader.getClass().getSimpleName()));
				System.exit(1);
			}
			String pathIn = args[0];
			String pathOut;
			
			if (args.length >= 2) {
				pathOut = args[1];
			} else {
				pathOut = args[0] + ".out";
			}
			reader.read(pathIn, pathOut);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
