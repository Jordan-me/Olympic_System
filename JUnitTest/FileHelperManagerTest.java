package JUnitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import model.Stadium;

class FileHelperManagerTest {

	@Test
	void testReadHostCountryAndStadiumFromFile() throws FileNotFoundException {
		Map<String, ArrayList<Stadium>> allStadiums = new LinkedHashMap<String, ArrayList<Stadium>>();
		File myStadiumFile = new File("Host countries and stadiums.txt");
		Scanner in = new Scanner(myStadiumFile);
		String location;
		int capacity;
		while (in.hasNextLine()) {
			ArrayList<Stadium> stadiumDB = new ArrayList<Stadium>();
			String dataLine = in.nextLine();
			String[] dataStadium = dataLine.split(";");
			String[] countriCityData = dataStadium[0].split("<");
			String country = countriCityData[0];
			int lastRgx = countriCityData[1].indexOf(">");
			location = countriCityData[1].substring(0, lastRgx);
			String[] allStadium = dataStadium[1].split(",");
			for (int i = 0; i < allStadium.length; i++) {
				String[] splittinArray = allStadium[i].split("<");
				capacity = Integer.parseInt(splittinArray[1].substring(0, splittinArray[1].indexOf(">")));
				Stadium myStad = new Stadium(splittinArray[0], location, capacity);
				stadiumDB.add(myStad);
			}
			allStadiums.put(country, stadiumDB);
		}
		System.out.println(allStadiums.toString());
	}

}
