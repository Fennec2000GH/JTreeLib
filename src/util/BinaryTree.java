package JTreeLib.util;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Collection;
import org.javatuples.Pair;

/** Generic binary tree that follows no particular ordering rule
 * @param <N> Data type of <i>BinaryNode</i>
 * @param <K> Data type of <i>Key</i>
 */
public class BinaryTree<N, K> extends Tree<N> {
    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor
     */
    public BinaryTree(){
        super();
        this.setDegree(2);
    }

    /** Constructor applies <i>name</i> and <i>capacity</i>
     * @param name A string that identifies a specific <i>BinaryTree</i>
     * @param capacity Maximum number of <i>BinaryNode</i> that the tree can hold
     */
    public BinaryTree(String name, int capacity){ super(name, 2, capacity); }

    /** Copy constructor
     */
    public BinaryTree(BinaryTree<N, K> otherTree){
        if(otherTree.getCapacity() != -1)
            this.tree = Collections.unmodifiableList(new ArrayList<>(otherTree.getMaximumSize()));
        else
            this.tree = new ArrayList<>(otherTree.getCurrentSize());
        for(int i = 0; i <= this.getCurrentSize() - 1; i++)
            this.tree.set(i, otherTree.tree.get(i) == null ? null : new BinaryNode<>(otherTree.getNode(i)));
    }

    //ACCESSORS
    @Override
    public BinaryNode<N, K> getNode(int index){
        return (BinaryNode<N, K>)super.getNode(index);
    }

    @Override
    public BinaryNode<N, K> getRoot(){ return (BinaryNode<N, K>)this.tree.get(0); }

    @Override
    public boolean hasNode(int index){
        if(index < 0 || index >= this.getTreeSize() || this.tree.get(index) == null)
            return false;
        return true;
    }

    /** Checks whether <i>BinaryTree</i> has a non-null <i>BinaryNode</i>
     * @param node <i>BinaryNode</i> to check existence in <i>BinaryTree</i>
     * @return Whether <i>BinaryNode</i> is in current <i>BinaryTree</i>
     */
    public boolean hasNode(BinaryNode<N, K> node){ return this.tree.contains(node); }

    /** Finds out if <i>Node</i> at given index of tree is a leaf
     * @param index Index where <i>Node</i> is located
     * @return Whether specified <i>Node</i> is a leaf or not
     */
    @Override
    public boolean isLeaf(int index){
        if(!this.hasNode(index))
            return false;
        if(!(this.hasNode(this.getLeftChild(index)) || this.hasNode(this.getRightChild(index))))
            return true;
        return false;
    }

    /** Finds out if <i>Node</i> at given index of tree is a leaf
     * @param node <i>Node</i> of interest
     * @return Whether specified <i>Node</i> is a leaf or not
     */
    public boolean isLeaf(Node<N> node){ return this.isLeaf(this.tree.indexOf(node)); }

    /** With a set capacity, finds the maximum possible size of the container holding <i>BinaryNode</i> when capacity is full
     * @param capacity Maximum number of <i>BinaryNode</i> that <i>this</i> can contain in total
     * @return Maximum size of container holding full capacity of <i>BinaryNode</i> when all flushed to right side of tree
     */
    public static int getMaximumSize(int capacity){ return (int)Math.pow(2, capacity); }

    /** With a set capacity, finds the maximum possible size container holding <i>BinaryNode</i> when capacity is full
     * for current <i>BinaryTree </i>
     * @return Maximum size of container holding full capacity of <i>BinaryNode</i> when all flushed to right side of tree
     */
    public int getMaximumSize(){ return BinaryTree.getMaximumSize(this.getCapacity()); }

    /** Finds the parent of a given child
     * @param childNode <i>BinaryNode</i> that is not root
     * @return <i>BinaryNode</i> that has <i>childNode</i> as an immediate descendent
     */
    public BinaryNode<N, K> getParent(BinaryNode<N, K> childNode){
        if(!this.hasNode(childNode))
            return null;
        return this.getParent(this.tree.indexOf(childNode));
    }

    /** Finds the parent <i>BinaryNode</i> of a given child <i>BinaryNode</i>
     * @param childIndex Index of a <i>BinaryNode</i> that is not root
     * @return <i>BinaryNode</i> object that has child as an immediate descendent
     */
    @Override
    public BinaryNode<N, K> getParent(int childIndex){
        if(!this.hasNode(childIndex))
            return null;
        int parentIndex = BinaryTree.getParentIndex(childIndex);
        BinaryNode<N, K> parent = this.getNode(parentIndex);
        if(!Arrays.asList(parent.getChildrenAsArray()).contains(this.getNode(childIndex)))
            if(BinaryTree.getLeftChildIndex(parentIndex) == childIndex)
                parent.setLeftChild(this.getNode(childIndex));
            else
                parent.setRightChild(this.getNode(childIndex));
        return parent;
    }

    /** Creates deep copy of subtree rooted at <i>rootIndex</i> with no enforced capacity
     * @param rootIndex Index of subtree root
     * @return <i>BinaryTree</i> containing copies of all <i>BinaryNode</i> in subtree in same order
     */
    @Override
    public BinaryTree<N, K> getSubtree(int rootIndex){
        if(!this.hasNode(rootIndex))
            return null;
        BinaryTree<N, K> output = new BinaryTree<>();
        ArrayBlockingQueue<Pair<Integer, Integer>> Q = new ArrayBlockingQueue<>(this.getTreeSize());
        Q.offer(Pair.with(rootIndex, 0));
        while(!Q.isEmpty()){
            Pair<Integer, Integer> indexPair = Q.poll();
            int thisIndex = indexPair.getValue0(), outputIndex = indexPair.getValue1();
            if(this.hasNode(BinaryTree.getLeftChildIndex(thisIndex)))
                Q.offer(Pair.with(BinaryTree.getLeftChildIndex(thisIndex), BinaryTree.getLeftChildIndex(outputIndex)));
            if(this.hasNode(BinaryTree.getRightChildIndex(thisIndex)))
                Q.offer(Pair.with(BinaryTree.getRightChildIndex(thisIndex), BinaryTree.getRightChildIndex(outputIndex)));
            output.setNode(outputIndex, new BinaryNode<>(this.getNode(thisIndex)));
        }
        return output;
    }

    /** Finds the last <i>BinaryNode</i> in current <i>BinaryTree</i>
     * @return Last non-null <i>BinaryNode</i> in <i>BinaryTree</i>
     */
    public BinaryNode<N, K> getLast(){ return (BinaryNode<N, K>)this.tree.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList())
            .get(this.getTreeSize() - 1);
    }

    /** Finds the left child of some <i>BinaryNode</i>
     * @param parent <i>BinaryNode</i> potentially with left child
     * @return Left child of <i>parent</i>
     */
    public BinaryNode<N, K> getLeftChild(BinaryNode<N, K> parent){
        if(!this.hasNode(parent))
            return null;
        int parentIndex = this.tree.indexOf(parent);
        return this.getLeftChild(parentIndex);
    }

    /** Finds the left child of some <i>BinaryNode</i>
     * @param parentIndex Index of parent
     * @return Left child of <i>parent</i>
     */
    public BinaryNode<N, K> getLeftChild(int parentIndex){
        if(!this.hasNode(parentIndex))
            return null;
        return this.getNode(BinaryTree.getLeftChildIndex(parentIndex));
    }

    /** Finds the right child of some <i>BinaryNode</i>
     * @param parent <i>BinaryNode</i> potentially with right child
     * @return Right child of <i>parent</i>
     */
    public BinaryNode<N, K> getRightChild(BinaryNode<N, K> parent){
        if(!this.hasNode(parent))
            return null;
        int parentIndex = this.tree.indexOf(parent);
        return this.getRightChild(parentIndex);
    }

    /** Finds the right child of some <i>BinaryNode</i>
     * @param parentIndex Index of parent
     * @return Right child of <i>parent</i>
     */
    public BinaryNode<N, K> getRightChild(int parentIndex){
        if(!this.hasNode(parentIndex))
            return null;
        return this.getNode(BinaryTree.getRightChildIndex(parentIndex));
    }

    /** Finds index in <i>this</i> that holds parent <i>BinaryNode</i> of some child <i>BinaryNode</i>
     * @param childIndex Index of a <i>BinaryNode</i> that is not root
     * @return Index of parent <i>BinaryNode</i> of <i>BinaryNode</i> located at childIndex
     */
    public static int getParentIndex(int childIndex){
        return (int)Math.ceil((double)childIndex / 2 - 1);
    }

    /** Finds index in <i>this</i> that holds left child <i>BinaryNode</i> of some other <i>BinaryNode</i>
     * @param parentIndex Index of a <i>BinaryNode</i> in <i>this</i>
     * @return Index of left child <i>BinaryNode</i> of parent <i>BinaryNode</i>
     */
    public static int getLeftChildIndex(int parentIndex){
        return 2 * parentIndex + 1;
    }

    /** Finds index in <i>this</i> that holds right child <i>BinaryNode</i> of some other <i>BinaryNode</i>
     * @param parentIndex Index of a <i>BinaryNode</i> in <i>this</i>
     * @return Index of right child <i>BinaryNode</i> of parent <i>BinaryNode</i>
     */
    public static int getRightChildIndex(int parentIndex){
        return 2 * parentIndex + 2;
    }

    /** Finds key located at index
     * @param index Index of key
     * @return Key if it exists at index
     */
    public K getKey(int index){
        if(!this.hasNode(index))
            return null;
        return this.getNode(index).getKey();
    }

    /**Finds maximum number of nodes that can fill a given level; level 1 has only root
     * @param level Number of horizontal layers of <i>BinaryNode</i> below root, which is at level 1
     * @return Maximum number of nodes level can have
     */
    public int capacityAtLevel(int level){
        if(level <= 0)
            return 0;
        return (int)Math.pow(2, level - 1);
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

    /** Check whether a given level of <i>BinaryTree</i> is full with no null spots left
     * @param level Level of tree to check, with root being in level 1
     * @return Whether specified level contains maximum possible number of <i>BinaryNode</i>
     */
    public boolean isFullLevel(int level){
        if(this.tree == null)
            return true;
        int startIndex = (int)Math.pow(2, level - 1) - 1, endIndex = startIndex + (int)Math.pow(2, level - 1);
        return this.tree.subList(startIndex, endIndex).stream().allMatch(Objects::nonNull);
    }

    /** Finds out if <i>BinaryNode</i> at given index is a lonely and detached <i>BinaryNode</i>, meaning no children and no parent
     * @param index Index of <i>BinaryNode</i> of interest
     * @return Whether <i>BinaryNode</i> is a singleton or not
     */
    @Override
    public boolean isSingleton(int index) {
        if(!this.hasNode(index))
            return false;
        return !this.hasNode(this.getParent(index)) && this.isLeaf(index);
    }

    /** Finds out if <i>BinaryNode</i> is a lonely and detached <i>BinaryNode</i>, meaning no children and no parent
     * @param node <i>BinaryNode</i> of interest
     * @return Whether <i>BinaryNode</i> is a singleton or not
     */
    public boolean isSingleton(BinaryNode<N, K> node) { return this.isSingleton(this.tree.indexOf(node)); }

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
        if(this.getNode(index).isLeaf() || !this.hasNode(index))
            return 0;
        return 1 + Math.max(this.getHeight(BinaryTree.getLeftChildIndex(index)), this.getHeight(BinaryTree.getRightChildIndex(index)));
    }

    /** Finds next <i>BinaryNode</i> to be visited in LVR depth-first traversal
     * @param rootIndex Starting index to consider
     * @return Whether criteria for an inorder successor is met or not
     */
    public BinaryNode<N, K> getInorderSuccessor(int rootIndex) {
        int index = rootIndex;
        if(!this.hasNode(rootIndex) || this.isSingleton(rootIndex) || this.getNode(rootIndex) == this.getRoot() && !this.hasNode(2))
            return null;
        if(this.isLeaf(rootIndex)) { //Case 1 : leaf
            if(BinaryTree.getLeftChildIndex(BinaryTree.getParentIndex(rootIndex)) == rootIndex)
                return this.getParent(rootIndex);
            else {
                while(BinaryTree.getLeftChildIndex(BinaryTree.getParentIndex(index)) != index && index > 0)
                    index = BinaryTree.getParentIndex(index);
                if(index == 0) //rootIndex happens to be location of last-to-be-visited binary node
                    return null;
                return this.getParent(index);
            }
        } else if(this.hasNode(this.getLeftChild(rootIndex))) { //Case 2 : non-leaf with left child
            while(this.hasNode(BinaryTree.getLeftChildIndex(index)))
                index = BinaryTree.getLeftChildIndex(index);
            return this.getNode(index);
        } else //Case 3 : non-leaf with no left child but a right child
            return this.getInorderSuccessor(BinaryTree.getRightChildIndex(rootIndex));
    }

    /** Finds next <i>BinaryNode</i> to be visited in LVR depth-first traversal
     * @param node <i>BinaryNode</i> to start inorder successor search from
     * @return Whether criteria for an inorder successor is met or not
     */
    public BinaryNode<N, K> getInorderSuccessor(BinaryNode<N, K> node){ return this.getInorderSuccessor(this.tree.indexOf(node)); }

    /** Prints out each visited key on the go
     * @param index Index of <i>this</i> containing <i>key</i>
     * @return Whether visit is successful or not
     */
    private boolean visit(int index){
        if(index < 0 || index > this.getTreeSize() || this.getKey(index) == null)
            return false;
        System.out.println(this.getKey(index));
        return true;
    }

    /** Adds each visit key on the go by adding to a collection
     * to <i>visitedBinaryNodes</i>
     * @param index Index of <i>this</i> containing <i>key</i>
     * @param arr Collection to add <i>key</i> to
     * @param print Whether to print each key once visited
     * @return Whether visit is successful or not
     */
    private boolean visit(int index, Collection<K> arr, boolean print){
        if(index < 0 || index > this.getTreeSize() || this.getKey(index) == null)
            return false;
        arr.add(this.getKey(index));
        return true;
    }

    /** Traversal by breadth by storing each visited key in given <i>Collection</i> and starts at given <i>index</i>
     * @param index Index of subtree root to traverse
     * @return Whether traversal is successful or not
     */
    public void traverseByBreath(int index, Collection<K> arr, boolean print){
        ArrayBlockingQueue<Integer> Q = new ArrayBlockingQueue<>(this.getTreeSize());
        Q.offer(index);
        while(!Q.isEmpty()){
            int topIndex = Q.poll();
            this.visit(topIndex, arr, print);
            if(this.getLeftChild(topIndex) != null)
                Q.offer(BinaryTree.getLeftChildIndex(topIndex));
            if(this.getRightChild(topIndex) != null)
                Q.offer(BinaryTree.getRightChildIndex(topIndex));
        }
    }

    /** Traversal by breadth by storing visited keys in given <i>Collection</i> and traverses whole <i>BinaryTree</i>
     * @param arr Where to collect visited keys
     * @param print Whether to print each key once visited
     * @return Whether traversal is successful or not
     */
    public void traverseByBreadth(Collection<K> arr, boolean print){ this.traverseByBreath(0, arr, print); }

    /** Traverses by depth (LVR) by using a given <i>Collection</i> to collect each visited key and starts at given <i>index</i>
     * @param index Current <i>index</i> of <i>BinaryNode</i> of focus during traversal
     * @param arr Collection to add <i>key</i> to
     * @return Whether traversal is successful or not
     */
    private void traverseByDepth(int index, Collection<K> arr, boolean print){
        if(this.getLeftChild(index) != null)
            this.traverseByDepth(BinaryTree.getLeftChildIndex(index), arr, print);
        this.visit(index, arr, print);
        if(this.getRightChild(index) != null)
            this.traverseByDepth(BinaryTree.getRightChildIndex(index), arr, print);
    }

    /** Traverses by depth (LVR) by using a given <i>Collection</i> to collect each visited key and traverses whole <i>BinaryTree</i>
     * @return Array containing each visited key
     */
    public void traverseByDepth(Collection<K> arr, boolean print){ this.traverseByDepth(0, arr, print);}

    /** Searches for given key
     * @param key Key to search for
     * @return Index of <i>BinaryNode</i> containing <i>key</i> if exists, otherwise returns -1
     */
    public int search(K key){ return this.tree.stream().map(n -> ((BinaryNode<N, K>)n).getKey()).collect(Collectors.toList()).indexOf(key); }

    //MUTATORS
    /** Sets a new key for some <i>BinaryNode</i> at <i>index</i>
     * @param index Index of <i>BinaryNode</i> needing to change key
     * @param key New key to replace old key
     * @return Whether setting a new key is successful or not
     */
    public boolean setKey(int index, K key){
        if(this.getNode(index).getKey().equals(key))
            return false;
        this.getNode(index).setKey(key);
        return true;
    }

    /** Sets a new key for some <i>BinaryNode</i>
     * @param node <i>BinaryNode</i> to undergo change in key
     * @param key New key to replace old key
     * @return Whether setting a new key is successful or not
     */
    public boolean setKey(BinaryNode<N, K> node, K key){
        if(!this.hasNode(node))
            return false;
        int index = this.tree.indexOf(node);
        this.setKey(index, key);
        return true;
    }

    /** Sets new left child for some <i>BinaryNode</i> without changing any other <i>BinaryNode</i>
     * @param parentIndex Index of parent
     * @param leftChild New left child
     * @return Whether setting a new left child is successful or not
     */
    public boolean setLeftChild(int parentIndex, BinaryNode<N, K> leftChild){
        int newIndex = BinaryTree.getLeftChildIndex(parentIndex);
        if(!this.hasNode(parentIndex) || this.hasNode(leftChild))
            return false;
        leftChild.setLeaf();
        this.getNode(parentIndex).setLeftChild(leftChild);
        this.tree.set(BinaryTree.getLeftChildIndex(parentIndex), leftChild);
        if(this.hasNode(BinaryTree.getLeftChildIndex(newIndex)))
            leftChild.setLeftChild(this.getNode(BinaryTree.getLeftChildIndex(newIndex)));
        if(this.hasNode(BinaryTree.getRightChildIndex(newIndex)))
            leftChild.setRightChild(this.getNode(BinaryTree.getRightChildIndex(newIndex)));
        return true;
    }

    /** Sets new left child for <i>parent</i> without changing any other <i>BinaryNode</i>
     * @param parent A <i>BinaryNode</i>
     * @param leftChild New left child
     * @return Whether setting a new left child is successful or not
     */
    public boolean setLeftChild(BinaryNode<N, K> parent, BinaryNode<N, K> leftChild){
        if(!this.hasNode(parent))
            return false;
        int parentIndex = this.tree.indexOf(parent);
        return this.setLeftChild(parentIndex, leftChild);
    }

    /** Sets new right child for some <i>BinaryNode</i> without changing any other <i>BinaryNode</i>
     * @param parentIndex Index of parent
     * @param rightChild New left child
     * @return Whether setting a new left child is successful or not
     */
    public boolean setRightChild(int parentIndex, BinaryNode<N, K> rightChild){
        int newIndex = BinaryTree.getRightChildIndex(parentIndex);
        if(!this.hasNode(parentIndex) || this.hasNode(rightChild))
            return false;
        rightChild.setLeaf();
        this.getNode(parentIndex).setRightChild(rightChild);
        this.tree.set(BinaryTree.getRightChildIndex(parentIndex), rightChild);
        if(this.hasNode(BinaryTree.getLeftChildIndex(newIndex)))
            rightChild.setLeftChild(this.getNode(BinaryTree.getLeftChildIndex(newIndex)));
        if(this.hasNode(BinaryTree.getRightChildIndex(newIndex)))
            rightChild.setRightChild(this.getNode(BinaryTree.getRightChildIndex(newIndex)));
        return true;
    }

    /** Sets new right child for <i>parent</i> without changing any other <i>BinaryNode</i>
     * @param parent A <i>BinaryNode</i>
     * @param rightChild New right child
     * @return Whether setting a new left child is successful or not
     */
    public boolean setRightChild(BinaryNode<N, K> parent, BinaryNode<N, K> rightChild){
        if(!this.hasNode(parent))
            return false;
        int parentIndex = this.tree.indexOf(parent);
        return this.setRightChild(parentIndex, rightChild);
    }

    /** Inserts <i>BinaryNode</i> with given key at the first open spot, given that <i>BinaryTree</i> is not already full
     * @param key Key of inserted <i>BinaryNode</i>
     * @return Whether inserting <i></i>BinaryNode</i> is successful or not
     */
    public boolean insertFirst(K key){ return this.insertFirst(new BinaryNode<>(key)); }

    /** Inserts <i>BinaryNode</i> at the first open spot, given that <i>BinaryTree</i> is not already full
     * @param node <i>BinaryNode</i> to insert
     * @return Whether inserting <i></i>BinaryNode</i> is successful or not
     */
    public boolean insertFirst(BinaryNode<N, K> node){
        if(node == null)
            return false;
        if(this.hasNode(node) || this.getTreeSize() >= this.getCapacity() && this.getCapacity() != -1)
            return false;
        int index = this.tree.indexOf(null);
        this.setNode(index, node);
        return true;
    }

    /** Inserts <i>BinaryNode</i> with given key at the first open spot after last fully filled level,
     * given that <i>BinaryTree</i> is not already full
     * @param key Key of inserted <i>BinaryNode</i>
     * @return Whether inserting <i></i>BinaryNode</i> is successful or not
     */
    public boolean insertLast(K key){ return this.insertLast(new BinaryNode<>(key)); }

    /** Inserts <i>BinaryNode</i> at first open spot after last fully filled level,
     * given that <i>BinaryTree</i> is not already full
     * @param node <i>BinaryNode</i> to insert
     * @return Whether inserting <i></i>BinaryNode</i> is successful or not
     */
    public boolean insertLast(BinaryNode<N, K> node){
        if(node == null)
            return false;
        if(this.hasNode(node) || this.getTreeSize() >= this.getCapacity() && this.getCapacity() != -1)
            return false;
        int index = this.tree.indexOf(
                this.tree.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                        .get(this.getTreeSize() - 1));
        int level = BinaryTree.getLevels(index + 1);
        while(!this.isFullLevel(level))
            --level;
        ++level;
        for(int startIndex = (int)Math.pow(2, level - 1) - 1, endIndex = startIndex + (int)Math.pow(2, level - 1), i = startIndex;
            i <= endIndex; i++)
            if(!this.hasNode(i)){
                node.setLeaf();
                this.setNode(i, node);
                return true;
            }
        return false;
    }

    /** If <i>BinaryNode</i> with given key is leaf, then <i>BinaryNode</i> will be simply removed.
     * Otherwise, last <i>BinaryNode</i> is removed first and then replaces <i>BinaryNode</i> with given key.
     * @param key Key to remove
     * @return Whether removal of <i>BinaryNode</i> with given key is successful or not
     */
    public boolean remove(K key){
        int index = this.search(key);
        if(index == -1)
            return false;
        BinaryNode<N, K> node = this.getNode(index);
        if(node.isLeaf())
            this.tree.set(index, null);
        else {
            this.setNode(index, this.getLast());
            this.remove(this.getLast());
        }
        return true;
    }

    /** If <i>BinaryNode</i> is leaf, then <i>BinaryNode</i> will be simply removed.
     * Otherwise, last <i>BinaryNode</i> is removed first and then replaces chosen <i>BinaryNode</i>.
     * @param node <i>BinaryNode</i> to remove
     * @return  Whether removal of <i>BinaryNode</i> with given key is successful or not
     */
    public boolean remove(BinaryNode<N, K> node){
        int index = this.tree.indexOf(node);
        if(index == -1)
            return false;
        this.tree.set(index, null);
        return true;
    }

    /** Inserts new subtree via deep copy at given <i>rootIndex</i>
     * @param rootIndex Index in <i>this</i> to store root of new subtree
     * @param subtree New subtree to insert
     * @return Whether inserting new subtree is successful for not
     */
    @Override
    public boolean insertSubtree(int rootIndex, Tree<N> subtree){
        if(!this.hasNode(rootIndex))
            return false;
        if(rootIndex == 0){
            this.tree = subtree.tree;
            return true;
        }
        this.removeSubtree(rootIndex);
        ArrayBlockingQueue<Pair<Integer, Integer>> Q = new ArrayBlockingQueue<>(this.getTreeSize());
        Q.offer(Pair.with(rootIndex, 0));
        while(!Q.isEmpty()){
            Pair<Integer, Integer> indexPair = Q.poll();
            int thisIndex = indexPair.getValue0(), subtreeIndex = indexPair.getValue1();
            this.setNode(thisIndex, new BinaryNode<N, K>((BinaryNode<N, K>)subtree.getNode(subtreeIndex)));
            if(subtree.hasNode(((BinaryTree<N, K>)subtree).getLeftChild(subtreeIndex)))
                Q.offer(Pair.with(BinaryTree.getLeftChildIndex(thisIndex), BinaryTree.getLeftChildIndex(subtreeIndex)));
            if(subtree.hasNode(((BinaryTree<N, K>)subtree).getRightChild(subtreeIndex)))
                Q.offer(Pair.with(BinaryTree.getRightChildIndex(thisIndex), BinaryTree.getRightChildIndex(subtreeIndex)));
        }
        return true;
    }

    /** Deletes subtree rooted at <i>Node</i> located at given <i>rootIndex</i>
     * @param rootIndex Index of subtree root
     * @return Whether removal of subtree is successful or not
     */
    @Override
    public boolean removeSubtree(int rootIndex){
        if(!this.hasNode(rootIndex))
            return false;
        if(rootIndex == 0){
            this.clear();
            return true;
        }
        ArrayBlockingQueue<Integer> Q = new ArrayBlockingQueue<Integer>(this.getTreeSize());
        Q.offer(rootIndex);
        while(!Q.isEmpty()){
            int topIndex = Q.poll();
            this.tree.set(topIndex, null);
            if(this.hasNode(BinaryTree.getLeftChildIndex(topIndex)))
                Q.offer(BinaryTree.getLeftChildIndex(topIndex));
            if(this.hasNode(BinaryTree.getRightChildIndex(topIndex)))
                Q.offer(BinaryTree.getRightChildIndex(topIndex));
        }
        return true;
    }

    /** Performs left rotation rooted at given index
     * @param rootIndex Index of root of subtree to rotate left about
     * @return Whether left rotation is successful or not
     */
    public boolean leftRotation(int rootIndex){
        if(!this.hasNode(rootIndex))
            return false;
        int leftIndex = BinaryTree.getLeftChildIndex(rootIndex),
                rightIndex = BinaryTree.getRightChildIndex(rootIndex),
                grandChildIndex = BinaryTree.getLeftChildIndex(rightIndex);
        BinaryNode<N, K> originalRoot = new BinaryNode<>(this.getNode(rootIndex));
        BinaryTree<N, K> leftSubtree = this.getSubtree(leftIndex);
        BinaryTree<N, K> grandChildSubtree = this.getSubtree(grandChildIndex);
        this.removeSubtree(grandChildIndex);
        BinaryTree<N, K> rightSubtree = this.getSubtree(rightIndex);
        this.removeSubtree(rootIndex);
        this.insertSubtree(rootIndex, rightSubtree);
        this.setLeftChild(rootIndex, originalRoot);
        this.insertSubtree(BinaryTree.getLeftChildIndex(leftIndex), leftSubtree);
        this.insertSubtree(BinaryTree.getRightChildIndex(leftIndex), grandChildSubtree);
        return true;
    }

    /** Performs right rotation rooted at given index
     * @param rootIndex Index of root of subtree to rotate left about
     * @return Whether right rotation is successful or not
     */
    public boolean rightRotation(int rootIndex){
        if(!this.hasNode(rootIndex))
            return false;
        int rightIndex = BinaryTree.getRightChildIndex(rootIndex),
                leftIndex = BinaryTree.getLeftChildIndex(rootIndex),
                grandChildIndex = BinaryTree.getRightChildIndex(leftIndex);
        BinaryNode<N, K> originalRoot = new BinaryNode<>(this.getNode(rootIndex));
        BinaryTree<N, K> rightSubtree = this.getSubtree(rightIndex);
        BinaryTree<N, K> grandChildSubtree = this.getSubtree(grandChildIndex);
        this.removeSubtree(grandChildIndex);
        BinaryTree<N, K> leftSubtree = this.getSubtree(leftIndex);
        this.removeSubtree(rootIndex);
        this.insertSubtree(rootIndex, leftSubtree);
        this.setRightChild(rootIndex, originalRoot);
        this.insertSubtree(BinaryTree.getRightChildIndex(rightIndex), rightSubtree);
        this.insertSubtree(BinaryTree.getLeftChildIndex(leftIndex), grandChildSubtree);
        return true;
    }

}

/** Variant of <i>BinaryTree</i> that sorts <i>key</i>
 * @param <N> Data type of <i>BinaryNode</i>
 * @param <K> Data type of <i>Key</i>
 */
class BinarySearchTree<N, K> extends BinaryTree<N, K>{
    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor creates empty tree
     */
    public BinarySearchTree(){super();}

    /** Constructor applies custom root <i>BinaryNode</i> and sets capacity
     * @param name A string that identifies a specific <i>BinarySearchTree</i>
     * @param capacity Maximum number of <i>BinaryNode</i> that the tree can hold
     */
    BinarySearchTree(String name, int capacity){ super(name, capacity); }

    //ACCESSORS
    @Override
    public boolean insertFirst(K key){ return false; }

    @Override
    public boolean insertFirst(BinaryNode<N, K> node){ return false; }

    @Override
    public boolean insertLast(K key){ return false; }

    @Override
    public boolean insertLast(BinaryNode<N, K> node){ return false; }

    /** Search for index of <i>BinaryNode</i> containing given key
     * @param key Key to search for
     * @return Index of <i>BinaryNode</i> containing given key, otherwise -1 if does not exist
     */
    @Override
    public int search(K key){
        return this.search(key, 0);
    }

    /** Search for index of <i>BinaryNode</i> containing given key, but starting search at some given index
     * @param key Key to search for
     * @return Index of <i>BinaryNode</i> containing given key, otherwise -1 if does not exist
     */
    public int search(K key, int startIndex){
        if(this.tree == null)
            return -1;
        if(this.tree.isEmpty())
            return -1;
        int index = startIndex;
        while(this.hasNode(index)){
            if(this.getKey(index).equals(key))
                return index;
            else if(String.valueOf(key).compareTo(String.valueOf(this.getKey(index))) < 0)
                index = BinaryTree.getLeftChildIndex(index);
            else
                index = BinaryTree.getRightChildIndex(index);
        }
        return -1;
    }

    //MUTATORS
    /** Inserts new <i>BinaryNode</i> with given key into correct spot by key ordering
     * @param key Key to insert
     * @return Whether inserting new <i>BinaryNode</i> is successful or not
     */
    public boolean insert(K key){ return this.insert(new BinaryNode<>(key)); }

    /** Inserts given <i>BinaryNode</i> into correct spot by key ordering
     * @param node <i>BinaryNode</i> to insert
     * @return Whether inserting new <i>BinaryNode</i> is successful or not
     */
    public boolean insert(BinaryNode<N, K> node){
        if(this.tree.isEmpty())
            this.tree.set(0, node);
        K key = node.getKey();
        int p = 0, prev = 0;
        while(this.hasNode(p)){
            prev = p;
            K currentKey = ((BinaryNode<N, K>)this.tree.get(p)).getKey();
            if(currentKey.equals(key))
                return false;
            else if(String.valueOf(key).compareTo(String.valueOf(currentKey)) < 0)
                p = BinaryTree.getLeftChildIndex(p);
            else
                p = BinaryTree.getRightChildIndex(p);
        }
        if(String.valueOf(key).compareTo(String.valueOf(this.getKey(prev))) < 0)
            this.setLeftChild(prev, node);
        else
            this.setRightChild(prev, node);
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
        if(this.getNode(index).isLeaf())
            super.remove(key);
        else if(!this.hasNode(BinaryTree.getLeftChildIndex(index))){
            BinaryTree<N, K> rightSubtree = this.getSubtree(BinaryTree.getRightChildIndex(index));
            this.tree.set(index, null);
            this.insertSubtree(index, rightSubtree);
        } else if(!this.hasNode(BinaryTree.getRightChildIndex(index))){
            BinaryTree<N, K> leftSubtree = this.getSubtree(BinaryTree.getLeftChildIndex(index));
            this.tree.set(index, null);
            this.insertSubtree(index, leftSubtree);
        } else {
            int inorderSuccessorIndex = this.tree.indexOf(this.getInorderSuccessor(BinaryTree.getRightChildIndex(index)));
            BinaryNode<N, K> newTopNode = this.getNode(inorderSuccessorIndex);
            this.tree.set(inorderSuccessorIndex, null);
            this.tree.set(index, null);
            this.setNode(index, newTopNode);
        }
        return true;
    }

}

/** Variant of <i>BinarySearchTree</i> that uses threads to faciliate traversal
 * @param <N> Data type of <i>BinaryNode</i>
 * @param <K> Data type of <i>Key</i>
 */
class ThreadedBinarySearchTree<N, K> extends BinarySearchTree<N, K> {
    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor
     */
    public ThreadedBinarySearchTree(){ super(); }

    /** Constructor applies <i>name</i> and <i>capacity</i>
     * @param name A string that identifies a specific <i>ThreadedBinarySearchTree</i>
     * @param capacity Maximum number of <i>BinaryNode</i> that the tree can hold
     */
    public ThreadedBinarySearchTree(String name, int capacity){ super(name, capacity); }

    //ACCESSORS
    /** Finds next <i>BinaryNode</i> to be visited in LVR depth-first traversal
     * @param rootIndex Starting index to consider
     * @return Whether criteria for an inorder successor is met or not
     */
    @Override
    public BinaryNode<N, K> getInorderSuccessor(int rootIndex){
        if(!this.hasNode(rootIndex) || this.getNode(rootIndex).countChildren() == 0)
            return null;
        return this.getNode(rootIndex).getInorderSuccessor(this);
    }

    /** Finds next <i>BinaryNode</i> to be visited in LVR depth-first traversal
     * @param node <i>BinaryNode</i> to start inorder successor search from
     * @return Whether criteria for an inorder successor is met or not
     */
    @Override
    public BinaryNode<N, K> getInorderSuccessor(BinaryNode<N, K> node){ return this.getInorderSuccessor(this.tree.indexOf(node)); }

    //MUTATORS



}

/** Variant of <i>BinaryTree</i> that cannot allow a height difference of more than 1 for any
 *  adjacent pair of subtrees
 * @param <N> Data type of <i>BinaryNode</i>
 * @param <K> Data type of <i>Key</i>
 */
class AVLTree<N, K> extends BinarySearchTree<N, K> {
    //CONSTRUCTORS
    /** Default constructor creates empty tree
     */
    public AVLTree(){super(); }

    /** Constructor applies custom root <i>BinaryNode</i> and sets capacity
     * @param name A string that identifies a specific <i>AVLTree</i>
     * @param capacity Maximum number of <i>BinaryNode</i> that the tree can hold
     */
    public AVLTree(String name, int capacity){
        super(name, capacity);
    }

    //ACCESSORS
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
    /** Inserts new <i>AVLNode</i>
     * @param key Key of inserted <i>AVLNode</i>
     * @return Whether inserting new <i>AVLNode</i> is successful or not
     */
    @Override
    public boolean insert(K key){
        if(!super.insert(key))
            return false;
        int insertIndex = super.search(key);

        return true;
    }
}

