package JTreeLib.util;
import com.google.common.collect.Range;

public class IntervalTree extends BinaryTree<Range<Double>, Double> {
    //MEMBER VARIABLES

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor
     */
    public IntervalTree(){ super(); }

    /** Constructor applies <i>name</i> and <i>capacity</i>
     * @param name A string that identifies a specific <i>IntervalTree</i>
     * @param capacity Maximum number of <i>BinaryNode</i> that the tree can hold
     */
    public IntervalTree(String name, int capacity){ super(name, capacity); }

    /** Constructor applies <i>name</i>, <i>capacity</i>, and builds tree from array of intervals
     * @param name A string that identifies a specific <i>IntervalTree</i>
     * @param capacity Maximum number of <i>BinaryNode</i> that the tree can hold
     * @param arr Array of intervals using <i>Google Guava Range</i>
     */
    public IntervalTree(String name, int capacity, Range<Double>[] arr){
        super(name, capacity);

    }

    /**
     * @param arr
     */
    @Override
    private void BuildTree(Range<Double>[] arr){
        if(arr == null)
            return;
        int arrIndex = 0, nodeIndex = 0;
        while(arrIndex < arr.length){
            this.setNode(nodeIndex, new BinaryNode<>(arr[arrIndex], arr[arrIndex].upperEndpoint()));
            if(!this.isLeaf(nodeIndex)){
                if(arr[arrIndex + 1].lowerEndpoint() <= arr[arrIndex].lowerEndpoint())
                    nodeIndex = BinaryTree.getLeftChildIndex(nodeIndex);
                else
                    nodeIndex = BinaryTree.getRightChildIndex(nodeIndex);
            } else if(this.get){




            }
        }
    }

    //ACCESSORS
//    /**
//     * @param index Index where <i>BinaryNode</i> is located
//     * @return
//     */
//    @Override
//    public boolean isLeaf(int index){
//
//    }

    /**
     * @param index
     * @return
     */
    public Range<Double> getInterval(int index){
        if(!this.hasNode(index))
            return null;
        return this.getNode(index).getID();
    }

    //MUTATORS


}
