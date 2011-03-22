package kanjitraininggenerators;

import java.io.*;
import java.util.*;
/**
 *
 * @author mlalevic
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        //Action_ConvertImgDefToDat();
        //Action_ConvertKanjiDefToDat();
        Action_ConvertGroupDefToDat();
    }

    private final static int KanjiCount = 2042;

    private static void Action_ConvertImgDefToDat() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        InputStreamHelper is = new InputStreamHelper("imgdef.txt");
        OutputStreamHelper os = new OutputStreamHelper("imgdef.dat");
        String line;
        while((line = is.ReadLine("\r\n")) != null){
            String[] parts = line.split(" ");
            short y = (short)Integer.parseInt(parts[0]);
            String[] fromTo = parts[1].split("-");
            short from = (short)Integer.parseInt(fromTo[0]);
            short to = (short)Integer.parseInt(fromTo[1]);
            os.WriteShort(y);
            os.WriteShort(from);
            os.WriteShort(to);
            os.WriteString("/".concat(parts[2]));
        }
        os.Close();
    }

    private static void Action_ConvertKanjiDefToDat() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        InputStreamHelper is = new InputStreamHelper("kanjidef.txt");
        OutputStreamHelper os = new OutputStreamHelper("kanjidef.dat");
        String line;
        while((line = is.ReadLine("\r\n")) != null){
            String[] parts = line.split("\\[");
            String[] numbers = parts[1].substring(0, parts[1].length()-1).split(",");
            os.WriteString(parts[0]);
            os.WriteShort(numbers[0]); //x
            os.WriteShort(numbers[1]); //y
            os.WriteShort(numbers[2]); //width
            os.WriteShort(numbers[3]); //height
        }
        os.Close();
    }

    private static void Action_ConvertGroupDefToDat() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        String inFile = "kanagroups.txt"; //"groupdef.txt";
        String outFile = "kanagroups.dat"; //"groupdef.dat";
        InputStreamHelper is = new InputStreamHelper(inFile);
        OutputStreamHelper os = new OutputStreamHelper(outFile);
        String line;

        //write number of groups first
        os.WriteShort((short)73);

        while((line = is.ReadLine("\n")) != null){
            String[] parts = line.split(":");
            if(parts.length == 2){
                //root
                os.WriteByte(parts[0]); //index
                os.WriteByte('R');
                os.WriteString(parts[1].substring(1)); //skip R from the file
            }else{
                //leaf
                os.WriteByte(parts[0]); //index
                if(parts[1].substring(0, 1).equals("G")){
                    os.WriteByte('N');
                    os.WriteByte(parts[1].substring(1)); //skip G from the parent def
                    os.WriteString(parts[3]); //description
                }else{
                    os.WriteByte('L');
                    os.WriteByte(parts[1].substring(1)); //skip L from the parent def
                    String[] numbers = parts[2].split("-");
                    os.WriteShort(numbers[0]); //from
                    os.WriteShort(numbers[1]); //to
                    os.WriteString(parts[3]); //description
                }
            }
        }
        os.Close();
    }

}
