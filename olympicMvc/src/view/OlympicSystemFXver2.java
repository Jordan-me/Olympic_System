package view;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.w3c.dom.views.AbstractView;

import excepsions.NoAthletesInCountryException;
import excepsions.failedMassegeException;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
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
import javafx.util.StringConverter;
import listeners.OlympicsUIeventListener;
import model.Competition;
import model.FileHelperManger1;
import model.Participant;
import model.Participant.sportType;

public class OlympicSystemFXver2 implements AbstractOlympicsView {
	private Vector<OlympicsUIeventListener> allListeners = new Vector<OlympicsUIeventListener>();
	private Stage theStage;
	private Scene theScene;
	private TabPane tabPane;
	private TabPane tabPaneSign;
	private Tab tabCompetition;
	private Tab tabContries;
	private Tab tabSignUp;
	private StackPane spRoot = new StackPane();
	private BackgroundImage myBI;

	private VBox vbFirstSession;
	private VBox vbOpeningSession;
	private VBox vbRegisterSession;
	private VBox vbCompetingSession;
	// for OpeningSession
	private ComboBox<String> hostCountry;
	private DatePicker startDate;
	private DatePicker endDate;
	private Text forCombo;
	private final String pattern = "dd/MM/yyyy";
	// for competingSession
	private ComboBox<String> allCountriesOp;
	private ComboBox<String> allTypes;
	private ComboBox<String> allStadiums = new ComboBox<String>();
	private RadioButton teamComp;
	private RadioButton personalComp;
	// for tabcountries
	private GridPane gpRoot;
	private int rowIndex;
	private int column;
	private Text buttomWindowText;

	public OlympicSystemFXver2(Stage inStage, Image ring) throws FileNotFoundException {
		tabPane = new TabPane();
		this.theStage = inStage;

		vbFirstSession = new VBox();
		Button btnSignUp = new Button("Sign-up");
		Button btnOpenEvent = new Button("Open big event");
		btnOpenEvent.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				vbFirstSession.setVisible(false);
				creatOpeningSession();
			}

		});
		btnSignUp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vbFirstSession.setVisible(false);
				creatSignUpSession();
			}

		});
		myBI = new BackgroundImage(ring, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

		vbFirstSession.setBackground(new Background(myBI));
		theStage.setTitle("welcome!");
		vbFirstSession.setAlignment(Pos.CENTER);
		vbFirstSession.setSpacing(50);
		vbFirstSession.getChildren().addAll(btnSignUp, btnOpenEvent);
		spRoot.getChildren().add(vbFirstSession);
		theScene = new Scene(spRoot, 550, 400);
		theStage.setScene(theScene);
		theStage.show();

	}

	public void creatSignUpSession() {
		ToggleGroup tglOption = new ToggleGroup();
		tabPaneSign = new TabPane();

		vbRegisterSession = new VBox(30);
		vbRegisterSession.setPadding(new Insets(10));
		vbRegisterSession.setAlignment(Pos.CENTER);
		tabSignUp = new Tab("Manage participant");

		allCountriesOp = new ComboBox<String>();
		allCountriesOp.setStyle("-fx-font-size:15");
		allCountriesOp.setMinSize(200, 20);
		Text textForRead = new Text("Remove/Add Participant");
		Text forCountriesCombo = new Text("Choose country:");

		RadioButton rbtnAddP = new RadioButton("Add participant to the country");
		rbtnAddP.setToggleGroup(tglOption);
		RadioButton rbtnRmvP = new RadioButton("Remove participant from the country");
		rbtnRmvP.setToggleGroup(tglOption);
		RadioButton rbtnRemoveCountry = new RadioButton("Remove country");
		rbtnRemoveCountry.setToggleGroup(tglOption);

		Button btnMake = new Button("Make change");
		Button btnDone = new Button("I'm ready");

		HBox firstRow = new HBox(20);
		firstRow.setAlignment(Pos.BASELINE_LEFT);
		firstRow.getChildren().addAll(forCountriesCombo, allCountriesOp);
		btnDone.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null,
						"From here you start the opening of the Olympics - are you sure?") == 0) {
					theStage.setScene(theScene);
					creatOpeningSession();
				}
			}
		});
		btnMake.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (allCountriesOp.getSelectionModel().isEmpty()) {
					allCountriesOp.setBorder(new Border(
							new BorderStroke(Color.RED, BorderStrokeStyle.DASHED, null, new BorderWidths(1))));
				} else if (!rbtnAddP.isSelected() && !rbtnRmvP.isSelected() && !rbtnRemoveCountry.isSelected()) {
					JOptionPane.showMessageDialog(null, "choose kind of change");
				} else {
					if (rbtnAddP.isSelected()) {
						handleMakeSignSessionBTN("Add", allCountriesOp.getSelectionModel().getSelectedItem());
					} else if (rbtnRemoveCountry.isSelected()) {
						handleMakeSignSessionBTN("RemoveC", allCountriesOp.getSelectionModel().getSelectedItem());

					} else {
						handleMakeSignSessionBTN("RemoveP", allCountriesOp.getSelectionModel().getSelectedItem());
					}
				}

			}

		});
		vbRegisterSession.getChildren().addAll(textForRead, firstRow, rbtnAddP, rbtnRmvP, rbtnRemoveCountry, btnMake,
				btnDone);
		tabSignUp.setContent(vbRegisterSession);
		buildTab2Countries();
		tabPaneSign.getTabs().add(tabSignUp);
		tabPaneSign.getTabs().add(tabContries);
		tabPaneSign.setBackground(new Background(myBI));
		Scene SignSessionScene = new Scene(tabPaneSign, 1200, 500);
		theStage.setX(300);
		theStage.setScene(SignSessionScene);
		theStage.setTitle("Competition management system");

	}

	private VBox vbRemoveP = new VBox(20);
	private VBox vbAddP = new VBox(20);
	private ComboBox<Participant> allParticipants;
	private RadioButton rbtnJude;
	private RadioButton rbtnAthlet;
	private Button btnRemv;
	private Button btnAdd;
	private Button btnRtrn;
	private TextField fieldForName;
	private ComboBox<sportType> sporTypeCB;

	protected void handleMakeSignSessionBTN(String action, String selectedCountry) {
		vbRegisterSession.setVisible(false);
		switch (action) {
		case "Add":
			if (vbAddP.getChildren().isEmpty()) {
				initVBAddP();
			}
			btnAdd.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (!rbtnAthlet.isSelected() && !rbtnJude.isSelected()) {
						JOptionPane.showMessageDialog(null, "you must select role!");
					} else if (sporTypeCB.getSelectionModel().isEmpty()) {
						JOptionPane.showMessageDialog(null, "you must select sport type!");
					} else if (fieldForName.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "you must fill name!");
					} else {
						for (OlympicsUIeventListener l : allListeners) {
							if (rbtnAthlet.isSelected()) {
								try {
									l.addParticipantTocountry(fieldForName.getText(),
											sporTypeCB.getSelectionModel().getSelectedItem(), rbtnAthlet.getText(),
											selectedCountry);
									JOptionPane.showMessageDialog(null, "Added successfully");
									fieldForName.clear();
								} catch (failedMassegeException e) {
									JOptionPane.showMessageDialog(null, e.getMessage());
								}
							} else {
								try {
									l.addParticipantTocountry(fieldForName.getText(),
											sporTypeCB.getSelectionModel().getSelectedItem(), rbtnJude.getText(),
											selectedCountry);
									JOptionPane.showMessageDialog(null, "Added successfully");
									fieldForName.clear();
								} catch (failedMassegeException e) {
									JOptionPane.showMessageDialog(null, e.getMessage());
								}
							}

						}
					}

				}

			});
			btnRtrn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					tabSignUp.setContent(vbRegisterSession);
					vbRegisterSession.setVisible(true);
				}

			});
			tabSignUp.setContent(vbAddP);
			break;
		case "RemoveC":
			vbRegisterSession.setVisible(true);
			if (JOptionPane.showConfirmDialog(null,
					"This action will delete all state participants from the pool (judges / athletes) - are you sure?") == 0) {
				for (OlympicsUIeventListener l : allListeners) {
					l.deleteCountry(selectedCountry);
					buildTab2Countries();
					tabPaneSign.getTabs().set(1, tabContries);
					allCountriesOp.getItems().remove(selectedCountry);
				}
			}
			break;
		case "RemoveP":
			if (vbRemoveP.getChildren().isEmpty()) {
				initVBremoveP();
			}

			rbtnJude.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (rbtnJude.isSelected()) {
						for (OlympicsUIeventListener l : allListeners) {
							try {
								allParticipants.getItems().clear();
								allParticipants.getItems().addAll(l.getAllJudesFromCountry(selectedCountry));
							} catch (failedMassegeException e) {
								JOptionPane.showMessageDialog(null, e.getMessage());
							}
						}
					}
				}
			});

			rbtnAthlet.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (rbtnAthlet.isSelected()) {
						for (OlympicsUIeventListener l : allListeners) {
							try {
								allParticipants.getItems().clear();
								allParticipants.getItems().addAll(l.getAllAthletsFromCountry(selectedCountry));
							} catch (NoAthletesInCountryException e) {
								JOptionPane.showMessageDialog(null, e.getMessage());
							}
						}
					}
				}
			});

			btnRemv.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (allParticipants.getSelectionModel().isEmpty()) {
						allParticipants.setBorder(new Border(
								new BorderStroke(Color.RED, BorderStrokeStyle.DASHED, null, new BorderWidths(1))));
					} else {
						allParticipants.setBorder(new Border(new BorderStroke(Color.AQUAMARINE,
								BorderStrokeStyle.DASHED, null, new BorderWidths(1))));
						for (OlympicsUIeventListener l : allListeners) {
							l.removeParticipant(allParticipants.getSelectionModel().getSelectedItem());
						}
						allParticipants.getItems().remove(allParticipants.getSelectionModel().getSelectedItem());
					}
				}

			});

			btnRtrn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					tabSignUp.setContent(vbRegisterSession);
					allParticipants.getItems().clear();
					vbRegisterSession.setVisible(true);
				}

			});

			tabSignUp.setContent(vbRemoveP);

			break;

		}

	}

	public void initVBAddP() {
		Text ins2 = new Text("For add participant please choose a role first:");
		ins2.setFont(Font.font("SanSerif", FontWeight.BOLD, 15));
		ToggleGroup tgRole = new ToggleGroup();
		rbtnJude = new RadioButton("Judge");
		rbtnJude.setToggleGroup(tgRole);
		rbtnAthlet = new RadioButton("Athlet");
		rbtnAthlet.setToggleGroup(tgRole);
		vbAddP.setAlignment(Pos.CENTER);
		fieldForName = new TextField();
		fieldForName.setMaxSize(1000, 80);
		fieldForName.setPromptText("Insert name participant");
		btnAdd = new Button("Add");
		btnRtrn = new Button("Return");
		sporTypeCB = new ComboBox<sportType>();
		sporTypeCB.getItems().addAll(sportType.values());

		vbAddP.getChildren().addAll(ins2, rbtnJude, rbtnAthlet, sporTypeCB, fieldForName, btnAdd, btnRtrn);

	}

	public void initVBremoveP() {
		Text ins1 = new Text("For remove participant please choose a role first:");
		ins1.setFont(Font.font("SanSerif", FontWeight.BOLD, 15));
		if (allParticipants == null) {
			allParticipants = new ComboBox<Participant>();
		} else {
			allParticipants.getItems().clear();
		}

		ToggleGroup tgRole = new ToggleGroup();
		rbtnJude = new RadioButton("Judge");
		rbtnJude.setToggleGroup(tgRole);
		rbtnAthlet = new RadioButton("Athlet");
		rbtnAthlet.setToggleGroup(tgRole);
		btnRemv = new Button("Remove participant");
		btnRtrn = new Button("Return");
		vbRemoveP.setAlignment(Pos.CENTER);
		vbRemoveP.getChildren().addAll(ins1, rbtnJude, rbtnAthlet, allParticipants, btnRemv, btnRtrn);
	}

	public void creatOpeningSession() {
		vbOpeningSession = new VBox();
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
		hostCountry = new ComboBox<String>();
		forCombo = new Text("Select a host country");
		forCombo.setFont(Font.font("SanSerif", FontWeight.LIGHT, 30));
		// text edit
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
					vbOpeningSession.setVisible(false);
					clearFieldMainSession();
					buildCompetingSession();
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
		vbOpeningSession.setAlignment(Pos.TOP_CENTER);
		vbOpeningSession.setSpacing(20);
		vbOpeningSession.getChildren().addAll(textForRead, startTime, startDate, finishTime, endDate, hostCountry,
				startPlay);
		spRoot.getChildren().add(vbOpeningSession);
	}

	private void buildCompetingSession() {
		vbCompetingSession = new VBox(30);
		vbCompetingSession.setPadding(new Insets(10));
		vbCompetingSession.setAlignment(Pos.TOP_CENTER);
		tabCompetition = new Tab("Competition management system");
		buildTab2Countries();
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

		vbCompetingSession.getChildren().addAll(textForRead, firstRow, secondtRow, personalComp, teamComp, btnStart);
//		spRoot.getChildren().add(vbSecondSession);
		tabCompetition.setContent(vbCompetingSession);
		tabPane.getTabs().add(tabCompetition);
		tabPane.getTabs().add(tabContries);

		Scene seconSessionScene = new Scene(tabPane, 1300, 400);
		theStage.setX(300);
		theStage.setScene(seconSessionScene);
		theStage.setTitle("Competition management system");

	}

	public void buildTab2Countries() {
		tabContries = new Tab("Countries DataBase");
		gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(10));
		gpRoot.setAlignment(Pos.CENTER_LEFT);
		gpRoot.setHgap(20);
		gpRoot.setVgap(10);
		ArrayList<String> allCountries = new ArrayList<String>();
		for (OlympicsUIeventListener l : allListeners) {
			try {
				allCountries.addAll(l.getAllcountriesToUI());
			} catch (failedMassegeException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
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
			gpRoot.setBackground(new Background(myBI));
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

	public boolean handleButtonAddComp(RadioButton teamComp, RadioButton personalComp, ComboBox<String> allTypes) {
		if (allTypes.getSelectionModel().isEmpty()) {
			failedOpenEventMessage("Type of sport missing");
			clearFieldCompetingSession();
			return false;
		}
		if (allStadiums.getSelectionModel().isEmpty()) {
			failedOpenEventMessage("Venue missing");
			clearFieldCompetingSession();
			return false;
		}
		if (allCountriesOp.getItems().isEmpty()) {
			failedOpenEventMessage("Select countries from the database");
			clearFieldCompetingSession();
			return false;
		}
		if ((!teamComp.isSelected()) && (!personalComp.isSelected())) {
			teamComp.setTextFill(Color.RED);
			personalComp.setTextFill(Color.RED);
			clearFieldCompetingSession();
			return false;
		}
		return true;
	}

	private void clearFieldCompetingSession() {
		teamComp.setSelected(false);
		personalComp.setSelected(false);
		allTypes.getSelectionModel().clearSelection();
		allStadiums.getSelectionModel().clearSelection();

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

	public void failedOpenEventMessage(String message) {
		JOptionPane.showMessageDialog(null, message);

	}

	public void clearFieldMainSession() {
		startDate.setValue(null);
		endDate.setValue(null);
		hostCountry.getSelectionModel().clearSelection();

	}

	@Override
	public void registerListener(OlympicsUIeventListener listener) {
		allListeners.add(listener);
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
		clearFieldCompetingSession();

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
