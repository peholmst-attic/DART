package net.pkhapps.dart.common.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import net.pkhapps.dart.common.i18n.I18N;
import net.pkhapps.dart.common.i18n.Locales;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO Document me!
 */
public class LoginDialog {

    private final Dialog<LoginResult> dialog;
    private final Label usernameLbl;
    private final TextField username;
    private final Label passwordLbl;
    private final PasswordField password;
    private final Label languageLbl;
    private final ComboBox<Locale> language;
    private final I18N i18n;
    private final ButtonType loginButtonType;

    /**
     *
     */
    public LoginDialog() {
        i18n = new I18N(getClass().getPackage());
        dialog = new Dialog<>();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        usernameLbl = new Label();
        usernameLbl.setMinWidth(Region.USE_PREF_SIZE);
        username = new TextField();
        grid.add(usernameLbl, 0, 0);
        grid.add(username, 1, 0);

        passwordLbl = new Label();
        passwordLbl.setMinWidth(Region.USE_PREF_SIZE);
        password = new PasswordField();
        grid.add(passwordLbl, 0, 1);
        grid.add(password, 1, 1);

        languageLbl = new Label();
        languageLbl.setMinWidth(Region.USE_PREF_SIZE);
        language = new ComboBox<>(FXCollections.observableArrayList(Locales.ALL_LOCALES));
        // Change the language of the UI when another language is selected from the combo box
        language.valueProperty().addListener(((observable, oldValue, newValue) -> updateMessageStrings()));
        grid.add(languageLbl, 0, 2);
        grid.add(language, 1, 2);

        loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // TODO Validation

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return new LoginResult(language.getValue(), username.getText(), password.getText());
            }
            return null;
        });
    }

    private void updateMessageStrings() {
        I18N.MessageSource messages = i18n.forLocale(language.getValue());
        dialog.setTitle(messages.get("LoginDialog.title"));

        usernameLbl.setText(messages.get("LoginDialog.username.label"));
        passwordLbl.setText(messages.get("LoginDialog.password.label"));
        languageLbl.setText(messages.get("LoginDialog.language.label"));

        ((Button) dialog.getDialogPane().lookupButton(loginButtonType)).setText(messages.get("LoginDialog.buttons.login"));
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(messages.get("LoginDialog.buttons.cancel"));
    }

    /**
     * @param initialLocale
     * @return
     */
    public Optional<LoginResult> showAndWait(Locale initialLocale) {
        language.setValue(initialLocale);
        Platform.runLater(username::requestFocus);
        return dialog.showAndWait();

    }

    /**
     * @return
     */
    @NotNull
    public Optional<LoginResult> showAndWait() {
        return showAndWait(Locales.ENGLISH);
    }

    /**
     * @param applicationName
     */
    public void setApplicationName(@Nullable String applicationName) {
        dialog.setHeaderText(applicationName);
    }

    /**
     * TODO Document me!
     */
    public static class LoginResult {

        private final Locale locale;
        private final String username;
        private final String password;

        public LoginResult(@NotNull Locale locale, @NotNull String username, @NotNull String password) {
            this.locale = Objects.requireNonNull(locale);
            this.username = Objects.requireNonNull(username);
            this.password = Objects.requireNonNull(password);
        }

        @NotNull
        public Locale getLocale() {
            return locale;
        }

        @NotNull
        public String getUsername() {
            return username;
        }

        @NotNull
        public String getPassword() {
            return password;
        }
    }
}
