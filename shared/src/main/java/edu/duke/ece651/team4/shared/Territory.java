package edu.duke.ece651.team4.shared;

import java.io.Serializable;
import java.util.Objects;

public class Territory implements Serializable {
    private String name;
    private int units;
    /**
     * Constructor for Territory
     *
     * @param newName is the name we want to assign to this territory
     * @param newUnits is the units number we want this territory to have
     */
    public Territory(String newName, int newUnits) {
        this.name = newName;
        this.units = newUnits;
    }
    /**
     * getName() is used to get the name of current territory
     *
     * @return current territory name
     */
    public String getName(){
        return this.name;
    }

    /**
     * getNumUnits() is used to get the number of units current territory has
     *
     * @return current units number
     */
    public int getNumUnits(){
        return this.units;
    }

    /**
     * addUnits() is used to add units to current units
     *
     * @param unitsToAdd is units we want to add to current territory
     */
    public void addUnits(int unitsToAdd){
        this.units = this.units + unitsToAdd;
    }

    /**
     * setUnits() is used to set units
     *
     * @param unitsToSet is units we want to set for current territory
     */
    public void setUnits(int unitsToSet){
        this.units = unitsToSet;
    }

    /**
     * equals() is used to override .equals method for Territory
     * If they have the same units and name then they are the same
     *
     * @param o is the object that compares with current Territory
     * @return true if they hold the same amount of numUnits and have same name
     * @return false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Territory territory = (Territory) o;
        return Objects.equals(name, territory.name);
    }
  /**
   * String representation of territory. It's name.
   */
  @Override
  public String toString() {
    return name;
  }

}
