package controller;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JOptionPane;

import excepsions.JudgeIsNotFoundException;
import excepsions.NoAthletesInCountryException;
import excepsions.endSomeCompetitionsException;
import excepsions.failedMassegeException;
import listeners.OlympicsEventListener;
import listeners.OlympicsUIeventListener;
import model.Competition;
import model.Olympic;
import model.Participant;
import view.AbstractOlympicsView;

public class OlympicsController implements OlympicsEventListener, OlympicsUIeventListener {
	private Olympic olympicModel;
	private AbstractOlympicsView olympicView;

	public OlympicsController(AbstractOlympicsView view) {
		olympicView = view;
		olympicView.registerListener(this);

	}

	public OlympicsController(AbstractOlympicsView view, Olympic model) {
		olympicView = view;
		olympicModel = model;
		olympicView.registerListener(this);
		olympicModel.registerListener(this);
	}

	@Override
	public void openOlympics(LocalDate inStratTime, LocalDate inFinishTime, String inCountry) {
		olympicModel.setStartTime(inStratTime);
		olympicModel.setFinishTime(inFinishTime);
		try {
			olympicModel.setHostCountry(inCountry);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}

	}

	public void registerListenerForModel() {
		olympicModel.registerListener(this);

	}

	@Override
	public void show(int i) {
		olympicModel.showWindowToUI(i);

	}

	@Override
	public void fireShowWindow(int i) {
		olympicView.showWindow(i);

	}

	@Override
	public void addCountryToCompetition(String text) {
		olympicModel.addCountryModelEvent(text);

	}

	@Override
	public void removeCountryFromCompetition(String text) {
		olympicModel.removeCountryModelEvent(text);

	}

	@Override
	public void fireAddCountry(String text) {
		olympicView.addCountryToUI(text);

	}

	@Override
	public void fireRemoveCountry(String text) {
		olympicView.removeCountryFromUI(text);

	}

	@Override
	public void addCompetitionEvent(String type, String kind, String venue, ArrayList<String> allCountries) {
		try {
			olympicModel.addCompetition(type, kind, venue, allCountries);
		} catch (JudgeIsNotFoundException | endSomeCompetitionsException | NoAthletesInCountryException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (failedMassegeException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	@Override
	public void fireAddCompetition(int indexCompetition, Competition<Participant> theCom) {
		olympicView.addCompetitionToUI(indexCompetition, theCom);
	}

	@Override
	public void endCompetitionEvent(int index, Competition<Participant> theSelected) {
		olympicModel.endCompetition(index, theSelected);

	}

	@Override
	public void fireEndCompetition(int index, Map<String, Integer> allCountriesMedals, String theWins,
			Competition<Participant> theCom) {
		olympicView.endCompetitionToUI(index, allCountriesMedals, theWins, theCom);
		System.out.println("Please refresh (push) the winners column in the table");

	}

	@Override
	public void endOlympicEvent() {
		try {
			olympicModel.getWinnersOlympic();
		} catch (endSomeCompetitionsException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

	}

	@Override
	public void fireEndOlympic(String[] winningCountries) {
		olympicView.endOlympicToUI(winningCountries);

	}

}
