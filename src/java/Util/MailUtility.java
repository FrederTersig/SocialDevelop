package Util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class MailUtility
{
  public static void sendMail (String mitt, String oggetto, String testoEmail)
      throws MessagingException
  {
    // Creazione di una mail session
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    	props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
    Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("claudio.more94@gmail.com","claudio18081994");
				}
			});

    // Creazione del messaggio da inviare
    MimeMessage message = new MimeMessage(session);
    message.setSubject(oggetto);
    message.setContent("<p>testo:" + testoEmail + "</p><br><p>mittente:" + mitt + "</p>", "text/html; charset=utf-8");

    // Aggiunta degli indirizzi del mittente e del destinatario
   
  
    message.setFrom(new InternetAddress(mitt));
    message.setRecipient(Message.RecipientType.TO,new InternetAddress("claudio.more94@gmail.com"));

    // Invio del messaggio
    Transport.send(message);
  }
}