/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;

import java.io.DataInputStream;
import java.io.IOException;
/**
 *
 * @author mlalevic
 */
public class CharacterDefinition {
    String text;
    Rectangle rect;

	/*public CharacterDefinition(String definition){
		int indexOfRectStart = definition.indexOf('[');
		text = definition.substring(0, indexOfRectStart);
		int nextComma = definition.indexOf(',', indexOfRectStart);
		int x = Integer.parseInt(definition.substring(indexOfRectStart+1, nextComma));
		int previousComma = nextComma + 1;
		nextComma = definition.indexOf(',', previousComma);
		int y = Integer.parseInt(definition.substring(previousComma, nextComma));
		previousComma = nextComma + 1;
		nextComma = definition.indexOf(',', previousComma);
		int width = Integer.parseInt(definition.substring(previousComma, nextComma));
		int height = Integer.parseInt(definition.substring(nextComma + 1, definition.length() - 1));
        rect = new Rectangle(x, y, width, height);
	}*/

    public CharacterDefinition(DataInputStream di) throws IOException{
		text = di.readUTF();
		int x = di.readShort();
		int y = di.readShort();
		int width = di.readShort();
		int height = di.readShort();
        rect = new Rectangle(x, y, width, height);
	}

    public CharacterDefinition(String text, Rectangle rect){
        this.text = text;
        this.rect = rect;
    }

	public String getText() {
		return text;
	}

	public Rectangle getRect() {
		return rect;
	}
}
