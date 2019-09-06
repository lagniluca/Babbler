package com.babbler.ws.model;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class that represents a single babble for our Babbler application
 * @author Luca Lagni
 *
 */
@XmlRootElement
public class Babble {
	//Author of the Babble
	private String author = null;
	//Content of the Babble
	private String content = null;
	//Timestamp associated to the Babble
	private String timestamp = null;
	//Location associated to the Babble
	private String location = null;
	//Set of tags assogiated to the Babble
	private HashSet<String> tags = null;
	//Set of mentions associated to the Babble
	private HashSet<String> mentions = null;
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public HashSet<String> getTags() {
		return tags;
	}
	public void setTags(HashSet<String> tags) {
		this.tags = tags;
	}
	public HashSet<String> getMentions() {
		return mentions;
	}
	public void setMentions(HashSet<String> mentions) {
		this.mentions = mentions;
	}
	@Override
	public String toString() {
		String state = "Babble [author=" + author + ", content=" + content + ", timestamp=" + timestamp + ", location="
				+ location + ", tags={" ;
		
		for(String tag : tags) {
			
			state += tag + " ";
		}
		
		state += "}, mentions={" ;
		
		for(String mention : mentions) {
			state += mention + " ";
		}
		
		state += "}]";
		
		return state ;
	}

	@Override
	public boolean equals(Object obj) {
		
		Babble other = (Babble) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (mentions == null) {
			if (other.mentions != null)
				return false;
		} else if (!mentions.equals(other.mentions))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		
		return true;
	}
	
	
	
	
}
