package account.mgt.useraccountmanagment.controller;

import account.mgt.useraccountmanagment.model.AccountVerification;
import account.mgt.useraccountmanagment.model.EAccountStates;
import account.mgt.useraccountmanagment.model.User;
import account.mgt.useraccountmanagment.security.UserCustomDetails;
import account.mgt.useraccountmanagment.service.implementation.AccountVerificationServiceImpl;
import account.mgt.useraccountmanagment.service.implementation.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.List;

@CrossOrigin("*")
@Controller
@RequestMapping("/account")
public class AccountVerificationController {
    private final AccountVerificationServiceImpl verificationService;
    private final UserServiceImpl userService;
    @Autowired
    public AccountVerificationController(AccountVerificationServiceImpl verificationService, UserServiceImpl userService){
        this.verificationService=verificationService;
        this.userService=userService;
    }
    @GetMapping({"/","","/home"})
    public String verificationPage(Model model){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                UserCustomDetails userDetails = (UserCustomDetails)authentication.getPrincipal();
                User theUser = userDetails.getUser();
                theUser = userService.searchById(theUser);
                if(theUser!=null){
                    model.addAttribute("accounts",verificationService.allNormalUserAccount());
                    model.addAttribute("user",theUser);
                    return "admin/home";
                }
            }
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
                User theUser = theVerification.getUser();
                theUser.setVerified(true);
                theUser = userService.updateUser(theUser);
                /**
                 * Inform User about validation of account using twillio
                 */
                String message = "Hi User with "+theUser.getPhoneNumber()+"\n your Account is Verified";
                SMSController smsService = new SMSController();
                String feedback = smsService.sendSMS(theUser.getPhoneNumber(),message);
                if(feedback.isEmpty()){
                    return "404";
                }else{
                    return "redirect:/account/";
                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "404";

    }

    @GetMapping("/user/document/nid/{id}")
    public ResponseEntity<FileSystemResource> getNidDocument(@PathVariable("id") Long id){
        try {
            AccountVerification verification = verificationService.searchAccount(new AccountVerification(id));
            if(verification!=null){
                FileSystemResource file = new FileSystemResource("src/main/resources/static/user_document/"+verification.getId()+verification.getUser().getFirstName()+"_"+verification.getUser().getLastName()+"/"+verification.getNidDocumentName());
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + verification.getNidDocumentName());
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
                headers.add(HttpHeaders.LOCATION, "/data");
                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .body(file);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    @GetMapping("/user/document/passport/{id}")
    public ResponseEntity<FileSystemResource> getPassportDocument(HttpResponse response, @PathVariable("id") Long id){
        try {
            AccountVerification verification = verificationService.searchAccount(new AccountVerification(id));
            if(verification!=null){
                FileSystemResource file = new FileSystemResource("src/main/resources/static/user_document/"+verification.getId()+verification.getUser().getFirstName()+"_"+verification.getUser().getLastName()+"/"+verification.getPassportDocumentName());
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + verification.getPassportDocumentName());
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
                headers.add(HttpHeaders.LOCATION, "/data");
                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .body(file);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

//    @GetMapping("/user/document/nid/{id}")
//    public ResponseEntity<InputStreamResource> getNidDocument(@PathVariable("id") Long id) {
//        try {
//            AccountVerification verification = verificationService.searchAccount(new AccountVerification(id));
//            if (verification != null) {
//                String filePath = "src/main/resources/static/user_document/"
//                        + verification.getId() + verification.getUser().getFirstName() + "_"
//                        + verification.getUser().getLastName() + "/" + verification.getNidDocumentName();
//
//                InputStream inputStream = new FileInputStream(filePath);
//
//                HttpHeaders headers = new HttpHeaders();
//                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=file.pdf");
//
//                headers.setContentType(MediaType.APPLICATION_PDF);
//
//                return ResponseEntity
//                        .ok()
//                        .headers(headers)
//                        .body(new InputStreamResource(inputStream));
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            // Handle the exception, for example, return an error response
//            return ResponseEntity
//                    .status(500)
//                    .body(null);
//        }
//
//        // Return a 404 Not Found response if the verification is not found
//        return ResponseEntity
//                .notFound()
//                .build();
//    }

}
