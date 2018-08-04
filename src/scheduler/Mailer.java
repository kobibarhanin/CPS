package scheduler;

import java.io.File;
import java.nio.file.Files;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Main Responsibility: Building and sending of attached file through email
 * @author itayguy
 */
public final class Mailer implements Runnable {
	private final static String USERNAME = "se.haifa.final.2017@gmail.com"; //smtp server using fake accout in gmail with IMAP protocol settings //change accordingly
	private final static String PASSWORD = "designpattern"; //its fake password //change accordingly
	private final static String HOST = "smtp.gmail.com";
	private final static String PORT = "587";
	private static final String DEFAULT_FROM = "se.haifa.final.2017@gmail.com";
	private static final String DEFAULT_ADDR = "itgu1010@gmail.com";
	private static final String DEFAULT_SAY_HEY = "Hello From CPS";
	private static final String DEFAULT_MSG_TITLE = "Reservation Exceeding";
	private String sayHey;
	private String msgTitle;
	private File report;
	private String to;
	private String[] cc;
	
	public Mailer(File attachedFile,String to) {
		this(Mailer.DEFAULT_SAY_HEY,Mailer.DEFAULT_MSG_TITLE,attachedFile,to);
	}

	public Mailer(String sayHey, String msgTitle,File report,String to,String... cc) {
		this.sayHey = sayHey;
		this.msgTitle = msgTitle;
		this.report = report;
		this.to = to;
		if(cc != null) {
			this.cc = cc;
		}
	}

	/**
	 * Main Responsibility: Build an email message
	 * @param from			email address to send from
	 * @param sayHey		header title
	 * @param msgTitle		message title
	 * @param filename		file to be attached
	 * @param to			list of email addresses to send to
	 */
	public void send() {
		(new Thread(this)).start();
	}
	
	@Override
	public void run() {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", Mailer.HOST);
		props.put("mail.smtp.port",Mailer.PORT);

		// Get the Session object.
		Session session = Session.getInstance(props,new javax.mail.Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Mailer.USERNAME, Mailer.PASSWORD);
	        }
		});

		try {
			Message message = new MimeMessage(session); // Create a default MimeMessage object
			message.setFrom(new InternetAddress(Mailer.DEFAULT_FROM)); // Set From: header field of the header -> if this will be set, specific email address has to be written
			String prim = "";
			if(this.to == null) {
				prim = Mailer.DEFAULT_ADDR;
			} else {
				prim = this.to;
			}
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(prim)); // Set To: header field of the header
			if(this.cc != null) {
				for(int i = 1;i < this.cc.length;i++) {
					message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(this.cc[i].toString()));
				}
			}
			message.setSubject(this.msgTitle); // Set Subject: header field
			BodyPart messageBodyPart = new MimeBodyPart(); // Create the message part
			messageBodyPart.setText(this.sayHey); // Now set the actual message
			Multipart multipart = new MimeMultipart(); // Create a multipar message
			multipart.addBodyPart(messageBodyPart); // Set text message part
			messageBodyPart = new MimeBodyPart(); // Part two is attachment
			DataSource source = null;
			if(this.report != null && Files.exists(this.report.toPath())) {
				source = new FileDataSource(this.report.toString()); // for example: filename = "C:\\Users\\itayguy\\Desktop\\alloc.html";
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(this.report.getName().toString());
			} else {
				messageBodyPart.setText("attached file is not exist");
			}
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart); // Send the complete message parts
	       	Transport.send(message); // Send message
			String desList = "[";
			desList += this.to;
			if(this.cc != null) {
				for(int i = 0;i < this.cc.length;i++) {
					desList += this.cc[i];
					if(i < this.cc.length-1) {
						desList += ",";
					}
				}	
			}
			desList += "]";
	       	System.out.println("mail is sent to: " + desList);
		} catch(Exception ex) {
			System.out.println("could not send an email through some problems.");
		}
	}
}