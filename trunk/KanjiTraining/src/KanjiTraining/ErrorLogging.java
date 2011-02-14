/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;
import java.io.*;
import javax.microedition.rms.*;
import java.util.Calendar;
import java.util.Date;
/**
 *
 * @author mlalevic
 */
public class ErrorLogging {
    private static final String dbName = "ErrorLog";
    private static final int CurrentIndexIndex = 1;
    private static final int LogStartIndex = 2;

    private RecordStore rs = null;
    private int currentIndex = -1;
    ByteArrayOutputStream os;
    DataOutputStream ds;
    private int MaxCount = 0;
    private Calendar calendar = null;

    public void setMaxCount(int max){
        MaxCount = max;
    }

    public ErrorLogging(int max){
        MaxCount = max;
        try{
            rs = RecordStore.openRecordStore(dbName, true);
            if(rs.getNumRecords() == 0){
                currentIndex = LogStartIndex;
                WriteCurrentIndex();
            }else{
                currentIndex = ReadCurrentIndex();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public ErrorLogging(){
        this(100);
    }

    private void WriteCurrentIndex() throws IOException, RecordStoreException{
        if(rs == null)return;
        CreateOutputStream();
        ds.writeInt(currentIndex);
        byte[] data = GetOutputBytes();
        CloseOutputStream();
        if(rs.getNumRecords() == 0){
            rs.addRecord(data, 0, data.length);
        }else{
            rs.setRecord(CurrentIndexIndex, data, 0, data.length);
        }
    }

    private int ReadCurrentIndex() throws IOException, RecordStoreException{
        if(rs == null)return -1;
        byte[] data = rs.getRecord(CurrentIndexIndex);
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        int result = di.readInt();
        di.close();
        return result;
    }

    private String[] ReadRow(int rowIndex) throws IOException, RecordStoreException{
        if(rs == null)return null;
        if(rowIndex > rs.getNumRecords()) return null;

        byte[] data = rs.getRecord(rowIndex);
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        String[] result = new String[3];
        result[0] = getDateString(getCalendar(di.readLong()));
        result[1] = di.readUTF();
        result[2] = di.readUTF();
        di.close();
        return result;
    }

    private Calendar getCalendar(){
        if(calendar == null){
            calendar = Calendar.getInstance();
        }
        return calendar;
    }

    private Calendar getCalendar(long time){
        getCalendar().setTime(new Date(time));
        return getCalendar();
    }

    private void CreateOutputStream(){
        os = new ByteArrayOutputStream();
        ds = new DataOutputStream(os);
    }

    private void CloseOutputStream() throws IOException{
        ds.close(); //will close byte output stream (os)
        ds = null;
        os = null;
    }

    private byte[] GetOutputBytes() throws IOException{
        os.flush();
        return os.toByteArray();
    }

    public void LogError(String message, String details){
        try{
            if(rs == null)return;
            CreateOutputStream();
            ds.writeLong(System.currentTimeMillis());
            ds.writeUTF(message);
            ds.writeUTF(details);
            byte[] record = GetOutputBytes();
            CloseOutputStream();
            WriteLogRecord(record);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void WriteLogRecord(byte[] data) throws RecordStoreException, IOException{
        if(data == null)return;
        if(currentIndex <= 0)return;
        if(currentIndex > MaxCount + 1) currentIndex = LogStartIndex;
        if(currentIndex > rs.getNumRecords()){
            rs.addRecord(data, 0, data.length);
        }else{
            rs.setRecord(currentIndex, data, 0, data.length);
        }
        currentIndex ++;
        WriteCurrentIndex();
    }

    protected void finalize(){
        if(rs == null) return;
        try{
            rs.closeRecordStore();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public String[][] getErrors(){ //returns last 10 errors
        if(rs == null)return null;
        try{
            int totalCount = rs.getNumRecords() - 1; //minus starting index
            if(totalCount > MaxCount)totalCount = MaxCount;
            if(totalCount > 10)totalCount = 10;
            if(totalCount == 0)return null;
            int index = currentIndex - 1; //set starting index; we're going backwards
            String[][] result = new String[totalCount][];
            for(int i = 0; i < totalCount; i ++){
                if(index <= CurrentIndexIndex){ //skip the first row since there's configuration
                    index = rs.getNumRecords();
                    if(index > MaxCount){
                        index = MaxCount + 1; //this will work since if it is greater then MaxCount it is at least MaxCount+1
                    }
                }
                result[i] = ReadRow(index);
                index --; //we're going backwards
            }
            return result;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    private String getDateString(Calendar c){
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return String.valueOf(day)
                .concat("-").concat(String.valueOf(month))
                .concat("-").concat(String.valueOf(year))
                .concat(" ").concat(String.valueOf(hour))
                .concat(":").concat(String.valueOf(minute));
    }

    public void Test(int count){
        for(int i = 0; i < count; i++){
            LogError("Message".concat(String.valueOf(i)), String.valueOf(i));
        }
    }
}
