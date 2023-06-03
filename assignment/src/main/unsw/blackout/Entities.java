package unsw.blackout;

import java.util.HashMap;
import java.util.Map;

import unsw.utils.Angle;


public class Entities {
    private String type;
    private Angle position;
    private String ID;
    private HashMap<String, File> files = new HashMap<>();

    public Entities(String type, Angle position, String ID, HashMap<String, File> files) {
        this.type = type;
        this.position = position;
        this.ID = ID;
    }
    /**
     * 
     * @return type of the entity
     */
    public String getType() {
        return type;
    } 

    /**
     * 
     * @return the position of the entity relative to the radius of jupiter in degrees
     */
    public Angle getPosition() {
        return position;
    }

    /**
     * setter for the position of the entity
     * @param position
     */
    public void setPosition(Angle position) {
        this.position = position;
    }
    
    /**
     * 
     * @return the id (name) of the entity
     */
    public String getID() {
        return ID;
    }
    
    /**
     * 
     * @return a map of files that the entity contains
     */
    public Map<String, File> getFiles() {
        return files;
    }

    /**
     * setter for map of files
     * @param files
     */
    public void setFiles(HashMap<String, File> files) {
        this.files = files;
    }
    
    /**
     * send a file from a device to a satellite
     */
    // public void sendFileToSatellite(String fromId, String toId, String fileName) {
        
    // }
}
