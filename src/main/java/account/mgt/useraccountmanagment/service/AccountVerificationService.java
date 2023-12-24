package account.mgt.useraccountmanagment.service;

import account.mgt.useraccountmanagment.model.AccountVerification;

import java.util.List;

public interface AccountVerificationService {
    AccountVerification submitInformation(AccountVerification verification);
    AccountVerification changeAccountState(AccountVerification verification);
    AccountVerification updateInformation(AccountVerification verification);
    Boolean isVerified(AccountVerification verification);

    List<AccountVerification> allAccount();

    AccountVerification searchAccount(AccountVerification accountVerification);

    AccountVerification initializeInformation(AccountVerification verification);
}
