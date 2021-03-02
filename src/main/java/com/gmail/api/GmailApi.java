package com.gmail.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;

public class GmailApi {
	
	private static final String APPLICATION_NAME ="Gmail API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String user = "me";
	static Gmail service =null;
	private static File filePath = new File(System.getProperty("user.dir")+"/credentials.json");
	
	
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		InputStream in = new FileInputStream(filePath);//read credentials.json
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		
		
		
		//credentials builder
		Credential authorize = new GoogleCredential.Builder().setTransport(GoogleNetHttpTransport.newTrustedTransport())
		.setJsonFactory(JSON_FACTORY)
		.setClientSecrets(clientSecrets)
		.build()
		.setAccessToken("ya29.A0AfH6SMDBXe5ZzfmOlm6MYfPGb4F01ei7qOBK4dtze1wu8yxIRsr6FXuvlkajJ3cU1nGhVj2E1RtpghhSgZpKxKVqY3Yj36bdI-7y41W5R55wlXRj2Gz1KFs0-wZcVan3blmG8PyNPdkEGIhNY8t33lWX3u4W")
		.setRefreshToken("1//0gHuWrdoIu10KCgYIARAAGBASNwF-L9Irp6NbfSG1y6bq75faDdR7AV7XFNcgvLMcNFqNu8GTLM1uUyDp9VDjayar3WMCNo7pPH0");
	
		//Create Gmail service
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize).setApplicationName(GmailApi.APPLICATION_NAME).build();
		
		
		//Access gmail inbox
		Gmail.Users.Messages.List request = service.users().messages().list(user).setQ("from: @uel.edu.vn");
		ListMessagesResponse messageResponse = request.execute();
		request.setPageToken(messageResponse.getNextPageToken());
		
		String messageId;
		Message message;
		//Get ID of the email
		for(Message m:messageResponse.getMessages()) {
			messageId = m.getId();
			message = service.users().messages().get(user,messageId).execute();
			List<MessagePartHeader> emailPayloads = message.getPayload().getHeaders();
			for(MessagePartHeader s:emailPayloads) {
				if(s.getName().equals("Subject")) System.out.println(s.getValue());
			}
		}
//		String messageId = messageResponse.getMessages().get(0).getId();
//		
//		Message message = service.users().messages().get(user, messageId).execute();
		
//		//Print email body
//		List<MessagePartHeader> emailSubjects = message.getPayload().getHeaders();
//		for(MessagePartHeader s:emailSubjects) {
//			if (s.getName().equals("Subject"))
//			{
//				System.out.println("Email subject:"+s.getValue());;
//				break;
//			}
//		}
//		String emailBody = StringUtils
//				.newStringUtf8(Base64.decodeBase64(message.getPayload().getParts().get(0).getBody().getData()));
//		System.out.println("Email body:"+ emailBody);
		
//		System.out.println("Printing Labels:");
//		System.out.println(service.users().getProfile(user).execute().toString());
//		ListLabelsResponse listResponse = service.users().labels().list(user).execute();
//        List<Label> labels = listResponse.getLabels();
//        if (labels.isEmpty()) {
//            System.out.println("No labels found.");
//        } else {
//            System.out.println("Labels:");
//            for (Label label : labels) {
//                System.out.printf("- %s\n", label.getName());
//            }
//        }
	}
}
