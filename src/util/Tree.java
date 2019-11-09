package JTreeLib.util;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects; 
import java.util.Collections;
import org.javatuples.Pair;

/** Welcome to JTreeLib! JTreeLib is a comprehensive Java library aiming to facilitate users with the most common and
 *  useful tree data structures. Each specific type of tree comes with extensive features.
 * @author Caijun Qin
 * @version 1.0
 * @since 1.0
 */

/** Generic tree structure skeleton
 * @param <N> Data type of <i>Node</i>
 */
public abstract class Tree<N> implements Cloneable {
    //MEMBER VARIABLES
    /** Name of tree as a string
     */
    private String name;

    /** Maximum number of children each <i>Node</i> is allowed to have, and and whether degree is either enforced or not.
     * If not, then any and all <i>Node</i> can add more childs indefinitely.
     */
    private Pair<Integer, Boolean> degree;

    /** Maxmimum number of <i>Node</i> allowed, and whether capacity is either enforced or not.
     * If not, <i>Tree</i> does not limit the number of <i>Node</i> added
     */
    private Pair<Integer, Boolean> capacity;

    /** Table / array-based implementation of tree
     */
    protected List<Node<N>> tree;

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor
     */
    public Tree(){
        this.name = null;
        this.degree = Pair.with(-1, false);
        this.capacity = Pair.with(-1, false);
    }

    /** Constructor applies <i>name</i>, <i>degree</i>, and <i>capacity</i> to <i>Tree</i>
     * @param name A string that identifies a specific <i>Tree</i>
     * @param degree A Javatuple Pair that holds maximum number of childs allowed per <i>Node</i> and whether that is enforced or not
     * @param capacity A Javatuple Pair that holds maximum number of <i>Node</i> allowed and whether that is enforced or not
     */
    public Tree(String name, int degree, int capacity){
        this.name = name;
        this.degree = Pair.with(degree, true);
        this.capacity = Pair.with(capacity, true);
        this.tree = Collections.unmodifiableList(new ArrayList<>(capacity));
    }

    /** Copy constructor
     * @param otherTree <i>Tree</i> to be copied
     */
    public Tree(Tree<N> otherTree){
        this.name = otherTree.name;
        this.degree = Pair.with(otherTree.degree.getValue0(), otherTree.degree.getValue1());
        this.capacity = Pair.with(otherTree.capacity.getValue0(), otherTree.capacity.getValue1());
        this.tree = otherTree.tree;
    }

    //ACCESSORS
    /** Finds the name of <i>Tree</i>
     * @return A string name
     */
    public String getName() { return this.name; }

    /** Abstract function to find parent of <i>Node</i> located at <i>childIndex</i>
     * @param childIndex Index of child
     * @return Parent of <i>Node</i>  if exists, otherwise returns <i>null</i>
     */
    public abstract Node<N> getParent(int childIndex);

    /** Gets deep copy of subtree rooted at <i>rootIndex</i>
     * @param rootIndex Index of subtree root
     * @return New <i>Tree</i> containing copies of all <i>Node</i> in copied subtree in same order
     */
    public abstract Tree<N> getSubtree(int rootIndex);

    /** Gets array of all non-null <i>Node</i> in <i>Tree</i>
     * @return Array of <i>Node</i>
     */
    public Node<N>[] getTreeAsArray(){
        Node<N>[] output = new Node[this.getTreeSize()];
        return this.tree.toArray(output);
    }

    /** Finds the degree
     * @return Maximum number of children each node can have
     */
    public int getDegree(){ return this.degree.getValue0(); }

    /** Finds the number of non-null <i>Node</i>
     * @return Number of non-null <i>Node</i>
     */
    public int getTreeSize(){
        return (int)this.tree.stream().filter(Objects::nonNull).count();
    }

    /** Finds the physical size of <i>tree</i> currently, including null <i>Node</i> in count
     * @return Current size of <i>tree</i> s
     */
    public int getCurrentSize(){
        return this.tree.size();
    }

    /** Find the cap of <i>Node</i> <i>this</i> can contain in total
     * @return Maximum number of <i>Node</i> <i>this</i> can have if maximum cap is set; otherwise, returns -1
     */
    public int getCapacity(){
        if(!this.capacity.getValue1())
            return -1;
        return this.capacity.getValue0();
    }

    /** Finds out if <i>this</i> uses a cap on number of <i>Node</i>
     * @return Whether currently set capacity is enforced
     */
    @Deprecated
    public boolean hasActiveCapacity(){
        return this.capacity.getValue1();
    }

    /** Finds the <i>Node</i> located at given <i>index</i> in <i>tree</i>
     * @param index
     * @return <i>Node</i> at given index
     */
    public Node<N> getNode(int index){
        if(!this.hasNode(index))
            return null;
        return this.tree.get(index);
    }

    /** Checks whether <i>Tree</i> has a non-null <i>Node</i> at given <i>index</i>
     * @param index Index of <i>Node</i>
     * @return Whether <i>Node</i> at given <i>index</i> exists or not
     */
    public boolean hasNode(int index){
        if(index < 0 || index >= this.getCurrentSize()
                || index >= this.getCapacity() && this.getCapacity() > -1
                || this.tree.get(index) == null)
            return false;
        return true;
    }

    /** Checks whether <i>Tree</i> has a non-null <i>Node</i>
     * @param node <i>Node</i> to check existence in <i>Tree</i>
     * @return Whether <i>Node</i> is in current <i>Tree</i>
     */
    public boolean hasNode(Node<N> node){ return this.tree.contains(node); }

    /** Finds first <i>Node</i> in <i>tree</i>
     * @return Root of <i>tree</i>
     */
    public Node<N> getRoot(){ return this.tree.stream().findFirst().get(); }

    /** Determines if current <i>tree</i> is fully filled with non-null <i>BinaryNode</i>, regardless of <i>capacity</i>
     * @return Whether tree contains only non-null <i>BinaryNode</i> or not
     */
    public boolean isFull(){
        return !this.tree.contains(null);
    }

    /** Determines if number of non-null <i>BinaryNode</i> in <i>tree</i> fills up to <i>capacity</i>
     * @return Whether tree contains same number of non-null nodes as full capacity
     */
    public boolean isFullToCapacity(){ return this.tree.stream().filter(Objects::nonNull).count() == this.getCapacity(); }

    /** Finds out if <i>Node</i> at given index of tree is a leaf
     * @param index Index where <i>Node</i> is located
     * @return Whether specified <i>Node</i> is a leaf or not
     */
    public boolean isLeaf(int index){
        if(!this.hasNode(index))
            return false;
        return this.getNode(index).isLeaf();
    }

    /** Finds out if <i>Node</i> at given index of tree is a leaf
     * @param node <i>Node</i> of interest
     * @return Whether specified <i>Node</i> is a leaf or not
     */
    public boolean isLeaf(Node<N> node){ return this.isLeaf(this.tree.indexOf(node)); }

    /** Finds out if <i>Node</i> at given index is a lonely and detached <i>Node</i>, meaning no children and no parent
     * @param index Index of <i>Node</i> of interest
     * @return Whether <i>Node</i> is a singleton or not
     */
    public abstract boolean isSingleton(int index);

    //MUTATORS
    /** Inserts new subtree at given <i>rootIndex</i>
     * @param rootIndex Index to store root of new subtree
     * @param subTree New subtree to insert
     * @return Whether inserting new subtree is successful for not
     */
    public abstract boolean insertSubtree(int rootIndex, Tree<N> subTree);

    /** Removes subtree rooted at <i>Node</i> located at given <i>rootIndex</i>
     * @param rootIndex Index of subtree root
     * @return Whether removal of subtree is successful or not
     */
    public abstract boolean removeSubtree(int rootIndex);

    /** Sets new <i>Node</i> at some given index and replaces old <i>Node</i> if applicable.
     * Note: does not check whether a parent already exists, therefore may lead to a detached <i>Node</i> in tree
     * @param index Index to store new <i>Node</i>
     * @param node <i>Node</i> to be added
     * @return Whether adding new <i>Node</i> is successful or not
     */
    public boolean setNode(int index, Node<N> node){
        if(index <= 0 || index >= this.getCapacity() && this.getCapacity() != -1 || this.getDegree() != node.getChildCapacity())
            return false;
        node.setLeaf();
        this.tree.set(index, node);
        return true;
    }

    /** Sets the degree to a new value and allows enforcement
     * @param degree Maximum number of children each node can have
     * @return Whether degree changed successfully or not
     */
    public boolean setDegree(int degree){
        if(degree <= 0 || this.degree.getValue0() == degree)
            return false;
        this.degree = Pair.with(degree, true);
        return true;
    }

    /** Sets a maximum limit on number of <i>Node</i> allowed and allows enforcement
     * @param capacity Capacity of <i>Node</i>
     * @return Whether setting a new capacity is successful or not
     */
    public boolean setCapacity(int capacity){
        if(capacity < 0 || capacity == this.getCapacity())
            return false;
        this.capacity = Pair.with(capacity, true);
        List<Node<N>> newTree = new ArrayList<>(capacity);
        for(int index = 0; index <= capacity - 1; index++)
            newTree.set(index, this.tree.get(index));
        this.tree = Collections.unmodifiableList(newTree);
        return true;
    }

    /** Manually claim <i>Node</i> to be a leaf by removing all children, and thus descendants of <i>Node</i>
     * @param index Index of <i>Node</i> to make leaf
     */
    public void setLeaf(int index){
        if(!this.hasNode(index))
             return;
        this.getNode(index).setLeaf();
    }

    /** Manually claim <i>Node</i> to be a leaf by removing all children, and thus descendants of <i>Node</i>
     * @param node <i>Node</i> to make leaf
     */
    public void setLeaf(Node<N> node){
        int index = this.tree.indexOf(node);
        if(index == -1)
            return;
        this.setLeaf(index);
    }

    /** Reserve a minimum number of slots in <i>tree</i> as long as <i>capacity</i> is not breached
     * If initial capacity is greater than reserved space, function does nothinh but still  returns true.
     * @param numNodes Number of slots for <i>Node</i> to reserve
     * @return Whether reserving slots is successful or not
     */
    @Deprecated
    public boolean reserve(int numNodes){
        if(numNodes > this.getCapacity() && this.hasActiveCapacity())
            return false;
        if(numNodes <= this.getTreeSize())
            return true;
        this.tree.addAll(Collections.nCopies(numNodes - this.getCurrentSize(), null));
        return true;
    }

    /** Removes all nodes, and if capacity is enforced, refill tree to full capacity with null
     */
    public void clear(){
        this.clear(this.getCapacity());
    }

    /** Removes all nodes and sets a new capacity
     * @param newSize Number of spots to be reserved and initialized to null
     */
    public void clear(int newSize){
        this.tree.clear();
        this.setCapacity(newSize);
    }

    /** Enable or disable usage of capacity
     * @param enableCapacity Whether capacity will be enforced or not
     */
    @Deprecated
    public void setActiveCapacity(boolean enableCapacity){
        this.capacity.setAt1(enableCapacity);
    }

    /** Makes <i>capacity</i> no longer enforced; there is not limit to number of <i>Node</i>
     */
    public void disableCapacity(){ this.capacity = Pair.with(-1, false); }

}


