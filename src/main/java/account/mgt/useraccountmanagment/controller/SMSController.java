package account.mgt.useraccountmanagment.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.web.bind.annotation.CrossOrigin;

//@Controller
@CrossOrigin("*")
public class SMSController {

    public static final String ACCOUNT_SID = "AC589edd8eb246a03aee781a6c5504ba0e";
    public static final String AUTH_TOKEN = "9afa82db489189b3d2dfbb49f18dde6f";

    public String sendSMS(String phone, String response){
        if(!phone.startsWith("+"))
            phone="+"+phone;
//        System.out.println("TWILLIO Phone: "+phone);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(phone),
                        new com.twilio.type.PhoneNumber("Test"),
                        response)
                .create();
        System.out.println(message.getSid());
        return message.getSid();
    }
}
