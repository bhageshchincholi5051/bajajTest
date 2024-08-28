package web_maven_hibernate;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DestinationHashGenerator {

	public static void main(String[] args) {
		
		   if (args.length != 2) {
		        System.out.println("Usage: java -jar DestinationHashGenerator.jar <PRN Number> <JSON File Path>");
		        return;
		    }
	
		String prnNo = args[0].toLowerCase();
		String filePath = args[1];
		
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(new File(filePath));
			
			String destinationValue = findFirstDestination(rootNode);
			if (destinationValue == null) {
                System.out.println("No destination key founnd !!");
                return;
            }
			
			String randomString = RandomStringUtils.randomAlphanumeric(8);
			
			String concatValue = prnNo + destinationValue + randomString;
			
			String md5Hash = generateMD5Hash(concatValue);
			
			System.out.println(md5Hash + ";" + randomString);
			
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	private static String findFirstDestination(JsonNode node) {
		if (node.has("destination")) {
            return node.get("destination").asText();
        }
        for (JsonNode childNode : node) {
            String result = findFirstDestination(childNode);
            if (result != null) {
                return result; 
            }
        }

        return null; //
	}
	
	 private static String generateMD5Hash(String concatenatedValue) {
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            md.update(concatenatedValue.getBytes());

	            
	            byte[] digest = md.digest();

	           
	            StringBuilder hexString = new StringBuilder();
	            for (byte b : digest) {
	                hexString.append(String.format("%02x", b));
	            }

	            return hexString.toString();
	        } catch (NoSuchAlgorithmException e) {
	            
	            throw new RuntimeException("MD5 algorithm not available", e);
	        }
	    }
}
