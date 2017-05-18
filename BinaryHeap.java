package huffmanencoderdecoder;

import java.util.ArrayList;

public class BinaryHeap
{
    ArrayList<Node> _heap;
    
    //Initialize necessary variables
    public BinaryHeap()
    {
            //This initializes the heap array
            _heap = new ArrayList<Node>();
    }

    /*
    A function that returns the left child of a particular node
    @author     Kunal Bajaj
    @params     index i
    @returns    Node
    @throws     Nothing
    */
    public Node getLeftChild(int i)
    {
        if(2*i+1 < _heap.size() && 2*i+1 >= 0)
            return _heap.get((2*i)+1);
        return null;
    }

    /*
    A function that returns the right child of a particular node
    @author     Kunal Bajaj
    @params     index i
    @returns    Node
    @throws     Nothing
    */
    public Node getRightChild(int i)
    {
        if(2*i+2 < _heap.size() && 2*i+2 >= 0)
            return _heap.get(2*i+2);
        return null;
    }

    /*
    A function that returns the parent of a particular node
    @author     Kunal Bajaj
    @params     index i
    @returns    Node
    @throws     Nothing
    */
    public Node getParent(int i)
    {
        if((i-1)/2 < _heap.size() && (i-1)/2 >= 0)
            return _heap.get((i-1)/2);
        return null;
    }

    /*
    This function appends a new node at the end of the _heap arraylist and then calls bubbleUp to handle possible violation
    @author     Kunal Bajaj
    @params     Node toInsert
    @returns    void
    @throws     No exception
    */
    public void add(Node toInsert)
    {
        //Proceed only when newNode is not null
        if (null != toInsert)
        {
            //Insert the value to the end of the heap
            _heap.add(toInsert);
            
            //There maybe a violation with its parent for call bubbleUp to rearrange
            bubbleUp(_heap.size()-1);
        }
    }

    /*
    A simple function to swap Node values
    @params     Nodes. These are objects that contain count and value.
    @returns    void
    @author     Kunal Bajaj
    @throws     No exception
    */
    public void swap(int index1, int index2)
    {
        if(index1 < _heap.size() && index1>= 0 && index2 < _heap.size() && index2>=0)
        {
            Node temp = _heap.get(index1);
            _heap.set(index1, _heap.get(index2));
            _heap.set(index2 ,temp);
        }
    }

    /*
    Function that bubblesUp inconsistency that might have been caused due to an insert statement
    This checks whether heap property is violated by addition of a newNode. If yes then it swaps the node with its parent.
    This is a recursive function
    @params     Index. The position of the element that needs to be checked.
    @returns    nothing.
    @throws     No exception
    */
    public void bubbleUp(int index)
    {
        if(index < _heap.size() && index > 0)
        {
            //There can be a possible violation only with this node's parent.
            Node parent = getParent(index);
            Node currentNode = _heap.get(index);
            if(null != parent && null != currentNode && currentNode._count < parent._count)
            {
                //Here we do a swap
                swap((index-1)/2, index);
                //Call the function again with index of parent
                bubbleUp((index-1)/2);
            }
        }
    }

    /*
    Function that bubblesDown inconsistency that might have been caused due to an extractMin operation
    This checks whether heap property is violated by removing min. If yes then it swaps the node with minimum of its children.
    This is a recursive function
    @params     Index. The position of the element that needs to be checked.
    @returns    nothing.
    @throws     No exception
    */
    public void bubbleDown(int index)
    {
        //Only proceed when heap is not empty
        if(index < _heap.size() && index >= 0)
        {
            //get current node and its children
            Node inPossibleConflict = _heap.get(index);
            Node left = getLeftChild(index);
            Node right = getRightChild(index);
            Node min = null;
            int indexOfChild = -1;
            if(null != left && null != right)
            {
                if(left._count < right._count)
                {
                    min = left;
                    indexOfChild = 2*index+1;
                }
                else
                {
                    min = right;
                    indexOfChild = 2*index+2;
                }
                
                    
            }
            else if(null == left && null != right)
            {
                min = right;
                indexOfChild = 2*index+2;
            }
            else if (null == right && null != left)
            {
                min = left;
                indexOfChild = 2*index+1;
            }
            
            if(null != min && min._count < inPossibleConflict._count)
            {
                swap(index, indexOfChild);
                //Possible conflict at left node
                bubbleDown(indexOfChild);
            }
        }
    }

    /*
    This function returns the value at the top of the heap.
    This is done by replacing top element with last element in the heap.
    This may violate heap property - bubbleDown is then called to handle this violation which takes O(log n) time.
    @author     Kunal Bajaj
    @params     None
    @returns    Double - Min value or 0 if heap is empty
    @throws     HeapEmpty Exception
    */
    public Node extractMin() throws Exception
    {
        // We can do this only when the list if not empty
        if(!isEmpty())
        {
            //Call swap on first and last element
            swap(0, _heap.size()-1);
            //Remove the last element
            Node min = _heap.remove(_heap.size()-1);
            if(!isEmpty())
                bubbleDown(0);
            
           return min;
        }
        else
            throw new Exception("HeapEmpty Exception");
    }
    
    public boolean isEmpty()
    {
        return (_heap.isEmpty());
    }
    
    /*
    Starts min-heapify process on the frequencyTable
    @author     Kunal Bajaj
    @params     frequencyTable - Count of values taken from input file.
    @returns    nothing
    @throws     EmptyFrequencyTable Exception
    */
    public void heapify(int[] frequencyTable, int length) throws Exception
    {
        if(0 != length)
        {
            //Proceed only when size is not empty
            for(int i=0; i<length; i++)
            {
                if(0 != frequencyTable[i])
                {
                    Node newNode = new Node(frequencyTable[i], i);

                    //Insert the new node in the heap
                    add(newNode);
                }
            }
        }
        else
            throw new Exception("EmptyFrequencyTable Exception");
    }
    
    /*
    A function useful for debugging.
    @author     Kunal Bajaj
    @returns    Nothing
    @throws     No exception
    */
    public void heapToString()
    {
        for(Node n: _heap)
        {
            System.out.println(n._value);
        }
    }
    
    /*
    Function that increases frequency of a particular node with a positive value.
    This function helps with construction of huffman tree
    @author     Kunal Bajaj
    @params     index - the index of the node whose key we want to increase
    @params     value  - the amount by which frequency needs to be increased.
    @returns    Nothing
    @throws     No exception
    */
    public void increaseKey(int index, int value)
    {
        if(index >=0 && index < _heap.size() && value >=0)
        {
            Node temp = _heap.get(index);
            temp._count += value;
            _heap.set(index, temp);
        }
    }
}