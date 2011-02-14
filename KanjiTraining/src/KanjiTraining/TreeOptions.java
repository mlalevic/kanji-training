/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;
import java.io.IOException;
import javax.microedition.lcdui.*;
import java.util.Vector;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author mlalevic
 */
public class TreeOptions extends List implements CommandListener {

    IGroupDefinition currentGroup;
    IGroupDefinition rootGroup;
    Image selected;
    Image notSelected;
    Image partial;
    Displayable toReturnTo;
    IMidlet midlet;
    Command ok;
    Command cancel;
    Command drillDown;
    Command selectItem;

    public TreeOptions(String title, IMidlet midlet) throws IOException{
        super(title, List.IMPLICIT);
        this.midlet = midlet;
        setCommandListener(this);
        ok = new Command("OK", Command.OK, 1);
        cancel = new Command("Cancel", Command.CANCEL, 4);
        drillDown = new Command("Expand", Command.SCREEN, 3);
        selectItem = new Command("Select", Command.ITEM, 2);
        selected = Image.createImage("/selected.png");
        notSelected = Image.createImage("/notSelected.png");
        partial = Image.createImage("/partial.png");
        addCommand(ok);
        setSelectCommand(selectItem);
    }

    private void setCommands(){
        removeCommand(drillDown);
        removeCommand(cancel);
        if(isShowingRoot()){
            addCommand(drillDown);
            addCommand(cancel);
        }
    }

    public void LoadGroups() throws IOException{
        rootGroup = midlet.getGroupDefinitions();
    }

    public void Show(Displayable toReturn){
        this.toReturnTo = toReturn;
        currentGroup = rootGroup;
        ShowItems();
    }

    public void ShowChildren(IGroupDefinition newGroup){
        currentGroup = newGroup;
        ShowItems();
    }

    public void ShowChildren(int index){
        Vector v = currentGroup.getChildren();
        ShowChildren((IGroupDefinition)v.elementAt(index));
    }

    private void ShowItems(){
        setCommands();
        this.deleteAll();
        Vector toShow = currentGroup.getChildren();
        for(int i = 0; i < toShow.size(); i++){
            IGroupDefinition d = (IGroupDefinition)toShow.elementAt(i);
            append(d.getTitle(), chooseImage(d.getSelectedStatus()));
        }
    }

    private Image chooseImage(byte selectionType){
        switch(selectionType){
            case IGroupDefinition.NOT_SELECTED:
                return notSelected;
            case IGroupDefinition.PARTIAL:
                return partial;
            case IGroupDefinition.SELECTED:
                return selected;
        }
        return null;
    }

    private void drillDownAction(){
        int index = getSelectedIndex();
        if(index == -1){return;}
        ShowChildren(index);
    }

    public void commandAction(Command c, Displayable d) {
        if(c==selectItem){
            int index = getSelectedIndex();
            if(index == -1){return;}
            IGroupDefinition item = (IGroupDefinition)currentGroup.getChildren().elementAt(index);
            toggle(item, index);
        }else if(c == ok){
            if(currentGroup == rootGroup){
                midlet.SaveConfiguration(rootGroup);
                midlet.returnTo(toReturnTo);
                return;
            }
            IGroupDefinition parent = rootGroup.findParent(currentGroup);
            if(parent != null){
                ShowChildren(parent);
            }
        }else if(c == cancel){
            rootGroup = midlet.getGroupDefinitions();
            midlet.returnTo(toReturnTo);
        }else if(c == drillDown){
            drillDownAction();
        }
    }

    private boolean isShowingRoot(){
        return currentGroup == rootGroup;
    }

    private void toggle(IGroupDefinition item, int index) {
        if(item.getSelectedStatus() == IGroupDefinition.NOT_SELECTED){
            item.select();
        }else{
            item.unselect();
        }
        set(index, item.getTitle(), chooseImage(item.getSelectedStatus()));
    }
}
