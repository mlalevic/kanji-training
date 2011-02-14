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
public interface IGroupDefinition {
    public final byte NOT_SELECTED = 0;
    public final byte SELECTED = 1;
    public final byte PARTIAL = 2;
    byte getSelectedStatus();
    String getTitle();
    void select();
    void unselect();
    void addChild(IGroupDefinition def);
    Vector getChildren();
    boolean isLeaf();
    IGroupDefinition findParent(IGroupDefinition def);
    int getSelectedGlyphCount();
    void recountSelectedGlyphs();
}
