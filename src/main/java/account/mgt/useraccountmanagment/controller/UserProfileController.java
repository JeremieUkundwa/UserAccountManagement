package account.mgt.useraccountmanagment.controller;

import account.mgt.useraccountmanagment.model.AccountVerification;
import account.mgt.useraccountmanagment.model.EAccountStates;
import account.mgt.useraccountmanagment.model.EMaritalStatus;
import account.mgt.useraccountmanagment.model.User;
import account.mgt.useraccountmanagment.service.implementation.AccountVerificationServiceImpl;
import account.mgt.useraccountmanagment.service.implementation.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        model.addAttribute("status", EMaritalStatus.values());
        return "registrationForm";
    }
    @PostMapping("/new")
    public String registerUser(@ModelAttribute("user")User theUser, @RequestParam("profile") MultipartFile file){
        try{
            LocalDate localDate = LocalDate.now();
            theUser.setAge(theUser.getDateOfBirth().getYear() - localDate.getYear());
            theUser.setPassword(encoder.encode(theUser.getPassword()));
            theUser.setVerified(false);
            if(!file.isEmpty())
                theUser.setProfilePicture(file.getBytes());
            User user = userService.registerUser(theUser);
            if(user!=null){
                AccountVerification verification = new AccountVerification();
                verification.setUser(user);
                verification.setStates(EAccountStates.UNVERIFIED);
                AccountVerification theVerification = verificationService.initializeInformation(verification);
                if(theVerification!=null)
                    return "redirect:/user/";
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

    /**
     *
     * @param id
     * @param model
     * @return
     */

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
