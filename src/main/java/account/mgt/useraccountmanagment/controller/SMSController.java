package account.mgt.useraccountmanagment.controller;

import ClickSend.Api.SmsApi;
import ClickSend.ApiClient;
import ClickSend.ApiException;
import ClickSend.Model.SmsMessage;
import ClickSend.Model.SmsMessageCollection;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Arrays;
import java.util.List;

//@Controller
@CrossOrigin("*")
public class SMSController {

    public static final String ACCOUNT_SID = "AC589edd8eb246a03aee781a6c5504ba0e";
    public static final String AUTH_TOKEN = "9afa82db489189b3d2dfbb49f18dde6f";

    public String sendSMS(String phone, String response){
        ApiClient defaultClient = new ApiClient();
        defaultClient.setUsername("jeremie.tuyisenge@yahoo.com");
        defaultClient.setPassword("FD244342-D335-84CF-578B-907DE1FE24B2");
        SmsApi apiInstance = new SmsApi(defaultClient);

        SmsMessage smsMessage=new SmsMessage();
        smsMessage.body(response);
        smsMessage.to(phone);
        smsMessage.source("TEST");

        List<SmsMessage> smsMessageList= Arrays.asList(smsMessage);
        // SmsMessageCollection | SmsMessageCollection model
        SmsMessageCollection smsMessages = new SmsMessageCollection();
        smsMessages.messages(smsMessageList);
        try {
            String result = apiInstance.smsSendPost(smsMessages);
            System.out.println(result);
            return result;
        } catch (ApiException e) {
            System.err.println("Exception when calling SmsApi#smsSendPost");
            e.printStackTrace();
        }
        return "ERROR";
    }
}
