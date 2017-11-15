package book.reminder.nandy.bookreminder;

import android.util.Log;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendAttachment {


    public static void main(String email_to, String email_from, String book_name, String content,String pwd) {


            Log.d(" Send attachment", "on main ");
            String to = email_to;
            final String user = email_from;
          final String password=pwd;
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);

            Properties properties = System.getProperties();
            properties.put("mail.smtp.port", "465");
        //    properties.put("mail.smtp.host", "smtp.tamucc.edu");
       properties.put("mail.smtp.host", "smtp.gmail.com");
          // properties.put("mail.smtp.starttls.enable", "true");

            properties.put("mail.smtp.socketFactory.port", "465");
            properties.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", "true");
            //  properties.put("mail.smtp.port", "465");
            Log.d(" Send attachment", "after prop");
     //  javax.mail.Session session = Session.getDefaultInstance(properties);
  Session session= Session.getDefaultInstance(properties,new javax.mail.Authenticator()
       {
         protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
              return new javax.mail.PasswordAuthentication(user, password);
         }
      });
            Log.d(" Send attachment", "after javax");
            try {
                Log.d(" Send attachment", "in try prop");
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(user));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                Log.d(" Send attachment", "after inter address");
                message.setSubject("Book Reminder");
                BodyPart messageBodyPart1 = new MimeBodyPart();
               messageBodyPart1.setText("This is to remind that, the due date to return the book has arrived. Please return the book as soon as possible \n\n " +
                       "Book name: " + book_name + "\n\n " + content);
                Log.d(" Send attachment", "after report address");
                Log.d(" Send attachment", "before multi part");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart1);
                Log.d(" Send attachment", "before multi part 1");
                message.setContent(multipart);
                Log.d(" Send attachment", "before multi part 2");
                Transport.send(message);
                Log.d(" Send attachment", "before multi part 3");
                //      System.out.println("MESSAGE SENT....");
            } catch (MessagingException ex)

            {
                Log.e("error", "Message not sent");
                ex.printStackTrace();
            }
        }
    }

