package GoogleDriveUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

public class GoogleMailManager  extends GoogleManager{
	private String mPASSWD;
	private String mUSERNAME; //without@gmail.com
	
	private static Gmail _mailService;
	
	public static Gmail getGMailService() throws IOException, MessagingException {
		if (_mailService != null)
		{
			return _mailService;
		}
		
		Credential credential = GoogleManager.getCredentials();
		
		_mailService = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
		//MimeMessage message = createEmail("clement.beze@gmail.com", "clement.beze@gmail.com", "Titre", "Sujet");
		//_mailService.users().messages().send("clement.beze@gmail.com", createMessageWithEmail(message)).execute();
		return _mailService;
	}
	
	public static MimeMessage createEmail(String to,
            String from,
            String subject,
            String bodyText)
		throws MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		MimeMessage email = new MimeMessage(session);
		
		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO,
		new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}


	public static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

}
