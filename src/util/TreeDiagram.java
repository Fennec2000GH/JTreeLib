import Array.ArrayUtils;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;
import kotlin.Deprecated;

/** Tree diagram used for laying out all possibilities for permutation and combination problems
 * @param <T> Data type of each node
 */
public class TreeDiagram<T> {
    //MEMBER VARIABLES
    /** Expandable list organizing tree diagram level-by-level
     */
    private ArrayList<ArrayList<TreeNode<T>[]>> levels;

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Constructor applies new root
     * @param newRoot <code>TreeNode</code> to be root of tree diagram
     */
    public TreeDiagram(TreeNode<T> newRoot){
        this.levels = new ArrayList<>();
        this.levels.add(new ArrayList<>());
        this.levels.get(0).add(new TreeNode[]{newRoot});
    }

    /** Copy constructor
     * @param otherTreeDiagram Another <Code>TreeDiagram</Code> to deep copy from
     * @param moveConstructor Whether the constructor should be used as a move constructor or not
     */
    @Deprecated(message = "Copy (not move) constructor still needs to be tested!")
    public TreeDiagram(TreeDiagram<T> otherTreeDiagram, boolean moveConstructor){
        try {
            Preconditions.checkNotNull(otherTreeDiagram);
        } catch (NullPointerException e) {
            this.levels = null;
            return;
        }
        if(moveConstructor) {
            this.levels = otherTreeDiagram.levels;
            otherTreeDiagram.levels = null;
        } else {
            this.levels = new ArrayList<>();
            this.levels.add(new ArrayList<>());
            this.levels.get(0).add(new TreeNode[]{new TreeNode<>(otherTreeDiagram.getRoot())});
            //for each level
            for (int i = 1; i < otherTreeDiagram.levels.size(); i++) {
                this.levels.add(new ArrayList<>());

                //for each array of children per level
                int level2Index = 0;
                for (TreeNode<T>[] nodeArr : otherTreeDiagram.levels.get(i)) {
                    this.levels.get(i).add(Arrays.copyOf(nodeArr, nodeArr.length));
                    for (int j = 0; j < this.levels.get(i).get(level2Index).length; j++)
                        this.getNode(i, level2Index, j).setChild(j, new TreeNode<T>(this.levels.get(i).get(level2Index)[j]));
                    ++level2Index;
                }

                //adding children to previous level's nodes
                level2Index = 0;
                for(TreeNode<T>[] prevArr : this.levels.get(i - 1))
                    for(TreeNode<T> prevNode : prevArr) {
                        prevNode.setAllChildren(this.levels.get(i).get(level2Index));
                        ++level2Index;
                    }
            }
        }
    }

    //ACCESSORS
    /** Finds root of tree diagram
     * @return Topmost <code>TreeNode</code>
     */
    public TreeNode<T> getRoot(){ return this.levels.get(0).get(0)[0]; }

    /** Finds <code>TreeNode</code> located at this 3-tuple of indices
     * @param levelIndex 0-based tier-1 index spanning <code>levels</code>
     * @param arrIndex 0-based tier-2 index spanning <code>ArrayList</code> element in <code>levels</code>
     * @param elementIndex 0-based tier-3 index marking a specific element in a <code>TreeNode</code> array that is an
     *                     element corresponding to some <code>arrIndex</code>
     * @return <code>TreeNode</code> at given set of indices if indices are valid, or <code>null</code> if no
     * <code>TreeNode</code> exists there
     */
    public TreeNode<T> getNode(int levelIndex, int arrIndex, int elementIndex){
        try {
            Preconditions.checkElementIndex(levelIndex, this.levels.size(), "Level index is out of bounds!");
            Preconditions.checkElementIndex(arrIndex, this.levels.get(levelIndex).size(),
                    "Array index for picked level is out of bounds!");
            Preconditions.checkElementIndex(elementIndex, this.levels.get(levelIndex).get(arrIndex).length,
                    "Element index in picked children array is out of bounds!");
            Preconditions.checkNotNull(this.levels.get(levelIndex).get(arrIndex)[elementIndex],
                    "This is a null!");
        } catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
        return this.levels.get(levelIndex).get(arrIndex)[elementIndex];
    }

    /** Gets unmodifiable list view of all non-null children of <code>TreeNode</code> at given 3-tuple of indices
     * @param levelIndex 0-based tier-1 index spanning <code>levels</code>
     * @param arrIndex 0-based tier-2 index spanning <code>ArrayList</code> element in <code>levels</code>
     * @param elementIndex 0-based tier-3 index marking a specific element in a <code>TreeNode</code> array that is an
     *                     element corresponding to some <code>arrIndex</code>
     * @return <code>unmodifiableList</code> of <code>TreeNode</code> that are children of a common parent
     */
    public List<TreeNode<T>> getChildren(int levelIndex, int arrIndex, int elementIndex){
        TreeNode<T> parent = this.getNode(levelIndex, arrIndex, elementIndex);
        try {
            Preconditions.checkNotNull(parent);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        List<TreeNode<T>> output = new ArrayList<>(parent.getChildSize());
        for(int i = 0; i < parent.getChildArraySize(); i++)
            if(parent.getChild(i) != null)
                output.add(parent.getChild(i));
        return Collections.unmodifiableList(output);
    }

    //MUTATORS
    /** Adds new <code>TreeNode</code> as child
     * @param levelIndex 0-based tier-1 index spanning <code>levels</code>
     * @param arrIndex 0-based tier-2 index spanning <code>ArrayList</code> element in <code>levels</code>
     * @param elementIndex 0-based tier-3 index marking a specific element in a <code>TreeNode</code> array that is an
     *                     element corresponding to some <code>arrIndex</code>
     * @param newChild New child <code>TreeNode</code> to be added
     * @return
     */
    public boolean addChild(int levelIndex, int arrIndex, int elementIndex, TreeNode<T> newChild){
        try {
            Preconditions.checkNotNull(newChild, "New child cannot be null!");
            Preconditions.checkElementIndex(elementIndex, this.levels.size(), "Level index is out of bounds!");
            Preconditions.checkElementIndex(arrIndex, this.levels.get(levelIndex).size(),
                    "Array index for picked level is out of bounds!");
            Preconditions.checkElementIndex(elementIndex, this.levels.get(levelIndex).get(arrIndex).length,
                    "Element index in picked children array is out of bounds!");
        } catch (RuntimeException e) {
            return false;
        }
        return this.getNode(levelIndex, arrIndex, elementIndex).addChild(newChild);
    }
    
    /** Sets a new root
     * @param newRoot <code>TreeNode</code> to be new root
     * @return
     */
    public void setRoot(TreeNode<T> newRoot){
        try {
            Preconditions.checkNotNull(newRoot, "New root cannot be null!");
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }
        this.levels.get(0).get(0)[0] = newRoot;
        this.getRoot().setAllChildren(this.levels.get(1).get(0));
    }

    /** Sets given <code>TreeNode</code> at given 3-tuple of indices
     * @param levelIndex 0-based tier-1 index spanning <code>levels</code>
     * @param arrIndex 0-based tier-2 index spanning <code>ArrayList</code> element in <code>levels</code>
     * @param elementIndex 0-based tier-3 index marking a specific element in a <code>TreeNode</code> array that is an
     *                     element corresponding to some <code>arrIndex</code>
     * @param otherNode <code>TreeNode</code> to be set at specified indices
     */
    public void setNode(int levelIndex, int arrIndex, int elementIndex, TreeNode<T> otherNode){
        try {
            Preconditions.checkNotNull(otherNode, "New node cannot be null!");
            Preconditions.checkElementIndex(elementIndex, this.levels.size(), "Level index is out of bounds!");
            Preconditions.checkElementIndex(arrIndex, this.levels.get(levelIndex).size(),
                    "Array index for picked level is out of bounds!");
            Preconditions.checkElementIndex(elementIndex, this.levels.get(levelIndex).get(arrIndex).length,
                    "Element index in picked children array is out of bounds!");
        } catch (RuntimeException e) {
            return;
        }
        this.levels.get(levelIndex).get(arrIndex)[elementIndex] = otherNode;
    }
}

/** Node element to be used in <code>TreeDiagram</code>
 * @param <T> Data type of each node
 */
class TreeNode<T> {
    //MEMBER VARAIBLES
    /** Data element
     */
    private T data;

    /**
     */
    private double loadFactor;

    /**
     */
    private int childCapacity;

    /** Array holding all children <code>TreeNode</code> of current <code>TreeNode</code>
     */
    private TreeNode<T>[] children;

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Constructor applies value for <code>data</code>
     * @param newData New data value
     */
    public TreeNode(T newData){
        this.data = newData;
        this.loadFactor = 0.75;
        this.children = new TreeNode[20];
    }

    /** Constructor applies values for <code>data</code> and <code>loadFactor</code>
     * @param newData New data value
     * @param newLoadFactor Triggers doubling of <code>children</code> array size when actually filled size meets or exceed this value
     */
    public TreeNode(T newData, double newLoadFactor){
        this(newData);
        try {
            Preconditions.checkArgument(newLoadFactor > 0 && newLoadFactor <= 1,
                    "New load factor must be between 0 (exclusive) and 1 (inclusive)!");
        } catch (IllegalArgumentException e) {
            return;
        }
        this.loadFactor = newLoadFactor;
    }

    /** Copy constructor only copies over <code>data</code>
     * @param otherTreeNode Another <code>TreeNode</code> to deep copy from
     */
    public TreeNode(TreeNode<T> otherTreeNode){
        this.data = otherTreeNode.data;
        this.loadFactor = otherTreeNode.loadFactor;
        this.children = new TreeNode[20];
    }

    //ACCESSORS
    /** Gets child <code>TreeNode</code> at a given index
     * @param index 0-based index valid for the array <code>children</code>
     * @return <code>TreeNode</code> at <code>index</code> if exists, or <code>null</code>
     */
    public TreeNode<T> getChild(int index){
        try {
            Preconditions.checkElementIndex(index, this.getChildArraySize(), "Invalid index for current children array!");
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
        return this.children[index];
    }

    /** Finds the number of non-null children
     * @return Nonnegative integer counting number of non-null <code>TreeNode</code> in <code>children</code>
     */
    public int getChildSize(){ return (int)Arrays.stream(this.children).parallel().filter(Objects::nonNull).count(); }

    /** Gets the current load factor
     * @return Load factor
     */
    public double getLoadFactor(){ return this.loadFactor; }

    /** Gets the current set maximum limit for number of children
     * @return Child capacity
     */
    public int getChildCapacity(){ return this.childCapacity; }

    /** Gets the current length of array holding children
     * @return Length of <code>children</code>
     */
    public int getChildArraySize(){ return this.children.length; }

    //MUTATORS
    /** Adds new <code>TreeNode</code> as child
     * @param newChild New child <code>TreeNode</code> to be added
     * @return <code>true</code> if adding 1 more <code>TreeNode</code> to <code>children</code> does not violate
     * current capacity, if active
     */
    public boolean addChild(TreeNode<T> newChild){
        try {
            Preconditions.checkNotNull(newChild, "New child cannot be null!");
            if(this.getChildSize() == this.getChildCapacity())
                throw new RuntimeException("Number of children already fills full capacity!");
        } catch (RuntimeException e) {
            return false;
        }
        int index = ArrayUtils.indexOfNull(this.children);
        if(index == -1 || ((double)this.getChildSize() + 1 )/ this.getChildArraySize() >= this.getLoadFactor()){
            int newSize = Math.min(this.getChildCapacity(), 2 * this.getChildArraySize());
            TreeNode<T>[] newArr = new TreeNode[newSize];
            System.arraycopy(this.children, 0, newArr, 0, this.getChildSize());
            this.children = newArr;
            index = ArrayUtils.indexOfNull(this.children);
        }
        this.setChild(index, newChild);
        return true;
    }

    /** Sets new child <code>TreeNode</code> at given index of <code>children</code>
     * @param index 0-based index on where to insert
     * @param newChild New <code>TreeNode</code> to be inserted into <code>children</code>
     */
    public void setChild(int index, TreeNode<T> newChild){
        try {
            Preconditions.checkNotNull(newChild, "New child cannot be null!");
            Preconditions.checkElementIndex(this.getChildArraySize(), index, "Index is out of bounds!");
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return;
        }
        this.children[index] = newChild;
    }

    /** Sets all <code>TreeNode</code> from given array as new children and replaces all previous children
     * @param newChildArr Array of <code>TreeNode</code> to be new children
     */
    public void setAllChildren(TreeNode<T>[] newChildArr){ this.children = newChildArr; }

    /** Sets a new value for <code>data</code>
     * @param newData New value for <code>data</code>
     */
    public void setData(T newData){ this.data = newData; }

    /** Sets a new capacity for number of children allowed
     * @param newChildCapacity A positive number larger than or equal to the current <code>childCapacity</code>
     * @return <code>true</code> if <code>newChildCapacity</code> is equal to or more than current <code>childCapacity</code>
     */
    public boolean setChildCapacity(int newChildCapacity){ return this.setChildCapacity(newChildCapacity, false); }

    /** Sets a new capacity for number of children allowed and truncates <code>children</code> if new value is less than
     * current <code>childCapacity</code>
     * @param newChildCapacity A positive number
     * @param forceSet Whether to forcefully set new <code>childCapacity</code> even if new value is less than current value
     * @return <code>true</code> if <code>newChildCapacity</code> is equal to or greater than current <code>childCapacity</code>,
     * or when <code>newChildCapacity</code> is positive and <code>forceSet</code> is <code>true</code>
     */
    public boolean setChildCapacity(int newChildCapacity, boolean forceSet){
        try {
            if(!forceSet)
                Preconditions.checkArgument(newChildCapacity >= this.getChildCapacity(),
                    "New child capacity should be greater than or equal to current child capacity!");
            Preconditions.checkArgument(newChildCapacity >= 0, "New child capacity cannot be negative!");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        if(newChildCapacity < this.getChildCapacity() && forceSet) {
            this.children = Arrays.copyOf(this.children, newChildCapacity);
            this.childCapacity = newChildCapacity;
        } else
            this.childCapacity = newChildCapacity;
        return true;
    }

}
