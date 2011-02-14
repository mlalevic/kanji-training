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
public class GroupDefinition implements IGroupDefinition {

    private boolean selected;
    private String title; //title to show
    private int from; //subgroup indexes - inclusive
    private int to; //subgroup indexes - inclusive

    public GroupDefinition(String title, int from, int to, boolean selected){
        this.title = title;
        this.from = from;
        this.to = to;
        this.selected = selected;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the from
     */
    public int getFrom() {
        return from;
    }

    /**
     * @return the to
     */
    public int getTo() {
        return to;
    }

    public byte getSelectedStatus() {
        if(selected){
            return IGroupDefinition.SELECTED;
        }

        return IGroupDefinition.NOT_SELECTED;
    }

    public void select() {
        selected = true;
    }

    public void unselect() {
        selected = false;
    }

    public void addChild(IGroupDefinition def){
        //throw new UnsupportedOperationException("addChild unsupported on leaf");
    }

    public Vector getChildren(){
        return null;
    }

    public boolean isLeaf(){
        return true;
    }

    public IGroupDefinition findParent(IGroupDefinition d){
        return null;
    }

    public int getSelectedGlyphCount(){
        if(!selected){
            return 0;
        }
        return to - from + 1;
    }

    public void recountSelectedGlyphs(){
        //nothing - no need to recount
    }
}
