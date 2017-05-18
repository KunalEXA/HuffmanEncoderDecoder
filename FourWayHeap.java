/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmanencoderdecoder;

import java.util.ArrayList;

/**
 *
 * @author Kunal
 */
public class FourWayHeap {
    
    ArrayList<Node> _heap;
    private int _shift;
    
    public FourWayHeap(int shift)
    {
        _heap = new ArrayList<Node>();
        _heap.add(null);
        _heap.add(null);
        _heap.add(null);
        _shift = shift;
    }
    
    public boolean isEmpty()
    {
        return (_heap.size() == 0);
    }
    
    public int size()
    {
        return _heap.size();
    }
    
    public Node peek()
    {
        if(!isEmpty())
        {
            return _heap.get(0);
        }
        return null;
    }
    
    public Node getParent(int index)
    {
        if(index-_shift >0 && index<_heap.size() && _shift>=0)
            return _heap.get(((index-_shift-1)/4)+_shift);
        return null;
    }
    
    public Node getKthChild(int index, int k)
    {
        if(k>=0 && k<=4 && index-_shift>=0 &&  (4*(index-_shift)+k+_shift) < _heap.size())
            return _heap.get(4*(index-_shift)+k+_shift);
        return null;
    }
    
    public int getMinimumChildIndex(int index, int k)
    {
        if(index-_shift >=0 && index < _heap.size())
        {
            //Get the first child and set it to min
            Node min = getKthChild(index, 1);
            Node current = null;
            int minChildIndex = -1;
            
            if(4*(index-_shift)+1+_shift < _heap.size())
                minChildIndex = 4*(index-_shift)+1+_shift;
            
            //Compare with other children
            for(int i=2; i<=k; i++)
            {
                current = getKthChild(index, i);
                if(null != current && null != min && current._count < min._count)
                {
                    min = current;
                    minChildIndex = (4*(index-_shift)+i)+_shift;
                }
            }
            return minChildIndex;
        }
        return -1;
    }
    
    public void increaseKey(int index, int value)
    {
        if(index >= 3 && index < _heap.size() && value >=0)
        {
            Node temp = _heap.get(index);
            temp._count += value;
            _heap.set(index, temp);
        }
    }
    
    public void decreaseKey(int index, int value)
    {
        if(index>= 3 && index < _heap.size() && value >=0)
        {
            Node temp = _heap.get(index);
            temp._count -= value;
            _heap.set(index, temp);
        }
    }
    
    public Node extractMin() throws Exception
    {
        if(_heap.size() == 3)
            throw new Exception("Heap underflow exception");
        else
        {
            swap(3, _heap.size()-1);
            Node min = _heap.remove(_heap.size()-1);
            if(!isEmpty())
                bubbleDown(3);
            return min;
        }
    }
    
    public void bubbleDown(int index)
    {
        if(index >=3 && index < _heap.size())
        {
            //get the minimum child
            int minChildIndex = getMinimumChildIndex(index, 4);
            Node min = null;
            if(-1 != minChildIndex)
                min = _heap.get(minChildIndex);
            Node current = _heap.get(index);
            if(null != min && min._count < current._count)
            {
                swap(index, minChildIndex);
                bubbleDown(minChildIndex);
            }
        }
    }
    
    public void bubbleUp(int index)
    {
        if(index > 3 && index <= _heap.size())
        {
            Node currentNode = _heap.get(index);
            Node parent = getParent(index);
            if(null != currentNode && null != parent && currentNode._count < parent._count)
            {
                swap(index, ((index-_shift-1)/4)+_shift);
                
                //call bubbleUp on parent
                bubbleUp(((index-_shift-1)/4)+_shift);
            }
        }
    }
    
    public void swap(int index1, int index2)
    {
        if(index1 < _heap.size() && index1>= 0 && index2 < _heap.size() && index2>=0)
        {
            Node temp = _heap.get(index1);
            _heap.set(index1, _heap.get(index2));
            _heap.set(index2 ,temp);
        }
    }
    
    public void insert(Node toInsert) throws Exception
    {
        if(null != toInsert)
        {
            //Insert node to the heap
            _heap.add(toInsert);
            
            //call bubbleUp on the inserted index
            bubbleUp(_heap.size()-1);
        }
        else
            throw new Exception("Node to be inserted was empty");
    }
    
    public void heapify(int[] frequencyTable) throws Exception
    {
        int len = frequencyTable.length;
        if(len != 0)
        {
            for(int i=0; i<len; i++)
            {
                if(frequencyTable[i] > 0)
                {
                    Node newNode = new Node(frequencyTable[i], i);
                    insert(newNode);
                }
            }
        }
        else
            throw new Exception("Empty frequency table exception");
    }
    
    /*
    A simple function to debug Four way heap
    */
    public void heapToString()
    {
        if(!_heap.isEmpty())
        {
            for(int i=0; i<_heap.size(); i++)
                if(null != _heap.get(i))
                    System.out.println(_heap.get(i)._value+":"+_heap.get(i)._count);
            else
                    System.out.println("null");
        }
    }
}
