package listeners;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import excepsions.NoAthletesInCountryException;
import excepsions.failedMassegeException;
import model.Competition;
import model.Participant;
import model.Participant.sportType;

public interface OlympicsUIeventListener {

	void openOlympics(LocalDate value, LocalDate value2, String selectedItem);

	void show(int i);

	void addCountryToCompetition(String text);

	void removeCountryFromCompetition(String text);

	void addCompetitionEvent(String type, String kind, String venue, ArrayList<String> allCountries);

	void endCompetitionEvent(int index, Competition<Participant> theSelected);

	void endOlympicEvent();

	Collection<String> getAllcountriesToUI() throws failedMassegeException;

	void deleteCountry(String selectedCountry);

	Collection<Participant> getAllAthletsFromCountry(String selectedCountry) throws NoAthletesInCountryException;

	Collection<Participant> getAllJudesFromCountry(String selectedCountry) throws failedMassegeException;

	void removeParticipant(Participant selectedItem);

	void addParticipantTocountry(String nameP, sportType typeS, String role, String selectedCountry)
			throws failedMassegeException;

}
