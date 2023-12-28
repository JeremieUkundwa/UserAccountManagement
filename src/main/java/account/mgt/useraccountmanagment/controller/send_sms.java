package account.mgt.useraccountmanagment.controller;

import ClickSend.Api.SmsApi;
import ClickSend.ApiClient;
import ClickSend.ApiException;
import ClickSend.Model.SmsMessage;
import ClickSend.Model.SmsMessageCollection;

import java.util.Arrays;
import java.util.List;

public class send_sms {
    public static void main(String[] args) {
        ApiClient defaultClient = new ApiClient();
        defaultClient.setUsername("jeremie.tuyisenge@yahoo.com");
        defaultClient.setPassword("FD244342-D335-84CF-578B-907DE1FE24B2");
        SmsApi apiInstance = new SmsApi(defaultClient);

        SmsMessage smsMessage=new SmsMessage();
        smsMessage.body("Hello Test Java Then and elimination of Delay without code");
        smsMessage.to("0784688953");
        smsMessage.source("TEST");

        List<SmsMessage> smsMessageList=Arrays.asList(smsMessage);
        // SmsMessageCollection | SmsMessageCollection model
        SmsMessageCollection smsMessages = new SmsMessageCollection();
        smsMessages.messages(smsMessageList);
        try {
            String result = apiInstance.smsSendPost(smsMessages);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling SmsApi#smsSendPost");
            e.printStackTrace();
        }
    }
}
