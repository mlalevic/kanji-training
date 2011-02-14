/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;

import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.util.Vector;
import javax.microedition.lcdui.Image;

/**
 *
 * @author mlalevic
 */
public class MultipleImage {
    private Vector imageDefinitions;
    private Image current;
    private Image buffered;
    private ImageDefinition bufferedDef;
    private ImageDefinition currentDef;

    public MultipleImage(String definitionResource) throws IOException{
        //this is reading from dat file
        imageDefinitions = new Vector();
        DataInputStream ds = new DataInputStream(getClass().getResourceAsStream(definitionResource));
        //next time I need to set first integer to carry count of items
        for(int i =0; i < 57; i++){
            imageDefinitions.addElement(new ImageDefinition(ds));
        }
        imageDefinitions.trimToSize();
        ds.close();
    }

    /*public MultipleImage(String definitionResource, int initialSize) throws IOException{
        imageDefinitions = new Vector(initialSize);
        InputStream is = getClass().getResourceAsStream(definitionResource);
		StringBuffer sb = new StringBuffer();
		int oneChar = 0;
		while((oneChar = is.read()) != -1){
			sb.append((char)oneChar);
		}
		is.close();

        String wholeDef = sb.toString();
		int previousNewLine = 0;
		int nextNewLine = 0;
		//NOTE our def has to have \r\n at the end of the file
		while((nextNewLine = wholeDef.indexOf("\r\n", previousNewLine)) != -1){
			imageDefinitions.addElement(new ImageDefinition(wholeDef.substring(previousNewLine, nextNewLine)));
			previousNewLine = nextNewLine + 2; //depends on line terminater, it will be 1 or 2
		}
		imageDefinitions.trimToSize();
    }*/

    public int GetStartY(){
        if(currentDef == null) return 0;
        return currentDef.getStartY();
    }

    /*
    public Image SetImageByIndex(int index) throws IOException{
        for(int i = 0; i < imageDefinitions.size(); i++){
            ImageDefinition d = (ImageDefinition)imageDefinitions.elementAt(i);
            if(d.getFromIndex() <= index && d.getToIndex() >= index){
                return GetCurrentOrLoad(d);
            }
        }
        ResetCurrent();
        return null;
    }*/

    private void ResetCurrent(){
        current = null;
        currentDef = null;
    }

    private Image GetCurrentOrLoad(ImageDefinition d) throws IOException{
        if(current != null && currentDef == d){
            return current;
        }
        if(buffered != null && bufferedDef == d){
            useBuffered();
            return current;
        }

        currentDef = d;
        //current = Image.createImage(d.getResourceName());
        InputStream is = this.getClass().getResourceAsStream(d.getResourceName());
        if(is == null){
            current = null;
            return null;
        }
        current = Image.createImage(is);
        is.close();
        return current;
    }

    public Image SetImageByY(int y) throws IOException{
        for(int i = imageDefinitions.size() - 1; i >= 0; i--){
            ImageDefinition d = (ImageDefinition)imageDefinitions.elementAt(i);
            if(d.getStartY() <= y){
                return GetCurrentOrLoad(d);
            }
        }
        ResetCurrent();
        return null;
    }

    public ImageDefinition getDef(){
        return currentDef;
    }

    public Vector getVec(){
        return imageDefinitions;
    }

    private void SetBufferedOrLoad(ImageDefinition d) throws IOException {
        if(buffered != null && bufferedDef == d){
            return;
        }

        if(current != null && currentDef == d){
            buffered = current;
            bufferedDef = currentDef;
            return;
        }

        bufferedDef = d;
        //buffered = Image.createImage(d.getResourceName());
        InputStream is = this.getClass().getResourceAsStream(d.getResourceName());
        if(is == null){
            buffered = null;
            return;
        }
        buffered = Image.createImage(is);
        is.close();
    }

    private void useBuffered() {
        currentDef = bufferedDef;
        current = buffered;
    }

    public void SetBufferedByY(int y) throws IOException{
        for(int i = imageDefinitions.size() - 1; i >= 0; i--){
            ImageDefinition d = (ImageDefinition)imageDefinitions.elementAt(i);
            if(d.getStartY() <= y){
                SetBufferedOrLoad(d);
                return;
            }
        }
    }
}
