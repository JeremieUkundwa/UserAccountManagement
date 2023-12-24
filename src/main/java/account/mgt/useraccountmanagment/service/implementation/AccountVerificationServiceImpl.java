package account.mgt.useraccountmanagment.service.implementation;

import account.mgt.useraccountmanagment.model.AccountVerification;
import account.mgt.useraccountmanagment.repository.AccountVerificationRepository;
import account.mgt.useraccountmanagment.service.AccountVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountVerificationServiceImpl implements AccountVerificationService {

    private final AccountVerificationRepository repo;
    @Autowired
    public AccountVerificationServiceImpl(AccountVerificationRepository repo) {
        this.repo = repo;
    }
    @Override
    public AccountVerification submitInformation(AccountVerification verification) {
        return repo.save(verification);
    }

    @Override
    public AccountVerification changeAccountState(AccountVerification verification) {
        AccountVerification theVerification = repo.findById(verification.getId()).get();
        if(theVerification!=null){
            theVerification.setStates(verification.getStates());
            return repo.save(theVerification);
        }
        return null;
    }

    @Override
    public AccountVerification updateInformation(AccountVerification verification) {
        AccountVerification theVerification = repo.findById(verification.getId()).get();
        if(theVerification!=null){
            theVerification.setNid(verification.getNid());
            theVerification.setPassportNumber(verification.getPassportNumber());
            theVerification.setStates(verification.getStates());
            return repo.save(theVerification);
        }
        return null;
    }

    @Override
    public Boolean isVerified(AccountVerification verification) {
        return repo.isVerified(verification);
    }
}
