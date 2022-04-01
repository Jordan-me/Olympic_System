package model;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import excepsions.JudgeIsNotFoundException;
import excepsions.NoAthletesInCountryException;
import excepsions.endSomeCompetitionsException;
import excepsions.failedMassegeException;
import listeners.OlympicsEventListener;
import model.Participant.sportType;

public class Olympic {
	private Vector<OlympicsEventListener> listeners;
	private LocalDate startDate;
	private LocalDate finishDate;
	private String hostCountry;
	private ArrayList<Competition> allCompetitions;
	private Map<String, Integer> allCountriesMedals = new LinkedHashMap<String, Integer>();
	private ArrayList<Stadium> allStadiums = new ArrayList<Stadium>();
	private ArrayList<Participant> allJudges = new ArrayList<Participant>();
	private ArrayList<Participant> allAthletes = new ArrayList<Participant>();

	public Olympic() throws FileNotFoundException {
		listeners = new Vector<OlympicsEventListener>();
		FileHelperManger1.readCountryFromFile(allCountriesMedals);
		;
		FileHelperManger1.readParticipantFile(allJudges, allAthletes);
		allCompetitions = new ArrayList<Competition>();
	}

	public void registerListener(OlympicsEventListener listen) {
		listeners.add(listen);
	}

	public void setStartTime(LocalDate inStratTime) {
		this.startDate = inStratTime;

	}

	public void setFinishTime(LocalDate inFinishTime) {
		this.finishDate = inFinishTime;

	}

	public void setHostCountry(String inCountry) throws FileNotFoundException {
		this.hostCountry = inCountry;
		setallStadiums();

	}

	public void setallStadiums() throws FileNotFoundException {
		FileHelperManger1.readHostCountryAndStadiumFromFile(allStadiums, hostCountry);

	}

	public void showWindowToUI(int i) {
		for (OlympicsEventListener l : listeners) {
			l.fireShowWindow(i);
		}

	}

	public void addCountryModelEvent(String text) {
		for (OlympicsEventListener l : listeners) {
			l.fireAddCountry(text);
		}

	}

	public void removeCountryModelEvent(String text) {
		for (OlympicsEventListener l : listeners) {
			l.fireRemoveCountry(text);
		}

	}

	public void addCompetition(String type, String kind, String venue, ArrayList<String> allCountries)
			throws JudgeIsNotFoundException, NoAthletesInCountryException, endSomeCompetitionsException,
			failedMassegeException {
		final int MAX_NUM_COPETING = 3;
		if (allCountries.size() < MAX_NUM_COPETING) {
			throw new failedMassegeException("Select more countries to compete (at least 3)");
		}
		ArrayList<Participant> allCompetitors = new ArrayList<Participant>();
		Competition<Participant> theEvent;
		String theJudge = null;
		sportType theType = sportType.valueOf(type);
		Stadium theStadium = null;
		for (int i = 0; i < allStadiums.size(); i++) {
			if (allStadiums.get(i).getName().equalsIgnoreCase(venue)) {
				theStadium = allStadiums.get(i);
			}
		}
		if (getJudgeRandomlly(type) != null) {
			theJudge = getJudgeRandomlly(type);
		} else {
			throw new JudgeIsNotFoundException();
		}
		for (int i = 0; i < allCountries.size(); i++) {
			if (kind.contains("Group")) {
				Team theTeam = creatTeamRandomlly(allCountries.get(i), type);
				allCompetitors.add(theTeam);
			} else {
				Participant theParticipant = getRandommlySingleParticipant(allCountries.get(i), type);
				if (theParticipant == null) {
					throw new NoAthletesInCountryException(allCountries.get(i));
				}
				allCompetitors.add(theParticipant);

			}
		}
		if (theStadium != null && theStadium.getAvailable()) {
			theEvent = new Competition<Participant>(theStadium, theType, theJudge, allCompetitors);
			checkIfExist(theEvent);
			allCompetitions.add(theEvent);
		} else {
			throw new endSomeCompetitionsException();
		}
		int indexCompetition = allCompetitions.indexOf(theEvent);

		fireAddCompetition(indexCompetition, theEvent);

	}

	public void checkIfExist(Competition<Participant> theEvent) throws failedMassegeException {
		if (allCompetitions != null) {
			for (int i = 0; i < allCompetitions.size(); i++) {
				if (allCompetitions.get(i).equals(theEvent)) {
					theEvent.getTheStadium().setAvailable(true);
					throw new failedMassegeException("You have already organized this competition");
				}
			}
		}

	}

	public void fireAddCompetition(int indexCompetition, Competition<Participant> theEvent) {
		for (OlympicsEventListener l : listeners) {
			l.fireAddCompetition(indexCompetition, theEvent);
		}

	}

	public Participant getRandommlySingleParticipant(String theCountry, String type) {
		Collections.shuffle(allAthletes);
		Participant theParticipant = null;
		for (int j = 0; j < allAthletes.size(); j++) {
			if (allAthletes.get(j).getCountry().equals(theCountry)) {
				if (allAthletes.get(j).getSportBranch().name().equals(type)
						|| allAthletes.get(j).getSportBranch().name().equalsIgnoreCase("BOTH")) {
					theParticipant = allAthletes.get(j);
					return theParticipant;
				}
			}

		}
		return theParticipant;

	}

	public Team creatTeamRandomlly(String country, String sportType) throws NoAthletesInCountryException {
		final int MAX_AT_TEAM = 4;
		ArrayList<Participant> allAthletCountry = getAllAthletFromCountry(country);
		Collections.shuffle(allAthletCountry);
		ArrayList<Participant> allTeam = new ArrayList<Participant>();
		if (allAthletCountry.isEmpty()) {
			throw new NoAthletesInCountryException(country);
		}
		for (int i = 0; i < allAthletCountry.size(); i++) {
			if (allTeam.size() <= MAX_AT_TEAM) {
				if ((allAthletCountry.get(i).getSportBranch().toString().equals(sportType))
						|| (allAthletCountry.get(i).getSportBranch().toString().equals("BOTH"))) {
					allTeam.add(allAthletCountry.get(i));

				}
			} else {
				break;
			}
		}
		Team theTeam = new Team(country, sportType, allTeam);
		return theTeam;
	}

	public ArrayList<Participant> getAllAthletFromCountry(String country) {
		ArrayList<Participant> allAthletCountry = new ArrayList<Participant>();
		for (int i = 0; i < allAthletes.size(); i++) {
			if (allAthletes.get(i).getCountry().equalsIgnoreCase(country)) {
				allAthletCountry.add(allAthletes.get(i));
			}
		}
		return allAthletCountry;
	}

	public String getJudgeRandomlly(String SportType) {
		Collections.shuffle(allJudges);
		for (int i = 0; i < allJudges.size(); i++) {
			if (allJudges.get(i).getSportBranch().name().equalsIgnoreCase(SportType)
					|| allJudges.get(i).getSportBranch().name().equalsIgnoreCase("BOTH")) {
				return allJudges.get(i).getName();
			}
		}
		return null;
	}

	public void endCompetition(int index, Competition<Participant> theSelected) {
		theSelected.getTheStadium().setAvailable(true);
		Participant[] theWinners = theSelected.getWinners();
		for (int i = 0; i < theWinners.length; i++) {
			String theWinCountry = theWinners[i].getCountry();
			Integer value = allCountriesMedals.get(theWinCountry);
			value++;
			allCountriesMedals.put(theWinCountry, value);
		}
		String theWins = theSelected.getWinnersString();
		for (OlympicsEventListener l : listeners) {
			l.fireEndCompetition(index, allCountriesMedals, theWins, theSelected);
		}

	}

	public void getWinnersOlympic() throws endSomeCompetitionsException {
		final int MAX_WINNERS = 3;
		int counter = 0;
		for (int i = 0; i < allCompetitions.size(); i++) {
			if (allCompetitions.get(i).getTheWinners() == null) {
				throw new endSomeCompetitionsException();
			}
		}
		LinkedHashMap<String, Integer> sortedCountriesMedal = new LinkedHashMap<>();
		sortedCountriesMedal.putAll(sortAllCountriesMedals());
		String[] winningCountries = new String[MAX_WINNERS];
		for (Entry<String, Integer> entry : sortedCountriesMedal.entrySet()) {
			if (counter == MAX_WINNERS) {
				break;
			} else {
				winningCountries[counter] = entry.getKey();
				counter++;
			}

		}
		for (OlympicsEventListener l : listeners) {
			l.fireEndOlympic(winningCountries);
		}

	}

	public LinkedHashMap<String, Integer> sortAllCountriesMedals() {
		Set<Entry<String, Integer>> unsortedEntrySet = allCountriesMedals.entrySet();

		ArrayList<Entry<String, Integer>> enrtyList = new ArrayList<>(unsortedEntrySet);

		Comparator<Entry<String, Integer>> valueComparator = (entry1, entry2) -> entry1.getValue()
				.compareTo(entry2.getValue());
		Comparator<Entry<String, Integer>> reverseOrderValueComparator = Collections.reverseOrder(valueComparator);

		Collections.sort(enrtyList, reverseOrderValueComparator);

		LinkedHashMap<String, Integer> sortedCountriesMedal = new LinkedHashMap<>();

		for (Entry<String, Integer> entry : enrtyList) {
			sortedCountriesMedal.put(entry.getKey(), entry.getValue());
		}
		return sortedCountriesMedal;
	}

}
