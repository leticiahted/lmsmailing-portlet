package com.tls.liferaylms.mail.message;

import java.util.Date;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.mail.Account;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.util.mail.LiferayMimeMessage;
import com.liferay.util.mail.MailEngine;
import com.tls.liferaylms.mail.model.AuditSendMails;
import com.tls.liferaylms.mail.service.AuditSendMailsLocalServiceUtil;

public class LmsMailMessageListener implements MessageListener {
	private static Log log = LogFactoryUtil.getLog(LmsMailMessageListener.class);

	@Override
	public void receive(Message message) {
		// TODO Auto-generated method stub

		try {
			doReceive(message);
		} catch (Exception e) {
			_log.error("Unable to process message " + message, e);
		}
	}

	protected void doReceive(Message message) throws Exception {
		String subject = message.getString("subject");
		String body = message.getString("body");
		long groupId = message.getLong("groupId");
		long userId = message.getLong("userId");
		String testing = message.getString("testing");
		
		String portal = message.getString("portal");
		String community = message.getString("community");
		
		String url = message.getString("url");
		String urlcourse = message.getString("urlcourse");
		
		String templateId = message.getString("templateId");
		
		String toMail = message.getString("to");
		String userName = message.getString("userName");
		
		User sender = UserLocalServiceUtil.getUser(userId);
		Group scopeGroup = GroupLocalServiceUtil.getGroup(groupId);
		long companyId = scopeGroup.getCompanyId();
		
		String fromName = PrefsPropsUtil.getString(companyId,
				PropsKeys.ADMIN_EMAIL_FROM_NAME);
		String fromAddress = PrefsPropsUtil.getString(companyId,
				PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);
		
		long nUsers = 0,millis = 0;
		String numberUsers = PrefsPropsUtil.getString("lmsmailing.sendmails.number.users");
		String milliseconds = PrefsPropsUtil.getString("lmsmailing.sendmails.wating.time.milliseconds");
		
		if(numberUsers != null && !numberUsers.equals("")){
			nUsers = Long.valueOf(numberUsers);
		}
		
		if(milliseconds != null && !milliseconds.equals("")){
			millis = Long.valueOf(milliseconds);
		}
		
		InternetAddress from = new InternetAddress(fromAddress, fromName);
		
		//System.out.print("toMail: "+toMail+", userName: "+userName);
		
		if ("true".equals(testing)) {
			if(_log.isDebugEnabled())_log.debug("Test");
			InternetAddress to = new InternetAddress(sender.getEmailAddress(),
					sender.getFullName());
			
			body = createMessage(body, portal, community, sender.getFullName(), UserLocalServiceUtil.getUserById(userId).getFullName(),url,urlcourse);

			String calculatedBody = LanguageUtil.get(Locale.getDefault(),"mail.header");
			calculatedBody += createMessage(body, portal, community, sender.getFullName(), UserLocalServiceUtil.getUserById(userId).getFullName(),url,urlcourse);
			calculatedBody += LanguageUtil.get(Locale.getDefault(),"mail.footer");
			
			subject = createMessage(subject, portal, community, sender.getFullName(), UserLocalServiceUtil.getUserById(userId).getFullName(),url,urlcourse);
			
			MailMessage mailm = new MailMessage(from, to, subject, calculatedBody, true);
			MailServiceUtil.sendEmail(mailm);
		} 
		else if(toMail != null && userName != null && !toMail.contains("all")){
			if(_log.isDebugEnabled())_log.debug("User");
			User userSender = UserLocalServiceUtil.getUserById(userId);
			User user = UserLocalServiceUtil.getUserByEmailAddress(userSender.getCompanyId(), toMail);
			if(user!=null && user.isActive()){
				
				InternetAddress to = new InternetAddress(toMail, userName);

				if(_log.isDebugEnabled())_log.debug("Language::"+user.getLocale());
				String calculatedBody = LanguageUtil.get(user.getLocale(),"mail.header");
				calculatedBody += createMessage(body, portal, community, userName, UserLocalServiceUtil.getUserById(userId).getFullName(),url,urlcourse);
				calculatedBody += LanguageUtil.get(user.getLocale(),"mail.footer");
				
				String calculatedsubject = createMessage(subject, portal, community, userName, UserLocalServiceUtil.getUserById(userId).getFullName(),url,urlcourse);
				
				if(log.isDebugEnabled()){
					log.debug("\n----------------------");
					log.debug(" from: "+from);
					log.debug(" to: "+toMail + " "+userName);
					log.debug(" subject: "+calculatedsubject);
					log.debug(" body: \n"+calculatedBody);
					log.debug("----------------------");
				}
	
				
				MailMessage mailm = new MailMessage(from, to, calculatedsubject, calculatedBody ,true);
				MailEngine.send(mailm);
			}
		}
		else if(toMail.contains("all"))
		{
			if(_log.isDebugEnabled())_log.debug("All-Start:"+Long.toString(groupId));
			
			
			java.util.List<User> users = UserLocalServiceUtil.getGroupUsers(groupId);

			int sendMails = 0;
			long totalMails=0;
			Session session=MailEngine.getSession();
			boolean smtpAuth = GetterUtil.getBoolean(
					_getSMTPProperty(session, "auth"), false);
				String smtpHost = _getSMTPProperty(session, "host");
				int smtpPort = GetterUtil.getInteger(
					_getSMTPProperty(session, "port"), Account.PORT_SMTP);
				String smtpuser = _getSMTPProperty(session, "user");
				String password = _getSMTPProperty(session, "password");
				Transport transport=null;
				String protocol = GetterUtil.getString(
						session.getProperty("mail.transport.protocol"),
						Account.PROTOCOL_SMTP);

					transport = session.getTransport(protocol);
				if (smtpAuth && Validator.isNotNull(smtpuser) &&
					Validator.isNotNull(password)) {

				
					if(_log.isDebugEnabled())_log.debug("Connecting to SMTP Server");
					try
					{
						_log.debug("Connecting to SMTP Server:"+protocol+":"+smtpHost+":"+smtpPort+":"+smtpuser+":"+password);
						transport.connect(smtpHost, smtpPort, smtpuser, password);
						_log.debug("Connected to SMTP Server:"+protocol+":"+smtpHost+":"+smtpPort+":"+smtpuser+":"+password);
						
					}
					catch(MessagingException me)
					{
						me.printStackTrace();
						throw new Exception(me);
					}
				}
				else
				{
					if(_log.isDebugEnabled())_log.debug("Connecting to SMTP Server");
					try
					{
						_log.debug("Connecting to SMTP Server:"+protocol+":"+smtpHost+":"+smtpPort);
						transport.connect(smtpHost, smtpPort, null, null);
						
						_log.debug("Connected to SMTP Server:"+protocol+":"+smtpHost+":"+smtpPort);
						
					}
					catch(MessagingException me)
					{
						me.printStackTrace();
						throw new Exception(me);
					}
				}
					
			for (User user : users) {
				
				if(user.isActive()){
				
					if(nUsers > 0 && sendMails == nUsers ){
						try {
							System.out.println(" Delay " + millis +" milliseconds. Users: "+nUsers);
						    Thread.sleep(millis);
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						
						sendMails = 0;
						
					}
					
					InternetAddress to = new InternetAddress(user.getEmailAddress(), user.getFullName());
					totalMails++;
					if(_log.isDebugEnabled())
					{
						_log.debug("User::"+user.getEmailAddress()+"  number:"+Long.toString(totalMails));
					
					}
					String calculatedBody = LanguageUtil.get(user.getLocale(),"mail.header");
					calculatedBody += createMessage(body, portal, community, user.getFullName(), UserLocalServiceUtil.getUserById(userId).getFullName(),url,urlcourse);
					calculatedBody += LanguageUtil.get(user.getLocale(),"mail.footer");
	
					String calculatedsubject = createMessage(subject, portal, community, user.getFullName(), UserLocalServiceUtil.getUserById(userId).getFullName(),url,urlcourse);

					
					MailMessage mailm = new MailMessage(from, to, calculatedsubject, calculatedBody ,true);
					sendMail(mailm,transport,session);
					sendMails++;
				}
			}
			transport.close();
			if(_log.isDebugEnabled())_log.debug("All-End");
			
		}
		
		//Guardar una auditoria del envio de emails.
		AuditSendMails auditSendMails = AuditSendMailsLocalServiceUtil.createAuditSendMails(CounterLocalServiceUtil.increment(AuditSendMails.class.getName()));
		auditSendMails.setUserId(userId);
		auditSendMails.setGroupId(groupId);
		auditSendMails.setTemplateId(Long.parseLong(templateId));
		auditSendMails.setSendDate(new Date(System.currentTimeMillis()));
		AuditSendMailsLocalServiceUtil.addAuditSendMails(auditSendMails); 

	}
		
		private static String _getSMTPProperty(Session session, String suffix) {
			String protocol = GetterUtil.getString(
				session.getProperty("mail.transport.protocol"));

			if (protocol.equals(Account.PROTOCOL_SMTPS)) {
				return session.getProperty("mail.smtps." + suffix);
			}
			else {
				return session.getProperty("mail.smtp." + suffix);
			}
		}
	
	private void sendMail(MailMessage mailm, Transport transport,Session session) throws MessagingException 
	{
		javax.mail.Message message = new MimeMessage(session);
		message.setFrom(mailm.getFrom());
		message.setRecipients(javax.mail.Message.RecipientType.TO,mailm.getTo());
		message.setSubject(mailm.getSubject());
		MimeMultipart messageMultipart = new MimeMultipart(
				"alternative");
		if (mailm.isHTMLFormat()) {
		
			message.setContent(mailm.getBody(), "text/html;charset=\"UTF-8\"");

			
		}
		else {
		
			message.setContent(mailm.getBody(),"text/plain;charset=\"UTF-8\"");

		}
		transport.sendMessage(message, mailm.getTo());
		
	}

	private String createMessage(String text, String portal, String community, String student, String teacher, String url, String urlcourse){
		String res = "";
	
		res = text.replace("[@portal]", 	portal);
		res = res.replace ("[@course]", 	community);
		res = res.replace ("[@student]", 	student);
		res = res.replace ("[@teacher]", 	teacher);
		res = res.replace ("[@url]", 		"<a href=\""+url+"\">"+portal+"</a>");
		res = res.replace ("[@urlcourse]", 	"<a href=\""+urlcourse+"\">"+community+"</a>");	

		//Para poner la url desde la pï¿½gina para que se vean los correos.
		res = changeToURL(res, url);
		
		return res;
	}

	private String changeToURL(String text, String url){
		String res ="";

		//Para imï¿½genes
		res = text.replaceAll("src=\"/image/image_gallery", "src=\""+url+"/image/image_gallery");
		
		return res;
	}

	private static Log _log = LogFactoryUtil.getLog(LmsMailMessageListener.class);

}
