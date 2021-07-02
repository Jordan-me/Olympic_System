package JUnitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class FileHelperManagerTest2 {

	@Test
	void testReadCountryFromFile() throws FileNotFoundException {
		File myCountryFile = new File("CountryDB.txt");
		Map<String, Integer> allCountriesMedals = new LinkedHashMap<String, Integer>();
		Scanner in = new Scanner(myCountryFile);
		while (in.hasNextLine()) {
			String dataLine = in.nextLine();
			String[] dataCountry = dataLine.split(";");
			for (int i = 0; i < dataCountry.length; i++) {
				allCountriesMedals.put(dataCountry[i], 0);
			}
		}
		System.out.println(allCountriesMedals.toString());
	}

}
