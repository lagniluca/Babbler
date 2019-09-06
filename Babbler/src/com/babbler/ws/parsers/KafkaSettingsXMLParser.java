package com.babbler.ws.parsers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class used for parsing XML data used as parameters for the Kafka cluster management
 * @author Luca Lagni
 */
public class KafkaSettingsXMLParser {
    
    //Path of the XML file
    private final String FILE_PATH = "deployment.xml";
    
    private HashMap<String, Integer> kafkaBrokers = null;
    private HashMap<String, Integer> schemaRegistries = null;
    
    public KafkaSettingsXMLParser() throws ParserConfigurationException, SAXException, IOException{
        File file = new File(FILE_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        
        //Extraction of kafka brokers
        this.kafkaBrokers = new HashMap<String, Integer>();
        NodeList brokers = document.getElementsByTagName("kafkaBrokers");
        
        for(int i = 0; i < brokers.getLength(); i++){
            Node node = brokers.item(i);
            Element element = (Element) node;
            
            this.kafkaBrokers.put(element.getElementsByTagName("kafkaBroker").item(0).getTextContent(), Integer.parseInt(element.getAttribute("port")));
        }
        
        //Extraction of schema registries
        this.schemaRegistries = new HashMap<String, Integer>();
        NodeList registries = document.getElementsByTagName("schemaRegistries");
        
        for(int i = 0; i < registries.getLength(); i++){
            Node node = registries.item(i);
            Element element = (Element) node;
            
            this.schemaRegistries.put(element.getElementsByTagName("schemaRegistry").item(0).getTextContent(), Integer.parseInt(element.getAttribute("port")));
        }
        
    }
    
    /**
     * Method used for getting the kafka brokers saved in the parameters
     * file
     * @return 
     */
    public HashMap<String, Integer> getKafkaBrokers(){
        return this.kafkaBrokers;
    }
    
    /**
     * Method used for getting the schema registries saved in the parameters
     * file
     * @return 
     */
    public HashMap<String, Integer> getSchemaRegistries(){
        return this.schemaRegistries;
    }
    
}

