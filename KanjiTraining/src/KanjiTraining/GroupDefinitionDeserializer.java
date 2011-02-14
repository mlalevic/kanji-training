/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;

import java.util.Vector;
import java.io.*;
import javax.microedition.rms.*;

/**
 *
 * @author mlalevic
 */
public class GroupDefinitionDeserializer {
    private static final String dbName = "GroupDef";
    private static final byte NodeFlag = 1;
    private static final byte LeafFlag = 2;
    private static final int ConfigId = 1;

    public static void Save(IGroupDefinition g) throws IOException, RecordStoreException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(os);
        Save(g, ds);
        os.flush();
        byte[] record = os.toByteArray();
        RecordStore rs = RecordStore.openRecordStore(dbName, true);
        rs.setRecord(ConfigId, record, 0, record.length);
        rs.closeRecordStore();
    }

    private static IGroupDefinition Load(RecordStore recordstore) throws RecordStoreException, IOException {
        byte[] data = recordstore.getRecord(ConfigId);
        DataInputStream ids = new DataInputStream(new ByteArrayInputStream(data));
        IGroupDefinition root = Load(ids);
        ids.close();
        return root;
    }
    
    private static IGroupDefinition Load(DataInputStream ids) throws IOException {
        switch(ids.readByte()){
            case LeafFlag:
                return LoadLeaf(ids);
            case NodeFlag:
                return LoadNode(ids);
        }
        return null;
    }

    private static IGroupDefinition LoadLeaf(DataInputStream ids) throws IOException {
        boolean selected = ids.readByte() == IGroupDefinition.SELECTED;
        return new GroupDefinition(ids.readUTF(), ids.readInt(), ids.readInt(), selected);
    }

    private static IGroupDefinition LoadNode(DataInputStream ids) throws IOException {
        short childrenCount = ids.readShort();
        GroupDefinitionNode def = new GroupDefinitionNode(ids.readUTF());
        for(short i = 0; i < childrenCount; i++){
            def.addChild(Load(ids));
        }
        return def;
    }
    private static void Save(IGroupDefinition g, DataOutputStream ds) throws IOException {
        if(g.isLeaf()){
            SaveDefinition((GroupDefinition)g, ds);
        }else{
            SaveDefinition((GroupDefinitionNode)g, ds);
        }
    }
    private static void SaveDefinition(GroupDefinitionNode groupDefinition, DataOutputStream ds) throws IOException {
        ds.writeByte(NodeFlag);
        ds.writeShort((short)groupDefinition.getChildren().size());
        ds.writeUTF(groupDefinition.getTitle());
        for(int i = 0; i < groupDefinition.getChildren().size(); i++){
            Save((IGroupDefinition)groupDefinition.getChildren().elementAt(i), ds);
        }
    }
    private static void SaveDefinition(GroupDefinition def, DataOutputStream ds) throws IOException {
        ds.writeByte(LeafFlag);
        ds.writeByte(def.getSelectedStatus());
        ds.writeUTF(def.getTitle());
        ds.writeInt(def.getFrom());
        ds.writeInt(def.getTo());
    }

    //private int currentIndex;
    private Vector allData;
    private DataInputStream is;
    //StringBuffer sb;
    IGroupDefinition node;

    private GroupDefinitionDeserializer(DataInputStream is){
        node = new GroupDefinitionNode("Kanji list");
        allData = new Vector();
        //currentIndex = 0;
        this.is = is;
        //sb = new StringBuffer();
    }

    private void Load() throws IOException{
        for(int i=0; i < 59; i++){
            AddElement();
        }
		//is.close(); //don't close it it will be closed by the caller
        is = null; //locally we don't need reference anymore
        allData.trimToSize();
    }

    private static void Save(IGroupDefinition g, RecordStore rs, int id) throws IOException, RecordStoreException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(os);
        Save(g, ds);
        os.flush();
        byte[] record = os.toByteArray();
        if(id == -1){
            rs.addRecord(record, 0, record.length);
        }else{
            rs.setRecord(id, record, 0, record.length);
        }
        ds.close(); //will close os
    }
    public static IGroupDefinition Deserialize(String resource, ErrorLogger logger) throws IOException{
        try {
            RecordStore recordstore = RecordStore.openRecordStore(dbName, true);
            if(recordstore.getNumRecords() == 0){
                DataInputStream dis = new DataInputStream(GroupDefinitionDeserializer.class.getResourceAsStream(resource));
                GroupDefinitionDeserializer d = new GroupDefinitionDeserializer(dis);
                d.Load();
                dis.close();
                //select first by default so it works out of the box
                ((IGroupDefinition)d.node.getChildren().elementAt(0)).select();
                Save(d.node, recordstore, -1);
                recordstore.closeRecordStore();
                return d.node;
            }else{
               return Load(recordstore);
            }
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
            //TODO show alert
            if(logger != null){
                logger.LogError(ex.toString(), "Error in character canvas paint");
            }
            return new GroupDefinitionNode("null");
        }
    }
    
    public void AddElement() throws IOException{
        int index = is.readByte(); //not using at the moment
        char type = (char)is.readByte();
        int parentIndex = -1;

        switch(type){ //get char on current index and move to the next
            case 'R':
                //do root
                IGroupDefinition defR = new GroupDefinitionNode(is.readUTF());
                node.addChild(defR);
                allData.addElement(defR);
                break;
            case 'L':
                //do leaf
                //read parent
                parentIndex = is.readByte();
                int from = is.readShort();
                int to = is.readShort();
                IGroupDefinition defL = new GroupDefinition(is.readUTF(), from, to, false);
                allData.addElement(defL);
                ((IGroupDefinition)allData.elementAt(parentIndex)).addChild(defL);
                break;
            case 'N':
                //do node
                //read parent
                parentIndex = is.readByte();
                IGroupDefinition defN = new GroupDefinitionNode(is.readUTF());
                allData.addElement(defN);
                ((IGroupDefinition)allData.elementAt(parentIndex)).addChild(defN);
                break;
        }
        
    }
}
