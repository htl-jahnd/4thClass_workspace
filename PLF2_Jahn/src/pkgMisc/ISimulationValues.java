/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgMisc;

/**
 *
 * @author schueler
 */
public interface ISimulationValues {
    final int MAX_CUSTOMERS = 8;
    
    final int MAX_NUMBER_OF_CASH = 2;
    final int MIN_DURATION_HUNGRY = 1000; //msec
    final int MAX_DURATION_HUNGRY = 3000; //msec
    final int MIN_DURATION_ORDER = 2000; //msec
    final int MAX_DURATION_ORDER = 4000; //msec
    final int MIN_DURATION_CASH = 2000; //msec
    final int MAX_DURATION_CASH = 4000; //msec
    final int MIN_DURATION_COOK = 2000; //msec
    final int MAX_DURATION_COOK = 4000; //msec
    
    final int START_X = 20;
    final int START_Y = 70;
    final int DISTANCE_X = 190;
    final int DISTANCE_Y = 40;
    
    final int HEIGHT = 30;
    final int WIDTH = 30;
    
    final int MAX_CAP_ORDER = 3;
    final int MAX_CAP_CASH = 2;
    
    public enum CustomerType {
        CAR, FIREBRIGADE, BIKER
    }
}
