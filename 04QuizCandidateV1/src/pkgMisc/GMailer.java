package pkgMisc;

import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.scene.control.TextInputDialog;

public class GMailer
{

    private String senderAddress;
    private String senderPassword;
    private String[] toAddresses;
    private static final String PORT = "587";
    private static final String HOST = "smtp.gmail.com";
    private String mailSubject;
    private String mailContent;
    private static final String CONTENT_TYPE = "text/html";
    private static final String TRANSPORT_PROTOCOL = "smtp";
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

    public GMailer(String from, String fromPwd, String[] to, String subject, String content)
    {
	super();
	this.senderAddress = from;
	this.senderPassword = fromPwd;
	this.toAddresses = to;
	this.mailSubject = subject;
	this.mailContent = content;
    }

    static public String showEmailInputDialog()
    {
	String ret = null;
	TextInputDialog dialog = new TextInputDialog("max.musterman@gmail.com");
	dialog.setTitle("Email input");
	dialog.setHeaderText("Enter your email");
	dialog.setContentText("Please enter your email addres: ");
	Optional<String> result = dialog.showAndWait();
	if (result.isPresent())
	{
	    ret = result.get();
	}
	return ret;
    }

    public void sendMail() throws AddressException, MessagingException
    {
	// Step1
	mailServerProperties = System.getProperties();
	mailServerProperties.put("mail.smtp.port", PORT);
	mailServerProperties.put("mail.smtp.auth", "true");
	mailServerProperties.put("mail.smtp.starttls.enable", "true");

	// Step2
	getMailSession = Session.getDefaultInstance(mailServerProperties, null);
	generateMailMessage = new MimeMessage(getMailSession);
	for (int i = 0; i < toAddresses.length; i++)
	{
	    generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddresses[i]));
	}
	generateMailMessage.setSubject(mailSubject);

	generateMailMessage.setContent(mailContent, CONTENT_TYPE);

	// Step3
	Transport transport = getMailSession.getTransport(TRANSPORT_PROTOCOL);

	// Enter your correct gmail UserID and Password
	transport.connect(HOST, senderAddress, senderPassword);
	transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
	transport.close();
    }

    public GMailer(String from, String fromPWd, String[] to)
    {
	this(from, fromPWd, to, "Default Subject", "Default Body");
    }

    public String getFrom()
    {
	return senderAddress;
    }

    public void setFrom(String from)
    {
	this.senderAddress = from;
    }

    public String getFromPwd()
    {
	return senderPassword;
    }

    public void setFromPwd(String fromPwd)
    {
	this.senderPassword = fromPwd;
    }

    public String[] getTo()
    {
	return toAddresses;
    }

    public void setTo(String[] to)
    {
	this.toAddresses = to;
    }

    public String getSubject()
    {
	return mailSubject;
    }

    public void setSubject(String subject)
    {
	this.mailSubject = subject;
    }

    public String getContent()
    {
	return mailContent;
    }

    public void setContent(String content)
    {
	this.mailContent = content;
    }

}
