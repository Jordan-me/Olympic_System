package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import model.Participant.sportType;

public class FileHelperManger1 {
	private static File myCountryFile = new File("CountryDB.txt");
	private static File myParticipantFile = new File("participantDB.txt");
	private static File myStadiumFile = new File("Host countries and stadiums.txt");
	private static Map<String, ArrayList<Stadium>> allStadiums = new LinkedHashMap<String, ArrayList<Stadium>>();

	public static void readCountryFromFile(Map<String, Integer> allCountriesMedals) throws FileNotFoundException {
		Scanner in = new Scanner(myCountryFile);
		while (in.hasNextLine()) {
			String dataLine = in.nextLine();
			String[] dataCountry = dataLine.split(";");
			for (int i = 0; i < dataCountry.length; i++) {
				allCountriesMedals.put(dataCountry[i], 0);
			}
		}
		in.close();
	}

	public static void readHostCountryAndStadiumFromFile(ArrayList<Stadium> allStadiums, String hostCountry)
			throws FileNotFoundException {
		Scanner in = new Scanner(myStadiumFile);
		String location;
		int capacity;
		while (in.hasNextLine()) {
			String dataLine = in.nextLine();
			String[] dataStadium = dataLine.split(";");
			String[] countriCityData = dataStadium[0].split("<");
			String country = countriCityData[0];
			if (country.equalsIgnoreCase(hostCountry)) {
				int lastRgx = countriCityData[1].indexOf(">");
				location = countriCityData[1].substring(0, lastRgx);
				String[] allStadium = dataStadium[1].split(",");
				for (int i = 0; i < allStadium.length; i++) {
					String[] splittinArray = allStadium[i].split("<");
					capacity = Integer.parseInt(splittinArray[1].substring(0, splittinArray[1].indexOf(">")));
					Stadium myStad = new Stadium(splittinArray[0], location, capacity);
					allStadiums.add(myStad);
				}
			}
		}
		in.close();
	}

	public static void readParticipantFile(ArrayList<Participant> allJudges, ArrayList<Participant> allAthletes)
			throws FileNotFoundException {
		Scanner in = new Scanner(myParticipantFile);
		String country;
		String name;
		String job;
		String theType;
		while (in.hasNextLine()) {
			String dataLine = in.nextLine();
			String[] firstData = dataLine.split(";");
			int firstPlace = firstData[0].indexOf("<");
			int secondPlace = firstData[0].indexOf(">");
			country = firstData[0].substring(0, firstPlace);
			job = firstData[0].substring(firstPlace + 1, secondPlace);
			String[] peopleData = firstData[1].split(",");
			for (int i = 0; i < peopleData.length; i++) {
				firstPlace = peopleData[i].indexOf("<");
				secondPlace = peopleData[i].indexOf(">");
				name = peopleData[i].substring(0, firstPlace);
				theType = peopleData[i].substring(firstPlace + 1, secondPlace);
				Participant myPerson = new Participant(name, country, job, theType);
				if (job.equalsIgnoreCase("JUDGE")) {
					allJudges.add(myPerson);
				} else {
					allAthletes.add(myPerson);

				}

			}

		}
		in.close();
	}

	public static Collection<? extends String> getAllCountries() {
		Map<String, Integer> allCountriesMedals = new LinkedHashMap<String, Integer>();
		ArrayList<String> allCountries = new ArrayList<String>();
		try {
			readCountryFromFile(allCountriesMedals);

			for (Entry<String, Integer> entry : allCountriesMedals.entrySet()) {
				allCountries.add(entry.getKey());
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return allCountries;
	}

	public static ArrayList<String> getHostCountry() {
		ArrayList<String> allHostCountry = new ArrayList<String>();
		Scanner in;
		try {
			in = new Scanner(myStadiumFile);
			String location;
			while (in.hasNextLine()) {
				String dataLine = in.nextLine();
				String[] dataStadium = dataLine.split(";");
				String[] countriCityData = dataStadium[0].split("<");
				String country = countriCityData[0];
				allHostCountry.add(country);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}

		return allHostCountry;
	}

	public static ArrayList<String> getAllTypes(int i) {
		ArrayList<String> types = new ArrayList<String>();
		sportType[] allTypes = sportType.values();
		for (sportType sport : allTypes) {
			types.add(sport.name());
		}
		if (i == 2) {
			types.remove(i);
		}
		return types;
	}

	public static ArrayList<String> getAllStadiumsName(String inCountry) {
		ArrayList<String> convertedStadiums = new ArrayList<String>();
		ArrayList<Stadium> allStadiumInCountry = new ArrayList<Stadium>();
		Scanner in;
		try {
			in = new Scanner(myStadiumFile);
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
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}

		allStadiumInCountry.addAll(allStadiums.get(inCountry));
		for (int i = 0; i < allStadiumInCountry.size(); i++) {
			convertedStadiums.add(allStadiumInCountry.get(i).getName());
		}

		return convertedStadiums;
	}
}
