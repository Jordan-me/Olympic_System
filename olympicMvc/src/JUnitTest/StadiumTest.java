package JUnitTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StadiumTest {

	@Test
	void addition() {
		int numOfSeats = (int) (Math.random() * (100000 - 500) + 500);
		System.out.println(numOfSeats);
	}

}
