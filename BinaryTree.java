package JTreeLib.util;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import javolution.util.FastSortedSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Queue;
import java.util.Stack;
import org.javatuples.Pair;

/** Generic binary tree that follows no particular ordering rule
 * @param <N> Data type of <b>BinaryNode</b>
 * @param <K> Data type of <b>Key</b>
 */
public class BinaryTree<N, K> extends Tree<N> {
    //CONSTRUCTORS
    /** Default constructor creates empty tree
     */
    BinaryTree(){
        super();
    }

    /** Constructor applies custom root <b>BinaryNode</b> and sets the capacity
     * @param root First <b>BinaryNode</b> in <i>this</i>
     * @param capacity Maximum number of <b>BinaryNode</b> that <i>this</i> can contain in total
     */
    BinaryTree(BinaryNode<N, K> root, int capacity){
        this.tree.addFirst(root);
        this.capacity = Pair.with(capacity, true);
    }

    //ACCESSORS
    /** Finds the <i>BinaryBinaryNode</i> located at given <i>index</i> in <i>this</i>
     * @param index 0-based index in <i>tree</i>
     * @return <i>BinaryNode</i> at given index
     */
    @Override
    public BinaryNode<N, K> getNode(int index){
        return (BinaryNode<N, K>)super.getNode(index);
    }

    /** Finds first <i>BinaryNode</i> in <i>this</i>
     * @return Root of <i>this</i>
     */
    @Override
    public BinaryNode<N, K> getRoot(){
        return (BinaryNode<N, K>)this.tree.getFirst();
    }

    /** Finds the parent <b>BinaryNode</b> of a given child <b>BinaryNode</b>
     * @param child <b>BinaryNode</b> object that is not root
     * @return <b>BinaryNode</b> object that has child as an immediate descendent
     */
    public BinaryNode<N, K> getParent(BinaryNode<N, K> child){
        int childIndex = this.tree.indexOf(child);
        return this.getParent(childIndex);
    }

    /** Finds the parent <b>BinaryNode</b> of a given child <b>BinaryNode</b>
     * @param childIndex Index of a <i>BinaryNode</i> that is not root
     * @return <b>BinaryNode</b> object that has child as an immediate descendent
     */
    @Override
    public BinaryNode<N, K> getParent(int childIndex){
        if(childIndex < 0
                || childIndex >= this.getTreeSize()
                || this.getNode(childIndex) == null
                || this.getNode(childIndex).equals(this.getRoot()))
            return null;
        return this.getNode(BinaryTree.getParentIndex(childIndex));
    }

    /** Gets deep copy of subtree rooted at <i>rootIndex</i> in <i>this</i>
     * @param rootIndex Index of subtree root
     * @return New <i>Tree</i> containing copies of all <i>Node</i> in copied subtree in same order
     */
    @Override
    public BinaryTree<N, K> getSubtree(int rootIndex){
        if(rootIndex < 0
            || rootIndex >= this.getTreeSize()
            || !this.hasNode(rootIndex))
            return null;
        BinaryTree<N, K> temp = new BinaryTree<>();
        temp.setCapacity(this.getCapacity());
        temp.clear();
        Queue<Pair<Integer, Integer>> Q = new LinkedBlockingQueue<>();
        Q.offer(Pair.with(rootIndex, 0));
        while(!Q.isEmpty()){
            int thisIndex = Q.peek().getValue0();
            int tempIndex = Q.peek().getValue1();
            if(this.hasNode(BinaryTree.getLeftChildIndex(thisIndex)))
                Q.offer(Pair.with(BinaryTree.getLeftChildIndex(thisIndex), BinaryTree.getLeftChildIndex(tempIndex)));
            if(this.hasNode(BinaryTree.getRightChildIndex(thisIndex)))
                Q.offer(Pair.with(BinaryTree.getRightChildIndex(thisIndex), BinaryTree.getRightChildIndex(tempIndex)));
            temp.tree.set(tempIndex, new BinaryNode<>(this.getNode(thisIndex)));
            Q.poll();
        }
        return temp;
    }

    /** Finds the left child <b>BinaryNode</b> of some other <b>BinaryNode</b>
     * @param parent Any <b>BinaryNode</b> preferably with a left child <b>BinaryNode</b>
     * @return <b>BinaryNode</b> connected to parent <b>BinaryNode</b> on left side
     */
    public BinaryNode<N, K> getLeftChild(BinaryNode<N, K> parent){
        int parentIndex = this.tree.indexOf(parent);
        return this.getLeftChild(parentIndex);
    }

    /** Finds the left child <b>BinaryNode</b> of some other <b>BinaryNode</b>
     * @param parentIndex Index of any <b>BinaryNode</b> preferably with a left child <b>BinaryNode</b>
     * @return <b>BinaryNode</b> connected to parent <b>BinaryNode</b> on left side
     */
    public BinaryNode<N, K> getLeftChild(int parentIndex){
        if(parentIndex < 0
                || parentIndex >= this.getTreeSize()
                || this.getNode(parentIndex) == null)
            return null;
        return this.getNode(BinaryTree.getLeftChildIndex(parentIndex));
    }

    /** Finds the right child <b>BinaryNode</b> of some other <b>BinaryNode</b>
     * @param parent Any <b>BinaryNode</b> preferably with a right child <b>BinaryNode</b>
     * @return <b>BinaryNode</b> connected to parent <b>BinaryNode</b> on right side
     */
    public BinaryNode<N, K> getRightChild(BinaryNode<N, K> parent){
        int parentIndex = this.tree.indexOf(parent);
        return this.getRightChild(parentIndex);
    }

    /** Finds the right child <b>BinaryNode</b> of some other <b>BinaryNode</b>
     * @param parentIndex Index of any <b>BinaryNode</b> preferably with a right child <b>BinaryNode</b>
     * @return <b>BinaryNode</b> connected to parent <b>BinaryNode</b> on right side
     */
    public BinaryNode<N, K> getRightChild(int parentIndex){
        if(parentIndex < 0
                || parentIndex >= this.getTreeSize()
                || this.getNode(parentIndex) == null)
            return null;
        return this.getNode(BinaryTree.getRightChildIndex(parentIndex));
    }

    /** Finds index in <i>this</i> that holds parent <b>BinaryNode</b> of some child <b>BinaryNode</b>
     * @param childIndex Index of a <b>BinaryNode</b> that is not root
     * @return Index of parent <b>BinaryNode</b> of <b>BinaryNode</b> located at childIndex
     */
    public static int getParentIndex(int childIndex){
        return (int)Math.ceil((double)childIndex / 2 - 1);
    }

    /** Finds index in <i>this</i> that holds left child <b>BinaryNode</b> of some other <b>BinaryNode</b>
     * @param parentIndex Index of a <b>BinaryNode</b> in <i>this</i>
     * @return Index of left child <b>BinaryNode</b> of parent <b>BinaryNode</b>
     */
    public static int getLeftChildIndex(int parentIndex){
        return 2 * parentIndex + 1;
    }

    /** Finds index in <i>this</i> that holds right child <b>BinaryNode</b> of some other <b>BinaryNode</b>
     * @param parentIndex Index of a <b>BinaryNode</b> in <i>this</i>
     * @return Index of right child <b>BinaryNode</b> of parent <b>BinaryNode</b>
     */
    public static int getRightChildIndex(int parentIndex){
        return 2 * parentIndex + 2;
    }

    /** Finds key located at index
     * @param index Index in <i>this</i>
     * @return Key if it exists at index
     */
    public K getKey(int index){
        if(index < 0
            || index >= this.getTreeSize()
            || this.getNode(index) == null)
            return null;
        return this.getNode(index).getKey();
    }

    /**Finds maximum number of nodes that can fill a given level; level 1 has only root
     * @param level Number of horizontal layers of <b>BinaryNode</b> below root, which is at level 1
     * @return Maximum number of nodes level can have
     */
    public int capacityAtLevel(int level){
        if(level <= 0)
            return 0;
        return (int)Math.pow(2, level - 1);
    }

    /**Finds index of next <b>BinaryNode</b> to be visited in inorder traversal (LVR)
     * @param key Value a <i>BinaryNode</i> holds
     * @return Index of inorder successor of <b>BinaryNode</b> containing <i>key</i>
     */
    public int getInorderSuccessorIndex(K key){
        int index = this.search(key);
        if(this.tree.isEmpty() || index == -1)
            return -1;
        boolean keyIsVisited = false;
        Stack<Integer> s = new Stack<>();
        FastSortedSet<Integer> visited = new FastSortedSet<>();
        s.push(0);
        while(!s.isEmpty()){
            int topIndex = s.peek();
            int leftIndex = BinaryTree.getLeftChildIndex(topIndex);
            int rightIndex = BinaryTree.getRightChildIndex(topIndex);
            if(!visited.contains(leftIndex) && this.getNode(leftIndex) != null) {
                s.push(leftIndex);
                continue;
            }
            if(keyIsVisited)
                return topIndex;
            if(topIndex == index)
                keyIsVisited = true;
            visited.add(s.pop());
            if(!visited.contains(rightIndex) && this.getNode(rightIndex) != null)
                s.push(rightIndex);
        }
        return -1;
    }

    /** Finds maximum number of nodes that <i>this</i> can have at given height
     * @param height Number of levels in <i>this</i>
     * @return Maximum number of nodes for given height
     */
    public int capacityAtHeight(int height){
        return (int)Math.pow(2, height) - 1;
    }

    /** Assuming a complete binary tree, finds number of levels needed to accommodate <i>numNode</i> <i>BinaryNode</i>
     * @param numNodes Number of <i>BinaryNode</i>
     * @return Number of levels needed to accommodate <i>numNodes</i> of <i>BinaryNode</i>
     */
    public static int getLevels(int numNodes){
        if(numNodes < 0)
            return 0;
        return (int)Math.ceil(Math.log(numNodes + 1) / Math.log(2));
    }

    /** Assuming a complete binary tree, finds minimum height needed to accommodate <i>numNode</i> <i>BinaryNode</i>
     * @param numNodes Number of <i>BinaryNode</i>
     * @return Height needed to accommodate <i>numNodes</i> of <i>BinaryNode</i>
     */
    public static int getMinimumHeight(int numNodes){
        return BinaryTree.getLevels(numNodes) - 1;
    }

    /** Finds height of <i>this</i>, which is number of edges of longest path from <i>root</i> to any leaf
     * @return Height of <i>this</i>
     */
    public int getHeight(){
        return this.getHeight(0);
    }

    /** Finds height of <i>this</i> starting from a specific <i>index</i>
     * @param index Index of root of subtree for height search
     * @return Height of tree or subtree rooted at <i>root</i>
     */
    public int getHeight(int index){
        if(this.getNode(index).getLeaf() || !this.hasNode(index))
            return 0;
        else
            return 1 + Math.max(this.getHeight(BinaryTree.getLeftChildIndex(index)),
                    this.getHeight(BinaryTree.getRightChildIndex(index)));
    }

    /** Prints out each <i>key</i> after traversal by breadth
     */
    public void traverseByBreadth(){
        for(K key : this.traverseByBreadthArray())
            System.out.println(key);
    }

    /** Traverses <i>this</i> by breadth using an array to collect each visited <i>key</i>
     * @return Array containing each visited <i>key</i>
     */
    public K[] traverseByBreadthArray(){
        ArrayList<K> arr = new ArrayList<>();
        if(this.getNodeSize() == 0)
            return null;
        Queue<Integer> Q = new LinkedBlockingQueue<>();
        Q.offer(0);
        while(!Q.isEmpty()){
            int topIndex = Q.poll();
            if(this.getNode(topIndex) != null)
                Q.offer(BinaryTree.getLeftChildIndex(topIndex));
            if(this.getRightChild(topIndex) != null)
                Q.offer(BinaryTree.getRightChildIndex(topIndex));
            this.visit(topIndex, arr);
        }
        return (K[])arr.toArray();
    }

    /** Prints out each <i>key</i> after inorder (LVR) traversal by depth
     */
    public void traverseByDepth(){
        for(K key : this.traverseByDepthArray())
            System.out.println(key);
    }

    /** Traverses <i>this</i> inorder (LVR) by depth using an array to collect each visited <i>key</i>
     * @return Array containing each visited <i>key</i>
     */
    public K[] traverseByDepthArray(){
        if(this.getNodeSize() == 0)
            return null;
        ArrayList<K> arr = new ArrayList<>();
        this.traverseByDepthArrayRecursion(0, arr);
        return (K[])arr.toArray();
    }

    /** Searches for given <i>key</i>
     * @return Index of <i>BinaryNode</i> with given <i>key</i> if exists, otherwise returns -1
     */
    public int search(K key){
        for(int i = 1; i <= this.getTreeSize(); i++)
            if(this.getNode(i - 1).getKey().equals(key))
                return i - 1;
        return -1;
    }

    //MUTATORS
    /** Sets new left child of <i>BinaryNode</i> at <i>parentIndex</i>
     * @param parentIndex Index of parent <i>BinaryNode</i>
     * @param l Potential left child <i>BinaryNode</i> of parent <i>BinaryNode</i>
     * @return Whether setting left child <i>BinaryNode</i> is successful or not
     */
    public boolean setLeftChild(int parentIndex, BinaryNode<N, K> l){
        int index = BinaryTree.getLeftChildIndex(parentIndex);
        if(index >= this.getCapacity() && this.hasActiveCapacity()
                || this.getNode(parentIndex) == null
                || this.getIndex(l) >= 0)
            return false;
        this.getNode(parentIndex).setLeaf(false);
        if(l.countChildren() > 0)
            l.setLeaf(false);
        else
            l.setLeaf(true);
        this.tree.set(BinaryTree.getLeftChildIndex(parentIndex), l);
        this.getNode(parentIndex).children.setAt0(l);
        return true;
    }

    /** Sets new left child of <i>BinaryNode</i> at <i>parentIndex</i>, where left child is leaf containing given <i>key</i>
     * @param parentIndex Index of parent <i>BinaryNode</i>
     * @param key Value a <i>BinaryNode</i> holds
     * @return Whether setting left child <i>BinaryNode</i> is successful or not
     */
    public boolean setLeftChild(int parentIndex, K key){
        return this.setLeftChild(parentIndex, new BinaryNode<>(key));
    }

    /** Sets new right child of <i>BinaryNode</i> at <i>parentIndex</i>
     * @param parentIndex Index of parent <i>BinaryNode</i>
     * @param r Potential left child <i>BinaryNode</i> of parent <i>BinaryNode</i>
     * @return Whether setting left child <i>BinaryNode</i> is successful or not
     */
    public boolean setRightChild(int parentIndex, BinaryNode<N, K> r){
        int index = BinaryTree.getRightChildIndex(parentIndex);
        if(index >= this.getCapacity() && this.hasActiveCapacity()
            || this.getNode(parentIndex) == null
            || this.getIndex(r) >= 0)
            return false;
        this.getNode(parentIndex).setLeaf(false);
        if(r.countChildren() > 0)
            r.setLeaf(false);
        else
            r.setLeaf(true);
        this.tree.set(BinaryTree.getRightChildIndex(parentIndex), r);
        this.getNode(parentIndex).children.setAt1(r);
        return true;
    }

    /** Sets new right child of <i>BinaryNode</i> at <i>parentIndex</i>, where right child is leaf containing given <i>key</i>
     * @param parentIndex Index of parent <i>BinaryNode</i>
     * @param key Value a <i>BinaryNode</i> holds
     * @return Whether setting left child <i>BinaryNode</i> is successful or not
     */
    public boolean setRightChild(int parentIndex, K key){
        return this.setRightChild(parentIndex, new BinaryNode<>(key));
    }

    /** Adds new <i>BinaryNode</i> at bottom of <i>this</i>, and <i>this</i> will expand by 1 level if full
     * @param key Value a <i>BinaryNode</i> holds
     * @return Whether inserting new <i>BinaryNode</i> is successful or not
     */
    public boolean insert(K key){
        if(this.isFullToCapacity() && this.hasActiveCapacity())
            return false;
        if(this.isFull())
            this.reserve(this.capacityAtHeight(this.getHeight() + 1));
        int index = 0;
        for(int i = this.getTreeSize(); i >= 1; i--)
            if(this.getNode(i - 1) != null)
                index = i - 1;
        this.tree.set(index, new BinaryNode<>(key));
        return true;
    }

    /** Removes <i>BinaryNode</i> from <i>tree</i> with given <i>key</i>
     * @param key Value a <i>BinaryNode</i> holds
     * @return Whether deletion of <i>BinaryNode</i> is successful or not
     */
    public boolean remove(K key){
        int index = this.search(key);
        if(this.tree.isEmpty() || index == -1)
            return false;
        int lastNodeIndex = this.getTreeSize() - 1;
        for(int i = this.getTreeSize(); i >= 1; i--)
            if(this.tree.get(i - 1) != null){
                lastNodeIndex = i - 1;
                break;
            }
        this.tree.remove(index);
        this.tree.set(index, this.tree.get(lastNodeIndex));
        this.tree.remove(lastNodeIndex);
        return true;
    }

    /** Inserts new subtree at given <i>rootIndex</i>
     * @param rootIndex Index in <i>this</i> to store root of new subtree
     * @param subtree New subtree to insert
     * @return Whether inserting new subtree is successful for not
     */
    @Override
    public boolean insertSubtree(int rootIndex, Tree<N> subtree){
        if(rootIndex < 0 || rootIndex >= this.getTreeSize())
            return false;
        if(rootIndex == 0){
            this.tree = subtree.tree;
            return true;
        }
        this.removeSubtree(rootIndex);
        Queue<Pair<Integer, Integer>> Q = new LinkedBlockingQueue<>();
        Q.offer(Pair.with(0, rootIndex));
        while(!Q.isEmpty()){
            int subtreeIndex = Q.peek().getValue0();
            int thisIndex = Q.peek().getValue1();
            if(subtree.hasNode(BinaryTree.getLeftChildIndex(subtreeIndex)))
                Q.offer(Pair.with(BinaryTree.getLeftChildIndex(subtreeIndex), BinaryTree.getLeftChildIndex(thisIndex)));
            if(subtree.hasNode(BinaryTree.getRightChildIndex(subtreeIndex)))
                Q.offer(Pair.with(BinaryTree.getRightChildIndex(subtreeIndex), BinaryTree.getRightChildIndex(thisIndex)));
            this.tree.set(thisIndex, new Node<>(subtree.getNode(subtreeIndex)));
            Q.poll();
        }
        return true;
    }

    /** Deletes subtree rooted at <i>Node</i> located at given <i>rootIndex</i>
     * @param rootIndex Index of subtree root
     * @return Whether removal of subtree is successful or not
     */
    @Override
    public boolean removeSubtree(int rootIndex){
        if(rootIndex < 0
                || rootIndex >= this.getTreeSize()
                || this.getNode(rootIndex) == null)
            return false;
        if(rootIndex == 0){
            this.clear();
            return true;
        }
        Queue<Integer> Q = new LinkedBlockingQueue<>();
        Q.offer(rootIndex);
        while(!Q.isEmpty()){
            int topIndex = Q.poll();
            if(this.hasNode(BinaryTree.getLeftChildIndex(topIndex)))
                Q.offer(BinaryTree.getLeftChildIndex(topIndex));
            if(this.hasNode(BinaryTree.getRightChildIndex(topIndex)))
                Q.offer(BinaryTree.getRightChildIndex(topIndex));
            this.tree.set(topIndex, null);
        }
        return true;
    }

    /** Performs left rotation rooted at given index
     * @param topIndex Index if topmost <i>BinaryNode</i> to be moved
     * @return Whether left-rotating subtree rooted at <i>topIndex</i> is successful or not
     */
    public boolean leftRotation(int topIndex){
        if(topIndex < 0
            || topIndex >= this.getTreeSize()
            || this.getRightChild(topIndex) == null
            || this.getNode(topIndex) == null)
            return false;
        int newTopIndex = BinaryTree.getLeftChildIndex(topIndex);
        int childIndex = BinaryTree.getRightChildIndex(topIndex);
        int grandChildIndex = BinaryTree.getLeftChildIndex(childIndex);
        BinaryTree<N, K> leftSubtree = this.getSubtree(newTopIndex);
        BinaryTree<N, K> grandChildSubtree = this.getSubtree(grandChildIndex);
        BinaryNode<N, K> topNode = new BinaryNode<>(this.getNode(topIndex));
        this.tree.set(grandChildIndex, null);
        this.removeSubtree(newTopIndex);

        BinaryTree<N, K> rightSubtree = this.getSubtree(childIndex);
        this.removeSubtree(childIndex);
        this.insertSubtree(topIndex, rightSubtree);
        this.setLeftChild(topIndex, topNode);
        this.insertSubtree(BinaryTree.getLeftChildIndex(newTopIndex), leftSubtree);
        this.insertSubtree(BinaryTree.getRightChildIndex(newTopIndex), grandChildSubtree);
        this.getNode(newTopIndex).children.setAt0(this.getLeftChild(newTopIndex));
        this.getNode(newTopIndex).children.setAt1(this.getRightChild(newTopIndex));
        return true;
    }

    /** Performs right rotation rooted at given index
     * @param topIndex Index if topmost <i>BinaryNode</i> to be moved
     * @return Whether right-rotating subtree rooted at <i>topIndex</i> is successful or not
     */
    public boolean rightRotation(int topIndex){
        if(topIndex < 0
                || topIndex >= this.getTreeSize()
                || this.getLeftChild(topIndex) == null
                || this.getNode(topIndex) == null)
            return false;
        int newTopIndex = BinaryTree.getRightChildIndex(topIndex);
        int childIndex = BinaryTree.getLeftChildIndex(topIndex);
        int grandChildIndex = BinaryTree.getRightChildIndex(childIndex);
        BinaryTree<N, K> rightSubtree = this.getSubtree(newTopIndex);
        BinaryTree<N, K> grandChildSubtree = this.getSubtree(grandChildIndex);
        BinaryNode<N, K> topNode = new BinaryNode<>(this.getNode(topIndex));
        this.tree.set(grandChildIndex, null);
        this.removeSubtree(newTopIndex);

        BinaryTree<N, K> leftSubtree = this.getSubtree(childIndex);
        this.removeSubtree(childIndex);
        this.insertSubtree(topIndex, leftSubtree);
        this.setRightChild(topIndex, topNode);
        this.insertSubtree(BinaryTree.getRightChildIndex(newTopIndex), rightSubtree);
        this.insertSubtree(BinaryTree.getLeftChildIndex(newTopIndex), grandChildSubtree);
        this.getNode(newTopIndex).children.setAt0(this.getLeftChild(newTopIndex));
        this.getNode(newTopIndex).children.setAt1(this.getRightChild(newTopIndex));
        return true;
    }

    //MEMBER VARIABLES AND SPECIAL FUNCTIONS
    /** Confirm visit of <i>BinaryNode</i> during traversal of <i>this</i> by printing out <i>key</i> at index
     * @param index Index of <i>this</i> containing <i>key</i>
     * @return Whether visit is successful or not
     */
    protected boolean visit(int index){
        if(index < 0 || index > this.tree.size() || this.getKey(index) == null)
            return false;
        System.out.println(this.getKey(index));
        return true;
    }

    /** Confirm visit of <i>key</i> during traversal of <i>this</i> by adding <i>key</i> at <i>index</i> to <i>arr</i>
     * to <i>visitedBinaryNodes</i>
     * @param index Index of <i>this</i> containing <i>key</i>
     * @param arr Collection to add <i>key</i> to
     * @return Whether visit is successful or not
     */
    protected boolean visit(int index, Collection<K> arr){
        if(index < 0 || index > this.getTreeSize())
            return false;
        arr.add(this.getKey(index));
        return true;
    }

    /** Sub-function to be used within <i>traverseByDepthArray</i>; constitutes the recursive call
     * @param index Current <i>index</i> of <i>BinaryNode</i> of focus during traversal
     * @param arr Collection to add <i>key</i> to
     */
    protected void traverseByDepthArrayRecursion(int index, Collection<K> arr){
        if(this.getLeftChild(index) != null)
            this.traverseByDepthArrayRecursion(BinaryTree.getLeftChildIndex(index), arr);
        this.visit(index, arr);
        if(this.getRightChild(index) != null)
            this.traverseByDepthArrayRecursion(BinaryTree.getRightChildIndex(index), arr);
    }

}

/** Variant of <i>BinaryTree</i> that sorts <i>key</i>
 * @param <N> Data type of <b>BinaryNode</b>
 * @param <K> Data type of <b>Key</b>
 */
class BinarySearchTree<N, K> extends BinaryTree<N, K>{
    //CONSTRUCTORS
    /** Default constructor creates empty tree
     */
    BinarySearchTree(){super();}

    /** Constructor applies custom root <b>BinaryNode</b> and sets capacity
     * @param root First <b>BinaryNode</b> in <i>this</i>
     * @param capacity Maximum number of <b>BinaryNode</b> that <i>this</i> can contain in total
     */
    BinarySearchTree(BinaryNode<N, K> root, int capacity){
        super(root, capacity);
    }

    //ACCESSORS
    /** Search for index of <i>BinaryNode</i> containing given <i>key</i>
     * @param key Value a <i>BinaryNode</i> holds
     * @return Index of <i>BinaryNode</i> containing <i>key</i>, otherwise -1 if does not exist
     */
    @Override
    public int search(K key){
        return this.search(key, 0);
    }

    /** Search for index of <i>BinaryNode</i> containing given <i>key</i>, but starting seach at given <i>startIndex</i>
     * @param key Value a <i>BinaryNode</i> holds
     * @return Index of <i>BinaryNode</i> containing <i>key</i>, otherwise -1 if does not exist
     */
    public int search(K key, int startIndex){
        if (this.tree.isEmpty())
            return -1;
        int index = startIndex;
        while(this.getKey(index) != null){
            if(this.getKey(index).equals(key))
                return index;
            else if((double)this.getKey(index) > (double)key)
                index = BinaryTree.getLeftChildIndex(index);
            else
                index = BinaryTree.getRightChildIndex(index);
        }
        return -1;
    }

    /** Finds index of the next node to be visited in inorder traversal (LVR)
     * @param key Value a <i>BinaryNode</i> holds
     * @return Index of inorder successor to <i>BinaryNode</i> containing given <i>key</i>
     */
    @Override
    public int getInorderSuccessorIndex(K key){
        if(this.search(key) == -1)
            return -1;
        K[] arr = this.traverseByDepthArray();
        int arrIndex = Arrays.asList(arr).indexOf(key);
        if(arrIndex == arr.length - 1)
            return -1;
        return arrIndex + 1;
    }

    //MUTATORS
    /** Adds new <i>BinaryNode</i>at bottom of <i>this</i>, and <i>this</i> will expand by 1 level if full
     * @param key Value a <i>BinaryNode</i> holds
     * @return Whether inserting new <i>BinaryNode</i> is successful or not
     */
    @Override
    public boolean insert(K key){
        if(this.tree.isEmpty())
            this.tree.addFirst(new BinaryNode<>(key));
        int p = 0, prev = 0;
        while(this.hasNode(p)){
            prev = p;
            BinaryNode<N, K> node = (BinaryNode<N, K>)this.tree.get(p);
            if(node.getKey().equals(key))
                return false;
            else if(String.valueOf(key).compareTo(String.valueOf(node.getKey())) < 0)
                p = BinaryTree.getLeftChildIndex(p);
            else
                p = BinaryTree.getRightChildIndex(p);
        }
        if(String.valueOf(key).compareTo(String.valueOf(this.getKey(prev))) < 0)
            this.setLeftChild(prev, new BinaryNode<>(key));
        else
            this.setRightChild(prev, new BinaryNode<>(key));
        return true;
    }

    /** Removes <i>BinaryNode</i> with given <i>key</i> from <i>this</i>, if exists
     * @param key Value a <i>BinaryNode</i> holds
     * @return Whether removing <i>key</i> is successful or not
     */
    @Override
    public boolean remove(K key){
        int index = this.search(key);
        if(index == -1)
            return false;
        if(this.getNode(index).getLeaf())
            this.tree.set(index, null);
        else if(((BinaryNode<N, K>)this.tree.get(index)).countChildren() == 1){
            Queue<Pair<Integer, Integer>> Q = new LinkedBlockingQueue<>();
            int oldIndex = 0, newIndex = 0;
            if(this.tree.get(BinaryTree.getLeftChildIndex(index)) != null)
                Q.offer(Pair.with(BinaryTree.getLeftChildIndex(index), index));
            else
                Q.offer(Pair.with(BinaryTree.getRightChildIndex(index), index));
            while(!Q.isEmpty()){
                Pair<Integer, Integer> top = Q.poll();
                oldIndex = top.getValue0();
                newIndex = top.getValue1();
                if(this.tree.get(BinaryTree.getLeftChildIndex(oldIndex)) != null)
                    Q.offer(Pair.with(BinaryTree.getLeftChildIndex(oldIndex), BinaryTree.getLeftChildIndex(newIndex)));
                if(this.tree.get(BinaryTree.getRightChildIndex(oldIndex)) != null)
                    Q.offer(Pair.with(BinaryTree.getRightChildIndex(oldIndex), BinaryTree.getRightChildIndex(newIndex)));
                this.tree.set(newIndex, this.tree.get(oldIndex));
                this.tree.set(oldIndex, null);
            }
        } else {
            int inorder = this.getInorderSuccessorIndex(key);
            this.tree.set(index, this.tree.get(inorder));
            this.tree.set(inorder, null);
        }
        return true;
    }

}

/** Variant of <i>BinaryTree</i> that cannot allow a height difference of more than 1 for any
 *  adjacent pair of subtrees
 * @param <N> Data type of <b>BinaryNode</b>
 * @param <K> Data type of <b>Key</b>
 */
class AVLTree<N, K> extends BinarySearchTree<N, K> {
    //CONSTRUCTORS
    /** Default constructor creates empty tree
     */
    AVLTree(){super(); }

    /** Constructor applies custom root <b>BinaryNode</b> and sets capacity
     * @param root First <b>BinaryNode</b> in <i>this</i>
     */
    AVLTree(BinaryNode<N, K> root, int capacity){
        super(root, capacity);
    }

    //ACCESSORS
    /** Finds the <i>AVLNode</i> located at given <i>index</i> in <i>this</i>
     * @param index 0-based index in <i>tree</i>
     * @return <i>AVLNode</i> at given index
     */
    @Override
    public AVLNode<N, K> getNode(int index){
        return (AVLNode<N, K>)this.tree.get(index);
    }

    /** Checks whether <i>AVLNode</i> under <i>avlTree</i> at given <i>index</i> has balance factor equal either to -1, 0, or 1
     * @param index Index of <i>AVLNode</i> to check
     * @param avlTree <i>AVLTree</i>
     * @return Whether balance factor of <i>AVLNode</i> is acceptable
     */
    public static <N, K> boolean acceptableBalanceFactor(int index, AVLTree<N, K> avlTree){
        return AVLNode.acceptableBalanceFactor(avlTree.getNode(index), avlTree);
    }

    /** Checks whether <i>AVLNode</i> in <i>this</i> at given <i>index</i> has balance factor equal either to -1, 0
     * @param index Index of <i>AVLNode</i> to check
     * @return Whether balance factor of <i>AVLNode</i> is acceptable
     */
    public boolean acceptableBalanceFactor(int index){
        return AVLTree.acceptableBalanceFactor(index, this);
    }

    /** Gets and sets balance factor, which is height of right subtree minus height of left subtree;
     * Note: if <i>this</i> is not in <i>avlTree</i>, 0 will be returned
     * @return Balance factor
     */
    public int getBalanceFactor(int index){
        if(index < 0 || index >= this.getTreeSize())
            return 0;
        return this.getHeight(BinaryTree.getRightChildIndex(index))
                - this.getHeight(BinaryTree.getLeftChildIndex(index));
    }

    //MUTATORS
    /** Adds new <i>AVLNode</i> at bottom of <i>this</i>, and <i>this</i> will expand by 1 level if full
     * @param key Value a <i>BinaryNode</i> holds
     * @return Whether inserting new <i>BinaryNode</i> is successful or not
     */
    @Override
    public boolean insert(K key){
        boolean insertBool = super.insert(key);
        if(!insertBool)
            return false;
        int insertIndex = super.search(key);
        this.getNode(insertIndex).setBalanceFactor(0);
        K insertKey = this.getKey(insertIndex);
        int index = BinaryTree.getParentIndex(insertIndex);
        while(index > -1){
            if(String.valueOf(this.getKey(index)).compareTo(String.valueOf(insertKey)) < 0)
                this.getNode(index).setBalanceFactor(this.getNode(index).getBalanceFactor() + 1);
            else
                this.getNode(index).setBalanceFactor(this.getNode(index).getBalanceFactor() - 1);
            index = BinaryTree.getParentIndex(index);
        }

        index = BinaryTree.getParentIndex(insertIndex);
        while(index != 0){
            int topIndex;
            if(this.getBalanceFactor(index) > 1){
                topIndex = BinaryTree.getRightChildIndex(index);
                if(this.getBalanceFactor(topIndex) >= 0)
                    this.leftRotation(index);
                else {
                    this.rightRotation(topIndex);
                    this.leftRotation(index);
                }
                index = BinaryTree.getParentIndex(insertIndex);
            } else if(this.getBalanceFactor(index) < -1){
                topIndex = BinaryTree.getLeftChildIndex(index);
                if(this.getBalanceFactor(topIndex) <= 0)
                    this.rightRotation(index);
                else {
                    this.leftRotation(topIndex);
                    this.rightRotation(index);
                }
                index = BinaryTree.getParentIndex(insertIndex);
            }
            index = BinaryTree.getParentIndex(index);
        }
        return true;
    }

}

