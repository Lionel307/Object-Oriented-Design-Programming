package satellite;

public class Satellite {
    private int height = 0;
    private double position = 0, velocity = 0;
    private String name;
    public static void main(String[] args) {
        
        Satellite a = new Satellite("A", 10000, 122, 55);
        Satellite b = new Satellite("B", 5438, 0, 234000);
        Satellite c = new Satellite("C", 9029, 284, 0);
        
        System.out.println("I am Satellite " +a.getName() + " at position " + a.getPositionDegrees() + " degrees, " +a.getHeight()+ " km above the centre of the earth and moving at a velocity of " +a.getLinearVelocity()+" metres per second");
        a.height = 9999;
        System.out.println(a.getPositionRadians());
        b.position = 45;
        System.out.println(b.getAngularVelocity());
        c.velocity = 36.5;
        System.out.println(c.distance(120));

    }
   
    /**
     * Constructor for Satellite
     * @param name
     * @param height
     * @param velocity
     */
    public Satellite(String name, int height, double position, double velocity) {
        //this();
        this.name = name;
        this.height = height;
        this.position = position;
        this.velocity = velocity;
    }

    /**
     * Getter for name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter for position (degrees)
     */
    public double getPositionDegrees() {
        return position;
    }

    /**
     * Getter for position (radians)
     */
    public double getPositionRadians() {
        return Math.toRadians(position);
    }

    /**
     * Returns the linear velocity (metres per second) of the satellite
     */
    public double getLinearVelocity() {
        return velocity;
    }

    /**
     * Returns the angular velocity (degrees per second) of the satellite
     */
    public double getAngularVelocity() {
        // convert km to m for the radius
        return velocity/(height*1000);
    }

    /**
     * Setter for name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for height
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Setter for velocity
     * @param velocity
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    /**
     * Setter for position
     * @param position
     */
    public void setPosition(double position) {
        this.position = position;
    }

    /**
     * Calculates the distance travelled by the satellite in the given time
     * @param time (seconds)
     * @return distance in metres
     */
    public double distance(double time) {
        return velocity*time;
    }

}