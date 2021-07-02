package JUnitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import model.Participant;

class FileHelperManagerTest3 {

	@Test
	void testReadParticipantFile() throws IOException {
		File myParticipantFile = new File("participantDB.txt");
		Scanner in = new Scanner(myParticipantFile);
		/*
		 * FileReader fr =new FileReader(myParticipantFile); BufferedReader BR = new
		 * BufferedReader(fr);
		 */

		System.out.println(in.hasNextLine());
		ArrayList<Participant> allJudges = new ArrayList<Participant>();
		ArrayList<Participant> allAthletes = new ArrayList<Participant>();
		String country;
		String name;
		String job;
		String theType;
		while (in.hasNextLine()) {
			String dataLine = in.nextLine();
			System.out.println(dataLine);
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
		System.out.println(allAthletes.toString());
		System.out.println(allJudges.toString());

		in.close();
	}

}
