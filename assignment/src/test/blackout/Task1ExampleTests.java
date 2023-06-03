package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task1ExampleTests {
    @Test
    public void testExample() {
        // Task 1
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 3 devices
        // 2 devices are in view of the satellite
        // 1 device is out of view of the satellite
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(30));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"), controller.listSatelliteIds());
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceA", "DeviceB", "DeviceC"), controller.listDeviceIds());

        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));

        assertEquals(new EntityInfoResponse("DeviceA", Angle.fromDegrees(30), RADIUS_OF_JUPITER, "HandheldDevice"), controller.getInfo("DeviceA"));
        assertEquals(new EntityInfoResponse("DeviceB", Angle.fromDegrees(180), RADIUS_OF_JUPITER, "LaptopDevice"), controller.getInfo("DeviceB"));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice"), controller.getInfo("DeviceC"));
    }

    @Test
    public void testDelete() {
        // Task 1
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 3 devices and deletes them
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(30));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"), controller.listSatelliteIds());
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceA", "DeviceB", "DeviceC"), controller.listDeviceIds());

        controller.removeDevice("DeviceA");
        controller.removeDevice("DeviceB");
        controller.removeDevice("DeviceC");
        controller.removeSatellite("Satellite1");

        assertListAreEqualIgnoringOrder(Arrays.asList(), controller.listDeviceIds());
        assertListAreEqualIgnoringOrder(Arrays.asList(), controller.listSatelliteIds());

    }

    @Test
    public void basicFileSupport() {
        // Task 1
        BlackoutController controller = new BlackoutController();

        // Creates 1 device and add some files to it
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC"), controller.listDeviceIds());
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice"), controller.getInfo("DeviceC"));

        controller.addFileToDevice("DeviceC", "Hello World", "My first file!");
        Map<String, FileInfoResponse> expected = new HashMap<>();
        expected.put("Hello World", new FileInfoResponse("Hello World", "My first file!", "My first file!".length(), true));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice", expected), controller.getInfo("DeviceC"));
        
    }

    @Test
    public void testDeleteDeviceFile() {
        // Task 1
        BlackoutController controller = new BlackoutController();

        // Creates 1 device and add some files to it
        // delete the device and create it again
        // test the file is gone
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC"), controller.listDeviceIds());
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice"), controller.getInfo("DeviceC"));

        controller.addFileToDevice("DeviceC", "Hello World", "My first file!");
        Map<String, FileInfoResponse> expected = new HashMap<>();
        expected.put("Hello World", new FileInfoResponse("Hello World", "My first file!", "My first file!".length(), true));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice", expected), controller.getInfo("DeviceC"));

        controller.removeDevice("DeviceC");
        
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC"), controller.listDeviceIds());
        Map<String, FileInfoResponse> expected2 = new HashMap<>();

        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice", expected2), controller.getInfo("DeviceC"));
    }

    @Test
    public void addMultipleFiles() {
        // Task 1
        BlackoutController controller = new BlackoutController();

        // Creates 1 device and add multiple files to it
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC"), controller.listDeviceIds());
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice"), controller.getInfo("DeviceC"));

        controller.addFileToDevice("DeviceC", "Hello World", "My first file!");
        Map<String, FileInfoResponse> expected = new HashMap<>();
        expected.put("Hello World", new FileInfoResponse("Hello World", "My first file!", "My first file!".length(), true));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice", expected), controller.getInfo("DeviceC"));

        controller.addFileToDevice("DeviceC", "Hello", "My second file!");
        expected.put("Hello", new FileInfoResponse("Hello", "My second file!", "My second file!".length(), true));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice", expected), controller.getInfo("DeviceC"));

        controller.addFileToDevice("DeviceC", "Hi", "My third file!");
        expected.put("Hi", new FileInfoResponse("Hi", "My third file!", "My third file!".length(), true));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice", expected), controller.getInfo("DeviceC"));
    }

    @Test
    public void fileInOneDevice() {
        // Task 1
        BlackoutController controller = new BlackoutController();

        // Creates 2 devices and add some files to one
        // The other device should not have the same files
        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(30));
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceA", "DeviceC"), controller.listDeviceIds());

        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice"), controller.getInfo("DeviceC"));

        controller.addFileToDevice("DeviceC", "Hello World", "My first file!");
        Map<String, FileInfoResponse> expected = new HashMap<>();

        assertEquals(new EntityInfoResponse("DeviceA", Angle.fromDegrees(30), RADIUS_OF_JUPITER, "HandheldDevice", expected), controller.getInfo("DeviceA"));

        expected.put("Hello World", new FileInfoResponse("Hello World", "My first file!", "My first file!".length(), true));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice", expected), controller.getInfo("DeviceC"));
    }

    
}
