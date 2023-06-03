package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExampleTests {
    @Test
    public void testEntitiesInRange() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(315));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceE", "DesktopDevice", Angle.fromDegrees(325));
        controller.createSatellite("Satellite3", "StandardSatellite", 2000 + RADIUS_OF_JUPITER, Angle.fromDegrees(175));
        controller.createSatellite("Satellite4", "ShrinkingSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(325));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite2", "Satellite4"), controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1", "Satellite4"), controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC", "DeviceE", "Satellite1", "Satellite2"), controller.communicableEntitiesInRange("Satellite4"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2", "Satellite1", "Satellite4"), controller.communicableEntitiesInRange("DeviceC"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2", "Satellite1"), controller.communicableEntitiesInRange("DeviceB"));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceD"), controller.communicableEntitiesInRange("Satellite3"));
    }

    @Test
    public void testRanges() {
        BlackoutController controller = new BlackoutController();

        // test the ranges of each type of devices and satellites
        controller.createSatellite("Satellite1", "StandardSatellite", 150100 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "ShrinkingSatellite", 200100 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite3", "RelaySatellite", 300100 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));

        controller.createSatellite("Satellite", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));

        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(325));
        controller.createDevice("DeviceC", "Desktop", Angle.fromDegrees(315));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite", "Satellite2", "Satellite3"), controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite", "Satellite1", "Satellite3"), controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite","Satellite1", "Satellite2"), controller.communicableEntitiesInRange("Satellite3"));

        controller.createSatellite("Satellite4", "StandardSatellite", 60000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite5", "ShrinkingSatellite", 199100 + RADIUS_OF_JUPITER, Angle.fromDegrees(315));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite", "DeviceA", "DeviceB", "Satellite5", "Satellite1", "Satellite2"), controller.communicableEntitiesInRange("Satellite4"));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite"), controller.communicableEntitiesInRange("DeviceA"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite", "Satellite4"), controller.communicableEntitiesInRange("DeviceB"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite5"), controller.communicableEntitiesInRange("DeviceC"));

    }



    @Test
    public void testSomeExceptionsForSend() {
        // just some of them... you'll have to test the rest
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertThrows(FileTransferException.VirtualFileNotFoundException.class, () -> controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        controller.simulate(msg.length() * 2);
        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class, () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
    }

    @Test
    public void testMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(342.05), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testMovementShrinkingSatellite() {
        // Task 2
        BlackoutController controller = new BlackoutController();

        // Test the movement of the ShrinkingSatellite
        controller.createSatellite("Satellite1", "ShrinkingSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER, "ShrinkingSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340.82), 100 + RADIUS_OF_JUPITER, "ShrinkingSatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testMoreMovement() {
        // Task 2
        BlackoutController controller = new BlackoutController();

        // Creates a standard satellite and a shrinking satellite
        // simulate for 10 minutes twice
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(300));
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(300), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
        controller.simulate(10);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(320.46), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));

        controller.createSatellite("Satellite2", "ShrinkingSatellite", 200 + RADIUS_OF_JUPITER, Angle.fromDegrees(200));
        assertEquals(new EntityInfoResponse("Satellite2", Angle.fromDegrees(200), 200 + RADIUS_OF_JUPITER, "ShrinkingSatellite"), controller.getInfo("Satellite2"));
        
        controller.simulate(10);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340.92), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
        assertEquals(new EntityInfoResponse("Satellite2", Angle.fromDegrees(208.17), 200 + RADIUS_OF_JUPITER, "ShrinkingSatellite"), controller.getInfo("Satellite2"));
    }


   



    @Test
    public void testExample() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        controller.simulate(msg.length() * 2);
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        controller.simulate(msg.length());
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        // Hints for further testing:
        // - What about checking about the progress of the message half way through?
        // - Device/s get out of range of satellite
        // ... and so on.
    }
}
