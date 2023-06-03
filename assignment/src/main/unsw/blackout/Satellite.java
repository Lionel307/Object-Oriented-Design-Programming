package unsw.blackout;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class Satellite extends Entities {
    private double height;
    private int storage;
    private int bandwidth;

    public Satellite(String ID, double height, String type, Angle position, int storage, int bandwidth, HashMap<String, File> files) {
        super(type, position, ID, files);
        this.height = height;
        this.storage = storage;
        this.bandwidth = bandwidth;
    }
    /**
     * 
     * @return height the satellite
     */
    public double getHeight() {
        return height;
    }
    /**
     * 
     * @return the amount of bytes or files the satellite can store
     */
    public int getStorage() {
        return storage;
    }
    /**
     * 
     * @param storage 
     * setter for storage of the satellite
     */
    public void setStorage(int storage) {
        this.storage = storage;
    }
    /**
     * 
     * @return the bandwidth of the satellite ie how many bytes the satellite can send in a minute
     */
    public int getbandwidth() {
        return bandwidth;
    }
    /**
     * 
     * @param bandwidth
     * setter for bandwidth
     */
    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }


    /**
     * 
     * @param type
     * @return the bandwidth accoriding to the type of the satellite
     */
    public int setbandwidth(String type) {
        int bwidth = 0;
        if (type.equals("StandardSatellite")) {
            bwidth = 1;
        } else if (type.equals("ShrinkingSatellite")) {
            bwidth = 15;
        } 
        return bwidth;
    }
    
    /**
     * 
     * @param type
     * @return the storage size of the satellite according to its type
     */
    public int setStorageSize(String type) {
        int size = 0;
        if (type.equals("StandardSatellite")) {
            size = 80;
        } else if (type.equals("ShrinkingSatellite")) {
            size = 150;
        } 
        return size;
    }

    /**
     * Simulate the movement of a relay satellite
     * only moves between 140 and 190 degrees
     * if the satellite is created outside these boundaries the satellite will find the quickests to travel to these boundaries
     */ 
    public void relayMovement(Satellite satellite) {
        // satellite is outside 140 and 190
        Angle min = Angle.fromDegrees(140);
        Angle max = Angle.fromDegrees(190);
        if (satellite.getPosition().compareTo(min) == -1 || satellite.getPosition().compareTo(max) == 1) {
            
        } else {
            
        }
    }

    /**
     * 
     * @param satellite
     * @param devices
     * @param satellites
     * @param id
     * @return a List of IDs that are in range of the satellite
     */
    public List<String> entitiesInRange(Satellite satellite, List<Device> devices,List<Satellite> satellites, String id) {
        List<String> supports = new ArrayList<String>();
        List<String> entitiesInRange = new ArrayList<String>();

        double inRange = 0;

        supports.add("StandardSatellite");
        supports.add("ShrinkingSatellite");
        supports.add("RelaySatellite");
        supports.add("HandheldDevice");
        supports.add("LaptopDevice");
        supports.add("DesktopDevice");

        if (satellite.getType().equals("StandardSatellite")) {
                supports.remove("DesktopDevice");
                inRange = 150000;
        } else if (satellite.getType().equals("ShrinkingSatellite")) {
            inRange = 200000;
        } else {
            inRange = 300000;
        }
        for (Satellite satellite2 : satellites) {
            
            boolean visible = MathsHelper.isVisible(satellite.getHeight(), satellite.getPosition(), satellite2.getHeight(), satellite2.getPosition());
            Double range = MathsHelper.getDistance(satellite.getHeight(), satellite.getPosition(), satellite2.getHeight(), satellite2.getPosition());
            if (visible && range <= inRange && supports.contains(satellite2.getType()) && !id.equals(satellite2.getID()) ) {
                entitiesInRange.add(satellite2.getID());
            }
        }
        for (Device device : devices) {
            
            boolean visible = MathsHelper.isVisible(satellite.getHeight(), satellite.getPosition(), device.getPosition());
            Double range = MathsHelper.getDistance(satellite.getHeight(), satellite.getPosition(), device.getPosition());
            if (visible && range <= inRange && supports.contains(device.getType())) {
                entitiesInRange.add(device.getID());
            }
        }
        return entitiesInRange;
    }
    
    /**
     * send a file from a satelllite to a satellite
     */
    // public void sendFileToSatellite(String fromId, String toId, String fileName) {
        
    // }
   
}
