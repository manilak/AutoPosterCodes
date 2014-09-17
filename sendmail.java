import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//import com.google.protobuf.Message;
//import javax.mail.Message.*;
//import javax.activation.*;



public class Sendmail {
	public static void sendmail1(String msg) {

		System.out.println(msg + ". Sending email to poster.favista@gmail.com...");
		final String uname = "poster.favista@gmail.com";
		final String password = "poster!@#$";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		new Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {

		return new PasswordAuthentication(uname, password);
		}
		});

		try {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("manila.k@gmail.com"));
		message.setRecipients(MimeMessage.RecipientType.TO,
		InternetAddress.parse("manila.k@favista.com"));
		message.setSubject("Autoposter sendmail testing");
		message.setText("" + msg);

		Transport.send(message);

		System.out.println("Done");

		} catch (MessagingException e) {
		   StringWriter sw=new StringWriter();
		   e.printStackTrace(new PrintWriter(sw));
		   String stacktrace=sw.toString();
			throw new RuntimeException(e);
		
		}

		
		}
	public static void main(String[] args) {
		
		String msg="Sending mail from autoposter";
        Sendmail.sendmail1(msg);
}
}
