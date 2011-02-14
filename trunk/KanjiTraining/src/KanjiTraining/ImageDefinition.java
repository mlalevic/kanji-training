/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;

import java.io.IOException;
import java.io.DataInputStream;
/**
 *
 * @author mlalevic
 */
public class ImageDefinition {
    private int startY;
    private int fromIndex;
    private int toIndex;
    private String resourceName;

    /**
     * @return the startY
     */
    public int getStartY() {
        return startY;
    }

    /**
     * @return the fromIndex
     */
    public int getFromIndex() {
        return fromIndex;
    }

    /**
     * @return the toIndex
     */
    public int getToIndex() {
        return toIndex;
    }

    /**
     * @return the resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

    /*public ImageDefinition(String definition){
		int indexOfY = definition.indexOf(' ');
		startY = Integer.parseInt(definition.substring(0, indexOfY));
		int nextHyphen = definition.indexOf('-', indexOfY);
		fromIndex = Integer.parseInt(definition.substring(indexOfY+1, nextHyphen));
		int nextSpace = definition.indexOf(' ', nextHyphen);
		toIndex = Integer.parseInt(definition.substring(nextHyphen+1, nextSpace));
		resourceName = "/".concat(definition.substring(nextSpace + 1, definition.length()));
	}*/

    public ImageDefinition(DataInputStream di) throws IOException{
        startY = di.readShort();
		fromIndex = di.readShort();
		toIndex = di.readShort();
		resourceName = di.readUTF();
	}
}
