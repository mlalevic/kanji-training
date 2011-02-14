/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;
import java.io.*;
import javax.microedition.rms.*;

/**
 *
 * @author mlalevic
 */
public class ConfigurationStorage {
    private int logLinesCount = 10;
    private boolean loggingEnabled = false;
    private final String dbName = "kanjiConfig";
    private final int configIndex = 1;

    public ConfigurationStorage(){
        try{
            RecordStore rs = RecordStore.openRecordStore(dbName, true);
            if(rs.getNumRecords() == 0){
                Save(rs);
            }else{
                Read(rs);
            }
            rs.closeRecordStore();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * @return the logLinesCount
     */
    public int getLogLinesCount() {
        return logLinesCount;
    }

    /**
     * @param logLinesCount the logLinesCount to set
     */
    public void setLogLinesCount(int logLinesCount) {
        this.logLinesCount = logLinesCount;
    }

    /**
     * @return the loggingEnabled
     */
    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    /**
     * @param loggingEnabled the loggingEnabled to set
     */
    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    private void Read(RecordStore rs) {
        try{
            byte[] data = rs.getRecord(configIndex);
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
            loggingEnabled = dis.readBoolean();
            logLinesCount = dis.readInt();
            dis.close(); //byte array input stream will be closed automatically
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void Save(RecordStore rs) {
        try{
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bo);
            dos.writeBoolean(loggingEnabled);
            dos.writeInt(logLinesCount);
            bo.flush();
            byte[] data = bo.toByteArray();
            dos.close(); //bo will be closed by data output stream

            if(rs.getNumRecords() == 0){
                rs.addRecord(data, 0, data.length);
            }else{
                rs.setRecord(configIndex, data, 0, data.length);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void Save(){
        try{
            RecordStore rs = RecordStore.openRecordStore(dbName, true);
            Save(rs);
            rs.closeRecordStore();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void Reload(){
        try{
            RecordStore rs = RecordStore.openRecordStore(dbName, true);
            Read(rs);
            rs.closeRecordStore();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
