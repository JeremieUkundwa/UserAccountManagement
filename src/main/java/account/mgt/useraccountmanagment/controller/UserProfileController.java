package account.mgt.useraccountmanagment.controller;

import account.mgt.useraccountmanagment.model.AccountVerification;
import account.mgt.useraccountmanagment.model.EAccountStates;
import account.mgt.useraccountmanagment.model.User;
import account.mgt.useraccountmanagment.service.implementation.AccountVerificationServiceImpl;
import account.mgt.useraccountmanagment.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserProfileController {

    private PasswordEncoder encoder;
    private final UserServiceImpl userService;
    private final AccountVerificationServiceImpl verificationService;
    @Autowired
    public UserProfileController(UserServiceImpl userService, AccountVerificationServiceImpl verificationService) {
        this.userService = userService;
        this.verificationService = verificationService;
    }

    @GetMapping({"","/","/home"})
    public String homePage(Model model){
        model.addAttribute("users",userService.users());
        return "index";
    }

    @GetMapping("/new")
    public String registerUser(Model model){
        model.addAttribute("user",new User());
        return "registrationForm";
    }
    @PostMapping("/new")
    public String registerUser(@ModelAttribute("user")User theUser){
        try{
            LocalDate localDate = LocalDate.now();
            theUser.setAge(theUser.getDateOfBirth().getYear() - localDate.getYear());
            theUser.setPassword(encoder.encode(theUser.getPassword()));
            theUser.setVerified(false);
            User user = userService.registerUser(theUser);
            if(user!=null){
                AccountVerification verification = new AccountVerification();
                verification.setUser(user);
                verification.setStates(EAccountStates.UNVERIFIED);
                AccountVerification theVerification = verificationService.submitInformation(verification);
                return "redirect:/user/";
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";
    }

    @PostMapping("/changePasswordRequest")
    public String changePasswordRequest(@RequestParam("id")Long id , Model model){
        try {
            User theUser = userService.searchById(new User(id));
            if(theUser!=null){
                model.addAttribute("user",theUser);
                return "changePassword";
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";
    }
    @PostMapping("/changePassword")
    public String changePassport(@ModelAttribute("user") User theUser){
        try{
            theUser.setPassword(encoder.encode(theUser.getPassword()));
            User user = userService.changePassword(theUser);
            if(user!=null){
                return "redirect:/user/";
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";
    }

    @PostMapping("/additionData")
    public String additionalPage(@RequestParam("id")Long id,Model model){
        try {
            AccountVerification verification = verificationService.searchAccount(new AccountVerification(id));
            if(verification!=null){
                model.addAttribute("verification",verification);
                return "additionalInformation";
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";
    }

    @PostMapping("/verification/addition")
    public String additionalPage(@ModelAttribute("verification") AccountVerification verification){
        try{

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";
    }
}
