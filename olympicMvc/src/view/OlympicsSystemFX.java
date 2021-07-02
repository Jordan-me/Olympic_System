package view;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JOptionPane;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import listeners.OlympicsUIeventListener;
import model.Competition;
import model.FileHelperManger1;
import model.Participant;

public class OlympicsSystemFX implements AbstractOlympicsView {
	//window of creat competition and db of country
	private Vector<OlympicsUIeventListener> allListeners = new Vector<OlympicsUIeventListener>();
	private Stage theStage;
	private TabPane tabPane;
	private Tab tabCompetition;
	private Tab tabContries; 
	private StackPane spRoot = new StackPane();
	private VBox vbMainSession = new VBox();
	private VBox vbSecondSession;
	private ComboBox<String> hostCountry;
	private DatePicker startDate;
	private DatePicker endDate;
	private Text forCombo;
	private Scene theScene;
	private ComboBox<String> allCountriesOp;
	private ComboBox<String> allTypes;
	private ComboBox<String> allStadiums = new ComboBox<String>();
	private RadioButton teamComp;
	private RadioButton personalComp;
	private final String pattern = "dd/MM/yyyy";

	public OlympicsSystemFX(Stage inStage) throws FileNotFoundException {
		tabPane = new TabPane();
		//creat date picker
		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		};
		this.theStage = inStage;
		//create nodes of first window- for competition
		hostCountry = new ComboBox<String>();
		forCombo = new Text("Select a host country");
		forCombo.setFont(Font.font("SanSerif", FontWeight.LIGHT, 30));
		//text edit
		hostCountry.setPromptText(forCombo.getText());
		hostCountry.setMaxHeight(300);
		hostCountry.getItems().addAll(FileHelperManger1.getHostCountry());
		hostCountry.setStyle("-fx-font-size:18");
		theStage.setTitle("welcome!");
		Text textForRead = new Text("Welcome to the Olympic system - fill in the required fields");
		Text startTime = new Text("Start Time: ");
		Text finishTime = new Text("Finish Time: ");
		textForRead.setFont(Font.font("SanSerif", FontWeight.BOLD, 15));
		startDate = new DatePicker();
		startDate.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				setDisable(empty || date.compareTo(today) < 0);

			}
		});
		endDate = new DatePicker();
		endDate.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				setDisable(empty || date.compareTo(today) < 0);

			}
		});
		startDate.setPromptText("Start Time");
		startDate.setMaxWidth(300);
		startDate.setStyle("-fx-font-size:20");
		endDate.setPromptText("Finish time");
		endDate.setMaxWidth(300);
		endDate.setStyle("-fx-font-size:20");
		textForRead.setTextAlignment(TextAlignment.CENTER);
		startTime.setTextAlignment(TextAlignment.CENTER);
		finishTime.setTextAlignment(TextAlignment.CENTER);
		Button startPlay = new Button("Open Event");
		startPlay.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (handleWithButtonStartEvent()) {
					vbMainSession.setVisible(false);
					clearFieldMainSession();
					buildSecondSession();
					for (OlympicsUIeventListener l : allListeners) {
						l.show(1);
					}
				}
			}

		});
		endDate.setConverter(converter);
		endDate.requestFocus();
		startDate.setConverter(converter);
		startDate.requestFocus();
		vbMainSession.setAlignment(Pos.TOP_CENTER);
		vbMainSession.setSpacing(20);
		vbMainSession.getChildren().addAll(textForRead, startTime, startDate, finishTime, endDate, hostCountry,
				startPlay);
		spRoot.getChildren().add(vbMainSession);
		theScene = new Scene(spRoot, 550, 400);
		theStage.setScene(theScene);
		theStage.show();

	}

	public void buildSecondSession() {
		vbSecondSession = new VBox(30);
		vbSecondSession.setPadding(new Insets(10));
		vbSecondSession.setAlignment(Pos.TOP_CENTER);
		tabCompetition = new Tab("Competition management system");
		tabContries = new Tab("Countries DataBase");
		buildTab2();

		Text textForRead = new Text("Let The Games Begin");
		textForRead.setFont(Font.font("SanSerif", FontWeight.BOLD, 20));
		textForRead.setTextAlignment(TextAlignment.CENTER);

		allStadiums.setPromptText("Select venue");

		Text forCombo = new Text("Select sport type: ");
		forCombo.setFont(Font.font("SanSerif", FontWeight.MEDIUM, 15));
		allTypes = new ComboBox<String>();
		allTypes.setStyle("-fx-font-size:15");
		allTypes.getItems().addAll(FileHelperManger1.getAllTypes(2));

		allCountriesOp = new ComboBox<String>();
		allCountriesOp.setStyle("-fx-font-size:15");
		Text forCountriesCombo = new Text("Countries that play");
		forCountriesCombo.setFont(Font.font("SanSerif", FontWeight.MEDIUM, 15));

		ToggleGroup tglOption = new ToggleGroup();
		personalComp = new RadioButton("Personal competition:");
		personalComp.setStyle("-fx-font-size:15");
		personalComp.setToggleGroup(tglOption);

		teamComp = new RadioButton("Group competition:");
		teamComp.setStyle("-fx-font-size:15");
		teamComp.setToggleGroup(tglOption);

		Button btnStart = new Button("Register");
		btnStart.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (handleButtonAddComp(teamComp, personalComp, allTypes)) {
					personalComp.setTextFill(Color.BLACK);
					teamComp.setTextFill(Color.BLACK);

					for (OlympicsUIeventListener l : allListeners) {
						String type = allTypes.getSelectionModel().getSelectedItem();
						String kind = null;
						String venue = allStadiums.getSelectionModel().getSelectedItem();
						if (teamComp.isSelected()) {
							kind = teamComp.getText();
						} else {
							kind = personalComp.getText();
						}
						ArrayList<String> allCountries = new ArrayList<String>();
						allCountries.addAll(allCountriesOp.getItems());
						
						l.addCompetitionEvent(type, kind, venue, allCountries);
						
					}
				}
			}

		});

		HBox firstRow = new HBox(10);
		firstRow.getChildren().addAll(forCombo, allTypes, allStadiums);

		HBox secondtRow = new HBox(10);
		secondtRow.getChildren().addAll(forCountriesCombo, allCountriesOp);

		vbSecondSession.getChildren().addAll(textForRead, firstRow, secondtRow, personalComp, teamComp, btnStart);
//		spRoot.getChildren().add(vbSecondSession);
		tabCompetition.setContent(vbSecondSession);
		tabPane.getTabs().add(tabCompetition);
		tabPane.getTabs().add(tabContries);

		Scene seconSessionScene = new Scene(tabPane, 1300, 400);
		theStage.setX(300);
		theStage.setScene(seconSessionScene);
		theStage.setTitle("Competition management system");
	}

	private GridPane gpRoot;
	private int rowIndex;
	private int column;
	private Text buttomWindowText;

	public void buildTab2() {
		gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(10));
		gpRoot.setAlignment(Pos.CENTER_LEFT);
		gpRoot.setHgap(20);
		gpRoot.setVgap(10);
		ArrayList<String> allCountries = new ArrayList<String>();
		allCountries.addAll(FileHelperManger1.getAllCountries());
		final int numOfElements = 7;
		rowIndex = 0;
		column = 0;

		for (int i = 0; i < allCountries.size(); i++) {
			if (column == numOfElements) {
				column = 0;
				rowIndex++;
			}
			CheckBox cb = new CheckBox(allCountries.get(i));
			cb.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					if (handleWithCheckBoxEvent(cb)) {
						for (OlympicsUIeventListener l : allListeners) {
							l.addCountryToCompetition(cb.getText());
						}
					} else {
						for (OlympicsUIeventListener l : allListeners) {
							l.removeCountryFromCompetition(cb.getText());
						}
					}

				}
			});
			gpRoot.add(cb, column, rowIndex);
			column++;

		}
		rowIndex++;
		buttomWindowText = new Text("The country selected: ");
		buttomWindowText.setFont((Font.font("SanSerif", FontWeight.BOLD, 20)));
		gpRoot.addRow(rowIndex, buttomWindowText);
		ScrollPane scroll = new ScrollPane();
		scroll.fitToHeightProperty().set(true);
		scroll.fitToWidthProperty().set(true);
		scroll.setContent(gpRoot);
		tabContries.setContent(scroll);
		Platform.setImplicitExit(false);

	}

	public boolean handleWithCheckBoxEvent(CheckBox cb) {
		if (cb.isSelected()) {
			return true;
		}
		return false;
	}

	public boolean handleWithButtonStartEvent() {
		if (startDate.getEditor().getText().isEmpty() || endDate.getEditor().getText().isEmpty()) {
			failedOpenEventMessage("Fill the dates first");
			clearFieldMainSession();
			return false;
		} else if (startDate.getValue().isAfter(endDate.getValue())
				|| startDate.getValue().isEqual(endDate.getValue())) {
			failedOpenEventMessage("Start time cannot be after/equal end time");
			clearFieldMainSession();
			return false;
		} else if (hostCountry.getSelectionModel().isEmpty()) {
			failedOpenEventMessage("In which country will the Olympics be held?");
			clearFieldMainSession();
			return false;
		} else {
			for (OlympicsUIeventListener l : allListeners) {
				l.openOlympics(startDate.getValue(), endDate.getValue(),
						hostCountry.getSelectionModel().getSelectedItem());
				allStadiums.getItems().addAll(
						FileHelperManger1.getAllStadiumsName(hostCountry.getSelectionModel().getSelectedItem()));
			}
		}
		return true;

	}

	@Override
	public void registerListener(OlympicsUIeventListener listener) {
		System.out.println("assign");
		allListeners.add(listener);

	}

	public boolean handleButtonAddComp(RadioButton teamComp, RadioButton personalComp, ComboBox<String> allTypes) {
		if (allTypes.getSelectionModel().isEmpty()) {
			failedOpenEventMessage("Type of sport missing");
			clearFieldSecondSession();
			return false;
		}
		if (allStadiums.getSelectionModel().isEmpty()) {
			failedOpenEventMessage("Venue missing");
			clearFieldSecondSession();
			return false;
		}
		if (allCountriesOp.getItems().isEmpty()) {
			failedOpenEventMessage("Select countries from the database");
			clearFieldSecondSession();
			return false;
		}
		if ((!teamComp.isSelected()) && (!personalComp.isSelected())) {
			teamComp.setTextFill(Color.RED);
			personalComp.setTextFill(Color.RED);
			clearFieldSecondSession();
			return false;
		}
		return true;
	}

	public void clearFieldMainSession() {
		startDate.setValue(null);
		endDate.setValue(null);
		hostCountry.getSelectionModel().clearSelection();

	}

	public void clearFieldSecondSession() {
		teamComp.setSelected(false);
		personalComp.setSelected(false);
		allTypes.getSelectionModel().clearSelection();
		allStadiums.getSelectionModel().clearSelection();

	}

	public void failedOpenEventMessage(String message) {
		JOptionPane.showMessageDialog(null, message);

	}

	@Override
	public void showWindow(int i) {
		if (i == 0) {
			theStage.show();
		}

	}

	@Override
	public void addCountryToUI(String text) {
		allCountriesOp.getItems().add(text);
		buttomWindowText.setText("Country selected to be added:   " + text);
		buttomWindowText.setFont(Font.font("SanSerif", FontWeight.SEMI_BOLD, 15));
	}

	@Override
	public void removeCountryFromUI(String text) {
		allCountriesOp.getItems().remove(text);
		buttomWindowText.setText("Country selected to be removed:   " + text);
		buttomWindowText.setFont(Font.font("SanSerif", FontWeight.SEMI_BOLD, 15));
	}

	@Override
	public void addCompetitionToUI(int indexCompetition, Competition<Participant> theCom) {
		ObservableList<Node> allCB = gpRoot.getChildren();

		for (int i = 0; i < allCB.size(); i++) {
			if (allCB.get(i) instanceof CheckBox) {
				CheckBox cb = (CheckBox) allCB.get(i);
				cb.setSelected(false);
			}
		}
		allCountriesOp.getItems().clear();
		clearFieldSecondSession();

	}

	@Override
	public void endCompetitionToUI(int index, Map<String, Integer> allCountriesMedals, String theWins,
			Competition<Participant> theCom) {
		ObservableList<Node> allCB = gpRoot.getChildren();

		for (int i = 0; i < allCB.size(); i++) {
			if (allCB.get(i) instanceof CheckBox) {
				CheckBox cb = (CheckBox) allCB.get(i);
				cb.setSelected(false);
			}
		}
	}

	@Override
	public void endOlympicToUI(String[] winningCountries) {
		theStage.hide();

	}

}
