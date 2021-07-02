package view;

import java.util.ArrayList;
import java.util.Map;

import listeners.OlympicsUIeventListener;
import model.Competition;
import model.Participant;

public interface AbstractOlympicsView {

	void registerListener(OlympicsUIeventListener listener);

	void showWindow(int i);

	void addCountryToUI(String text);

	void removeCountryFromUI(String text);

	void addCompetitionToUI(int indexCompetition, Competition<Participant> theCom);

	void endCompetitionToUI(int index, Map<String, Integer> allCountriesMedals, String theWins,
			Competition<Participant> theCom);

	void endOlympicToUI(String[] winningCountries);

}
