/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;

import java.io.IOException;
import java.io.DataInputStream;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author mlalevic
 */
public class RandomCharacters {

    private Random randomGenerator;
    private Vector definitions;
    private CharacterDefinition currentCharacter;
    private CharacterDefinition bufferedCharacter;
    private static CharacterDefinition NoneSelected = new CharacterDefinition("Select Kanji in Options", new Rectangle(0, 0, 0, 0));
    IGroupDefinition config;


    public RandomCharacters(String textResource, int initialCapacity, IGroupDefinition config) throws IOException{
        this.config = config;
        randomGenerator = new Random(System.currentTimeMillis());
        definitions = new Vector(initialCapacity);
        LoadCharacters(textResource);
    }

    public void SetNext(){
        int realIndex = getNextIndex();
        if(realIndex == -1){
            currentCharacter = NoneSelected;
            return;
        }
        currentCharacter = bufferedCharacter;
        bufferedCharacter = (CharacterDefinition)definitions.elementAt(realIndex);

        if(currentCharacter == null){ //first time called this will be null
            SetNext(); //so this will be called only first time
        }
    }

    private int getNextIndex(){
        int count = config.getSelectedGlyphCount();
        if(count <= 0){
            return -1;
        }
        int nextCharacterIndex = randomGenerator.nextInt(count);
        return getRealIndex(nextCharacterIndex);
    }

    private int getRealIndex(int selectedIndex){
        if(config.getSelectedGlyphCount() > selectedIndex){
            return find(selectedIndex, config);
        }
        return -1;
    }

    private int find(int selectedIndex, IGroupDefinition def){
        if(def.isLeaf()){
            if(def.getSelectedGlyphCount() > selectedIndex){
                return ((GroupDefinition)def).getFrom() + selectedIndex - 1; //character index is 1 based (since it doesn't have kanji with index 0)
            }else{
                return -1;
            }
        }
        
        Vector children = def.getChildren();
        for(int i = 0; i < children.size(); i++){
            IGroupDefinition tmpd = (IGroupDefinition)children.elementAt(i);
            if(tmpd.getSelectedGlyphCount() > selectedIndex){
                return find(selectedIndex, tmpd);
            }
            selectedIndex -= tmpd.getSelectedGlyphCount();
        }
        return -1;
    }
    

    public CharacterDefinition Next(){
        SetNext();
        return currentCharacter;
	}

    public CharacterDefinition getBuffered(){
        return bufferedCharacter;
    }

    public CharacterDefinition Current(){
        if(currentCharacter != null){
            return currentCharacter;
        }

        return Next();
    }

    private void LoadCharacters(String resource) throws IOException{
		DataInputStream di = new DataInputStream(getClass().getResourceAsStream(resource));
		for(int i = 0; i < 2042; i++){
            definitions.addElement(new CharacterDefinition(di));
        }
		definitions.trimToSize();
        di.close();
    }
}
