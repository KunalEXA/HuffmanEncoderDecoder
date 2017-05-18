package huffmanencoderdecoder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class contains common functions like tree walk, print frequency table and print frequency table that is used by encoder and decoder classes
 * @author      Kunal
 * @package     huffmanencoderdecoder
 */
public class HuffmanCommon {
    
    /*
     * Function to print generated Huffman Tree. Primarly used for debugging
     */
    public void treeWalk(Node n)
    {
        if(n != null)
        {
            treeWalk(n._treeLeft);
            treeWalk(n._treeRight);
            if(n._value >= 0)
                System.out.println(n._value);
        }
    }
    
    
    /*
     * Debugging function to print generated codes table
     */
    public void printCodesTable(Map<Integer, String> _codes)
    {
        if(!_codes.isEmpty())
        {
            for (Map.Entry pair : _codes.entrySet()) 
            {
                System.out.println(pair.getKey() + " = " + pair.getValue());
            }
        }
    }
    
}
