package view;

import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import effects.OlympicRings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import listeners.OlympicsUIeventListener;
import model.Competition;
import model.Participant;

public class OlympicsSystemFX2 implements AbstractOlympicsView {
	private Vector<OlympicsUIeventListener> allListeners = new Vector<OlympicsUIeventListener>();
	private MediaPlayer mediaPlayer;
	private Stage theStage;
	private TabPane tabPane;
	private Tab tab1;
	private Tab tab2;
	private StackPane paneResults;
	private VBox vbTab1;
	private TableView<Competition<Participant>> tableCompetition;
	private TableColumn<Competition<Participant>, String> column1;
	private TableColumn<Competition<Participant>, String> column2;
	private TableColumn<Competition<Participant>, String> column3;
	private TableColumn<Competition<Participant>, String> column4;
	private TableColumn<Competition<Participant>, String> column5;
	private ObservableList<Competition<Participant>> competitionList;
	private Button btnEndOlympic;
	private Button btnGetResults;

	public OlympicsSystemFX2(Stage theStage, MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
		this.theStage = theStage;
		tabPane = new TabPane();
		tableCompetition = new TableView<Competition<Participant>>();
		tableCompetition.setMinHeight(700);
		competitionList = FXCollections.observableArrayList();
		theStage.setWidth(550);
		theStage.setHeight(550);

		tab1 = new Tab("Competitions Calendar");
		tab2 = new Tab("Scoreboard");

		column1 = new TableColumn<Competition<Participant>, String>("Type");
		column1.setMaxWidth(500);
		column1.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getTheBranch().name()));

		column2 = new TableColumn<Competition<Participant>, String>("Stadium");
		column2.setMinWidth(300);
		column2.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getTheStadium().getName()));

		column3 = new TableColumn<Competition<Participant>, String>("Judge");
		column3.setMinWidth(300);
		column3.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getJudge()));

		column4 = new TableColumn<Competition<Participant>, String>("Participants");
		column4.setMinWidth(700);
		column4.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getCompetitorsString()));

		column5 = new TableColumn<Competition<Participant>, String>("Winners");
		column5.setMinWidth(700);
		column5.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getTheWinners()));
		tableCompetition.setEditable(false);

		tableCompetition.getColumns().addAll(column1, column2, column3, column4, column5);
		tableCompetition.setEditable(false);
		tableCompetition.setItems(competitionList);

		paneResults = new StackPane();
		Pane paneForRings = new Pane();
		OlympicRings design = new OlympicRings(paneForRings, 200, 40);
		OlympicRings.STANDARD_OLYMPIC_COLORS.forEach(design::paintNextRing);

		btnEndOlympic = new Button("End Olympic");
		paneResults.setAlignment(Pos.CENTER);
		paneResults.getChildren().addAll(paneForRings, btnEndOlympic);

		VBox vbTab2 = new VBox(10);

		btnEndOlympic.setStyle("-fx-font-size:20");
		vbTab2.getChildren().addAll(paneResults);
		vbTab2.setPadding(new Insets(10, 0, 0, 10));
		vbTab2.setAlignment(Pos.CENTER);

		vbTab1 = new VBox(5);
		btnGetResults = new Button("Get Results");
		btnGetResults.setStyle("-fx-font-size:20");
		btnGetResults.setAlignment(Pos.CENTER);
		vbTab1.getChildren().addAll(tableCompetition, btnGetResults);
		vbTab1.setPadding(new Insets(10, 0, 0, 10));
		vbTab1.setAlignment(Pos.CENTER);

		btnGetResults.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int index = tableCompetition.getSelectionModel().selectedIndexProperty().get();
				Competition<Participant> theSelected = tableCompetition.getSelectionModel().selectedItemProperty()
						.get();
				if (index == -1) {
					JOptionPane.showMessageDialog(null, "Choose competition to finish first");
				} else {
					for (OlympicsUIeventListener l : allListeners) {
						l.endCompetitionEvent(index, theSelected);
					}
				}

			}

		});
		btnEndOlympic.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				for (OlympicsUIeventListener l : allListeners) {
					l.endOlympicEvent();
				}
			}
		});

		tab1.setContent(vbTab1);
		tab2.setContent(vbTab2);
		tabPane.getTabs().add(tab1);
		tabPane.getTabs().add(tab2);

		VBox vbWindow = new VBox(5);
		vbWindow.setPadding(new Insets(10, 0, 0, 10));
		vbWindow.getChildren().add(tabPane);
		vbWindow.setMinHeight(1000);
		vbWindow.setMinWidth(1000);

		Scene scene = new Scene(vbWindow, 3000, 1000);
		theStage.sizeToScene();
		theStage.fullScreenProperty();
		theStage.setScene(scene);
		theStage.setTitle("Data Base");
		theStage.hide();

	}

	@Override
	public void registerListener(OlympicsUIeventListener listener) {
		allListeners.add(listener);

	}

	@Override
	public void showWindow(int i) {
		if (i == 2) {
			theStage.show();
		}

	}

	@Override
	public void addCountryToUI(String text) {
		System.out.println("you selected to add " + text);
	}

	@Override
	public void removeCountryFromUI(String text) {
		System.out.println("you selected to remove " + text);
	}

	@Override
	public void addCompetitionToUI(int indexCompetition, Competition<Participant> theCom) {
		theStage.show();
		competitionList.add(theCom);

	}

	@Override
	public void endCompetitionToUI(int index, Map<String, Integer> allCountriesMedals, String theWins,
			Competition<Participant> theCom) {
		JOptionPane.showMessageDialog(null, "The winners are: \n" + theWins);
		theCom.setTheWinners(theWins);
		competitionList.remove(index);
		competitionList.add(index, (Competition<Participant>) theCom);
	}

	@Override
	public void endOlympicToUI(String[] winningCountries) {
		final int NUM_LABELS = 3;
		VBox vbWinnersLabels = new VBox(5);
		Label firstPlace = new Label("First Place: \t " + winningCountries[0]);
		firstPlace.setStyle("-fx-font-size:25");
		firstPlace.setFont(Font.font("SanSerif", FontWeight.BOLD, 30));
		Label secondPlace = new Label("Second Place:\t " + winningCountries[1]);
		secondPlace.setStyle("-fx-font-size:25");
		secondPlace.setFont(Font.font("SanSerif", FontWeight.BOLD, 30));
		Label thirdPlace = new Label("Third Place: \t " + winningCountries[2]);
		thirdPlace.setStyle("-fx-font-size:25");
		thirdPlace.setFont(Font.font("SanSerif", FontWeight.BOLD, 30));
		mediaPlayer.setAutoPlay(true);
		vbWinnersLabels.getChildren().addAll(firstPlace, secondPlace, thirdPlace);
		vbWinnersLabels.setAlignment(Pos.CENTER);
		btnEndOlympic.setVisible(false);
		paneResults.getChildren().add(vbWinnersLabels);
		tab1.setDisable(true);

	}

}
