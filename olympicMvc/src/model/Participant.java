package model;

import java.util.ArrayList;

public class Participant {
	public enum Role {
		ATHLET, JUDGE
	}

	public enum sportType {
		RUNNING, HIGHJUMP, BOTH
	}

	private Role role;
	private sportType sportBranch;
	private String name;
	private String country;

	public Participant(String name, String country, String inRole, String inBranch) {
		this.name = name;
		this.country = country;
		setRole(inRole);
		setSportBranch(inBranch);
	}

	public Participant(String name, String country, Role theRole, sportType theBranch) {
		this(name, country, theRole.toString(), theBranch.toString());

	}

	public Participant(String country, sportType theBranch) {
		this(country, theBranch.toString());

	}

	public Participant(String country, String theBranch) {
		this(country + " national Team", country, (Role.ATHLET).toString(), theBranch);
		setName(country + " national Team");

	}

	public Role getRole() {
		return role;
	}

	public void setRole(String str) {
		if (str.equalsIgnoreCase("ATHLET")) {
			this.role = Role.ATHLET;
		} else {
			this.role = Role.JUDGE;
		}
	}

	public void setSportBranch(String str) {
		if (str.equals("RUNNING")) {
			this.sportBranch = sportType.RUNNING;
		} else if (str.equals("HIGHJUMP")) {
			this.sportBranch = sportType.HIGHJUMP;
		} else {
			this.sportBranch = sportType.BOTH;
		}
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public sportType getSportBranch() {
		return sportBranch;
	}

	public void setSportBranch(sportType sportBranch) {
		this.sportBranch = sportBranch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public ArrayList<String> getAllRoles() {
		ArrayList<String> allJobs = new ArrayList<String>();
		Role[] all = Role.values();
		for (Role job : all) {
			allJobs.add(job.name());
		}
		return allJobs;
	}

	public ArrayList<String> getAllSportType() {
		ArrayList<String> allSportType = new ArrayList<String>();
		sportType[] all = sportType.values();
		for (sportType sport : all) {
			allSportType.add(sport.name());
		}
		return allSportType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Participant other = (Participant) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (role != other.role)
			return false;
		if (sportBranch != other.sportBranch)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String str = sportBranch + ": " + role + " Name: " + name + " From: " + country;
		return str;
	}

}
