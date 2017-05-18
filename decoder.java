/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmanencoderdecoder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kunal
 */
public class decoder {
    
    private Node _decodedHuffmanTree;
    private String _DECODEDFILENAME;
    private String _CODETABLE;
    private String _ENCODEDFILENAME;
    private final Map<Integer, String> _codes;
    
    public decoder()
    {
        _decodedHuffmanTree = null;
        _DECODEDFILENAME = "";
        _CODETABLE = "";
        _ENCODEDFILENAME = "";
        _codes = new HashMap();
    }
    
    private void decode()
    {
        if(!_codes.isEmpty() && _ENCODEDFILENAME.trim().compareTo("") != 0)
        {
            //Iterate over the hashmap
            for (Map.Entry pair : _codes.entrySet()) 
            {
                contructDecoderTree(pair.getValue().toString(), Integer.parseInt(pair.getKey().toString()));
            }
            System.out.println("Decode tree constructed:" + (System.currentTimeMillis() - startTime)/1000);
            try
            {
                //Get the encoded file
                File obj = new File(_ENCODEDFILENAME);
                byte[] encoded = new byte[(int)obj.length()];
                int totalBytesRead = 0;
                InputStream input = new BufferedInputStream(new FileInputStream(obj));
                while(totalBytesRead < encoded.length)
                {
                    int bytesRemaining = encoded.length - totalBytesRead;
                    int bytesRead = input.read(encoded, totalBytesRead, bytesRemaining);//parameters are byte[], offset and length
                    if (bytesRead > 0)
                    {
                        totalBytesRead = totalBytesRead + bytesRead;
                    }
                }
                input.close();
                System.out.println("Encoded file read:" + (System.currentTimeMillis() - startTime)/1000);
                //creation of decode.txt
                FileWriter fileWriter = new FileWriter(_DECODEDFILENAME);
                BufferedWriter out = new BufferedWriter(fileWriter);
                BitIterator bitIterator = new BitIterator(encoded);
                Node traversalTree = _decodedHuffmanTree;
                
                while(bitIterator.hasNext())
                {
                    if(bitIterator.getNextBit())
                        traversalTree = traversalTree._treeRight;
                    else 
                        traversalTree = traversalTree._treeLeft;
                    if(traversalTree._value != - 100)
                    {
                        out.write(traversalTree._value+"\n");
                        traversalTree = _decodedHuffmanTree;
                    }
                }
                //out.write("\n");
                System.out.println("Final output:" + (System.currentTimeMillis() - startTime)/1000);
                out.close();
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
    }
    
    private void contructDecoderTree(String s, int value)
    {
        if(null == _decodedHuffmanTree)
            _decodedHuffmanTree = new Node();
        
        Node current = _decodedHuffmanTree;
        for(int i=0; i<s.length(); i++)
        {
            if(s.charAt(i) == '0')
            {
                if(i == s.length()-1)
                {
                    Node newNode = new Node();
                    newNode._value = value;
                    current._treeLeft = newNode;
                }
                else
                {
                    if(null != current._treeLeft)
                        current = current._treeLeft;
                    else
                    {
                        Node newNode = new Node();
                        current._treeLeft = newNode;
                        current = newNode;
                    }
                }
            }
            else
            {
                if(i == s.length()-1)
                {
                    Node newNode = new Node();
                    newNode._value = value;
                    current._treeRight = newNode;
                }
                else
                {
                    if(null != current._treeRight)
                        current = current._treeRight;
                    else
                    {
                        Node newNode = new Node();
                        current._treeRight = newNode;
                        current = newNode;
                    }
                }
            }
        }
    }
    /*
    A simple function to build code table using code_table.txt file.
    Lines are split on tabs
    @author     Kunal Bajaj
    @params     None
    @returns    void
    @throws     FileNotFoundException, IOException
    */
    private void getSymbolTable()
    {
        if(_CODETABLE.compareTo("") != 0)
        {
            try
            {
                FileReader fr = new FileReader(_CODETABLE);
                BufferedReader br = new BufferedReader(fr);
                String line;
                String[] keyFrequency = new String[2];
                while((line = br.readLine()) != null)
                {
                    keyFrequency = line.trim().split("\\t");
                    
                    //System.out.println(keyFrequency.toString());
                    //Put values in the hashmap
                    if(isNumeric(keyFrequency[0]))
                        _codes.put(Integer.parseInt(keyFrequency[0]), keyFrequency[1]);
                }
                
                fr.close();
            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public static boolean isNumeric(String str)
    {
      return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    
    /*
    Initializes class variables and calls getSymbolTable to create symbol table
    @author     Kunal Bajaj
    @params     decodeFileName, codeTableName, encodedFileName
    */
    private void initialize(String decodeFileName, String codeTableName, String encodedFileName)
    {
        if(decodeFileName.trim().compareTo("") != 0)
            _DECODEDFILENAME = decodeFileName;
        
        if(codeTableName.trim().compareTo("") != 0)
            _CODETABLE = codeTableName;
        
        if(encodedFileName.trim().compareTo("") != 0)
            _ENCODEDFILENAME = encodedFileName;
        
        //Build symbol table
        getSymbolTable();
    }
    
    public void runDecodeProcess(String decodeFileName, String codeTableName, String encodedFileName)
    {
        //Call initialize to set appropriate values
        initialize(decodeFileName, codeTableName, encodedFileName);
        decode();
    }
    
    
    class BitIterator
    {
        byte[] byteArray;
        int arrayIndex = 0;
        int bitIndex = 0;
        final char[] masks = {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};

        public BitIterator(byte[] byteArray)
        {
            this.byteArray = byteArray;
        }

        boolean getNextBit()
        {
            if(bitIndex==8) 
            {
                arrayIndex++;
                bitIndex=0;
            }
            return (byteArray[arrayIndex]&masks[bitIndex++]) > 0;
        }

        boolean hasNext() 
        {
            return !(bitIndex == 8 && arrayIndex == (byteArray.length-1));
        }
    }
    
    public static void main(String args[])
    {
        decoder d = new decoder();
        
        //Call intialize to set all values
        d.runDecodeProcess("decoded.txt",args[1], args[0]);
    }
    
}
