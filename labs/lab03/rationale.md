# Rationale - Lab 03 Cars - Lionel Kha

Make sure you articulate each of your decisions in terms of 
* What you chose to do
* Why you chose to do it - here it is good to draw on course concepts

* I have done XXX because XXX

e.g. I have used an interface over an abstract class because ...,
there is an aggregation between X and Y because ...



For the class owner:
- I have used composition to represent the relationship between car and owner as a car must have an owner
- I also assumed that a car can only have 1 official owner as it does not seem logical for a car to have multiple owners

For the two new engines:
- they are both similar to the representation of thermalEngine and electricalEngine but nuclearEngine has the attribute, "NuclearValue", and getters and setters for it
- All the engines inherit the "Engine" class because they share common attributes, produce and speed, and getter, getSpeed

Similar to TimeTravelling, I made an interface called "Flying"
- Plane, FlyingCar and TimeTravellingCar all implement flying which has the method flying(int, int, int)
- For FlyingCar and TimeTravellingCar they both inherit "Car"
- Plane has a list of passengers  
