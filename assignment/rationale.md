
Entities:
    - I decided to make a new class called Entities that have attributes type, position, Id and files.
    - Devices and satellites both have a type, position, id and a map of files 
    - Each entity that has 1 or more files can send a file to a satellite except relay satellites
        - This is why I put the sendFileToSatellite method (which is not complete) in Entities so that both devices and satellites can inherit it

Devices:
    - Devices have an "is-a" relationship with entities so I used inheritance to represent this relationship
    - There also can be 1 or more devices that exist
    - In devices, there are two lists that contain a list of which satellites the device type supports and a list of satellites in range
    - I included the satellitesInRange method here as Devices can only detect satellites and not other devices

Satellites:
    - Satellites also have an relationship with entities
    - There also can be 1 or more satellites that exist
    - Each satellite has a height, storage space and bandwidth on top of the additional attributes of entities
    - I have two setters (setStorageSpace and setBwidth) to set the storage space and bandwidth based off the type of satellite being called
    - I decided to refactor some code in the Satellite class by creating the following helper methods:
        - I included the entitiesInRange method in Satellites since satellites can communicate with devices and other satellites
        - relayMovement (which is incomplete) simulates the movement of relay satellites which travel differently to the other types of satellites
        - quantumCheck (which is incomplete) checks the satellite's map of files to check for any files that contain the phrase "quantum" 
        - sendFileToDevice (which is incomplete) sends the chosen file to the desired device
            - this method is exclusive to satellites as device cannot directly send files to other devices
    - I only displayed this relatioship in my UML diagram and could not refactor the actual code

Files:
    - Files have a "has-a" relationship with Devices as devices are used to create files
    - I used composition to represent this relationship 
    - Each device can contain 1 or more files
    - Each file has a filename, content and size of the file
    - Files are associated with Satellites and vice versa but they are independant of each other, thus I used association to represent this relationship

