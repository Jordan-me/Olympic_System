//Name: Yarden Dahan
//ID: 208730523
package main;

import java.io.File;

import controller.OlympicsController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import model.Olympic;
import view.AbstractOlympicsView;
import view.OlympicSystemFXver2;
import view.OlympicsSystemFX;
import view.OlympicsSystemFX2;

public class OlympicsSystemMain extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		System.out.println("Here we go...");
		Olympic olympicsModel = new Olympic();

		AbstractOlympicsView theMainView = new OlympicSystemFXver2(new Stage(), image());
		OlympicsController controller = new OlympicsController(theMainView, olympicsModel);

		AbstractOlympicsView theTableView = new OlympicsSystemFX2(new Stage(), sound());
		OlympicsController controller2 = new OlympicsController(theTableView, olympicsModel);

	}

	private Image image() {
		String path = new File("resources/ring2.jpg").getAbsolutePath();
		Image im = new Image(new File(path).toURI().toString());
		return im;
	}

	public static MediaPlayer sound() {
		String path = new File("resources/AthemHatikva.mp3").getAbsolutePath();
		Media me = new Media(new File(path).toURI().toString());
		MediaPlayer mp = new MediaPlayer(me);
		mp.setVolume(0.8);
		return mp;
	}

}
