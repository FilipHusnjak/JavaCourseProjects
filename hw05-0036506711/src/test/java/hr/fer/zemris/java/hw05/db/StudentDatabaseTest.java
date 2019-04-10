package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;

public class StudentDatabaseTest {

	@Test
	public void testForJMBAGFilterFalse() throws IOException {
		StudentDatabase database = getData();
		assertEquals(0, database.filter(s -> false).size());
	}
	
	@Test
	public void testForJMBAGFilterTrue() throws IOException {
		StudentDatabase database = getData();
		assertEquals(63, database.filter(s -> true).size());
	}
	
	public static StudentDatabase getData() throws IOException {
		return new StudentDatabase(
				Files.readAllLines(Paths.get("./src/main/resources/database.txt"), StandardCharsets.UTF_8));
	}
	
}
