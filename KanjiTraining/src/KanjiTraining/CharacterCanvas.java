/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;

import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;

/**
 * @author mlalevic
 */
public class CharacterCanvas extends Canvas {

    private ErrorLogger logger;
    private MultipleImage multiImage;
    private Rectangle currentCharacter;
    private final int White = 0xFFFFFF;
    /**
     * constructor
     */
    public CharacterCanvas(String imageDefinitionResource) throws IOException {
        multiImage = new MultipleImage(imageDefinitionResource);
        currentCharacter = null;
    }

    public MultipleImage getMultiImage(){
        return multiImage;
    }

    public void setErrorLogger(ErrorLogger logger){
        this.logger = logger;
    }

    public void ShowCharacter(Rectangle definition){
        currentCharacter = definition;
    }

    protected void clear(Graphics g){
        int color = g.getColor();
        g.setColor(White);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(color);
    }

    protected void paintEx(Graphics g){
         try {
            if (currentCharacter == null) {
                return;
            }
            Image characters = multiImage.SetImageByY(currentCharacter.getY());
            if(characters == null){
                g.setColor(0x0);
                g.drawString("Error", 0, 30, 0);
                String toShow = "(".concat(multiImage.getDef().getResourceName()).concat(")");
                g.drawString(toShow, 0, 15, 0);
                return;
            }
            int offsetX = (getWidth() - currentCharacter.getWidth()) / 2;
            int offsetY = (getHeight() - currentCharacter.getHeight()) / 2;
            int adjustedY = currentCharacter.getY() - multiImage.GetStartY();

            g.drawRegion(characters, currentCharacter.getX(), adjustedY, currentCharacter.getWidth(), currentCharacter.getHeight(), Sprite.TRANS_NONE, offsetX, offsetY, 0);
        } catch (IOException ex) {
            ex.printStackTrace();
            LogError(ex.toString(), "Error in character canvas paint");
            DrawErrorDetails(g, ex);
        } catch (IllegalArgumentException ex){
            ex.printStackTrace();
            LogError(ex.toString(), "Error in character canvas paint");
            DrawErrorDetails(g, ex);
        }
    }

    private void DrawErrorDetails(Graphics g, Exception ex){
        g.setColor(0x0);
        String toPrint = ex.toString();
        if(currentCharacter == null){
            g.drawString("null", 0, 15, 0);
        }
        else{
        g.drawString(toPrint, 0, 15, 0);
                toPrint = "("
                                .concat(String.valueOf(currentCharacter.getX()))
                                .concat(", ")
                                .concat(String.valueOf(currentCharacter.getY()))
                                .concat(", ")
                                .concat(String.valueOf(currentCharacter.getWidth()))
                                .concat(", ")
                                .concat(String.valueOf(currentCharacter.getHeight()))
                                .concat(")");
            g.drawString(toPrint, 0, 30, 0);
        }
        if(multiImage.getDef() == null){
            g.drawString("null", 0, 40, 0);
        }else{
            g.drawString(multiImage.getDef().getResourceName(), 0, 40, 0);
        }
        if(multiImage.getVec() == null){
            g.drawString("null", 0, 55, 0);
        }else{
            g.drawString(String.valueOf(multiImage.getVec().size()), 0, 55, 0);
        }
    }

    protected void paint(Graphics g) {
       this.clear(g);
       this.paintEx(g);
    }

    protected void LogError(String message, String details){
        if(logger == null)return;
        logger.LogError(message, details);
    }

}