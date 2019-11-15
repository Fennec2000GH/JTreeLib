package JTreeLib.util;
import java.util.Arrays;
import org.javatuples.Triplet;

public class SegmentTree extends BinaryTree<Triplet<Integer, Integer, double[]>, Double> {
    //MEMBER VARIABLES
    private double[] array;

    //MEMBER FUNCTIONS
    //CONSTRUCTORS

    /** Constructor builds <i>SegmentTree</i> from an array
     * @param arr Array to implement into tree
     */
    public SegmentTree(double[] arr){
        super();
        this.array = arr;
        this.TreeBuilder(arr, 0, 0);
    }

    /** Constructor applies <i>name</i>, <i>capacity</i>, and builds <i>SegmentTree</i> from an array
     * @param name A string that identifies a specific <i>SegmentTree</i>
     * @param capacity Maximum number of <i>BinaryNode</i> that the tree can hold
     * @param arr Array to implement into tree
     */
    public SegmentTree(String name, int capacity, double[] arr){
        super(name, capacity);
        this.array = arr;
        this.TreeBuilder(arr, 0, 0);
    }

    /**
     * @param arr Array to implement into tree
     * @param index Index to insert <i>arr</i> or sub-array of <i>arr</i> in tree
     */
    private void TreeBuilder(double[] arr, int index, int startIndex) {
        this.setNode(index, new BinaryNode<>(Triplet.with(startIndex, startIndex + arr.length - 1, Arrays.copyOf(arr, arr.length)), null));
        if(arr.length >= 3){
            this.TreeBuilder(Arrays.copyOfRange(arr, 0, arr.length/2), BinaryTree.getLeftChildIndex(index), startIndex);
            this.TreeBuilder(Arrays.copyOfRange(arr, arr.length/2, arr.length), BinaryTree.getRightChildIndex(index), startIndex + arr.length / 2);
        } else if(arr.length == 2) {
            this.TreeBuilder(Arrays.copyOfRange(arr, 0, 1), BinaryTree.getLeftChildIndex(index), startIndex);
            this.TreeBuilder(Arrays.copyOfRange(arr, 1, 2), BinaryTree.getLeftChildIndex(index), startIndex + 1);
        } else {
            this.getNode(index).setKey(arr[0]);
            int parentIndex = BinaryTree.getParentIndex(index);
            while(parentIndex >= 0) {
                this.getNode(parentIndex).setKey(this.getKey(parentIndex) + arr[0]);
                parentIndex = BinaryTree.getParentIndex(parentIndex); //when parentIndex turns 0, parentIndex = -1 on next iteration
            }
        }
    }

    //ACCESSORS
    /** Finds out if <i>BinaryNode</i> at given index of tree is a leaf
     * @param index Index where <i>BinaryNode</i> is located
     * @return Whether specified <i>BinaryNode</i> is a leaf or not
     */
    @Override
    public boolean isLeaf(int index){
        if(!this.hasNode(index))
            return false;
        return this.getLeftBound(index) == this.getRightBound(index);
    }

    /** Finds sub-array stored in a specific <i>BinaryNode</i> at given index
     * @param index Index of <i>BinaryNode</i> of interest
     * @return Array of primitive doubles that is a subset of root's array
     */
    public double[] getSubarray(int index){
        if(!this.hasNode(index))
            return null;
        return this.getNode(index).getID().getValue2();
    }

    /** Finds the leftmost element's index of array represented by tree, which is 0 by definition
     * @return If array is not null, returns 0, otherwise returns -1
     */
    public int getLeftBound(){
        if(this.array == null)
            return -1;
        return 0;
    }

    /** Finds the leftmost element's index in sub-array of some <i>BinaryNode</i>
     * @param index Index of <i>BinaryNode</i> of interest
     * @return Leftmost element's index
     */
    public int getLeftBound(int index){
        if(!this.hasNode(index))
            return -1;
        return this.getNode(index).getID().getValue0();
    }

    /** Finds the rightmost element's index of array represented by tree, which is length - 1 by definition
     * @return If array is not null, returns length of array, otherwise returns -1
     */
    public int getRightBound(){
        if(this.array == null)
            return -1;
        return this.array.length - 1;
    }

    /** Finds the rightmost element's index in sub-array of some <i>BinaryNode</i>
     * @param index Index of <i>BinaryNode</i> of interest
     * @return Rightmost element's index
     */
    public int getRightBound(int index){
        if(!this.hasNode(index))
            return -1;
        return this.getNode(index).getID().getValue1();
    }

    /** Finds sum of sub-array of root's array bounded by 2 indices
     * @param nodeIndex Index of current <i>BinaryNode</i> and possible descendants in consideration of sum search
     * @param startIndex Index of first element in sub-array for summation
     * @param endIndex Index of last element in sub-array for summation
     * @return Sum of sub-array, or -1 if root's array does not fully contain sub-array with given bounds
     */
    public double sum(int nodeIndex, int startIndex, int endIndex){
        if(!this.hasNode(nodeIndex) || startIndex < 0 || endIndex >= this.array.length)
            return -1;
        else if(this.getLeftBound(nodeIndex) == startIndex && this.getRightBound(nodeIndex) == endIndex)
            return this.getNode(nodeIndex).getKey();
        else if(endIndex <= this.getRightBound(BinaryTree.getLeftChildIndex(nodeIndex)))
            return this.sum(BinaryTree.getLeftChildIndex(nodeIndex), startIndex, endIndex);
        else if(startIndex >= this.getLeftBound(BinaryTree.getRightChildIndex(nodeIndex)))
            return this.sum(BinaryTree.getRightChildIndex(nodeIndex), startIndex, endIndex);
        else {
            double partSum1 = this.sum(BinaryTree.getLeftChildIndex(nodeIndex), startIndex, this.getRightBound(BinaryTree.getLeftChildIndex(nodeIndex)));
            double partSum2 = this.sum(BinaryTree.getRightChildIndex(nodeIndex), this.getLeftBound(BinaryTree.getRightChildIndex(nodeIndex)), endIndex);
            return partSum1 == -1 || partSum2 == -1 ? -1 : partSum1 + partSum2; //only returns -1 when tree is incompletely built i.e. missing leaves
        }
    }

    //MUTATORS
    /** Updates element at specific index of original array stored in root, and updates keys where necessary
     * @param index Index of element to be updated
     * @param newValue New value to replace current value located at given index
     * @return Whether update is successful or not
     */
    public boolean update(int index, double newValue){
        if(index < 0 || index >= this.array.length)
            return false;
        this.array[index] = newValue;
        this.getNode(index).setKey(newValue);
        int currentIndex = index;
        while(!this.isLeaf(currentIndex)) {
            if(index <= this.getRightBound(BinaryTree.getLeftChildIndex(currentIndex)))
                currentIndex = BinaryTree.getLeftChildIndex(currentIndex);
            else
                currentIndex = BinaryTree.getRightChildIndex(currentIndex);
            this.getNode(currentIndex).setKey(newValue);
        }
        return true;
    }



}

class PersistentSegmentTree extends SegmentTree {
    //MEMBER VARIABLES

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Constructor builds <i>PersistentSegmentTree</i> from an array
     * @param arr Array to implement into tree
     */
    public PersistentSegmentTree(double[] arr){ super(arr); }

    //ACCESSORS

    //MUTATORS

}