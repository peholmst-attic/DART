package net.pkhapps.dart.dispatcher.ui.tickets;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.pkhapps.dart.common.i18n.UTF8Control;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * TODO Document me!
 */
public class TicketForm {

    private final VBox layout = new VBox();

    private Locale locale;

    private ResourceBundle messages;

    private TicketDetails ticketDetails;

    public TicketForm() {
        setLocale(Locale.getDefault());
    }

    @NotNull
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(@Nullable Locale locale) {
        this.locale = Optional.ofNullable(locale).orElse(Locale.getDefault());
        this.messages = ResourceBundle.getBundle(getClass().getPackage().getName() + ".messages", this.locale, new UTF8Control());
        updateLocalizedStrings();
    }

    private void updateLocalizedStrings() {
        ticketDetails.updateLocalizedStrings(messages);
    }

    @NotNull
    public Node getView() {
        return layout;
    }

    @PostConstruct
    void init() {
        ticketDetails = new TicketDetails(layout);
        updateLocalizedStrings(); // This has not been called before
    }

    @PreDestroy
    void destroy() {

    }

    private static class TicketDetails {

        Text title;
        Label openedLbl;
        TextField opened;
        Label statusLbl;
        TextField status; // TODO Replace with custom component
        Label idLbl;
        TextField id;
        Button closeBtn;

        TicketDetails(VBox root) {
            title = new Text();
            root.getChildren().add(root);
        }

        void updateLocalizedStrings(ResourceBundle messages) {
            title.setText(messages.getString("TicketForm.TicketDetails.title"));
        }
    }

    private class IncidentLocation {

    }

    private class IncidentDetails {

    }

    private class ReporterDetails {

    }
}
