package account.mgt.useraccountmanagment.service;

import account.mgt.useraccountmanagment.model.AccountVerification;

public interface AccountVerificationService {
    AccountVerification submitInformation(AccountVerification verification);
    AccountVerification changeAccountState(AccountVerification verification);
    AccountVerification updateInformation(AccountVerification verification);
    Boolean isVerified(AccountVerification verification);
}
