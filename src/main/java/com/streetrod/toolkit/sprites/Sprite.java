package com.streetrod.toolkit.sprites;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Sprite {

	public static final int IDENTIFIER = 0xBC;

	private int width;
	private int height;
	private int sizeDecoded;
	private int type; // 0: no transparency, 1: transparency, 2: mask (black & white)
	private byte[] directory;
	private int offset;
	private int sizeEncoded;
	private byte[] bitmapDecoded;
	private byte[] bitmapEncoded;

	public Sprite(short width, short height, short sizeDecoded, byte type, byte[] directory, byte alwaysZero, int offset, short sizeEncoded) {
		this.width = width;
		this.height = height;
		this.sizeDecoded = sizeDecoded;
		this.type = type;
		this.directory = directory;
		this.offset = offset;
		this.sizeEncoded = sizeEncoded;
	}

	public Sprite(int width, int height, int type, byte[] bitmapDecoded) {
		this.width = (short) width;
		this.height = (short) height;
		this.sizeDecoded = (width * height / 2);
		this.bitmapDecoded = bitmapDecoded;
		this.type = type;
		
		this.sizeDecoded = height * width / 2;
		
		this.directory = new byte[16];
	}

	@Override
	public String toString() {
		return String.format(String.format("w=%d, h=%d, o=%d, t=%d, s=%d, d=%d", width, height, offset, type, sizeEncoded, directory.length));
	}

	public int getOffset() {
		return offset;
	}

	public int getSizeEncoded() {
		return sizeEncoded;
	}

	public byte[] getDirectory() {
		return directory;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getType() {
		return type;
	}

	public void setBitmapDecoded(byte[] bitmapDecoded) {
		this.bitmapDecoded = bitmapDecoded;
	}

	public byte[] getBitmapDecoded() {
		return bitmapDecoded;
	}

	public void setSizeEncoded(int sizeEncoded) {
		this.sizeEncoded = sizeEncoded;
	}

	public void setBitmapEncoded(byte[] data) {
		this.bitmapEncoded = data;
		this.sizeEncoded = data.length;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public byte[] getBitmapEncoded() {
		return bitmapEncoded;
	}

	public byte[] getHeader() {
		ByteBuffer buffer = ByteBuffer.allocate(30);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		
		buffer.putShort((short) width);
		buffer.putShort((short) height);
		buffer.putShort((short) sizeDecoded);
		buffer.put((byte) type);
		buffer.put(directory);
		buffer.put((byte) 0); // always zero
		buffer.putInt(offset);
		buffer.putShort((short) sizeEncoded);
		
		return buffer.array();
	}
}
