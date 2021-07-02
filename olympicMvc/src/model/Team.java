package model;

import java.util.ArrayList;
import java.util.Collection;

public class Team extends Participant {
	private ArrayList<Participant> allCompeting;

	public Team(String country, sportType theBranch, ArrayList<Participant> allCompeting) {
		super(country, theBranch);
		setAllCompeting(allCompeting);
	}

	public Team(String country, sportType theBranch) {
		super(country, theBranch);
		allCompeting = new ArrayList<Participant>();
	}

	public Team(String country, String theBranch, ArrayList<Participant> allCompeting) {
		super(country, theBranch);
		setAllCompeting(allCompeting);
	}

	public ArrayList<Participant> getAllCompeting() {
		return allCompeting;
	}

	public void setAllCompeting(ArrayList<Participant> allCompeting) {
		this.allCompeting = allCompeting;
	}

	public boolean add(Participant e) {
		if (e.getRole() != this.getRole()) {
			return false;
		}
		if (e.getSportBranch() != this.getSportBranch()) {
			return false;
		}
		if (!e.getCountry().equals(this.getCountry())) {
			return false;
		}
		return allCompeting.add(e);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName() + " in : " + getSportBranch().toString() + "- \n");
		for (int i = 0; i < allCompeting.size(); i++) {
			sb.append(allCompeting.get(i).getName() + "\n");
		}
		return sb.toString();
	}

	public Collection<String> getAllCompetingNames() {
		ArrayList<String> allNames = new ArrayList<String>();
		for (int i = 0; i < allCompeting.size(); i++) {
			allNames.add(allCompeting.get(i).getName());
		}
		return allNames;
	}

}
