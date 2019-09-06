package com.babbler.ws.ui.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;

import com.babbler.ws.model.Babble;
import com.babbler.ws.utilities.exceptions.BabblerException;
import com.babbler.ws.utilities.exceptions.BabblerExceptionCodes;

/**
 * Class used to check if the data passed to the system as a Requets is valid or not with 
 * respect to Babble standards
 * 
 * @author Luca Lagni
 *
 */
public class BabbleValidator {
	
	/**
	 * Method used to check if a certain babble author is a valid one 
	 * @param author
	 * @return
	 */
	public static boolean validateAuthor(String author) {
		boolean valid = true;
		
		if((author == null) || (author.isEmpty())) {
			valid = false;
		}
		
		return valid;
	}
	
	/**
	 * Method used to check if the content of a babble is valid or not
	 * @param content
	 * @return
	 */
	public static boolean validateContent(String content) {
		boolean valid = true;
		
		if((content == null) || (content.isEmpty())) {
			valid = false;
		}
		
		return valid;
	}
	
	/**
	 * Method used to check if the timestamp of a babble is valid or not
	 * @param timestamp
	 * @return
	 */
	public static boolean validateTimestamp(String timestamp) {
		boolean valid = true;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		
		if((timestamp == null) || (timestamp.isEmpty())) {
			valid = false;
		}
		
		try {
			dateFormat.parse(timestamp);
		} catch (ParseException e) {
			valid = false;
		}
		
		return valid;
	}
	
	/**
	 * Method used to check if a certain passed location is a valid one or not
	 * @param location
	 * @return
	 */
	public static boolean validateLocation(String location) {
		boolean valid = true;
		
		if((location == null) || (location.isEmpty())) {
			valid = false;
		}
		
		return valid;
	}
	
	/**
	 * Method used to check if a certain passed tags is a valid one
	 * @param tags
	 * @return
	 */
	public static boolean validateTags(HashSet<String> tags) {
		boolean valid = true;
		
		if((tags == null)) {
			valid = false;
		}
		
		return valid;
	}
	
	/**
	 * Method used to check if mentions of a babble are valid
	 * @param mentions
	 * @return
	 */
	public static boolean validateMentions(HashSet<String> mentions) {
		boolean valid = true;
		
		if(mentions == null) {
			return false;
		}
		
		return valid;
	}
	
	/**
	 * Method used to validate an entire babble at once
	 * @param babble
	 * @return
	 */
	public static boolean validateBabble(Babble babble) {
		boolean valid = true;
		
		valid = valid & BabbleValidator.validateAuthor(babble.getAuthor());
		valid = valid & BabbleValidator.validateContent(babble.getContent());
		valid = valid & BabbleValidator.validateTimestamp(babble.getTimestamp());
		valid = valid & BabbleValidator.validateLocation(babble.getLocation());
		valid = valid & BabbleValidator.validateTags(babble.getTags());
		valid = valid & BabbleValidator.validateMentions(babble.getMentions());
		
		return valid;
	}
	
	/**
	 * Method used to validate an entire babble at once, it will throw excpetions in case
	 * of not valid fields
	 * @param babble
	 * @throws BabblerException
	 */
	public static void validateBabbleWithException(Babble babble) throws BabblerException {
		if (!BabbleValidator.validateAuthor(babble.getAuthor())){
			throw new BabblerException(BabblerExceptionCodes.BABBLE_INVALID_AUTHOR.getCode());
		}
		
		if(!BabbleValidator.validateContent(babble.getContent())) {
			throw new BabblerException(BabblerExceptionCodes.BABBLE_INVALID_CONTENT.getCode());
		}
		
		if(!BabbleValidator.validateTimestamp(babble.getTimestamp())) {
			throw new BabblerException(BabblerExceptionCodes.BABBLE_INVALID_TIMESTAMP.getCode());
		}
		
		if(!BabbleValidator.validateLocation(babble.getLocation())) {
			throw new BabblerException(BabblerExceptionCodes.BABBLE_INVALID_LOCATION.getCode());
		}
		
		if(!BabbleValidator.validateTags(babble.getTags())) {
			throw new BabblerException(BabblerExceptionCodes.BABBLE_INVALID_TAGS.getCode());
		}
		
		if(!BabbleValidator.validateMentions(babble.getMentions())) {
			throw new BabblerException(BabblerExceptionCodes.BABBLE_INVALID_MENTIONS.getCode());
		}
	}

}
