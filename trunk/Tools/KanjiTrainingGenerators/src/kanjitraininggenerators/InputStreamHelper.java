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
public class InputStreamHelper {
    String data;
    int index = 0;
    public InputStreamHelper (String fileName) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        File f = new File("/home/mlalevic/Projects/Java/KanjiTrainingGenerators/src/kanjitraininggenerators/".concat(fileName));
        FileInputStream fs = new FileInputStream(f);
        InputStreamReader sr = new InputStreamReader(fs);
        BufferedReader r = new BufferedReader(sr);
        char[] buf = new char[(int)f.length()];
        r.read(buf);
        data = String.valueOf(buf);
    }

    public String ReadLine(String split) throws IOException {
        int oldIndex = index;
        if(data.length() <= index){
            return null;
        }
        int newIndex = data.indexOf(split, oldIndex);
        index = newIndex + split.length();
        return data.substring(oldIndex, newIndex);
    }
}
