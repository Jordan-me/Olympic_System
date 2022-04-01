package listeners;

import java.util.Map;

import model.Competition;
import model.Participant;

public interface OlympicsEventListener {

	void fireShowWindow(int i);

	void fireAddCountry(String text);

	void fireRemoveCountry(String text);

	void fireAddCompetition(int indexCompetition, Competition<Participant> theCom);

	void fireEndCompetition(int index, Map<String, Integer> allCountriesMedals, String theWins,
			Competition<Participant> theEvent);

	void fireEndOlympic(String[] winningCountries);

}
