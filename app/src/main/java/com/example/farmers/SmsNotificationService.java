//package com.example.farmers;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//
//public class SmsNotificationService {
//
//    public static final String ACCOUNT_SID = "your_account_sid";
//    public static final String AUTH_TOKEN = "your_auth_token";
//
//    public static void sendSms(String toPhoneNumber, String messageBody) {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
//        Message message = Message.creator(
//                        new PhoneNumber(toPhoneNumber),
//                        new PhoneNumber("your_twilio_number"),
//                        messageBody)
//                .create();
//
//        System.out.println("Message Sent: " + message.getSid());
//    }
//}
//
