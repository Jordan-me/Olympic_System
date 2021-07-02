package model;

public class Stadium {
	private String name;
	private String theLocate;
	private boolean available;
	private final int MINSEATS = 500;
	private final int MAXSEATS = 100000;
	private int numOfSeats;// random

	public Stadium(String inName, String inLocation, int numOfSeats) {
		this.numOfSeats = numOfSeats;
		this.name = inName;
		this.theLocate = inLocation;
		setAvailable(true);
	}

	public Stadium(String inName, String inLocation) {
		this.name = inName;
		this.theLocate = inLocation;
		setNumOfSeats();
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return theLocate;

	}

	public int getNumOfSeats() {
		return numOfSeats;
	}

	public boolean getAvailable() {
		return available;
	}

	public void setAvailable(boolean value) {
		available = value;
	}

	public void setNumOfSeats() {
		numOfSeats = (int) (Math.random() * (MAXSEATS - MINSEATS) + MINSEATS);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Stadium: " + name + " located at: " + theLocate + " have : " + numOfSeats + " seats");
		sb.append(available ? (" is available. ") : ("is taken."));

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Stadium other = (Stadium) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (theLocate.equals(other.theLocate)) {
			return false;
		}
		return true;
	}

}
