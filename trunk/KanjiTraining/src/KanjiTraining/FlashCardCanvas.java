/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;

import java.io.IOException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import java.util.Date;
import java.util.Calendar;

/**
 *
 * @author mlalevic
 */
public class FlashCardCanvas extends CharacterCanvas  {
    private CharacterDefinition current;
    private CharacterDefinition buffered;
    private boolean kanjiFirst = false;
    private boolean showAnswer = false;
    private int count = 0;
    private long startDate = System.currentTimeMillis();
    private Calendar calendar = Calendar.getInstance();

    public FlashCardCanvas(String imageDefinitionRecource) throws IOException{
        super(imageDefinitionRecource);
        current = null;
    }

    public void setBuffered(CharacterDefinition def){
        buffered = def;
    }

    protected void paint(Graphics g){
        realPaint(g);
        if(!showAnswer){
            //preload buffer
            try{
                if(buffered != null){
                    //getMultiImage().SetBufferedByY(buffered.getRect().getY());
                }
            }catch(Exception ex){
                LogError(ex.toString(), "Error in preload");
            }
        }
    }

    protected void realPaint(Graphics g) {
        super.clear(g);

        if(kanjiFirst || showAnswer){
            try{
            super.paintEx(g);
            }catch(Exception ex){
                g.drawString(ex.toString(), 0, 55, 0);
                g.drawString("Error in paint!", 0, 70, 0);
                LogError(ex.toString(), "Error in paint");
            }
        }
        DrawStatistics(g);
        
        if(kanjiFirst && !showAnswer){return;}

        if(current == null){
            return;
        }

        Font currentFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
        g.setFont(currentFont);
        g.setColor(0x0); //black

        int h = currentFont.getHeight();
        int w = currentFont.stringWidth(current.getText());
        //TODO wrap if required
        int x = (getWidth() - w) / 2;
        x = x < 0?0:x;
        int y = (getHeight()/2 + current.getRect().getHeight() / 2 + 20);
        y = (y + h) > getHeight()?(getHeight() - h):y;
        g.drawString(current.getText(), x, y, 0);
    }

    /**
     * @return the current
     */
    public CharacterDefinition getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void ShowNext(CharacterDefinition current) {
        count ++;
        if(current == null){
            return;
        }
        this.current = current;
        super.ShowCharacter(current.getRect());
        HideAnswer();
        repaint();
    }

    public void ShowAnswer(){
        showAnswer = true;
        repaint();
    }

    public void HideAnswer(){
        showAnswer = false;
    }

    /**
     * @return the kanjiFirst
     */
    public boolean isKanjiFirst() {
        return kanjiFirst;
    }

    /**
     * @param kanjiFirst the kanjiFirst to set
     */
    public void setKanjiFirst(boolean kanjiFirst) {
        this.kanjiFirst = kanjiFirst;
    }

    private void DrawStatistics(Graphics g) {
        g.drawString(String.valueOf(count), 5, 5, Graphics.TOP | Graphics.LEFT);
        Date diff = new Date(System.currentTimeMillis() - startDate);
        calendar.setTime(diff);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String hourMinute = hour >= 10 ? String.valueOf(hour) : "0".concat(String.valueOf(hour));
        hourMinute = hourMinute.concat(":");
        hourMinute = hourMinute.concat(minute >= 10 ? String.valueOf(minute) : "0".concat(String.valueOf(minute)));
        g.drawString(hourMinute, getWidth() - 5, 5, Graphics.RIGHT | Graphics.TOP);
    }
}
