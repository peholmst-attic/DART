package net.pkhapps.dart.server.certificatemanager.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import fi.jasoft.qrcode.QRCode;
import net.pkhapps.dart.server.certificatemanager.domain.CSRToken;
import net.pkhapps.dart.server.certificatemanager.domain.CSRTokenService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@SpringUI
@Theme(ValoTheme.THEME_NAME)
@Widgetset("net.pkhapps.dart.server.certificatemanager.CertificateManager")
public class CertificateManagerUI extends UI {

    @Autowired
    CSRTokenService csrTokenService;

    private Label csrTokenHash;
    private Label validUntil;
    private QRCode qrCode;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        setContent(layout);

        final Label title = new Label("DART Certificate Manager");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        addAndCenter(title, layout);

        getPage().setTitle("DART Certificate Manager");

        final Button generateCsrToken = new Button("Generate new CSR Token", this::generateCsrToken);
        addAndCenter(generateCsrToken, layout);

        csrTokenHash = new Label("No token generated");
        csrTokenHash.setSizeUndefined();
        csrTokenHash.addStyleName(ValoTheme.LABEL_HUGE);
        addAndCenter(csrTokenHash, layout);

        qrCode = new QRCode();
        qrCode.setWidth(200, Unit.PIXELS);
        qrCode.setHeight(200, Unit.PIXELS);
        qrCode.setVisible(false);
        addAndCenter(qrCode, layout);

        validUntil = new Label();
        validUntil.setSizeUndefined();
        validUntil.setVisible(false);
        validUntil.setContentMode(ContentMode.HTML);
        addAndCenter(validUntil, layout);
    }

    private void addAndCenter(Component component, AbstractOrderedLayout layout) {
        layout.addComponent(component);
        layout.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
    }

    private void generateCsrToken(Button.ClickEvent event) {
        CSRToken token = csrTokenService.generateNewToken();
        csrTokenHash.setValue(formatHash(token.getHash()));
        qrCode.setValue(token.getHash());
        qrCode.setVisible(true);
        validUntil.setValue(String.format("Valid until <strong>%s</strong>.", ZonedDateTime.ofInstant(token.getExpirationTime(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm:ss z"))));
        validUntil.setVisible(true);
    }

    private static String formatHash(String hash) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hash.length(); ++i) {
            if (i > 0 && i % 8 == 0) {
                sb.append(" ");
            }
            sb.append(Character.toUpperCase(hash.charAt(i)));
        }
        return sb.toString();
    }
}
