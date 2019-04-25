package hr.fer.zemris.java.hw06.crypto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class UtilTest {
	
	@Test
	public void testHexToByteNullArgument() {
		assertThrows(NullPointerException.class, () -> Util.hextobyte(null));
	}

	@Test
	public void testHexToByteIllegalStringLength() {
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("123"));
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("aaabb"));
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("aaac123"));
	}
	
	@Test
	public void testHexToByteIllegalChars() {
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("123g"));
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("aabb1_"));
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("AABc?,"));
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("A Bccc"));
	}
	
	@Test
	public void testHexToByteZeroLengthString() {
		assertArrayEquals(new byte[] {}, Util.hextobyte(""));
	}
	
	@Test
	public void testHexToByteNormalArgument() {
		assertArrayEquals(new byte[] {1, -82, 34}, Util.hextobyte("01aE22"));
		assertArrayEquals(new byte[] {1, 2, 3}, Util.hextobyte("010203"));
		assertArrayEquals(new byte[] {-1, 0, 3}, Util.hextobyte("FF0003"));
	}
	
	@Test
	public void testByteToHexNullArgument() {
		assertThrows(NullPointerException.class, () -> Util.byteToHex(null));
	}
	
	@Test
	public void testByteToHexEmptyArray() {
		assertEquals("", Util.byteToHex(new byte[] {}));
	}
	
	@Test
	public void testByteToHexNormalArgument() {
		assertEquals("01ae22", Util.byteToHex(new byte[] {1, -82, 34}));
		assertEquals("010203", Util.byteToHex(new byte[] {1, 2, 3}));
		assertEquals("ff0003", Util.byteToHex(new byte[] {-1, 0, 3}));
	}
	
}
