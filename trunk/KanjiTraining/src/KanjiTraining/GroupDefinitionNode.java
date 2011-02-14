/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;
import java.util.Vector;
/**
 *
 * @author mlalevic
 */
public class GroupDefinitionNode implements IGroupDefinition {
    Vector children;
    String title;
    int selectedGlyphsCount = -1;

    public GroupDefinitionNode(String title){
        children = new Vector();
        this.title = title;
    }

    public void addChild(IGroupDefinition def){
        children.addElement(def);
    }

    public byte getSelectedStatus() {
        boolean oneSelected = false;
        boolean oneNotSelected = false;
        for(int i = 0; i < children.size(); i++){
            IGroupDefinition def = (IGroupDefinition)children.elementAt(i);
            switch(def.getSelectedStatus()){
                case IGroupDefinition.NOT_SELECTED:
                    if(oneSelected){
                        return IGroupDefinition.PARTIAL;
                    }
                    oneNotSelected = true;
                    break;
                case IGroupDefinition.PARTIAL:
                    return IGroupDefinition.PARTIAL;
                case IGroupDefinition.SELECTED:
                    if(oneNotSelected){
                        return IGroupDefinition.PARTIAL;
                    }
                    oneSelected = true;
                    break;
            }
        }
        if(oneSelected && !oneNotSelected){return IGroupDefinition.SELECTED;}
        if(oneSelected && oneNotSelected){return IGroupDefinition.PARTIAL;}
        return IGroupDefinition.NOT_SELECTED;
    }

    public String getTitle() {
        return title;
    }

    public void select(){
        for(int i=0; i < children.size(); i++){
            IGroupDefinition def = (IGroupDefinition)children.elementAt(i);
            def.select();
        }
    }

    public void unselect(){
        for(int i=0; i < children.size(); i++){
            IGroupDefinition def = (IGroupDefinition)children.elementAt(i);
            def.unselect();
        }
    }

    public Vector getChildren(){
        return children;
    }

    public boolean isLeaf(){
        return false;
    }

    public IGroupDefinition findParent(IGroupDefinition g){
        for(int i = 0; i < children.size(); i++){
            if(g == children.elementAt(i)){
                return this;
            }
        }
        for(int i = 0; i < children.size(); i++){
            IGroupDefinition def = ((IGroupDefinition)children.elementAt(i)).findParent(g);
            if(def != null){return def;}
        }
        return null;
    }

    public int getSelectedGlyphCount(){
        return selectedGlyphsCount;
    }

    public void recountSelectedGlyphs(){
        selectedGlyphsCount = 0;
        for(int i = 0; i < children.size(); i++){
            IGroupDefinition child = (IGroupDefinition)children.elementAt(i);
            child.recountSelectedGlyphs();
            selectedGlyphsCount += child.getSelectedGlyphCount();
        }
    }
}
