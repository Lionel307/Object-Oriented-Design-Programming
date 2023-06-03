package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;


public class BlackoutController {
    private List<Device> devices = new ArrayList<Device>();
    private List<String> devicesID = new ArrayList<String>();
    private List<Satellite> satellites = new ArrayList<Satellite>();
    private List<String> satelliteID = new ArrayList<String>();
    
    public void createDevice(String deviceId, String type, Angle position) {
        final HashMap<String, File> files = new HashMap<String, File>(); 

        Device device = new Device(deviceId, type, position, files);
        devices.add(device);
    }

    public void removeDevice(String deviceId) {
        for (Device device : devices) {
            if (deviceId.equals(device.getID())) {
                devices.remove(device);
                devicesID.remove(deviceId);
                break;
            }
        }
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        int storage = 0;
        int bandwidth = 0; 
        final HashMap<String, File> files = new HashMap<String, File>(); 

        Satellite satellite = new Satellite(satelliteId, height, type, position, storage, bandwidth, files);

        satellites.add(satellite);
    }

    public void removeSatellite(String satelliteId) {
        for (Satellite satellite : satellites) {
            if (satelliteId.equals(satellite.getID())) {
                satellites.remove(satellite);
                satelliteID.remove(satelliteId);
                break;
            }
        }
    }

    public List<String> listDeviceIds() {
        for (Device device : devices) {
            devicesID.add(device.getID());
        }
        return devicesID;
    }

    public List<String> listSatelliteIds() {
        for (Satellite satellite : satellites){
            satelliteID.add(satellite.getID());
        }
        return satelliteID;
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        for (Device device : devices) {
            if (deviceId.equals(device.getID())) {
                File file = new File(filename, content, content.length());
                device.getFiles().put(filename, file);
            }
        }
        
    }

    public EntityInfoResponse getInfo(String id) {
        for (Device device : devices) {
            if (id.equals(device.getID())) {
                final HashMap<String, FileInfoResponse> fMap= new HashMap<>();
                for (Map.Entry e : device.getFiles().entrySet()) {
                    File file = (File)e.getValue();
                    FileInfoResponse fileInfo = new FileInfoResponse(file.getFileName(), file.getContent(), file.getFileSize(), true);
                    fMap.put(file.getFileName(), fileInfo);
                }
                EntityInfoResponse entity = new EntityInfoResponse(id, device.getPosition(), RADIUS_OF_JUPITER, device.getType(), fMap);
                return entity;
            }
        }
        for (Satellite satellite : satellites) {
            if (id.equals(satellite.getID())) {
                EntityInfoResponse entity = new EntityInfoResponse(id, satellite.getPosition(), satellite.getHeight(), satellite.getType());
                return entity;
            }
        }
        return null;

    }

    public void simulate() {
        // TODO: Task 2a)
        for (Satellite satellite : satellites) {
            
            if (satellite.getType().equals("StandardSatellite")) {
                // change position due to speed
                Angle newPostion = satellite.getPosition();
                satellite.setPosition(newPostion.add(Angle.fromRadians(2500/satellite.getHeight())));

            } else if (satellite.getType().equals("ShrinkingSatellite")) {

                Angle newPostion = satellite.getPosition();
                satellite.setPosition(newPostion.add(Angle.fromRadians(1000/satellite.getHeight())));

            } else if (satellite.getType().equals("RelaySatellite")) {
                Angle newPostion = satellite.getPosition();
                satellite.setPosition(newPostion.add(Angle.fromRadians(1500/satellite.getHeight())));
                satellite.relayMovement(satellite);
            }
        }

    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        List<String> entitiesInRange = new ArrayList<String>();
        
        for (Device device : devices) {
            if (id.equals(device.getID())) {
                entitiesInRange = device.satellitesInRange(device, satellites, id);
            }
        }
        for (Satellite satellite : satellites) {
            if (id.equals(satellite.getID())) {
                entitiesInRange = satellite.entitiesInRange(satellite, devices, satellites, id);
            }
        }
        
        return entitiesInRange;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        // TODO Task 2c)

        for (Device device : devices) {
            if (fromId.equals(device.getID())) {

            } else if (toId.equals(device.getID())) {

            }
        }

        for (Satellite satellite : satellites) {
            if (fromId.equals(satellite.getID())) {
                satellite.setStorageSize(fromId);
                satellite.setbandwidth(fromId);
            } else if (toId.equals(satellite.getID())) {
                satellite.setStorageSize(toId);
                satellite.setbandwidth(toId);
            }
        }
    }
}
