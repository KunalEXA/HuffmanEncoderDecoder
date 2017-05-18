/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmanencoderdecoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Kunal
 */
public class HuffmanEncoder {

    private int[] _frequencyTable;
    private String _FILENAME;
    public HuffmanEncoder()
    {
        _frequencyTable = new int[1000000];
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
    
    /*
    A simple function for debugging that prints the frequency table like a key value pair
    @author     Kunal Bajaj
    @params     None
    @returns    Nothing
    @throws     No exceptions
    */
    public void printFrequencyTable()
    {   
        //for loop
        for(int i =0; i< _frequencyTable.length; i++)
        {
            //Print only if count > 0
            if(_frequencyTable[i] > 0)
                System.out.println(i+" : "+_frequencyTable[i]);
        }
        
    }
    
    public static void main(String[] args) {
        
    }
    
}
