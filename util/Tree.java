package JTreeLib.util;
import java.util.Collections;
import javolution.util.FastTable;
import java.util.Objects;
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
    //ACCESSORS
    /** Abstract function to find parent of <i>Node</i> located at <i>childIndex</i>
     * @param childIndex Index of <i>Node</i> in <i>this</i>
     * @return Parent of <i>Node</i>  if exists, otherwise returns <i>null</i>
     */
    public abstract Node<N> getParent(int childIndex);

    /** Gets deep copy of subtree rooted at <i>rootIndex</i> in <i>this</i>
     * @param rootIndex Index of subtree root
     * @return New <i>Tree</i> containing copies of all <i>Node</i> in copied subtree in same order
     */
    public abstract Tree<N> getSubtree(int rootIndex);

    /** Finds the degree
     * @return Maximum number of children each node can have
     */
    public int getDegree(){
        return this.degree;
    }

    /** Finds the number of non-null <i>Node</i> in <i>this</i>
     * @return Number of non-null <i>Node</i>
     */
    public int getNodeSize(){
        return this.tree.filtered(Objects::nonNull).size();
    }

    /** Finds the physical size of <i>tree</i> currently, including null <i>Node</i> in count
     * @return Current size of <i>tree</i> s
     */
    public int getTreeSize(){
        return this.tree.size();
    }

    /** Find the cap of <i>Node</i> <i>this</i> can contain in total
     * @return Maximum number of <i>Node</i> <i>this</i>  can have
     */
    public int getCapacity(){
        return this.capacity.getValue0();
    }

    /** Finds out if <i>this</i> uses a cap on number of <i>Node</i>
     * @return Whether currently set capacity is enforced
     */
    public boolean hasActiveCapacity(){
        return this.capacity.getValue1();
    }

    /** Finds index of <i>node</i> if exists
     * @param node <i>Node</i> to be found in <i>tree</i>
     * @return Index of <i>node</i> 
     */
    public int getIndex(Node<N> node){
        return this.tree.indexOf(node);
    }

    /** Finds the <i>Node</i> located at given <i>index</i> in <i>tree</i>
     * @param index
     * @return <i>Node</i> at given index
     */
    public Node<N> getNode(int index){
        if(index < 0 || index >= this.getTreeSize())
            return null;
        return this.tree.get(index);
    }

    /** Checks whether <i>this</i> has a non-null <i>Node</i> at given <i>index</i>
     * @param index Index of <i>Node</i> to determine existence of <i>Node</i>
     * @return Whether <i>Node</i> at given <i>index</i> exists or not
     */
    public boolean hasNode(int index){
        if(index < 0 || index >= this.getTreeSize())
            return false;
        if(this.tree.get(index) != null)
            return true;
        return false;
    }

    /** Finds first <i>Node</i> in <i>tree</i>
     * @return Root of <i>tree</i>
     */
    public Node<N> getRoot(){
        return this.tree.getFirst();
    }

    /** Determines if current <i>tree</i> is fully filled with non-null <i>BinaryNode</i>, regardless of <i>capacity</i>
     * @return Whether tree contains only non-null <i>BinaryNode</i> or not
     */
    public boolean isFull(){
        return !this.tree.contains(null);
    }

    /** Determines if number of non-null <i>BinaryNode</i> in <i>tree</i> fills up to <i>capacity</i>
     * @return Whether tree contains same number of non-null nodes as full capacity
     */
    public boolean isFullToCapacity(){
        return this.tree.stream().filter(Objects::nonNull).count() == this.getCapacity();
    }

    //MUTATORS
    /** Inserts new subtree at given <i>rootIndex</i>
     * @param rootIndex Index in <i>this</i> to store root of new subtree
     * @param subtree New subtree to insert
     * @return Whether inserting new subtree is successful for not
     */
    public abstract boolean insertSubtree(int rootIndex, Tree<N> subtree);

    /** Removes subtree rooted at <i>Node</i> located at given <i>rootIndex</i>
     * @param rootIndex Index of subtree root
     * @return Whether removal of subtree is successful or not
     */
    public abstract boolean removeSubtree(int rootIndex);

    /** Sets the degree to a new value
     * @param degree Maximum number of children each node can have
     * @return Whether degree changed successfully or not
     */
    public boolean setDegree(int degree){
        if(degree <= 0 || this.degree == degree)
            return false;
        this.degree = degree;
        return true;
    }

    /** Reserve a minimum number of slots in <i>tree</i> as long as <i>capacity</i> is not breached
     * @param numNodes Number of slots for <i>Node</i> to reserve
     * @return Whether reserving slots is successful or not
     */
    public boolean reserve(int numNodes){
        if(numNodes > this.getCapacity() && this.hasActiveCapacity())
            return false;
        if(numNodes <= this.getTreeSize())
            return true;
        while(this.getTreeSize() < numNodes)
            this.tree.add(null);
        return true;
    }

    /** Removes all nodes, and if capacity is enforced, refill tree to full capacity with null
     */
    public void clear(){
        this.clear(this.getCapacity());
    }

    /** Removes all nodes and reserves <i>newSize</i> number of spots
     * @param newSize Number of spots to be reserved and initialized to null
     */
    public void clear(int newSize){
        this.tree.clear();
        if(newSize >= this.getCapacity() && this.hasActiveCapacity())
            this.tree.addAll(Collections.nCopies(this.getCapacity(), null));
        else
            this.reserve(newSize);
    }

    /** Enable or disable usage of capacity
     * @param enableCapacity Whether capacity will be enforced or not
     */
    public void setActiveCapacity(boolean enableCapacity){
        this.capacity.setAt1(enableCapacity);
    }

    /** Sets a maximum limit on number of <i>Node</i> allowed in <i>this</i>
     * @param capacity Capacity of <i>Node</i>
     * @return Whether setting a new capacity is successful or not
     */
    public boolean setCapacity(int capacity){
        if(capacity < 0 || capacity == this.getCapacity())
            return false;
        this.capacity.setAt0(capacity);
        while(this.getTreeSize() > capacity)
            this.tree.removeLast();
        return true;
    }

    //MEMBER VARIABLES
    /** Maximum number of children each node can have
     */
    protected int degree;
    /** Maximum number of children each node can have, and whether that is enforced or not
     */
    protected Pair<Integer, Boolean> capacity;
    /** Table / array-based implementation of tree
     */
    protected FastTable<Node<N>> tree;

}


