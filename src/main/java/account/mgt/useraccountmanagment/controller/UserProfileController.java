package account.mgt.useraccountmanagment.controller;

import account.mgt.useraccountmanagment.model.*;
import account.mgt.useraccountmanagment.security.UserCustomDetails;
import account.mgt.useraccountmanagment.service.implementation.AccountVerificationServiceImpl;
import account.mgt.useraccountmanagment.service.implementation.OTPServiceImpl;
import account.mgt.useraccountmanagment.service.implementation.RoleServiceImpl;
import account.mgt.useraccountmanagment.service.implementation.UserServiceImpl;
import account.mgt.useraccountmanagment.validation.UserValidation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.apache.commons.logging.*;

@Controller
@RequestMapping("/user")
public class UserProfileController {
//    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

//    private final PasswordEncoder encoder;
    private final OTPServiceImpl otpService;
    private final UserServiceImpl userService;
    private final AccountVerificationServiceImpl verificationService;
    private final RoleServiceImpl roleService;
    private static final Log logger =LogFactory.getLog(UserValidation.class);

    @Autowired
    public UserProfileController( OTPServiceImpl otpService, UserServiceImpl userService, AccountVerificationServiceImpl verificationService, RoleServiceImpl roleService) {

        this.otpService = otpService;
        this.userService = userService;
        this.verificationService = verificationService;
        this.roleService = roleService;
    }

    @GetMapping({"","/","/home"})
    public String homePage(Model model){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                UserCustomDetails userDetails = (UserCustomDetails)authentication.getPrincipal();
                User theUser = userDetails.getUser();
                theUser = userService.searchById(theUser);
                if(theUser!=null){
                    model.addAttribute("user",theUser);
                    model.addAttribute("verification",theUser.getVerification());
                    return "user-profile";
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";
    }

    @GetMapping("/login")
    public String loginPage(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication ==null || authentication instanceof AnonymousAuthenticationToken)
                return "auth-signin";
            else{
                if(!(authentication instanceof AnonymousAuthenticationToken)) {
                    UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();
                    User theUser = userDetails.getUser();
                    if (theUser.getRole().getRoleName().equals("ADMIN"))
                        return "redirect:/account";
                    else
                        return "redirect:/user/";
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";
    }
    @GetMapping("/signUp")
    public String registerUser(Model model){
        model.addAttribute("user",new User());
        model.addAttribute("status", EMaritalStatus.values());
        return "auth-signup";
    }
    @PostMapping("/signUp")
    public String registerUser(@ModelAttribute("user")User theUser, @RequestParam("profile") MultipartFile file, Model model, BindingResult theBindingResult){
        try{
            UserValidation validator = new UserValidation(userService);
            validator.validate(theUser,theBindingResult);
            if(theBindingResult.hasErrors()) {
                FieldError fieldError = theBindingResult.getFieldError();
                logger.debug("Code:" + fieldError.getCode() + ", field:"
                        + fieldError.getField());
                model.addAttribute("status", EMaritalStatus.values());
                return "auth-signup";
            }
            LocalDate localDate = LocalDate.now();
            theUser.setAge(localDate.getYear() - theUser.getDateOfBirth().getYear());
            theUser.setPassword(encoder().encode(theUser.getPassword()));
            theUser.setVerified(false);
            if(!file.isEmpty())
                theUser.setProfilePicture(file.getBytes());
            theUser.setOtp(otpService.generateOTP(theUser.getPhoneNumber()));
            Role theRole = roleService.searchRoleByName("NORMAL");
            theUser.setRole(theRole);
            User user = userService.registerUser(theUser);
            if(user!=null){
                AccountVerification verification = new AccountVerification();
                verification.setUser(user);
                verification.setStates(EAccountStates.UNVERIFIED);
                AccountVerification theVerification = verificationService.initializeInformation(verification);
                if(theVerification!=null)
                    model.addAttribute("username",theUser.getPhoneNumber());
                    model.addAttribute("phone",theUser.getPhoneNumber());
                    String message = "Hi User with "+theUser.getPhoneNumber()+"\n your Generated OTP is: "+theVerification.getUser().getOtp();
                    SMSController smsService = new SMSController();
                    String feedback = smsService.sendSMS(theUser.getPhoneNumber(),message);
                    if(feedback.isEmpty()){
                        return "404";
                    }else{
                        model.addAttribute("login",true);
                        return "auth-2-step-verification";
                    }
//                    return "redirect:/user/";
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";
    }

    /**
     * @implNote method to request user to provide phone used during signup and verify if he/she is owner and provide OTP
     * sent after he will be redirected to reset password
     * @return
     */
    public String checkRequestedResetPasswordUser(){
        try{

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @GetMapping("/resetPassword")
    public String changePasswordPage(){
        try{
            return "auth-boxed-password-reset";
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @PostMapping("/resetPassword")
    public String validateRequestedResetPassword(@RequestParam("phone") String phoneNumber,Model model){
        try{
            User theUser = userService.searchUserByPhone(phoneNumber);
            if(theUser!=null){
                theUser.setOtp(otpService.generateOTP(theUser.getPhoneNumber()));
                theUser = userService.updateOtpUser(theUser);
                if(theUser!=null){
                    model.addAttribute("username",theUser.getPhoneNumber());
                    model.addAttribute("phone",theUser.getPhoneNumber());
                    String message = "Hi User with "+theUser.getPhoneNumber()+"\n your Password Reset OTP is: "+theUser.getOtp();
                    SMSController smsService = new SMSController();
                    String feedback = smsService.sendSMS(theUser.getPhoneNumber(),message);
                    if(feedback.isEmpty()){
                        return "404";
                    }else{
                        model.addAttribute("reset",true);
                        return "auth-2-step-verification";
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
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
    public String changePassport(@ModelAttribute("user") User theUser,BindingResult theBindingResult,Model model){
        try{
            UserValidation validator = new UserValidation(userService);
            validator.validate(theUser,theBindingResult);
            if(theBindingResult.hasErrors()) {
                FieldError fieldError = theBindingResult.getFieldError();
                logger.debug("Code:" + fieldError.getCode() + ", field:"
                        + fieldError.getField());
                model.addAttribute("user",theUser);
                return "auth-signup";
            }
            theUser.setPassword(encoder().encode(theUser.getPassword()));
            User user = userService.changePassword(theUser);
            if(user!=null){
                return "redirect:/user/login";
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
    public String additionalPage(@ModelAttribute("verification") AccountVerification verification,
                                 @RequestParam("nid_document")MultipartFile nidFile,
                                 @RequestParam("passportDoc") MultipartFile passportDoc){
        try{
            if (!nidFile.isEmpty() || !passportDoc.isEmpty()){
                verification.setStates(EAccountStates.PENDING_VERIFICATION);
                verification.setNidDocumentName(nidFile.getOriginalFilename());
                verification.setPassportNumber(passportDoc.getOriginalFilename());
                AccountVerification theVerification = verificationService.submitInformation(verification);
                if(theVerification!=null){
                    boolean isSaved = false;
                    String uploadDir = "src/main/resources/static/user_document/"+theVerification.getId()+theVerification.getUser().getFirstName()+"_"+theVerification.getUser().getLastName();
                    Path uploadPath = Paths.get(uploadDir);
                    if(!Files.exists(uploadPath))
                        Files.createDirectories(uploadPath);
                    if(!nidFile.isEmpty())
                        isSaved = saveDocumentOnDisk(uploadDir,nidFile);
                    else if (!passportDoc.isEmpty())
                        isSaved = saveDocumentOnDisk(uploadDir,passportDoc);
                    if(isSaved){
                        return "redirect:/user/";
                    }
                }
            }else{
                // validating
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";
    }

    private boolean saveDocumentOnDisk(String uploadDir , MultipartFile file) throws IOException{
        try{
            StringBuilder fileNames = new StringBuilder();
            Path fileNamePath = Paths.get(uploadDir,file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNamePath,file.getBytes());
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    @GetMapping("/image/display/{id}")
    public void productImage(@PathVariable("id")Long id, HttpServletResponse response){
        try{
            User theUser = new User(id);
            User userProfile = userService.searchById(theUser);
            if(userProfile != null){
                response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
                response.getOutputStream().write(userProfile.getProfilePicture());
                response.getOutputStream().close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
