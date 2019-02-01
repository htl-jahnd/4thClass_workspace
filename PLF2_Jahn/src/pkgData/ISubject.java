/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgData;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;

/**
 *
 * @author schueler
 */
public interface ISubject {
	public void start();

	public void setDp(double prop);
	public DoubleProperty getDp();
	
	public void setVisibility(boolean prop);
	public BooleanProperty getVisibility();
	
	public void setEnd();
}
