package app.ccb.services;

import java.io.IOException;
public interface BankAccountService {

    Boolean bankAccountsAreImported();

    String readBankAccountsXmlFile() throws IOException;

    String importBankAccounts();
}
