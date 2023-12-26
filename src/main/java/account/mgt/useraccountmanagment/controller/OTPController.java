package account.mgt.useraccountmanagment.controller;

import account.mgt.useraccountmanagment.model.User;
import account.mgt.useraccountmanagment.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OTPController {

    private final UserServiceImpl userService;

    @Autowired
    public OTPController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @RequestMapping(value ="/validateOtp", method = RequestMethod.POST)
    public String validateOtp(@RequestParam("otpnum") int otpnum, @RequestParam("username") String username, Model model){

        final String SUCCESS = "Entered Otp is valid";
        final String FAIL = "Entered Otp is NOT valid. Please Retry!";
        //Validate the Otp
        if(otpnum >= 0){
            User theUser = userService.searchNonValidateUserByPhone(username);
            if(theUser!=null){
                int serverOtp = theUser.getOtp();
                if(serverOtp > 0){
                    if(otpnum == serverOtp){
                        theUser.setValidated(true);
                        User userObj = userService.validatedUser(theUser);
                        if(userObj !=null){
                            System.out.println(SUCCESS);
                            return "redirect:/user/login";
                        }
                    }
                    else {
                        model.addAttribute("errorMessage",FAIL);
                        model.addAttribute("username",username);
                        return "auth-2-step-verification";
                    }
                }else {
                    model.addAttribute("errorMessage",FAIL);
                    model.addAttribute("username",username);
                    return "auth-2-step-verification";
                }
            }

        }else {
            model.addAttribute("errorMessage",FAIL);
            model.addAttribute("username",username);
            return "auth-2-step-verification";
        }
        model.addAttribute("errorMessage",FAIL);
        model.addAttribute("username",username);
        return "auth-2-step-verification";
    }
}
