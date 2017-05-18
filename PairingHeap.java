/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kunal
 */
public class PairingHeap {
    
    public Node _pairingHeapHead;
    
    public PairingHeap()
    {
        _pairingHeapHead = null;
    }
    
    public Node insert(Node n)
    {
        if(null != n)
            //Call meld operation
            return meld(n, _pairingHeapHead);
        return _pairingHeapHead;
    }
    
    /*
    This function returns min element from top of heap.
    Then rearrangment of heap is performed.
    */
    public Node extractMin()
    {
        if(null != _pairingHeapHead)
        {
            //Get the topmost value
            Node currentMin = _pairingHeapHead;
            Node child = null;
            
            //Remove reference to this node from child
            if(null != _pairingHeapHead._child)
            {
                _pairingHeapHead._child._left = null;
                child = _pairingHeapHead._child;
            }
            
            _pairingHeapHead = null;
            
            //call two-pass only if child exists
            if(null != child)
                twoPass(child);

            //After extractmin
            return currentMin;
        }
        return null;
    }
    
    /*
    This function performs meld operation by compairing top level nodes of two trees and then combining them into one.
    @params     n1, n2 - Nodes to meld
    @returns    Nothing
    @throws     No exception
    */
    private Node meld(Node n1, Node n2)
    {
        if(null != n1 || null != n2)
        {
            if(null == n2)
                return n1;
            else if(null == n1)
                return n2;
            else
            {
                //Compare data of new node with min
                if(n1._count < n2._count)
                {
                    //The new node becomes the leftmost child of the _pairingHeap
                    Node prevChild = n1._child;
                    n1._child = n2;
                    n2._left = n1;
                    if(null != prevChild)
                    {
                        prevChild._left = n2;
                        n2._right = prevChild;
                    }
                    else
                    {
                        n2._right = null;
                    }
                    n1._right = null;
                    return n1;
                }
                else
                {
                    Node prevChild = n2._child;
                    n2._child = n1;
                    n1._left = n2;
                    if(null != prevChild)
                    {
                        prevChild._left = n1;
                        n1._right = prevChild;
                    }
                    else
                    {
                        n1._right = null;
                    }
                    n2._right = null;
                    return n2;
                }
            }
        }
        return null;
    }
    
    private void twoPass(Node n1)
    {
        if(null != n1)
        {
            List<Node> firstPassArray = new ArrayList();
            List<Node> nodeArray = new ArrayList();
            //create an arrayList of Nodes to iterate for first pass
            while(n1 != null)
            {
                nodeArray.add(n1);
                n1 = n1._right;
            }
            
            Node a = null ;
            //Two pass will not be required if there is a single value in the array. So check if there is a single node in the arraylist
            if(nodeArray.size() > 1)
            {
                while(!nodeArray.isEmpty())
                {
                    if(nodeArray.size() < 2)
                        firstPassArray.add(meld(nodeArray.remove(0), firstPassArray.remove(firstPassArray.size()-1)));
                    else
                        firstPassArray.add(meld(nodeArray.remove(0), nodeArray.remove(0)));
                }

            //Now iterate from right to left using the firstPassArray
                a = firstPassArray.remove(firstPassArray.size()-1);
                while(!firstPassArray.isEmpty())
                {
                    a = meld(a, firstPassArray.remove(firstPassArray.size()-1));
                }
            }
            else
            {
                a = nodeArray.remove(0);
            }
            _pairingHeapHead = a;
        }
    }
    
    public Node heapify(int[] frequencyTable)
    {
        if(frequencyTable.length != 0)
        {
            long len = frequencyTable.length;
            for(int i=0; i<len; i++)
            {
                if(frequencyTable[i] > 0)
                {
                    Node newNode = new Node(frequencyTable[i], i);
                    _pairingHeapHead = insert(newNode);
                }
            }
        }
        
        return _pairingHeapHead;
    }
    
    /*
    This function performs an inorder walk of the pairing heap
    This function makes recursive calls to itself to print the entire tree
    @author     Kunal Bajaj
    @name       heapToString
    @params     Node root - The root of the subtree that needs to be printed.
    @returns    None
    */
    public void heapToString(Node root)
    {
        if(null != root)
        {
            System.out.println(root._value);
            //Print this if it has children
            if(null != root._child)
                heapToString(root._child);
            heapToString(root._right);
        }
    }
}
