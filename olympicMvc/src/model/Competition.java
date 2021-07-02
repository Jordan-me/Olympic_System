package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import model.Participant.sportType;

public class Competition<T extends Participant> {
	private ArrayList<T> allCompetitors;
	private Stadium theStadium;
	private sportType theBranch;
	private String judge;
	private String theWinners;

	public Competition(Stadium inStadium, sportType inBranch, String inJudge, ArrayList<T> allCompetitors) {
		this.theStadium = inStadium;
		theStadium.setAvailable(false);
		this.theBranch = inBranch;
		this.judge = inJudge;
		this.allCompetitors = allCompetitors;
		theWinners = null;
	}

	public Competition(Stadium inStadium, sportType inBranch, String inJudge) {
		allCompetitors = new ArrayList<T>();
		this.theStadium = inStadium;
		theStadium.setAvailable(false);
		this.theBranch = inBranch;
		this.judge = inJudge;
		theWinners = null;
	}

	// when someOne called this method seems like the competition is over.
	public Participant[] getWinners() {
		final int MAX_NUM_COPETING = 3;
		Participant[] theWinners = new Participant[MAX_NUM_COPETING];
		Collections.shuffle(allCompetitors);
		for (int i = 0; i < theWinners.length; i++) {
			theWinners[i] = (Participant) allCompetitors.get(i);
		}
		theStadium.setAvailable(true);
		return theWinners;
	}

	public String getWinnersString() {
		StringBuilder sb = new StringBuilder();
		Participant[] theWinners = getWinners();
		for (int i = 0; i < theWinners.length; i++) {
			sb.append(theWinners[i].getCountry() + " \t" + theWinners[i].getName());
			sb.append("\n");
		}
		return sb.toString();
	}

	public ArrayList<T> getAllCompetitors() {
		return allCompetitors;
	}

	public String getCompetitorsString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < allCompetitors.size(); i++) {
			sb.append(allCompetitors.get(i).getCountry() + ": " + allCompetitors.get(i).getName());
			if (allCompetitors.get(i) instanceof Team) {
				Team theTeam = (Team) allCompetitors.get(i);
				ArrayList<Participant> allDataTeam = new ArrayList<Participant>();
				allDataTeam.addAll(theTeam.getAllCompeting());
				for (int j = 0; j < allDataTeam.size(); j++) {
					sb.append("\t" + allDataTeam.get(j).getName());
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void setAllCompetitors(ArrayList<T> allCompetitors) {
		this.allCompetitors = allCompetitors;
	}

	public Stadium getTheStadium() {
		return theStadium;
	}

	public void setTheStadium(Stadium theStadium) {
		this.theStadium = theStadium;
	}

	public sportType getTheBranch() {
		return theBranch;
	}

	public void setTheBranch(sportType theBranch) {
		this.theBranch = theBranch;
	}

	public String getJudge() {
		return judge;
	}

	public void setJudge(String judge) {
		this.judge = judge;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Competition<?> other = (Competition<?>) obj;
		if (allCompetitors == null) {

			if (other.allCompetitors != null) {

				return false;
			}
		}
		if (theBranch != other.theBranch) {

			return false;
		}
		if (allCompetitors.size() != other.allCompetitors.size()) {

			return false;
		} else {
			ArrayList<String> otherCountryNames = new ArrayList<String>();
			ArrayList<String> thisCountryNames = new ArrayList<String>();
			for (int i = 0; i < allCompetitors.size(); i++) {
				String thisName = allCompetitors.get(i).getName();
				String otherName = other.allCompetitors.get(i).getName();

				if ((thisName.contains("Team") && !otherName.contains("Team")
						|| (!thisName.contains("Team") && otherName.contains("Team")))) {
					return false;
				}
				thisCountryNames.add(allCompetitors.get(i).getCountry());
				otherCountryNames.add(other.allCompetitors.get(i).getCountry());
			}
			for (int i = 0; i < thisCountryNames.size(); i++) {
				if (!otherCountryNames.contains(thisCountryNames.get(i))) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(theBranch.name() + " competition was held at" + theStadium.getName() + " Stadium and judged by "
				+ judge + "\n");
		sb.append("The participant are: ");
		for (int i = 0; i < allCompetitors.size(); i++) {
			sb.append(allCompetitors.get(i).toString() + "\n");
		}
		return sb.toString();
	}

	public Map<String, ArrayList<String>> getAllCompetitorsMapString() {
		Map<String, ArrayList<String>> allMapParticipant = new LinkedHashMap<String, ArrayList<String>>();
		ArrayList<String> namesParticipant = new ArrayList<String>();
		for (int i = 0; i < allCompetitors.size(); i++) {
			String name = allCompetitors.get(i).getName();
			String keyCountry = allCompetitors.get(i).getCountry();
			if (name.contains("Team")) {
				Team theTeam = (Team) allCompetitors.get(i);
				namesParticipant.addAll(theTeam.getAllCompetingNames());

			} else {
				namesParticipant.add(name);
			}
			allMapParticipant.put(keyCountry, namesParticipant);
		}
		return allMapParticipant;
	}

	public void setTheWinners(String theWins) {
		this.theWinners = theWins;

	}

	public String getTheWinners() {
		return theWinners;

	}

	public int getNumCompeting() {
		int numOfCompeting = allCompetitors.size();
		return numOfCompeting;
	}

}
