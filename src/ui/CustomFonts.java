package ui;

import java.lang.invoke.MethodHandles;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class CustomFonts {
	final static int TEXTFIELD_SIZE = 15;
	final static int BUTTON_SIZE = 20;
	final static int TITLE_SIZE = 30;
	final static int RADIO_BUTTON_SIZE = 35;

	public static void setCustomButtonFont(Button button, String textLabel) {
		button.setFont(applyCustomFont("/fonts/mmatch.ttf", BUTTON_SIZE));
		button.setText(textLabel);
	}

	public static void setCustomTitleButtonFont(Button button, String title) {
		button.setFont(applyCustomFont("/fonts/mmatch.ttf", TITLE_SIZE));
		button.setText(title);
	}

	public static void setCustomLabelFont(Label label, String textLabel) {
		label.setFont(applyCustomFont("/fonts/mmatch.ttf", TEXTFIELD_SIZE));
		label.setText(textLabel);
	}

	public static void setCustomTextFieldFont(TextField textField) {
		textField.setFont(applyCustomFont("/fonts/mmatch.ttf", TEXTFIELD_SIZE));
	}

	public static void setRadioButtonFont(RadioButton radioButton, String text) {
		radioButton.setFont(applyCustomFont("/fonts/MightyWindy.ttf",RADIO_BUTTON_SIZE));
		radioButton.setText(text);
	}

	private static Font applyCustomFont(String customFont, int size) {
		Font loadCustomButtonFont = Font.loadFont(MethodHandles.lookup()
				.lookupClass().getResourceAsStream(customFont), size);
		return loadCustomButtonFont;
	}
}