package account.mgt.useraccountmanagment.controller;

import account.mgt.useraccountmanagment.model.AccountVerification;
import account.mgt.useraccountmanagment.model.EAccountStates;
import account.mgt.useraccountmanagment.service.implementation.AccountVerificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountVerificationController {
    private final AccountVerificationServiceImpl verificationService;
    @Autowired
    public AccountVerificationController(AccountVerificationServiceImpl verificationService){
        this.verificationService=verificationService;
    }
    @GetMapping({"/","","/home"})
    public String verificationPage(Model model){
        try{
            model.addAttribute("account",verificationService.allAccount());
//            model.addAttribute("");
            return "accountVerificationPage";
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";
    }

    @PostMapping("/verify")
    public String validateAccount(@RequestParam("id")Long id){
        try{
            AccountVerification theVerification = verificationService.changeAccountState(new AccountVerification(id, EAccountStates.VERIFIED));
            if(theVerification!=null){
                /**
                 * Inform User about validation of account using twillio
                 */
                return "redirect:/account/";
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";

    }
}
