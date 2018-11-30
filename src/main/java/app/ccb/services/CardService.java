package app.ccb.services;

import java.io.IOException;
public interface CardService {

    Boolean cardsAreImported();

    String readCardsXmlFile() throws IOException;

    String importCards();
}
