package excepsions;

public class NoAthletesInCountryException extends Exception {
	public NoAthletesInCountryException(String theCountry) {
		super("The country" +theCountry+ "has no athletes");
		
	}

}
