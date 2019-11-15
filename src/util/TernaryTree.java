package JTreeLib.util;
import java.util.concurrent.ArrayBlockingQueue;
import org.javatuples.Pair;

import javax.jws.Oneway;

public class TernaryTree<N, K> extends Tree<N> implements Cloneable {
    //MEMBER VARIABLES

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor
     */
    public TernaryTree(){
        super();
        super.setDegree(3);
    }

    /** Constructor applies <i>name</i> and <i>capacity</i> to <i>TernaryTree</i>
     * @param name A string that identifies a specific <i>TernaryTree</i>
     * @param capacity A Javatuple Pair that holds maximum number of <i>TernaryNode</i> allowed and whether that is enforced or not
     */
    public TernaryTree(String name, int capacity){
        super(name, 3, capacity);
    }

    /** Copy constructor
     * @param subTree <i>TernaryTree</i> to be copied
     */
    public TernaryTree(TernaryTree<N, K> subTree){ super(subTree); }

    //ACCESSORS
    @Override
    public TernaryNode<N, K> getNode(int index){
        if(index < 0)
            return null;
        return this.getNode(index);
    }

    @Override
    public TernaryNode<N, K> getRoot(){
        return this.getNode(0);
    }

    /** Finds out if <i>TernaryNode</i> at given index is a lonely and detached <i>TernaryNode</i>, meaning no children and no parent
     * @param index Index of <i>TernaryNode</i> of interest
     * @return Whether <i>TernaryNode</i> is a singleton or not
     */
    @Override
    public boolean isSingleton(int index){
        if(!this.hasNode(index))
            return false;
        return !this.hasNode(index) && this.isLeaf(index);
    }

    /** Finds out if <i>TernaryNode</i> is a lonely and detached <i>TernaryNode</i>, meaning no children and no parent
     * @param node <i>TernaryNode</i> of interest
     * @return Whether <i>TernaryNode</i> is a singleton or not
     */
    public boolean isSingleton(TernaryNode<N, K> node){ return this.isSingleton(this.tree.indexOf(node)); }

    /** Finds index of parent for some <i>TernaryNode</i> located at <i>childIndex</i>
     * @param childIndex Index of child
     * @return Parent index
     */
    public int getParentIndex(int childIndex){
        if(childIndex < 0)
            return -1;
        return (int)Math.floor((childIndex - 1) / 3.0);
    }

    /** Finds index of first child
     * @param parentIndex Index of parent <i>TernaryNode</i>
     * @return Index of first child
     */
    public int getFirstChildIndex(int parentIndex){
        if(parentIndex < 0)
            return -1;
        return 3 * parentIndex + 1;
    }

    /** Finds index of middle child
     * @param parentIndex Index of parent <i>TernaryNode</i>
     * @return Index of middle child
     */
    public int getMiddleChildIndex(int parentIndex){
        if(parentIndex < 0)
            return -1;
        return 3 * parentIndex + 2;
    }

    /** Finds index of last child
     * @param parentIndex Index of parent <i>TernaryNode</i>
     * @return Index of last child
     */
    public int getLastChildIndex(int parentIndex){
        if(parentIndex < 0)
            return -1;
        return 3 * parentIndex + 3;
    }

    /** Abstract function to find parent of <i>TernaryNode</i> located at <i>childIndex</i>
     * @param childIndex Index of <i>TernaryNode</i> in <i>this</i>
     * @return Parent of <i>TernaryNode</i>  if exists, otherwise returns <i>null</i>
     */
    public TernaryNode<N, K> getParent(int childIndex){
        if(this.getParentIndex(childIndex) == -1)
            return null;
        return (TernaryNode<N, K>)super.getNode(this.getParentIndex(childIndex));
    }

    /** Finds first child of <i>TernaryNode</i> located at <i>parentIndex</i>
     * @param parentIndex Index of parent <i>TernaryNode</i>
     * @return First child
     */
    public TernaryNode<N, K> getFirstChild(int parentIndex){
        if(parentIndex < 0 || this.getNode(parentIndex) == null)
            return null;
        int childIndex = this.getFirstChildIndex(parentIndex);
        if(this.getNode(childIndex) != null)
            this.getNode(parentIndex).setFirstChild(this.getNode(childIndex));
        return this.getNode(childIndex);
    }

    /** Finds middle child of <i>TernaryNode</i> located at <i>parentIndex</i>
     * @param parentIndex Index of parent <i>TernaryNode</i>
     * @return Middle child
     */
    public TernaryNode<N, K> getMiddleChild(int parentIndex){
        if(parentIndex < 0 || this.getNode(parentIndex) == null)
            return null;
        int childIndex = this.getMiddleChildIndex(parentIndex);
        if(this.getNode(childIndex) != null)
            this.getNode(parentIndex).setMiddleChild(this.getNode(childIndex));
        return this.getNode(childIndex);
    }

    /** Finds last child of <i>TernaryNode</i> located at <i>parentIndex</i>
     * @param parentIndex Index of parent <i>TernaryNode</i>
     * @return Last child
     */
    public TernaryNode<N, K> getLastChild(int parentIndex){
        if(parentIndex < 0 || this.getNode(parentIndex) == null)
            return null;
        int childIndex = this.getLastChildIndex(parentIndex);
        if(this.getNode(childIndex) != null)
            this.getNode(parentIndex).setLastChild(this.getNode(childIndex));
        return this.getNode(childIndex);
    }

    /** Finds subtree with root at <i>rootIndex</i> and returns subtree as separate <i>TernaryTree</i>
     * @param rootIndex Index of root of subtree
     * @return New <i>TernaryTree</i> that represents a subtree
     */
    @Override
    public TernaryTree<N, K> getSubtree(int rootIndex){
        TernaryTree<N, K> output = new TernaryTree<>(null, super.getCurrentSize() - rootIndex + 1);
        ArrayBlockingQueue<Pair<TernaryNode<N, K>, Integer>> Q = new ArrayBlockingQueue<>(this.getTreeSize());
        Q.offer(Pair.with(this.getNode(rootIndex), 0));
        while(!Q.isEmpty()){
            Pair<TernaryNode<N, K>, Integer> top = Q.poll();
            output.setNode(top.getValue1(), top.getValue0());
            int oldParentIndex = super.tree.indexOf(top.getValue0()), newParentIndex = top.getValue1();
            if(this.getFirstChild(oldParentIndex) != null)
                Q.offer(Pair.with(this.getFirstChild(oldParentIndex), this.getFirstChildIndex(newParentIndex)));
            if(this.getMiddleChild(oldParentIndex) != null)
                Q.offer(Pair.with(this.getMiddleChild(oldParentIndex), this.getMiddleChildIndex(newParentIndex)));
            if(this.getLastChild(oldParentIndex) != null)
               Q.offer(Pair.with(this.getLastChild(oldParentIndex), this.getLastChildIndex(newParentIndex)));
        }
        return output;
    }

    //MUTATORS
    /** Sets new <i>TernaryNode</i> at some given index and replaces old <i>Node</i> if applicable.
     * Note: does not check whether a parent already exists, therefore may lead to a detached <i>TernaryNode</i> in tree
     * @param index Index to store new <i>TernaryNode</i>
     * @param node <i>TernaryNode</i> to be added
     * @return Whether adding new <i>TernaryNode</i> is successful or not
     */
    public boolean setNode(int index, TernaryNode<N, K> node){
        if(index <= 0 || index >= this.getCapacity() && this.getCapacity() != -1 || this.getDegree() != node.getChildCapacity())
            return false;
        this.tree.set(index, node);
        return true;
    }

    /** Inserts, if empty, or replaces subtree in <i>TernaryTree</i> rooted at <i>rootIndex</i>
     * @param rootIndex Index to store root of new subtree
     * @param subTree New subtree to insert
     * @return
     */
    @Override
    public boolean insertSubtree(int rootIndex, Tree<N> subTree){
        if(subTree == null || subTree.getDegree() != 3)
            return false;
        TernaryTree<N, K> sub = (TernaryTree<N, K>)subTree;
        ArrayBlockingQueue<Pair<TernaryNode<N, K>, Integer>> Q = new ArrayBlockingQueue<>(this.getTreeSize());
        Q.offer(Pair.with(sub.getRoot(), rootIndex));
        while(!Q.isEmpty()){
            Pair<TernaryNode<N, K>, Integer> top = Q.poll();
            int oldIndex = subTree.tree.indexOf(top.getValue0()), newIndex = top.getValue1();
            this.setNode(newIndex, top.getValue0());
            if(sub.getFirstChild(oldIndex) != null)
                Q.offer(Pair.with(sub.getFirstChild(oldIndex), this.getFirstChildIndex(newIndex)));
            if(sub.getMiddleChild(oldIndex) != null)
                Q.offer(Pair.with(sub.getMiddleChild(oldIndex), this.getMiddleChildIndex(newIndex)));
            if(sub.getLastChild(oldIndex) != null)
                Q.offer(Pair.with(sub.getLastChild(oldIndex), this.getLastChildIndex(newIndex)));
        }
        return true;
    }

    /** Removes subtree rooted at <i>rootIndex</i>
     * @param rootIndex Index of subtree root
     * @return Whether removal is successful or not
     */
    @Override
    public boolean removeSubtree(int rootIndex){
        if(this.getNode(rootIndex) == null)
            return false;
        ArrayBlockingQueue<Pair<TernaryNode<N, K>, Integer>> Q = new ArrayBlockingQueue<>(this.getTreeSize());
        Q.offer(Pair.with(this.getNode(rootIndex), rootIndex));
        while(!Q.isEmpty()){
            Pair<TernaryNode<N, K>, Integer> top = Q.poll();
            this.tree.remove(top.getValue0());
            int index = top.getValue1();
            if(this.getFirstChild(index) != null)
                Q.offer(Pair.with(this.getFirstChild(index), this.getFirstChildIndex(index)));
            if(this.getMiddleChild(index) != null)
                Q.offer(Pair.with(this.getMiddleChild(index), this.getMiddleChildIndex(index)));
            if(this.getLastChild(index) != null)
                Q.offer(Pair.with(this.getLastChild(index), this.getLastChildIndex(index)));
        }
        return true;
    }

}
