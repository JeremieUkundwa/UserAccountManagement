package account.mgt.useraccountmanagment.controller;

import account.mgt.useraccountmanagment.service.implementation.AccountVerificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountVerificationController {
    private final AccountVerificationServiceImpl verificationService;
    @Autowired
    public AccountVerificationController(AccountVerificationServiceImpl verificationService){
        this.verificationService=verificationService;
    }

    @PostMapping("/verify")
    public String validateAccount(@RequestParam("id")Long id){
        try{

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";

    }
}
