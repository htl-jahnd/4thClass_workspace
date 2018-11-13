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

    private String from;
    private String fromPwd;
    private String[] to;
    private final String PORT = "587";
    private final String HOST = "smtp.gmail.com";
    private String subject;
    private String content;
    private final String contentType = "text/html";
    private final String transportProtocol = "smtp";
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

    public GMailer(String from, String fromPwd, String[] to, String subject, String content)
    {
	super();
	this.from = from;
	this.fromPwd = fromPwd;
	this.to = to;
	this.subject = subject;
	this.content = content;
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
	System.out.println("\n 1st ===> setup Mail Server Properties..");
	mailServerProperties = System.getProperties();
	mailServerProperties.put("mail.smtp.port", PORT);
	mailServerProperties.put("mail.smtp.auth", "true");
	mailServerProperties.put("mail.smtp.starttls.enable", "true");
	System.out.println("Mail Server Properties have been setup successfully..");

	// Step2
	System.out.println("\n\n 2nd ===> get Mail Session..");
	getMailSession = Session.getDefaultInstance(mailServerProperties, null);
	generateMailMessage = new MimeMessage(getMailSession);
	generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("david.jahn2000@gmail.com"));
	generateMailMessage.setSubject(subject);

	generateMailMessage.setContent(content, contentType);
	System.out.println("Mail Session has been created successfully..");

	// Step3
	System.out.println("\n\n 3rd ===> Get Session and Send mail");
	Transport transport = getMailSession.getTransport(transportProtocol);

	// Enter your correct gmail UserID and Password
	// if you have 2FA enabled then provide App Specific Password
	transport.connect(HOST, from, fromPwd);
	transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
	transport.close();
    }

    public GMailer(String from, String fromPWd, String[] to)
    {
	this(from, fromPWd, to, "Default Subject", "Default Body");
    }

    public String getFrom()
    {
	return from;
    }

    public void setFrom(String from)
    {
	this.from = from;
    }

    public String getFromPwd()
    {
	return fromPwd;
    }

    public void setFromPwd(String fromPwd)
    {
	this.fromPwd = fromPwd;
    }

    public String[] getTo()
    {
	return to;
    }

    public void setTo(String[] to)
    {
	this.to = to;
    }

    public String getSubject()
    {
	return subject;
    }

    public void setSubject(String subject)
    {
	this.subject = subject;
    }

    public String getContent()
    {
	return content;
    }

    public void setContent(String content)
    {
	this.content = content;
    }

}
