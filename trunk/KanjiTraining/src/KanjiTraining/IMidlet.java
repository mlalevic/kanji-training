/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;

import javax.microedition.lcdui.Displayable;

/**
 *
 * @author mlalevic
 */
public interface IMidlet {
    void returnTo(Displayable d);
    void SaveConfiguration(IGroupDefinition root);
    IGroupDefinition getGroupDefinitions();
}
