package com.example.farmers.Mail;

import android.content.Intent;
import android.os.AsyncTask;

import android.content.Context;
import android.widget.Toast;

import com.example.farmers.HomeActivity;
import com.example.farmers.NotificationActivity;
import com.example.farmers.Utils.Config;

import java.util.Arrays;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class JavaMailAPI extends AsyncTask<Void, Void, Void> {

    private Context context;

    private Session session;
    private String[] recipientEmails;
    private String subject, message;


    public JavaMailAPI(Context context, String[] recipientEmails, String subject, String message) {
        this.context = context;
        this.recipientEmails = recipientEmails;
        this.subject = subject;
        this.message = message;
    }


    @Override
    protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
                Toast.makeText(context, "Email sent successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, NotificationActivity.class);
                context.startActivity(intent);


    }

    @Override
    protected Void doInBackground(Void... voids) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
            }
        });

//        MimeMessage mimeMessage = new MimeMessage(session);
//        try {
//            mimeMessage.setFrom(new InternetAddress(Config.EMAIL));
//            mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
//            mimeMessage.setSubject(subject);
//            mimeMessage.setText(message);
//            Transport.send(mimeMessage);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
        try {
            // Create message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(Config.EMAIL));

            // Add recipients (multiple emails)
            InternetAddress[] recipientAddresses = new InternetAddress[recipientEmails.length];
            for (int i = 0; i < recipientEmails.length; i++) {
                recipientAddresses[i] = new InternetAddress(recipientEmails[i]);
            }
            mimeMessage.addRecipients(Message.RecipientType.TO, recipientAddresses);

            // Set subject and content
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            // Send email
            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;

    }
}