package app.ccb.services;

import app.ccb.repositories.BankAccountRepository;
import app.ccb.util.Constants;
import app.ccb.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    
    private final BankAccountRepository bankAccountRepository;
    private final FileUtil fileUtil; 

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, FileUtil fileUtil) {
        this.bankAccountRepository = bankAccountRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public Boolean bankAccountsAreImported() {
        return this.bankAccountRepository.count() != 0;
    }

    @Override
    public String readBankAccountsXmlFile() throws IOException {
        return this.fileUtil.readFile(Constants.BANK_ACCOUNTS_PATH_FILA);
    }

    @Override
    public String importBankAccounts() {
        // TODO : Implement Me
        return null;
    }
}
