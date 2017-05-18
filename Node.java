/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmanencoderdecoder;

/*
 This class contains a Node object. The object contains a count and value variables along with left, right, child, treeLeft and treeRight pointers.
 Value variable contains the actual value that needs to be encoded.
 @author     Kunal Bajaj
 @package    HuffmanEncoderDecoder
 */
class Node{
    public int _value;
    public Node _left;
    public Node _right;
    public Node _child;
    public int _count;
    public Node _treeLeft;
    public Node _treeRight;


    public Node(int c, int v)
    {
        _value = v;
        _count = c;
        _left = null;
        _right = null;
        _child = null;
        _treeLeft = null;
        _treeRight = null;
    }
    
    public Node()
    {
        _value = -100;
        _count = 0;
        _left = null;
        _right = null;
        _child = null;
        _treeLeft = null;
        _treeRight = null;
    }
}
