/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kanjitraininggenerators;
import java.io.*;
import java.util.*;
/**
 *
 * @author mlalevic
 */
public class OutputStreamHelper {
    FileOutputStream fs;
    DataOutputStream ds;

    public OutputStreamHelper (String fileName) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        File f = new File("/home/mlalevic/Projects/Java/KanjiTrainingGenerators/src/kanjitraininggenerators/".concat(fileName));
        fs = new FileOutputStream(f);
        ds = new DataOutputStream(fs);
    }

    public void WriteShort(short data) throws IOException{
        ds.writeShort(data);
    }

    public void WriteShort(String data) throws IOException{
        ds.writeShort((short) Integer.parseInt(data));
    }

    public void WriteByte(String data) throws IOException{
        ds.writeByte((byte) Integer.parseInt(data));
    }

    public void WriteByte(char data) throws IOException{
        ds.writeByte((byte)data);
    }

    public void WriteString(String data) throws IOException{
        ds.writeUTF(data);
    }
    public void Close() throws IOException{
        ds.close();
        fs.close();
    }
}
