package com.streetrod.toolkit.gui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.streetrod.toolkit.sprites.BmpReader;
import com.streetrod.toolkit.sprites.LibWriter;
import com.streetrod.toolkit.sprites.Sprite;
import com.streetrod.toolkit.sprites.SpriteExtractor;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class SpriteViewer extends Application {

	private static final String APP_NAME = "Street Rod Toolkit";
	
	private static final Color[] COLORS = new Color[] {Color.GRAY, Color.BLACK, Color.RED, Color.BLUE, Color.MAGENTA};
	private static int currentColor = 0;
	
	private Stage stage;
	private ImageView imageView;
	private Spinner<Integer> spinner;
	
	private List<Sprite> sprites;
	private List<Image> images;
	private File currentDirectory;
	private File libFile;

	public static void main(String args[]){          
		 launch(args);     
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		
		BorderPane layout = new BorderPane();
		layout.setBackground(new Background(new BackgroundFill(COLORS[0], null, null)));
		
		Scene scene = new Scene(layout, 640, 400);
		
		Button buttonLoad = new Button("Load LIB");
		buttonLoad.setOnAction((event) -> {
			loadLib();
        });
		
		Button buttonSave = new Button("Save LIB");
		buttonSave.setOnAction((event) -> {
			saveLib();
        });
		
		Button buttonExportAll = new Button("Export All");
		buttonExportAll.setOnAction((event) -> {
			exportAllSprites();
        });
		
		Button buttonExport = new Button("Export Sprite");
		buttonExport.setOnAction((event) -> {
			exportSprite(spinner.getValue());
        });
		
		Button buttonReplace = new Button("Replace Sprite");
		buttonReplace.setOnAction((event) -> {
			replaceSprite(spinner.getValue());
        });
		
		Button buttonBackground = new Button("Background Color");
		buttonBackground.setOnAction((event) -> {
			currentColor++;
			currentColor %= COLORS.length;
			layout.setBackground(new Background(new BackgroundFill(COLORS[currentColor], null, null)));
        });
		
		spinner = new Spinner<Integer>(0, 0, 0);
		spinner.setPrefWidth(70);
		spinner.setEditable(true);
		
		HBox hbox = new HBox(10);
		hbox.setPadding(new Insets(5, 5, 5, 5));
		hbox.getChildren().addAll(buttonLoad, buttonSave, spinner, buttonExportAll, buttonExport, buttonReplace, buttonBackground);
		layout.setTop(hbox);
		
		imageView = new ImageView();
		layout.setCenter(imageView);
		
		primaryStage.setTitle(APP_NAME);
		primaryStage.setScene(scene);
		primaryStage.show();
    }

	private void saveLib() {
		if (sprites == null) return;
		
		try {
			LibWriter.writeLib(libFile, sprites);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadLib() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open LIB File");
		fileChooser.setInitialDirectory(currentDirectory);
		File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
        	currentDirectory = file.getParentFile();
        	libFile = file;
        	
        	images = loadSprites(file);
        	imageView.setImage(images.get(0));
        	
        	SpinnerValueFactory.IntegerSpinnerValueFactory factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, images.size() - 1, 0);
        	spinner.setValueFactory(factory);
        	
        	spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
    			if (newValue >= 0 && newValue < images.size()) {
    				imageView.setImage(images.get(newValue));
    			}
    		});
        }
	}

	private void replaceSprite(Integer value) {
		if (sprites == null) return;
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Sprite");
		fileChooser.setInitialDirectory(currentDirectory);
		File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
        	currentDirectory = file.getParentFile();
        	
        	int type = sprites.get(value).getType();
        	
        	try {
				Sprite sprite = BmpReader.readSprite(file.getAbsolutePath(), type);
				
				sprites.set(value, sprite);
				
				Image img = new Image(new ByteArrayInputStream(sprite.getBitmapDecoded()));
				img = replaceColor(img, Color.RED, Color.TRANSPARENT);
				images.set(value, img);
				imageView.setImage(img);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}

	private void exportAllSprites() {
		if (sprites == null) return;
		
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Export All Sprites");
		chooser.setInitialDirectory(currentDirectory);
		
		File file = chooser.showDialog(stage);
		
		if (file != null) {
			
			FileOutputStream out;
			File output;
			
			for (int i = 0; i < sprites.size(); i++) {
				Sprite s = sprites.get(i);
				
				output = new File(String.format("%s\\Sprite%03d.bmp", file.getAbsolutePath(), i));
				
	    		try {
	    			out = new FileOutputStream(output);
	    			out.write(s.getBitmapDecoded());
	    			out.close();
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
			}
		}
	}

	private void exportSprite(Integer value) {
		if (sprites == null) return;
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Sprite");
		fileChooser.setInitialDirectory(currentDirectory);
		fileChooser.setInitialFileName(String.format("Sprite%03d.bmp", value));
		
		File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
        	currentDirectory = file.getParentFile();
        	
    		FileOutputStream out;
    		try {
    			out = new FileOutputStream(file);
    			out.write(sprites.get(value).getBitmapDecoded());
    			out.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
	}

	private List<Image> loadSprites(File file) {
		SpriteExtractor extractor = new SpriteExtractor();
		
		String pathIn = file.getAbsolutePath();
		
		try {
			sprites = extractor.extract(pathIn, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Image> images = new ArrayList<>();
		
		for (Sprite s : sprites) {
			Image img = new Image(new ByteArrayInputStream(s.getBitmapDecoded()));
			img = replaceColor(img, Color.RED, Color.TRANSPARENT);
			images.add(img);
		}
		return images;
	}

	public Image replaceColor(Image image, Color oldColor, Color newColor) {
        PixelReader reader = image.getPixelReader();
        WritableImage writableImage = new WritableImage(reader, (int)image.getWidth(), (int)image.getHeight());
        PixelWriter writer = writableImage.getPixelWriter();
        for (int x = 0; x < writableImage.getWidth(); x++) {
            for (int y = 0; y < writableImage.getHeight(); y++) {
                if (reader.getColor(x, y).equals(oldColor)) {
                    writer.setColor(x, y, newColor);
                }
            }
        }
        return writableImage;
    }
}
