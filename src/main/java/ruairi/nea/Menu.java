package ruairi.nea;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Menu {
    private final MenusEnum currentState;
    private final Pane root;

    // Buttons (created once so getters can always return non-null)
    private final Button levelSelectButton = new Button("Level Select");
    private final Button settingsButton = new Button("Settings");
    private final Button howToPlayButton = new Button("How to Play");
    private final Button exitButton = new Button("Exit");

    private final Button levelOneButton = new Button("Level One");
    private final Button levelTwoButton = new Button("Level Two");
    private final Button levelThreeButton = new Button("Level Three");
    private final Button levelFourButton = new Button("Level Four");
    private final Button backToMainMenuButton = new Button("Back to main menu");

    public Menu(MenusEnum menuType) {
        this.currentState = menuType;
        this.root = switch (menuType) {
            case MAIN_MENU -> createMainMenu();
            case LEVEL_SELECT -> createLevelSelect();
            case SETTINGS, HOW_TO_PLAY -> createPlaceholder();
        };
    }

    private Pane createMainMenu() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(
                levelSelectButton,
                settingsButton,
                howToPlayButton,
                exitButton
        );
        return container;
    }

    private Pane createLevelSelect() {
        HBox container = new HBox(5);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(
                levelOneButton,
                levelTwoButton,
                levelThreeButton,
                levelFourButton,
                backToMainMenuButton
        );
        return container;
    }

    private Pane createPlaceholder() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    // Root container for the current menu
    public Pane getMenuContainer() {
        return root; // always non-null
    }

    // MAIN_MENU getters
    public Button getLevelSelectButton() {
        return levelSelectButton;
    }

    public Button getSettingsButton() {
        return settingsButton;
    }

    public Button getHowToPlayButton() {
        return howToPlayButton;
    }

    public Button getExitButton() {
        return exitButton;
    }

    // LEVEL_SELECT getters
    public Button getLevelOneButton() {
        return levelOneButton;
    }

    public Button getLevelTwoButton() {
        return levelTwoButton;
    }

    public Button getLevelThreeButton() {
        return levelThreeButton;
    }

    public Button getLevelFourButton() {
        return levelFourButton;
    }

    public Button getBackToMainMenuButton(){
        return backToMainMenuButton;
    }

    public MenusEnum getCurrentState() {
        return currentState;
    }
}