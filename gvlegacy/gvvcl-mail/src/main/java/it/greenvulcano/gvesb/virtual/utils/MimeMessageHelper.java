/*******************************************************************************
 * Copyright (c) 2009, 2016 GreenVulcano ESB Open Source Project.
 * All rights reserved.
 *
 * This file is part of GreenVulcano ESB.
 *
 * GreenVulcano ESB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * GreenVulcano ESB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with GreenVulcano ESB. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package it.greenvulcano.gvesb.virtual.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.codec.binary.Base64;

public class MimeMessageHelper {

	private enum MessageType {TEXT, HTML};

	private final MimeMessage mimeMessage;	
	private final Set<BodyPart> attachments = new LinkedHashSet<>();

	private String messageBody = "";
	private MessageType messageType = MessageType.TEXT;

	private MimeMessageHelper(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public static MimeMessageHelper createEmailMessage(String from, String to) throws AddressException, MessagingException, IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		email.setFrom(new InternetAddress(from));
		email.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		return new MimeMessageHelper(email);
	}
	
	public MimeMessageHelper setSubject(String subject) throws MessagingException {
		mimeMessage.setSubject(subject);
		return this;
	}

	public MimeMessageHelper addTo(String to) throws AddressException, MessagingException {
		mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		return this;
	}

	public MimeMessageHelper addCC(String cc) throws AddressException, MessagingException {
		mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
		return this;
	}

	public MimeMessageHelper addBCC(String bcc) throws AddressException, MessagingException {
		mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
		return this;
	}

	public MimeMessageHelper setTextBody(String body) throws MessagingException {
		messageBody = body;
		messageType = MessageType.TEXT;
		return this;
	}

	public MimeMessageHelper setHtmlBody(String body) throws MessagingException {
		messageBody = body;
		messageType = MessageType.HTML;
		return this;
	}

	public MimeMessageHelper addAttachment(String name, String type, String content) throws MessagingException {
		
		BodyPart attachmentPart = new MimeBodyPart();
		attachmentPart.setFileName(name);
		attachmentPart.setContent(content, type);
		attachmentPart.setDisposition(Part.ATTACHMENT);
		attachmentPart.setHeader("Content-Transfer-Encoding", "base64");
		attachments.add(attachmentPart);
		return this;		
	}

	public MimeMessage getMimeMessage() throws MessagingException {

		if (attachments.isEmpty()) {
			switch (messageType) {
			case HTML:
				mimeMessage.setContent(messageBody, "text/html; charset=utf-8");	
				break;

			case TEXT:
				mimeMessage.setText(messageBody, "UTF-8");
				break;
			}
		} else {

			MimeBodyPart mimeBodyPart = new MimeBodyPart();

			switch (messageType) {
			case HTML:
				mimeBodyPart.setContent(messageBody, "text/html; charset=utf-8");	
				break;

			case TEXT:
				mimeBodyPart.setText(messageBody, "UTF-8");
				break;
			}
			
			mimeBodyPart.setDisposition(Part.INLINE);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			for (BodyPart attachmentPart : attachments) {				
				multipart.addBodyPart(attachmentPart);
			}

			mimeMessage.setContent(multipart);
		}

		return mimeMessage;
	}

	public String getEncodedMimeMessage() throws IOException, MessagingException {

		ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
		getMimeMessage().writeTo(buffer);

		String encodedEmail = Base64.encodeBase64URLSafeString(buffer.toByteArray());

		return encodedEmail;
	}
}