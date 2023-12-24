package account.mgt.useraccountmanagment.controller;

import account.mgt.useraccountmanagment.model.AccountVerification;
import account.mgt.useraccountmanagment.model.EAccountStates;
import account.mgt.useraccountmanagment.service.implementation.AccountVerificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

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

    @GetMapping("/user/document/nid")
    public ResponseEntity<FileSystemResource> getNidDocument(HttpResponse response, @RequestParam("id") Long id){
        try {
            AccountVerification verification = verificationService.searchAccount(new AccountVerification(id));
            if(verification!=null){
                FileSystemResource file = new FileSystemResource("src/main/resources/static/user_document/"+verification.getId()+"_"+verification.getUser().getFirstName()+"_"+verification.getUser().getLastName()+"/"+verification.getNidDocumentName());
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
    @GetMapping("/user/document/passport")
    public ResponseEntity<FileSystemResource> getPassportDocument(HttpResponse response, @RequestParam("id") Long id){
        try {
            AccountVerification verification = verificationService.searchAccount(new AccountVerification(id));
            if(verification!=null){
                FileSystemResource file = new FileSystemResource("src/main/resources/static/user_document/"+verification.getId()+"_"+verification.getUser().getFirstName()+"_"+verification.getUser().getLastName()+"/"+verification.getPassportDocumentName());
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
}
