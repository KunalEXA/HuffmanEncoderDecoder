/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmanencoderdecoder;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Kunal
 */
public class encoder extends HuffmanEncoder{
    
    public Node _huffmanTreeNode;
    private Node _priorityQueue;
    private boolean _isInitialized;
    private PairingHeap _heapObj;
    private BinaryHeap _heapObjBinary;
    private FourWayHeap _heapObjFourWay;
    private final Map<Integer, String> _codes;
    private String _FILENAME;
    private final int[] _frequencyTable;
    private final String _ENCODEDFILENAME;
    private final String _CODETABLENAME;
    
    public encoder()
    {
        _huffmanTreeNode = null;
        _isInitialized = false;
        _codes = new HashMap();
        _frequencyTable = new int[1000000];
        _ENCODEDFILENAME = "encoded.bin";
        _CODETABLENAME = "code_table.txt";
    }
    
    private void initializeTree(Node head)
    {
        //Get access to priority queue
        _heapObj = new PairingHeap();
        _heapObj._pairingHeapHead = head;
        _priorityQueue = head;
    }
    
    private void initializeTree(BinaryHeap obj)
    {
        _heapObjBinary = obj;
    }
    
    private void initializeTree(FourWayHeap obj)
    {
        _heapObjFourWay = obj;
    }
    
    /*
    This function is written for pairing heaps.
    This takes in head of pairing heap as parameter. If the initial values for trees are not already initizlied then it calls initialize funtion.
    While heap head is not null it keeps on performing merge on a tuple of min nodes and inserts it back into the tree.
    @author     Kunal Bajaj
    @params     Node heap - head of the pairing heap
    @returns    Nothing. Just constructs a huffman tree
    @throws     No exception
    */
    public void constructTree(Node heap)
    {
        if(null != heap)
        {
            //Initialize parameters if not done already
            if(!_isInitialized)
                initializeTree(heap);

            Node merged = null;
            //Extract elements from priority queue until it is empty
            while(_priorityQueue != null)
            {
                
                Node n1 = _heapObj.extractMin();
                Node n2 = null;
                if(null != _heapObj)
                {
                    n2 = _heapObj.extractMin();
                }
                
                //Call merge
                merged = merge(n1, n2);
                if(null == _heapObj._pairingHeapHead)
                    break;
                else
                    //Need to update heap object
                    _priorityQueue = _heapObj.insert(merged);
            }
            //Huffman tree is now ready
            _huffmanTreeNode = merged;
        }
    }
    
    /*
    This function is written for Binary heaps.
    This takes in obj of Binary heap class as parameter. If the initial values for trees are not already initizlied then it calls initialize funtion.
    We iteratively merge tree nodes till the queue is empty
    @author     Kunal Bajaj
    @params     Binary Heap obj. This is done to keep access of generated heap
    @returns    Nothing. Just constructs a huffman tree
    @throws     EmptyHeapException
    */
    public void constructTree(BinaryHeap obj) throws Exception
    {
        //check for heap size
        if(null != obj)
        {
            if(!_isInitialized)
                initializeTree(obj);
            
            Node n1 = null;
            while(!_heapObjBinary._heap.isEmpty())
            {
                n1 = _heapObjBinary.extractMin();
                if(!_heapObjBinary._heap.isEmpty())
                {
                    Node newNode = new Node();
                    newNode._treeLeft = n1;
                    newNode._treeRight = _heapObjBinary._heap.get(0);
                    newNode._count = _heapObjBinary._heap.get(0)._count;
                    _heapObjBinary._heap.set(0, newNode);
                    
                    //call increase key
                    _heapObjBinary.increaseKey(0, n1._count);
                    
                    //call bubble down
                    _heapObjBinary.bubbleDown(0);
                }
            }
            _huffmanTreeNode = n1;
        }
        else
            throw new Exception("EmptyHeapException");
    }
    
    public void constructTree(FourWayHeap fwh) throws Exception
    {
        if(null != fwh)
        {
            if(!_isInitialized)
                initializeTree(fwh);
            
            if(null != _heapObjFourWay)
            {
                Node min = null;
                while(_heapObjFourWay.size() > 3)
                {
                    min = _heapObjFourWay.extractMin();
                    //Get min
                    if(_heapObjFourWay.size() > 3)
                    {
                        Node newNode = new Node();
                        newNode._treeLeft = min;
                        newNode._treeRight = _heapObjFourWay._heap.get(3);
                        newNode._count = _heapObjFourWay._heap.get(3)._count;
                        _heapObjFourWay._heap.set(3, newNode);
                        
                        //increase key
                        _heapObjFourWay.increaseKey(3, min._count);
                        
                        //call BubbleDown
                        _heapObjFourWay.bubbleDown(3);
                    }
                }
                _huffmanTreeNode = min;
            }
            else
                throw new Exception("EmptyFourWayHeapError");
        }
        else
            throw new Exception("NullValueForClassObjectException");
    }
    
    /*
    Functions that encodes file data into huffman code
    */
    public void encodeData(String toReadFrom, String toWriteTo)
    {
        //Open file
        try
        {
            //Create code.txt file
            FileWriter fileWriter = new FileWriter(_CODETABLENAME);
            BufferedWriter out = new BufferedWriter(fileWriter);
            Iterator<Map.Entry<Integer, String>> it = _codes.entrySet().iterator();
            while (it.hasNext()) 
            {
                Map.Entry<Integer, String> pairs = it.next();
                out.write(pairs.getKey() + "\t" + pairs.getValue() + "\n");
            }
            out.close();
            
            //Create encoded file
            StringBuilder binaryString = new StringBuilder("");
            FileReader fr = new FileReader(toReadFrom);
            BufferedReader br = new BufferedReader(fr);
            String line;
            OutputStream output = null;
            byte[] barray;
            output = new BufferedOutputStream(new FileOutputStream(toWriteTo));
            while((line = br.readLine()) != null)
            {
                if (line.trim().compareTo("") != 0)
                {
                    if(_codes.containsKey(Integer.parseInt(line)))
                        binaryString.append(_codes.get(Integer.parseInt(line)));
                    
                    if(binaryString.length()/8 > 0)
                    {
                        barray = new byte[binaryString.length()/8];
                        for(int i = 0; i < binaryString.length()/8; i++)
                        {
                            barray[i] = (byte) Short.parseShort(binaryString.substring(8*i,8*(i+1)),2);
                        }
                        output.write(barray);
                        binaryString =new StringBuilder(binaryString.substring(binaryString.length()-(binaryString.length()%8)));
                    }
                }
            }
            
            fr.close();
            output.close();
            
        }catch (FileNotFoundException e){
            System.err.println("could not create " + toWriteTo + " " + e);
        }catch(IOException e)
        {
            System.err.println("error while reading file" + toReadFrom + " " + e);
        }catch(NumberFormatException e)
        {
            e.printStackTrace();
        }
    }
    
    /*
    A function that walks the huffman tree and generates huffman code for each node.
    @author     Kunal Bajaj
    @params     Node n, StringBuilder code
    @returns    HashMap
    @throws     No exception
    */
    public void generateCode(Node treeNode, String code)
    {
        if(null != treeNode)
        {
            if(null == treeNode._treeLeft && null == treeNode._treeRight)
                _codes.put(treeNode._value, code);
            else
            {
                generateCode(treeNode._treeLeft, code+"0");
                generateCode(treeNode._treeRight, code+"1");
            }
        }
    }
    
    /*
    This function merges two nodes and returns a Huffman Node with no value, just a count.
    @params     n1, n2. Nodes that need to be merged.
    @author     Kunal Bajaj
    @name       merge
    */
    public Node merge(Node n1, Node n2)
    {
        if(null != n1 || null != n2)
        {
            Node merged = new Node();
            if(null == n1 && null != n2)
            {
                merged = n2;
                merged._child = null;
            }
            
            else if(null == n2 && null != n1)
            {
                merged = n1;
                merged._child = null;
            }
            
            else
            {
                merged._count = n1._count + n2._count;
                if(n1._count < n2._count)
                {
                    merged._treeLeft = n1;
                    merged._treeRight = n2;
                }
                else
                {
                    merged._treeLeft = n2;
                    merged._treeRight = n1;
                }
            }
            return merged;
        }
        return null;
    }
    
    /*
    A function that generates frequency list from a file. This uses filename as input and sets value in the frequency table class variable
    @author     Kunal Bajaj
    @returns    void
    @throws     IOException, IllegalArgumentException
     */
    public void generateFrequency()
    {
        //Only go ahead when filename is not null
        if(_FILENAME.trim().compareTo("") != 0)
        {
            try
            {
                FileReader fr = new FileReader(_FILENAME);
                BufferedReader br = new BufferedReader(fr);
                String line;
                int value;
                //Halt when we get an empty string or null
                while((line = br.readLine()) != null)
                {
                     if (line.trim().compareTo("") != 0)
                     {
                         value = Integer.parseInt(line);
                        _frequencyTable[value] += 1;
                     }
                }
                
                fr.close();
            }catch(IOException | IllegalArgumentException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String args[])
    {
        long startTime = System.currentTimeMillis();
        //Store filename in a variable
        encoder e = new encoder();
        e._FILENAME = args[0];
        
        //Generate frequency table
        e.generateFrequency();
        
        try
        {
			long startTime = System.currentTimeMillis();
			for(int i=0; i<10; i++)
			{
				FourWayHeap fwh = new FourWayHeap(3);
				fwh.heapify(e._frequencyTable);
			}
			System.out.println("Time required to build priority queue on average using FourWayHeap: "+ (System.currentTimeMillis() -startTime)/10000);
        
            //Now construct the tree
            e.constructTree(bh);
            
            //Generate code table
            e.generateCode(e._huffmanTreeNode, "");

            //Encode data
            e.encodeData(e._FILENAME, e._ENCODEDFILENAME);
            
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }
    
}
