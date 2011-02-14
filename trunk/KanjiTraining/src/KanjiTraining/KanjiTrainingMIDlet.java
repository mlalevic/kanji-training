/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;

import java.io.IOException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStoreException;
import org.netbeans.microedition.lcdui.SimpleTableModel;
import org.netbeans.microedition.lcdui.SplashScreen;
import org.netbeans.microedition.lcdui.TableItem;

/**
 * @author mlalevic
 */
public class KanjiTrainingMIDlet extends MIDlet implements  IMidlet, ErrorLogger, CommandListener, ItemCommandListener {

    private boolean initError;
    private String initErrorText;
    private RandomCharacters rand;
    private boolean midletPaused = false;
    IGroupDefinition config;
    ErrorLogging logging;
    ConfigurationStorage configStorage;
    private final String[][] emptyErrorLog = new String[][]{new String[]{"No Values", "", ""}};

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command closeErrorLogCommand;
    private Command exitCommand;
    private Command stopTest;
    private Command answerCommand;
    private Command nextFlashCommand;
    private Command backCommand;
    private Command cancelSettingsCommand;
    private Command saveSettingsCommand;
    private Form errorLogForm;
    private TableItem errorTableItem;
    private List menuList;
    private FlashCardCanvas flashCardCanvas;
    private List optionsList;
    private SplashScreen splashScreen;
    private Form settingsForm;
    private TextField textField;
    private ChoiceGroup choiceGroup;
    private Alert alert;
    private SimpleTableModel errorTableModel;
    private Image image;
    //</editor-fold>//GEN-END:|fields|0|

    private TreeOptions treeOptions;
    /**
     * The KanjiTrainingMIDlet constructor.
     */
    public KanjiTrainingMIDlet() {
        try{
            rand = new RandomCharacters("/kanjidef.dat", 2500, getGroupDefinitions());
            configStorage = new ConfigurationStorage();
            //configStorage.setLoggingEnabled(true);
            //configStorage.setLogLinesCount(3);
            ReconfigureLogging();
        }catch(Exception ex){
            initError = true;
            initErrorText = ex.getMessage();
        }
    }

    private void PrepareErrorList() {
        if(logging == null){
            getErrorTableModel().setValues(emptyErrorLog);
            return;
        }
        String[][] errors = logging.getErrors();
        if(errors == null){
            getErrorTableModel().setValues(emptyErrorLog);
        }else{
            getErrorTableModel().setValues(errors);
        }
    }

    private void ReconfigureLogging(){
        if(configStorage.isLoggingEnabled()){
            if(logging == null){
                logging = new ErrorLogging(configStorage.getLogLinesCount());
            }else{
                logging.setMaxCount(configStorage.getLogLinesCount());
            }
        }else{
            logging = null;
        }
    }

    private void SaveConfiguration() {
        boolean[] selected = new boolean[1];
        getChoiceGroup().getSelectedFlags(selected);
        configStorage.setLoggingEnabled(selected[0]);
        configStorage.setLogLinesCount(Integer.parseInt(getTextField().getString()));
        configStorage.Save();
        ReconfigureLogging();
    }

    private void ShowFlashCanvas(boolean isKanjiFirst) {
        // write post-action user code here
        getFlashCardCanvas().setKanjiFirst(isKanjiFirst);
        getFlashCardCanvas().setTitle(isKanjiFirst?"Kanji -> English":"English -> Kanji");
        CharacterDefinition ch = rand.Next();
        //preload MultipleImage
        PreloadImages();
        /////
        getFlashCardCanvas().ShowNext(ch);
        getFlashCardCanvas().removeCommand(getNextFlashCommand());
        getFlashCardCanvas().removeCommand(getAnswerCommand());
        getFlashCardCanvas().addCommand(getAnswerCommand());
    }

    private void PreloadImages(){
        try {
            //TODO Refactor this so I don't need to know here so many things
            getFlashCardCanvas().getMultiImage().SetImageByY(rand.Current().getRect().getY());
            //getFlashCardCanvas().getMultiImage().SetBufferedByY(rand.getBuffered().getRect().getY());
        } catch (IOException ex) {
            LogError(ex.toString(), "Error preloading images");
            ex.printStackTrace();
        }
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        if(initError){
            getAlert().setString(initErrorText);
            switchDisplayable(getAlert(), getMenuList());
        }
        switchDisplayable(null, getSplashScreen());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|


















public void ShowNextCommand(){
    getFlashCardCanvas().removeCommand(getAnswerCommand());
    getFlashCardCanvas().addCommand(getNextFlashCommand());
}

public void ShowAnswerCommand(){
    getFlashCardCanvas().removeCommand(getNextFlashCommand());
    getFlashCardCanvas().addCommand(getAnswerCommand());
}


//<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
/**
 * Called by a system to indicated that a command has been invoked on a particular displayable.
 * @param command the Command that was invoked
 * @param displayable the Displayable where the command was invoked
 */
public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
    if (displayable == flashCardCanvas) {//GEN-BEGIN:|7-commandAction|1|93-preAction
        if (command == answerCommand) {//GEN-END:|7-commandAction|1|93-preAction
            // write pre-action user code here
//GEN-LINE:|7-commandAction|2|93-postAction
            // write post-action user code here
             getFlashCardCanvas().ShowAnswer();
             ShowNextCommand();
        } else if (command == nextFlashCommand) {//GEN-LINE:|7-commandAction|3|95-preAction
            // write pre-action user code here
//GEN-LINE:|7-commandAction|4|95-postAction
            // write post-action user code here
            CharacterDefinition ch = rand.Next();
            getFlashCardCanvas().ShowNext(ch);
            ShowAnswerCommand();
            getFlashCardCanvas().setBuffered(rand.getBuffered());
            //PreloadImages(); //this is now done in the canvas itself
        } else if (command == stopTest) {//GEN-LINE:|7-commandAction|5|78-preAction
            // write pre-action user code here
            switchDisplayable(null, getMenuList());//GEN-LINE:|7-commandAction|6|78-postAction
            // write post-action user code here
        }//GEN-BEGIN:|7-commandAction|7|44-preAction
    } else if (displayable == menuList) {
        if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|7|44-preAction
                // write pre-action user code here
            menuListAction();//GEN-LINE:|7-commandAction|8|44-postAction
                // write post-action user code here
        } else if (command == exitCommand) {//GEN-LINE:|7-commandAction|9|49-preAction
                // write pre-action user code here
            exitMIDlet();//GEN-LINE:|7-commandAction|10|49-postAction
                // write post-action user code here
        }//GEN-BEGIN:|7-commandAction|11|100-preAction
    } else if (displayable == optionsList) {
        if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|11|100-preAction
            // write pre-action user code here
            optionsListAction();//GEN-LINE:|7-commandAction|12|100-postAction
            // write post-action user code here
        } else if (command == backCommand) {//GEN-LINE:|7-commandAction|13|106-preAction
            // write pre-action user code here
            switchDisplayable(null, getMenuList());//GEN-LINE:|7-commandAction|14|106-postAction
            // write post-action user code here
        }//GEN-BEGIN:|7-commandAction|15|127-preAction
    } else if (displayable == settingsForm) {
        if (command == cancelSettingsCommand) {//GEN-END:|7-commandAction|15|127-preAction
            // write pre-action user code here
            switchDisplayable(null, getMenuList());//GEN-LINE:|7-commandAction|16|127-postAction
            // write post-action user code here
        } else if (command == saveSettingsCommand) {//GEN-LINE:|7-commandAction|17|125-preAction
            // write pre-action user code here
            SaveConfiguration();
            switchDisplayable(null, getMenuList());//GEN-LINE:|7-commandAction|18|125-postAction
            // write post-action user code here
        }//GEN-BEGIN:|7-commandAction|19|112-preAction
    } else if (displayable == splashScreen) {
        if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|19|112-preAction
            // write pre-action user code here
            switchDisplayable(null, getMenuList());//GEN-LINE:|7-commandAction|20|112-postAction
            // write post-action user code here
        }//GEN-BEGIN:|7-commandAction|21|7-postCommandAction
    }//GEN-END:|7-commandAction|21|7-postCommandAction
        // write post-action user code here
}//GEN-BEGIN:|7-commandAction|22|
//</editor-fold>//GEN-END:|7-commandAction|22|








//<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|48-getter|0|48-preInit
/**
 * Returns an initiliazed instance of exitCommand component.
 * @return the initialized component instance
 */
public Command getExitCommand() {
    if (exitCommand == null) {//GEN-END:|48-getter|0|48-preInit
            // write pre-init user code here
        exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|48-getter|1|48-postInit
            // write post-init user code here
    }//GEN-BEGIN:|48-getter|2|
    return exitCommand;
}
//</editor-fold>//GEN-END:|48-getter|2|









//<editor-fold defaultstate="collapsed" desc=" Generated Getter: menuList ">//GEN-BEGIN:|42-getter|0|42-preInit
/**
 * Returns an initiliazed instance of menuList component.
 * @return the initialized component instance
 */
public List getMenuList() {
    if (menuList == null) {//GEN-END:|42-getter|0|42-preInit
            // write pre-init user code here
        menuList = new List("Menu", Choice.IMPLICIT);//GEN-BEGIN:|42-getter|1|42-postInit
        menuList.append("Kanji -> English", null);
        menuList.append("English -> Kanji", null);
        menuList.append("Options", null);
        menuList.append("About", null);
        menuList.addCommand(getExitCommand());
        menuList.setCommandListener(this);
        menuList.setFitPolicy(Choice.TEXT_WRAP_DEFAULT);
        menuList.setSelectedFlags(new boolean[] { false, false, false, false });//GEN-END:|42-getter|1|42-postInit
            // write post-init user code here
    }//GEN-BEGIN:|42-getter|2|
    return menuList;
}
//</editor-fold>//GEN-END:|42-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: menuListAction ">//GEN-BEGIN:|42-action|0|42-preAction
/**
 * Performs an action assigned to the selected list element in the menuList component.
 */
public void menuListAction() {//GEN-END:|42-action|0|42-preAction
        // enter pre-action user code here
    String __selectedString = getMenuList().getString(getMenuList().getSelectedIndex());//GEN-BEGIN:|42-action|1|52-preAction
    if (__selectedString != null) {
        if (__selectedString.equals("Kanji -> English")) {//GEN-END:|42-action|1|52-preAction
                // write pre-action user code here
            switchDisplayable(null, getFlashCardCanvas());//GEN-LINE:|42-action|2|52-postAction
                ShowFlashCanvas(true);
        } else if (__selectedString.equals("English -> Kanji")) {//GEN-LINE:|42-action|3|53-preAction
                // write pre-action user code here
            switchDisplayable(null, getFlashCardCanvas());//GEN-LINE:|42-action|4|53-postAction
                // write post-action user code here
                ShowFlashCanvas(false);
        } else if (__selectedString.equals("Options")) {//GEN-LINE:|42-action|5|108-preAction
            // write pre-action user code here
            switchDisplayable(null, getOptionsList());//GEN-LINE:|42-action|6|108-postAction
            // write post-action user code here
        } else if (__selectedString.equals("About")) {//GEN-LINE:|42-action|7|114-preAction
            // write pre-action user code here
            SplashScreen about = getSplashScreen();
            about.setTimeout(SplashScreen.FOREVER);
            switchDisplayable(null, about);
//GEN-LINE:|42-action|8|114-postAction
            // write post-action user code here
        }//GEN-BEGIN:|42-action|9|42-postAction
    }//GEN-END:|42-action|9|42-postAction
        // enter post-action user code here
}//GEN-BEGIN:|42-action|10|
//</editor-fold>//GEN-END:|42-action|10|









//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stopTest ">//GEN-BEGIN:|77-getter|0|77-preInit
/**
 * Returns an initiliazed instance of stopTest component.
 * @return the initialized component instance
 */
public Command getStopTest() {
    if (stopTest == null) {//GEN-END:|77-getter|0|77-preInit
            // write pre-init user code here
        stopTest = new Command("Stop", Command.STOP, 0);//GEN-LINE:|77-getter|1|77-postInit
            // write post-init user code here
    }//GEN-BEGIN:|77-getter|2|
    return stopTest;
}
//</editor-fold>//GEN-END:|77-getter|2|











//<editor-fold defaultstate="collapsed" desc=" Generated Getter: answerCommand ">//GEN-BEGIN:|92-getter|0|92-preInit
/**
 * Returns an initiliazed instance of answerCommand component.
 * @return the initialized component instance
 */
public Command getAnswerCommand() {
    if (answerCommand == null) {//GEN-END:|92-getter|0|92-preInit
            // write pre-init user code here
        answerCommand = new Command("Answer", Command.SCREEN, 0);//GEN-LINE:|92-getter|1|92-postInit
            // write post-init user code here
    }//GEN-BEGIN:|92-getter|2|
    return answerCommand;
}
//</editor-fold>//GEN-END:|92-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: nextFlashCommand ">//GEN-BEGIN:|94-getter|0|94-preInit
/**
 * Returns an initiliazed instance of nextFlashCommand component.
 * @return the initialized component instance
 */
public Command getNextFlashCommand() {
    if (nextFlashCommand == null) {//GEN-END:|94-getter|0|94-preInit
            // write pre-init user code here
        nextFlashCommand = new Command("Next", Command.SCREEN, 0);//GEN-LINE:|94-getter|1|94-postInit
            // write post-init user code here
    }//GEN-BEGIN:|94-getter|2|
    return nextFlashCommand;
}
//</editor-fold>//GEN-END:|94-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: optionsList ">//GEN-BEGIN:|99-getter|0|99-preInit
/**
 * Returns an initiliazed instance of optionsList component.
 * @return the initialized component instance
 */
public List getOptionsList() {
    if (optionsList == null) {//GEN-END:|99-getter|0|99-preInit
            // write pre-init user code here
        optionsList = new List("Options", Choice.IMPLICIT);//GEN-BEGIN:|99-getter|1|99-postInit
        optionsList.append("Select Kanji", null);
        optionsList.append("Other", null);
        optionsList.append("Error Log", null);
        optionsList.addCommand(getBackCommand());
        optionsList.setCommandListener(this);
        optionsList.setSelectedFlags(new boolean[] { false, false, false });//GEN-END:|99-getter|1|99-postInit
            // write post-init user code here
    }//GEN-BEGIN:|99-getter|2|
    return optionsList;
}
//</editor-fold>//GEN-END:|99-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: optionsListAction ">//GEN-BEGIN:|99-action|0|99-preAction
/**
 * Performs an action assigned to the selected list element in the optionsList component.
 */
public void optionsListAction() {//GEN-END:|99-action|0|99-preAction
        // enter pre-action user code here
    String __selectedString = getOptionsList().getString(getOptionsList().getSelectedIndex());//GEN-BEGIN:|99-action|1|102-preAction
    if (__selectedString != null) {
        if (__selectedString.equals("Select Kanji")) {//GEN-END:|99-action|1|102-preAction
            // write pre-action user code here
//GEN-LINE:|99-action|2|102-postAction
            // write post-action user code here
            switchDisplayable(null, getTreeOptions());
            getTreeOptions().Show(menuList);
        } else if (__selectedString.equals("Other")) {//GEN-LINE:|99-action|3|104-preAction
            // write pre-action user code here
            getChoiceGroup().setSelectedFlags(new boolean[]{configStorage.isLoggingEnabled()});
            getTextField().setString(String.valueOf(configStorage.getLogLinesCount()));
            switchDisplayable(null, getSettingsForm());//GEN-LINE:|99-action|4|104-postAction
            // write post-action user code here
        } else if (__selectedString.equals("Error Log")) {//GEN-LINE:|99-action|5|135-preAction
            // write pre-action user code here
            PrepareErrorList();
            getErrorTableItem().setModel(getErrorTableModel());
            switchDisplayable(null, getErrorLogForm());//GEN-LINE:|99-action|6|135-postAction
            // write post-action user code here
        }//GEN-BEGIN:|99-action|7|99-postAction
    }//GEN-END:|99-action|7|99-postAction
        // enter post-action user code here
}//GEN-BEGIN:|99-action|8|
//</editor-fold>//GEN-END:|99-action|8|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: flashCardCanvas ">//GEN-BEGIN:|76-getter|0|76-preInit
/**
 * Returns an initiliazed instance of flashCardCanvas component.
 * @return the initialized component instance
 */
public FlashCardCanvas getFlashCardCanvas() {
    if (flashCardCanvas == null) {//GEN-END:|76-getter|0|76-preInit
            // write pre-init user code here
            try{
                flashCardCanvas = new FlashCardCanvas("/imgdef.dat");//GEN-BEGIN:|76-getter|1|76-postInit
                flashCardCanvas.setTitle("flashCardCanvas");
                flashCardCanvas.addCommand(getStopTest());
                flashCardCanvas.addCommand(getAnswerCommand());
                flashCardCanvas.addCommand(getNextFlashCommand());
                flashCardCanvas.setCommandListener(this);//GEN-END:|76-getter|1|76-postInit
            // write post-init user code here
                flashCardCanvas.setErrorLogger(this);
            }catch(IOException ex){
                LogError(ex.toString(), "Error creating flash card canvas");
                ex.printStackTrace();
            }
    }//GEN-BEGIN:|76-getter|2|
    return flashCardCanvas;
}
//</editor-fold>//GEN-END:|76-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|105-getter|0|105-preInit
/**
 * Returns an initiliazed instance of backCommand component.
 * @return the initialized component instance
 */
public Command getBackCommand() {
    if (backCommand == null) {//GEN-END:|105-getter|0|105-preInit
        // write pre-init user code here
        backCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|105-getter|1|105-postInit
        // write post-init user code here
    }//GEN-BEGIN:|105-getter|2|
    return backCommand;
}
//</editor-fold>//GEN-END:|105-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: splashScreen ">//GEN-BEGIN:|110-getter|0|110-preInit
/**
 * Returns an initiliazed instance of splashScreen component.
 * @return the initialized component instance
 */
public SplashScreen getSplashScreen() {
    if (splashScreen == null) {//GEN-END:|110-getter|0|110-preInit
        // write pre-init user code here
        splashScreen = new SplashScreen(getDisplay());//GEN-BEGIN:|110-getter|1|110-postInit
        splashScreen.setTitle("kanjitraining@gmail.com");
        splashScreen.setCommandListener(this);
        splashScreen.setImage(getImage());
        splashScreen.setTimeout(3000);//GEN-END:|110-getter|1|110-postInit
        // write post-init user code here
    }//GEN-BEGIN:|110-getter|2|
    return splashScreen;
}
//</editor-fold>//GEN-END:|110-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: image ">//GEN-BEGIN:|115-getter|0|115-preInit
/**
 * Returns an initiliazed instance of image component.
 * @return the initialized component instance
 */
public Image getImage() {
    if (image == null) {//GEN-END:|115-getter|0|115-preInit
        // write pre-init user code here
        try {//GEN-BEGIN:|115-getter|1|115-@java.io.IOException
            image = Image.createImage("/splash.png");
        } catch (java.io.IOException e) {//GEN-END:|115-getter|1|115-@java.io.IOException
            LogError(e.toString(), "Error loading splash screen");
            e.printStackTrace();
        }//GEN-LINE:|115-getter|2|115-postInit
        // write post-init user code here
    }//GEN-BEGIN:|115-getter|3|
    return image;
}
//</editor-fold>//GEN-END:|115-getter|3|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: settingsForm ">//GEN-BEGIN:|119-getter|0|119-preInit
/**
 * Returns an initiliazed instance of settingsForm component.
 * @return the initialized component instance
 */
public Form getSettingsForm() {
    if (settingsForm == null) {//GEN-END:|119-getter|0|119-preInit
        // write pre-init user code here
        settingsForm = new Form("Settings", new Item[] { getChoiceGroup(), getTextField() });//GEN-BEGIN:|119-getter|1|119-postInit
        settingsForm.addCommand(getSaveSettingsCommand());
        settingsForm.addCommand(getCancelSettingsCommand());
        settingsForm.setCommandListener(this);//GEN-END:|119-getter|1|119-postInit
        // write post-init user code here
    }//GEN-BEGIN:|119-getter|2|
    return settingsForm;
}
//</editor-fold>//GEN-END:|119-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: saveSettingsCommand ">//GEN-BEGIN:|124-getter|0|124-preInit
/**
 * Returns an initiliazed instance of saveSettingsCommand component.
 * @return the initialized component instance
 */
public Command getSaveSettingsCommand() {
    if (saveSettingsCommand == null) {//GEN-END:|124-getter|0|124-preInit
        // write pre-init user code here
        saveSettingsCommand = new Command("Save", Command.OK, 0);//GEN-LINE:|124-getter|1|124-postInit
        // write post-init user code here
    }//GEN-BEGIN:|124-getter|2|
    return saveSettingsCommand;
}
//</editor-fold>//GEN-END:|124-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelSettingsCommand ">//GEN-BEGIN:|126-getter|0|126-preInit
/**
 * Returns an initiliazed instance of cancelSettingsCommand component.
 * @return the initialized component instance
 */
public Command getCancelSettingsCommand() {
    if (cancelSettingsCommand == null) {//GEN-END:|126-getter|0|126-preInit
        // write pre-init user code here
        cancelSettingsCommand = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|126-getter|1|126-postInit
        // write post-init user code here
    }//GEN-BEGIN:|126-getter|2|
    return cancelSettingsCommand;
}
//</editor-fold>//GEN-END:|126-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: choiceGroup ">//GEN-BEGIN:|121-getter|0|121-preInit
/**
 * Returns an initiliazed instance of choiceGroup component.
 * @return the initialized component instance
 */
public ChoiceGroup getChoiceGroup() {
    if (choiceGroup == null) {//GEN-END:|121-getter|0|121-preInit
        // write pre-init user code here
        choiceGroup = new ChoiceGroup("Logging", Choice.MULTIPLE);//GEN-BEGIN:|121-getter|1|121-postInit
        choiceGroup.append("On", null);
        choiceGroup.setSelectedFlags(new boolean[] { false });
        choiceGroup.setFont(0, null);//GEN-END:|121-getter|1|121-postInit
        // write post-init user code here
    }//GEN-BEGIN:|121-getter|2|
    return choiceGroup;
}
//</editor-fold>//GEN-END:|121-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField ">//GEN-BEGIN:|123-getter|0|123-preInit
/**
 * Returns an initiliazed instance of textField component.
 * @return the initialized component instance
 */
public TextField getTextField() {
    if (textField == null) {//GEN-END:|123-getter|0|123-preInit
        // write pre-init user code here
        textField = new TextField("Log History Count", null, 4, TextField.NUMERIC);//GEN-LINE:|123-getter|1|123-postInit
        // write post-init user code here
    }//GEN-BEGIN:|123-getter|2|
    return textField;
}
//</editor-fold>//GEN-END:|123-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: errorLogForm ">//GEN-BEGIN:|130-getter|0|130-preInit
/**
 * Returns an initiliazed instance of errorLogForm component.
 * @return the initialized component instance
 */
public Form getErrorLogForm() {
    if (errorLogForm == null) {//GEN-END:|130-getter|0|130-preInit
        // write pre-init user code here
        errorLogForm = new Form("Error Log", new Item[] { getErrorTableItem() });//GEN-LINE:|130-getter|1|130-postInit
        // write post-init user code here
    }//GEN-BEGIN:|130-getter|2|
    return errorLogForm;
}
//</editor-fold>//GEN-END:|130-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: errorTableItem ">//GEN-BEGIN:|131-getter|0|131-preInit
/**
 * Returns an initiliazed instance of errorTableItem component.
 * @return the initialized component instance
 */
public TableItem getErrorTableItem() {
    if (errorTableItem == null) {//GEN-END:|131-getter|0|131-preInit
        // write pre-init user code here
        errorTableItem = new TableItem(getDisplay(), "Errors");//GEN-BEGIN:|131-getter|1|131-postInit
        errorTableItem.addCommand(getCloseErrorLogCommand());
        errorTableItem.setItemCommandListener(this);//GEN-END:|131-getter|1|131-postInit
        // write post-init user code here
    }//GEN-BEGIN:|131-getter|2|
    return errorTableItem;
}
//</editor-fold>//GEN-END:|131-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Items ">//GEN-BEGIN:|17-itemCommandAction|0|17-preItemCommandAction
/**
 * Called by a system to indicated that a command has been invoked on a particular item.
 * @param command the Command that was invoked
 * @param displayable the Item where the command was invoked
 */
public void commandAction(Command command, Item item) {//GEN-END:|17-itemCommandAction|0|17-preItemCommandAction
    // write pre-action user code here
    if (item == errorTableItem) {//GEN-BEGIN:|17-itemCommandAction|1|134-preAction
        if (command == closeErrorLogCommand) {//GEN-END:|17-itemCommandAction|1|134-preAction
            // write pre-action user code here
            switchDisplayable(null, getMenuList());//GEN-LINE:|17-itemCommandAction|2|134-postAction
            // write post-action user code here
        }//GEN-BEGIN:|17-itemCommandAction|3|17-postItemCommandAction
    }//GEN-END:|17-itemCommandAction|3|17-postItemCommandAction
    // write post-action user code here
}//GEN-BEGIN:|17-itemCommandAction|4|
//</editor-fold>//GEN-END:|17-itemCommandAction|4|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: closeErrorLogCommand ">//GEN-BEGIN:|133-getter|0|133-preInit
/**
 * Returns an initiliazed instance of closeErrorLogCommand component.
 * @return the initialized component instance
 */
public Command getCloseErrorLogCommand() {
    if (closeErrorLogCommand == null) {//GEN-END:|133-getter|0|133-preInit
        // write pre-init user code here
        closeErrorLogCommand = new Command("Close", Command.CANCEL, 0);//GEN-LINE:|133-getter|1|133-postInit
        // write post-init user code here
    }//GEN-BEGIN:|133-getter|2|
    return closeErrorLogCommand;
}
//</editor-fold>//GEN-END:|133-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: errorTableModel ">//GEN-BEGIN:|132-getter|0|132-preInit
/**
 * Returns an initiliazed instance of errorTableModel component.
 * @return the initialized component instance
 */
public SimpleTableModel getErrorTableModel() {
    if (errorTableModel == null) {//GEN-END:|132-getter|0|132-preInit
        // write pre-init user code here
        errorTableModel = new SimpleTableModel(new java.lang.String[][] {}, new java.lang.String[] { "Date", "Message", "Details" });//GEN-LINE:|132-getter|1|132-postInit
        // write post-init user code here
    }//GEN-BEGIN:|132-getter|2|
    return errorTableModel;
}
//</editor-fold>//GEN-END:|132-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: alert ">//GEN-BEGIN:|138-getter|0|138-preInit
/**
 * Returns an initiliazed instance of alert component.
 * @return the initialized component instance
 */
public Alert getAlert() {
    if (alert == null) {//GEN-END:|138-getter|0|138-preInit
        // write pre-init user code here
        alert = new Alert("alert");//GEN-BEGIN:|138-getter|1|138-postInit
        alert.setTimeout(Alert.FOREVER);//GEN-END:|138-getter|1|138-postInit
        // write post-init user code here
    }//GEN-BEGIN:|138-getter|2|
    return alert;
}
//</editor-fold>//GEN-END:|138-getter|2|






































    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay () {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable (null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet ();
        } else {
            initialize ();
            startMIDlet ();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }

    public TreeOptions getTreeOptions() {
    if (treeOptions == null) {
        // write pre-init user code here
        try{
        treeOptions = new TreeOptions("Select Kanji", this);
        treeOptions.LoadGroups();
        }catch(IOException ex){
            LogError(ex.toString(), "Error loading tree options");
            ex.printStackTrace();
        }
    }
    return treeOptions;
}
    public void returnTo(Displayable d){
       switchDisplayable(null, d);
    }

    public void SaveConfiguration(IGroupDefinition root){
        try {
            GroupDefinitionDeserializer.Save(root);
            root.recountSelectedGlyphs();
        } catch (IOException ex) {
            LogError(ex.toString(), "Error saving configuration");
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            LogError(ex.toString(), "Error saving configuration");
            ex.printStackTrace();
        }
    }

    public IGroupDefinition getGroupDefinitions(){
        if(config == null){
            try {
                config = GroupDefinitionDeserializer.Deserialize("/groupdef.dat", this);
                config.recountSelectedGlyphs();
            } catch (IOException ex) {
                LogError(ex.toString(), "Error getting group definitions");
                ex.printStackTrace();
            }
        }
        return config;
    }

    public void LogError(String message, String details){
        if(logging == null){
            return;
        }

        logging.LogError(message, details);
    }
}
