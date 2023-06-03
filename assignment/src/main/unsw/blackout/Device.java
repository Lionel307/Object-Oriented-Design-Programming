package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import unsw.utils.MathsHelper;

import unsw.utils.Angle;

public class Device extends Entities{
    
   
    public Device(String ID, String type, Angle position, HashMap<String, File> files) {
        super(type, position, ID, files);
    }

    /**
     * 
     * @param device
     * @param satellites
     * @param id
     * @return a list of the satellite ids in range of the device
     */
    
    private List<String> supports = new ArrayList<String>();
    private List<String> satellitesInRange = new ArrayList<String>();
    public List<String> satellitesInRange(Device device, List<Satellite> satellites, String id) {
        double inRange = 0;

        supports.add("StandardSatellite");
        supports.add("ShrinkingSatellite");
        supports.add("RelaySatellite");
        supports.add("HandheldDevice");
        supports.add("LaptopDevice");
        supports.add("DesktopDevice");
        
        if (device.getType().equals("HandheldDevice")) {
            inRange = 50000;
        } else if (device.getType().equals("LaptopDevice")) {
            inRange = 100000;
        } else {
            inRange = 200000;
            supports.remove("StandardSatellite");
        }
        for (Satellite satellite : satellites) {
            boolean visible = MathsHelper.isVisible(satellite.getHeight(), satellite.getPosition(), device.getPosition());
            Double range = MathsHelper.getDistance(satellite.getHeight(), satellite.getPosition(), device.getPosition());
            if (visible && range <= inRange && supports.contains(satellite.getType())) {
                satellitesInRange.add(satellite.getID());
            }
        }
        return satellitesInRange;
    }
    
}
