/**
 * Email.java
 *
 * This class defines methods to send a file via email
 * 
 *@author Ronny Z. Suero
 * */
package com.utelecard.autoreport.plugin.utils;

import java.security.Security;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPTransport;

public class Email 
{
	private final MimeMessage msg;
	private Multipart multipart;
	private final Properties props;
	private final Session session;
	private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	/**
	 * class constructor
	 * 
	 * @param to the to parameter defines the list of people to whom send the mail
	 * @param from the from parameter defines who sent the mail
	 * @param subject the URL parameter defines defines the issue that will have mail
	 * */
	@SuppressWarnings("restriction")
	public Email(final List<String> to, final String from, final String subject) 
	{
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		this.props = System.getProperties();
		this.props.setProperty("mail.smtps.host", "smtp.gmail.com");
		this.props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		this.props.setProperty("mail.smtp.socketFactory.fallback", "false");
		this.props.setProperty("mail.smtp.port", "465");
		this.props.setProperty("mail.smtp.socketFactory.port", "465");
		this.props.setProperty("mail.smtps.auth", "true");
		this.props.put("mail.smtps.quitwait", "false");

		this.session = Session.getInstance(props, null);
		this.msg = new MimeMessage(session);

		try {
			msg.setFrom(new InternetAddress(from));

			for (final String m : to) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						m));
			}
			msg.setSubject(subject);
			
			this.multipart = new MimeMultipart();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method add the files to attach
	 * @param attachment the attachment parameter defines the list of files to send in the mail 
	 * @return void
	 * */
	public void setAttachment(final String[] attachment)
	{
		try{		
			for (final String attachFile : attachment) {
				final MimeBodyPart attachPart = new MimeBodyPart();
				attachPart.attachFile(attachFile);
				this.multipart.addBodyPart(attachPart);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method add the content to the body of the mail
	 * @param content the content parameter defines the content to the body of the mail
	 * @return void
	 * */
	public void setContent(String content)
	{
		try {
			final MimeBodyPart attachPart = new MimeBodyPart();
			attachPart.setContent(content, "text/html");
			this.multipart.addBodyPart(attachPart);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method send the mail
	 * @param userName the userName parameter defines the user of the mail sender
	 * @param password the password parameter defines the password of the mail sender
	 * @return void
	 * */
	public void sendMail(String username, String password)
	{
		try
		{
			this.msg.setContent(multipart);
			this.msg.setSentDate(new Date());

			final SMTPTransport t = (SMTPTransport) session
					.getTransport("smtps");
			t.connect("smtp.gmail.com", username, password);
			t.sendMessage(msg, msg.getAllRecipients());
			t.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}