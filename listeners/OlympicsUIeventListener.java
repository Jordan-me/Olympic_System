package listeners;

import java.time.LocalDate;
import java.util.ArrayList;

import model.Competition;
import model.Participant;

public interface OlympicsUIeventListener {

	void openOlympics(LocalDate value, LocalDate value2, String selectedItem);

	void show(int i);

	void addCountryToCompetition(String text);

	void removeCountryFromCompetition(String text);

	void addCompetitionEvent(String type, String kind, String venue, ArrayList<String> allCountries);

	void endCompetitionEvent(int index, Competition<Participant> theSelected);

	void endOlympicEvent();

}
