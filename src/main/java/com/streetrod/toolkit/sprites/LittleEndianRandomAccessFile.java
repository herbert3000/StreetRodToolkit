package com.streetrod.toolkit.sprites;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LittleEndianRandomAccessFile {

	private RandomAccessFile raf;

	public LittleEndianRandomAccessFile(String name, String mode) throws FileNotFoundException {
		raf = new RandomAccessFile(name, mode);
	}

	public void seek(int pos) throws IOException {
		raf.seek(pos);
	}

	public int skipBytes(int n) throws IOException {
		return raf.skipBytes(n);
	}

	public byte readByte() throws IOException {
		return raf.readByte();
	}

	public byte[] readBytes(int n) throws IOException {
		byte[] b = new byte[n];
		raf.read(b);
		return b;
	}

	public int readUnsignedByte() throws IOException {
		return raf.readUnsignedByte();
	}

	public int readInt() throws IOException {
		byte w[] = new byte[4];
		raf.readFully(w, 0, 4);
		return
			(w[3])		  << 24 |
			(w[2] & 0xFF) << 16 |
			(w[1] & 0xFF) <<  8 |
			(w[0] & 0xFF);
	}

	public short readShort() throws IOException {
		short s = (short) (readUnsignedByte() | readUnsignedByte() << 8);
		return s;
	}

	public short readSignedShort() throws IOException {
		short s = readShort();
		if (s > 32767) {
			s -= 65536;
		}
		return s;
	}

	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	public byte[] readDirectory() throws IOException {
		byte[] directory = new byte[16];
		raf.read(directory);
		return directory;
	}

	public String readIdent() throws IOException {
		byte w[] = new byte[4];
		raf.readFully(w, 0, 4);
		return new String(w);
	}

	public String readString(int numCharacters) throws IOException {
		String s = "";
		int i;
		for (i = 0; i < numCharacters; i++) {
			char b = (char) readUnsignedByte();
			if (b == 0) break;
			s += b;
		}
		skipBytes(numCharacters - i - 1);
		return s;
	}

	public void close() throws IOException {
		raf.close();
	}
}
