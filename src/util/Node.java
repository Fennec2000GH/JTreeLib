package JTreeLib.util;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiFunction;
import org.javatuples.Pair;

/** Tree-specific variants of <i>Node</i>
 * @param <N> Data type of identifier of <i>Node</i>
 */
public abstract class Node<N> implements Cloneable {
    //MEMBER VARIABLES
    /** <i>ID</i> for <i>Node</i>
     */
    private N id;

    /** Weight of <i>Node</i>
     */
    private double weight;

    /** Number of childs <i>Node</i> is allowed to have
     */
    private int childCapacity;

    /** Any children of <i>Node</i>
     */
    private List<Node<N>> children;

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor
     */
    public Node(){
        this.id = null;
        this.weight = 1;
        this.childCapacity = -1;
        this.children = new ArrayList<>();
    }

    /** Constructor applies <i>ID</i>
     * @param identifier <i>ID</i> for <i>Node</i>
     */
    public Node(N identifier){
        this.id = identifier;
        this.weight = 1;
        this.childCapacity = -1;
        this.children = new ArrayList<>();
    }

    /** Constructor applies <i>ID</i>, <i>weight</i>, and initialized <i>children</i>
     * @param identifier <i>ID</i> for <i>Node</i>
     * @param childCapacity Maximum number of childs allowed
     */
    public Node(N identifier, int childCapacity, List<Node<N>> children){
        this.id = identifier;
        this.weight = 1;
        this.childCapacity = childCapacity;
        this.children = children;
    }

    /** Copy constructor;
     * @param node <i>Node</i> to copy members from
     */
    public Node(Node<N> node){
        this.id = node.id;
        this.weight = node.weight;
        this.childCapacity = node.childCapacity;
        this.children = new ArrayList<>();
    }

    //ACCESSORS
    /** Finds <i>ID</i> of <i>Node</i>
     * @return <i>ID</i> of <i>Node</i>
     */
    public N getID(){
        return this.id;
    }

    /** Finds <i>weight</i> of <i>Node</i>
     * @return Weight
     */
    public double getWeight(){
        return this.weight;
    }

    /** Finds the number of childs <i>Node</i> can have
     * @return Child capacity of <i>Node</i>
     */
    public int getChildCapacity() {
        return this.childCapacity;
    }

    /** Counts number of non-null children in <i>BinaryNode</i>
     * @return How many child in <i>BinaryNode</i>
     */
    public int countChildren(){ return (int)this.children.stream().filter(Objects::nonNull).count(); }

    /** Gets child in <i>Node</i> by index
     * @param index The index where desired child is located
     */
    public Node<N> getChildByIndex(int index){
        if(index < 0 || index >= this.children.size())
            return null;
        return this.children.get(index);
    }

    /** Gets all childs as an array view
     * @return Array view of children of <i>Node</i>
     */
    public Node<N>[] getChildrenAsArray(){
        Node<N>[] output = new Node[this.countChildren()];
        this.children.stream().filter(Objects::nonNull).collect(Collectors.toList()).toArray(output);
        return output;
    }

    /** Determines whether <i>Node</i> is a leaf or not
     * @return Leaf status
     */
    public boolean isLeaf(){
        return this.children == null || this.children.stream().noneMatch(Objects::nonNull);
    }

    //MUTATORS
    /** Sets new <i>ID</i> for <i>Node</i>
     * @param identifier <i>ID</i> for <i>Node</i>
     */
    public void setID(N identifier){
        this.id = identifier;
    }

    /** Sets new <i>weight</i> for <i>Node</i>
     * @param weight Nonnegative numerical value
     * @return Whether setting new <i>weight</i> is successful or not
     */
    public boolean setWeight(double weight){
        if(weight <= 0)
            return false;
        this.weight = weight;
        return true;
    }

    /** Sets a new maximum limit for the number of childs allowed for <i>Node</i>
     * @param childCapacity New capacity for childs
     */
    public void setChildCapacity(int childCapacity) { this.childCapacity = childCapacity; }

    /** Adds new child to <i>Node</i>
     * @param child The child <i>Node</i> to be added
     * @return Whether adding a new child is successful or not
     */
    public boolean addChild(Node<N> child){
        if(this.children.size() >= this.childCapacity)
            return false;
        this.children.add(child);
        return true;
    }

    /** Set new child at <i>index</i> in <i>children</i> in <i>Node</i>
     * @param index The index to insert a new child
     * @param child The child <i>Node</i> to be set
     * @return Whether adding a new child is successful or not
     */
    public boolean setChildByIndex(int index, Node<N> child){
        if(this.children.size() >= this.childCapacity)
            return false;
        this.children.set(index, child);
        return true;
    }

    /** Manually claim <i>Node</i> to be a leaf by removing all children, and thus descendants of <i>Node</i>
     */
    public void setLeaf(){ this.children = null; }

}

/** Variant of <i>Node</i> with <i>key</i> and ability to access parent and siblings as well as childs
 * @param <N> Data type of identifier of <i>BinaryNode</i>
 */
class OmniNode<N, K> extends Node<N> {
    //MEMBER VARIABLES
    /** Key for <i>OmniNode</i>
     */
    private K key;

    /** Parent of current <i>OmniNode</i>
     */
    private OmniNode<N, K> parent;

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor
     */
    public OmniNode(){ super(); }

    /** Constructor applies <i>ID</i>
     * @param identifier <i>ID</i> for <i>OmniNode</i>
     */
    public OmniNode(N identifier){ super(identifier); }

    /** Constructor applies <i>ID</i> and <i>childCapacity</i>
     * @param identifier <i>ID</i> for <i>OmniNode</i>
     * @param childCapacity Maximum number of childs allowed
     */
    public OmniNode(N identifier, int childCapacity){ super(identifier, childCapacity, new ArrayList<>()); }

    //ACCESSORS
    /**
     * @return
     */
    public K getKey() { return this.key; }

    /**
     * @return
     */
    public OmniNode<N, K> getParent(){ return this.parent; }

    /**
     * @return
     */
    public OmniNode<N, K>[] getSiblingsAsArray(){
        int index = 0;
        OmniNode<N, K>[] output = new OmniNode[this.getParent().getChildrenAsArray().length - 1];
        for(OmniNode<N, K> obj : Arrays.stream(this.getParent().getChildrenAsArray()).map(n -> (OmniNode<N, K>)n).filter(n -> !n.equals(this)).collect(Collectors.toList())){
            output[index] = obj;
            ++index;
        }
        return output;
    }

    //MUTATORS

}

/** Variant of <i>Node</i> for <i>BinaryTree</i>
 * @param <N> Data type of identifier of <i>Node</i>
 * @param <K> Data type of identifier of <i>key</i> for each <i>BinaryNode</i>
 */
class BinaryNode<N, K> extends Node<N> {
    //MEMBER VARIABLES
    /** Key for <i>BinaryNode</i>
     */
    private K key;

    /** Stores inorder successor to be used only for <i>ThreadedBinarySearchTree</i>
     * First value is reference to inorder successor, and second value indicates whether inorder successor is also right child.
     */
    private Pair<BinaryNode<N, K>, Boolean> thread;

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Constructor initializes <i>key</i>
     * @param key Key for <i>Node</i>
     */
    public BinaryNode(K key){
        super(null, 2, Collections.unmodifiableList(new ArrayList<>(2)));
        this.setChildCapacity(2);
        this.key = key;
    }

    /** Constructor initializes <i>ID</i> and <i>key</i>
     * @param identifier <i>ID</i> for <i>BinaryNode</i>
     * @param key Key for <i>BinaryNode</i>
     */
    public BinaryNode(N identifier, K key){
        super(identifier, 2, Collections.unmodifiableList(new ArrayList<>(2)));
        this.setChildCapacity(2);
        this.key = key;
    }

    /** Copy constructor
     * @param node <i>BinaryNode</i> to copy members from
     */
    public BinaryNode(BinaryNode<N, K> node){
        super(node);
        this.setChildCapacity(2);
        this.key = node.key;
    }

    //ACCESSORS
    /** Finds <i>key</i> for <i>BinaryNode</i>
     * @return Key for <i>Node</i>
     */
    public K getKey(){
        return this.key;
    }

    /** Finds left child of <i>BinaryNode</i>
     * @return Left child if exists, otherwise returns null
     */
    public BinaryNode<N, K> getLeftChild(){ return (BinaryNode<N, K>)super.getChildByIndex(0); }

    /** Finds right child of <i>BinaryNode</i>
     * @return Right child if exists, otherwise returns null
     */
    public BinaryNode<N, K> getRightChild(){ return (BinaryNode<N, K>)super.getChildByIndex(1); }

    /** Finds inorder successor
     * @param otherTree Supposed <i>ThreadedBinaryTree</i> that current <i>BinaryNode</i> resides in
     * @return Whether an inorder successor is found or not
     */
    public BinaryNode<N, K> getInorderSuccessor(ThreadedBinarySearchTree<N, K> otherTree) {
        if(!otherTree.hasNode(this))
            return null;
        return this.thread.getValue0();
    }

    /** If inorder successor is not the right child, then a dashed line will connect current <i>BinaryNode</i> to its
     * inorder successor in a visual diagram of otherTree
     * @param otherTree Supposed <i>ThreadedBinaryTree</i> that current <i>BinaryNode</i> resides in
     * @return Whether, for display purposes, the inorder successor is also the right child
     */
    public boolean displayThread(ThreadedBinarySearchTree<N, K> otherTree) {
        if(!otherTree.hasNode(this))
            return false;
        return this.thread.getValue1();
    }

    //MUTATORS
    @Override
    public boolean addChild(Node<N> node){ return false; }

    /** Sets new <i>key</i>
     * @param key Key for <i>BinaryNode</i>
     */
    public void setKey(K key){
        this.key = key;
    }

    /** Sets a new left child
     * @param leftChild <i>BinaryNode</i> to be set as left child
     * @return Whether setting new left child is successful or not
     */
    public boolean setLeftChild(BinaryNode<N, K> leftChild){
        if(this.getLeftChild().equals(leftChild))
            return false;
        this.setChildByIndex(0, leftChild);
        return true;
    }

    /** Sets a new right child
     * @param rightChild <i>BinaryNode</i> to be set as left child
     * @return Whether setting new right child is successful or not
     */
    public boolean setRightChild(BinaryNode<N, K> rightChild){
        if(this.getLeftChild().equals(rightChild))
            return false;
        this.setChildByIndex(0, rightChild);
        return true;
    }

    /** Removes left chlid, if already exists
     * @return Whether removing left child is successful or not
     */
    public boolean removeLeftChild(){
        if(this.getLeftChild() == null)
            return false;
        this.setLeftChild(null);
        return true;
    }

    /** Removes right chlid, if already exists
     * @return Whether removing right child is successful or not
     */
    public boolean removeRightChild(){
        if(this.getRightChild() == null)
            return false;
        this.setRightChild(null);
        return true;
    }
}

/** Variant of <i>Node</i> for <i>TernaryTree</i>
 * @param <N> Data type of identifier of <i>TernaryNode</i>
 * @param <K> Data type of identifier of <i>key</i>
 */
class TernaryNode<N, K> extends Node<N> {
    //MEMBER VARIABLES

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor
     */
    public TernaryNode(){
        super();
        super.setChildCapacity(3);
    }

    /** Constructor applies <i>identifier</i> to <i>TernaryNode</i>
     * @param identifier <i>ID</i> for <i>Node</i>
     */
    public TernaryNode(N identifier){
        super(identifier);
        super.setChildCapacity(3);
    }

    /** Copy constructor
     * @param node <i>TernaryNode</i> to copy members from
     */
    public TernaryNode(TernaryNode<N, K> node){
        super(node);
        super.setChildCapacity(3);
    }

    //ACCESSORS

    /** Gets the leftmost child
     * @return First child of <i>TernaryNode</i>
     */
    public TernaryNode<N, K> getFirstChild(){ return (TernaryNode<N, K>)super.getChildByIndex(0); }

    /** Gets the middle child
     * @return Middle child of <i>TernaryNode</i>
     */
    public TernaryNode<N, K> getMiddleChild(){ return (TernaryNode<N, K>)super.getChildByIndex(1); }

    /** Gets the rightmost child
     * @return Last child of <i>TernaryNode</i>
     */
    public TernaryNode<N, K> getLastChild(){ return (TernaryNode<N, K>)super.getChildByIndex(2); }

    //MUTATORS
    /** Sets a new child as first child
     * @param child New child <i>TernaryNode</i>
     */
    public void setFirstChild(TernaryNode<N, K> child){ super.setChildByIndex(0, child); }

    /** Sets a new child as middle child
     * @param child child New child <i>TernaryNode</i>
     */
    public void setMiddleChild(TernaryNode<N, K> child){ super.setChildByIndex(1, child); }

    /** Sets a new child as last child
     * @param child child New child <i>TernaryNode</i>
     */
    public void setLastChild(TernaryNode<N, K> child){ super.setChildByIndex(2, child); }

}

/** Variant of <i>BinaryNode</i> for <i>AVLTree</i>
 * @param <N> Data type of identifier of <i>AVLNode</i>
 * @param <K> Data type of identifier of <i>key</i>
 */
class AVLNode<N, K> extends BinaryNode<N, K> {
    //MEMBER VARIABLES
    /** Difference in height between right subtree and left subtree;
     */
    private int balanceFactor;

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Constructor applies <i>key</i>
     * @param key Key for <i>AVLNode</i>
     */
    public AVLNode(K key){
        super(key);
    }

    /** Constructor applies <i>ID</i> and <i>key</i>
     * @param identifier <i>ID</i> for <i>AVLNode</i>
     * @param key Key for <i>AVLNode</i>
     */
    public AVLNode(N identifier, K key){
        super(identifier, key);
    }

    /** Copy constructor
     * @param node <i>AVLNode</i> to copy members from
     */
    public AVLNode(AVLNode<N, K> node){
        super(node);
    }

    //ACCESSORS
    @Override
    public int getChildCapacity(){
        return 2;
    }

    /** Checks whether balance factor is equal to -1, 0, or 1
     * @param node <i>AVLNode</i> to check balance factor on
     * @return Whether balance factor is acceptable
     */
    public static <N, K> boolean acceptableBalanceFactor(AVLNode<N, K> node, AVLTree<N, K> avlTree){ return Math.abs(node.balanceFactor) <= 1; }

    /** Gets correct balance factor in context of <i>avlTree</i>
     * Note: if <i>Node</i> is not in <i>avlTree</i>, 0 will be returned
     * @param avlTree <i>AVLTree</i> that <i>Node</i> belongs under
     * @return Balance factor
     */
    @Deprecated
    public int getBalanceFactorSecurely(AVLTree<N, K> avlTree){
        this.setBalanceFactorSecurely(avlTree);
        return this.balanceFactor;
    }

    //MUTATORS
    /** Sets or updates current balance factor to any user-given <i>int</i> value
     * @param bf Balance factor
     * @return Whether setting new balance factor is successful or not
     */
    public boolean setBalanceFactor(int bf){
        if(this.balanceFactor == bf)
            return false;
        this.balanceFactor = bf;
        return true;
    }

    /** Sets correct balance factor in context of <i>avlTree</i>
     * Note: Returns false only if <i>Node</i> is not a <i>AVLNode</i> in <i>avlTree</i>
     * @param avlTree <i>AVLTree</i> that <i>Node</i> belongs under
     * @return Whether setting balance factor is successful or not
     */
    @Deprecated
    public boolean setBalanceFactorSecurely(AVLTree<N, K> avlTree){
        int index = avlTree.tree.indexOf(this);
        if(index == -1)
            return false;
        int bf = avlTree.getBalanceFactor(index);
        this.balanceFactor = bf;
        return true;
    }
}

/** Variant of <i>BinaryNode</i> for <i>MultiNode</i>
 * @param <N> Data type of identifier of <i>MultiNode</i>
 * @param <K> Data type of identifier of <i>key</i>
 */
class MultiNode<N, K> extends Node<N> {
    //MEMBER VARIABLES
    /** Maximum number of keys allowed per <i>MultiNode</i>
     */
    private int keyCapacity;

    /** <i>MultiNode</i> child located at front of <i>data</i>; it has no right neighboring <i>key</i>
     */
    private MultiNode<N, K> firstChild;

    /** Contains each <i>key</i>, in which each child <i>MultiNode</i> stems from its right neigboring key
     */
    private TreeMap<K, MultiNode<N, K>> keys;

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor constructs nameless <i>MultiNode</i> with weight 1
     */
    public MultiNode(){
        super();
        super.setChildCapacity(2);
        this.keyCapacity = 1;
        this.keys = new TreeMap<>();
    }

    /** Constructor initializes an <i>ID</i> for <i>MultiNode</i>
     * @param identifier <i>ID</i> for <i>MultiNode</i>
     */
    public MultiNode(N identifier){
        super(identifier);
        this.keyCapacity = 1;
        this.keys = new TreeMap<>();
    }

    /** Constructor initializes an <i>ID</i> and <i>keyCapacity</i> for <i>MultiNode</i>
     * @param identifier <i>ID</i> for <i>MultiNode</i>
     * @param keyCapacity Maximum number of keys allowed per <i>MultiNode</i>
     */
    public MultiNode(N identifier, int keyCapacity){
        super(identifier);
        super.setChildCapacity(keyCapacity + 1);
        this.keyCapacity = keyCapacity;
    }

    /** Copy constructor
     * @param node <i>MultiNode</i> to copy members from
     */
    public MultiNode(MultiNode<N, K> node){
        super(node);
        this.keyCapacity = node.keyCapacity;
    }

    //ACCESSORS
    /** Finds the maximum number of keys allowed in <i>MultiNode</i>
     * @return Key capacity
     */
    public int getKeyCapacity(){ return this.keyCapacity; }

    /** Finds number of <i>key</i> currently in <i>keys</i>
     * @return How many keys <i>MultiNode</i> has
     */
    public int getKeySize(){
        return this.keys.size();
    }

    /** Finds child in <i>data</i> attached to given <i>key</i>
     * @param key Key of <i>MultiNode</i> to find
     * @return <i>MultiNode</i> of <i>key</i> if exists, otherwise returns null
     */
    public MultiNode<N, K> getNode(K key){
        if(!this.keys.containsKey(key))
            return null;
        return this.keys.get(key);
    }

    /** Finds left child of <i>Node</i> based on <i>key</i>
     * @param key One of the keys for <i>Node</i>
     * @return Left child if exists, otherwise returns null
     */
    public MultiNode<N, K> getLeftChild(K key) {
        if (!this.keys.containsKey(key))
            return null;
        if (key.equals(this.keys.firstKey()))
            return this.firstChild;
        K outputKey = this.keys.headMap(key).lastKey();
        return this.keys.get(outputKey);
    }

    /** Finds right child of <i>Node</i> based on <i>key</i>
     * @param key One of the keys for <i>Node</i>
     * @return Right child if exists, otherwise returns null
     */
    public MultiNode<N, K> getRightChild(K key){
        if(!this.keys.containsKey(key))
            return null;
        return this.keys.get(key);
    }

    /** Finds <i>key</i> immediately left to given <i>key</i>
     * @param key A key inside <i>MultiNode</i>
     * @return Left <i>key</i>
     */
    public K getLeftNeighborKey(K key){
        if(!this.keys.containsKey(key) || this.keys.firstKey().equals(key))
            return null;
        return this.keys.headMap(key).lastKey();
    }

    /** Finds <i>key</i> immediately right to given <i>key</i>
     * @param key A keys inside <i>MultiNode</i>
     * @return Right <i>key</i>
     */
    public K getRightNeighborKey(K key){
        if(!this.keys.containsKey(key) || this.keys.lastKey().equals(key))
            return null;
        BiFunction<K, TreeMap<K, MultiNode<N, K>>, TreeMap<K, MultiNode<N, K>>> func = (k, map) -> {
            TreeMap<K, MultiNode<N, K>> output = (TreeMap<K, MultiNode<N, K>>)map.tailMap(k);
            output.remove(output.firstKey());
            return output;
        };
        return func.andThen(TreeMap::firstKey).apply(key, this.keys);
    }

    //MUTATORS
    /** Set a limit to number of keys allowed in <i>MultiNode</i>
     * @param keyCapacity Maximum number of keys allowed
     * @return Whether setting <i>keyCapacity</i> is successful or not
     */
    public boolean setKeyCapacity(int keyCapacity){
        if(keyCapacity < 0)
            return false;
        this.keyCapacity = keyCapacity;
        return true;
    }

    /** Puts new (<i>key</i>,  <i>MultiNode</i>) entry in <i>Node</i> without violating <i>keyCapacity</i>
     * @param key One of the keys for <i>Node</i>
     * @return Whether adding new entry to <i>MultiNode</i> is successful or not
     */
    public boolean put(K key){
        if(this.keys.size() >= this.keyCapacity)
            return false;
        this.keys.put(key, new MultiNode<>());
        return true;
    }

    /** Replace old <i>key</i> with new <i>key</i> without changing child
     * @param oldKey Previously existing <i>key</i> in <i>Node</i>
     * @param newKey New <i>key</i>
     * @return Whether replacement of old <i>key</i> is successful or not
     */
    public boolean replaceKey(K oldKey, K newKey){
        if(!this.keys.containsKey(oldKey))
            return false;
        MultiNode<N, K> temp = this.keys.get(oldKey);
        this.keys.remove(oldKey);
        this.keys.put(newKey, temp);
        return true;
    }

    /** Merge 2 <i>MultiNode</i> to the same remaining <i>key</i>
     * @param remainKey 1 of 2 original keys to hold merged <i>MultiNode</i>
     * @param removeKey 1 of 2 original keys to be deleted after merging
     * @return Whether merging is successful or not
     */
    public boolean merge(K remainKey, K removeKey){
        if(!this.keys.containsKey(removeKey)
                || !this.keys.containsKey(remainKey)
                || this.keys.get(removeKey).getKeySize() + this.keys.get(remainKey).getKeySize() > this.keyCapacity)
            return false;
        if(removeKey.equals(remainKey))
            return true;
        MultiNode<N, K> removed = this.keys.remove(remainKey);
        for(K key : removed.keys.keySet())
            this.keys.put(key, removed.getNode(key));
        return true;
    }

    /** Merge <i>MultiNode</i> at <i>key</i> with its left neighbor <i>MultiNode</i>
     * @param remainKey 1 of 2 original keys to hold merged <i>MultiNode</i>
     * @return Whether merging is successful or not
     */
    public boolean mergeWithLeft(K remainKey){
        K removeKey = this.getLeftNeighborKey(remainKey);
        return this.merge(remainKey, removeKey);
    }

    /** Merge <i>MultiNode</i> at <i>key</i> with its right neighbor <i>MultiNode</i>
     * @param remainKey 1 of 2 original keys to hold merged <i>MultiNode</i>
     * @return Whether merging is successful or not
     */
    public boolean mergeWithRight(K remainKey){
        K removeKey = this.getRightNeighborKey(remainKey);
        return this.merge(remainKey, removeKey);
    }

    /** Add a child before for a given <i>key</i>;
     * if <i>rightKey</i> already exists, then value of <i>rightKey</i> will be replaced
     * @param rightKey <i>key</i> right of child to be inserted
     * @param node <i>MultiNode</i> to be inserted
     * @return Whether insertion of child is successful or not
     */
    public boolean addChild(K rightKey, MultiNode<N, K> node){
        if(!this.keys.containsKey(rightKey))
            return false;
        this.keys.put(rightKey, node);
        return true;
    }

    /** Deletes a child left of given <i>key</i> and all descendents
     * @param rightKey <i>key</i> right of child to be deleted
     * @return Whether deletion is successful or
     */
    public boolean deleteChild(K rightKey){
        if(!this.keys.containsKey(rightKey))
            return false;
        this.keys.remove(rightKey);
        return true;
    }

}

/** Variant of <i>MultiNode</i> for <i>BNode</i>
 * @param <N> Data type of identifier of <i>MultiNode</i>
 * @param <K> Data type of identifier of <i>key</i>
 */
class BNode<N, K> extends MultiNode<N, K> {
    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor constructs nameless <i>Node</i> with weight 1
     */
    public BNode(){ super(); }

    /** Constructor initializes an <i>ID</i> for <i>Node</i>
     * @param identifier <i>ID</i> for <i>Node</i>
     */
    public BNode(N identifier){
        super(identifier);
    }

    /** Copy constructor
     * @param node <i>BNode</i> to copy members from
     */
    public BNode(BNode<N, K> node){
        super(node);
    }

    //ACCESSORS


    //MUTATORS
    /** Canceled function; See <i>BTree</i> to uniformly set <i>keyCapacity</i>  using <i></i>
     * @param kc Maximum number of keys allowed in <i>Node</i>
     * @return false
     */
    @Override
    public boolean setKeyCapacity(int kc){
        return false;
    }

    /** Canceled function
     * @param remainKey
     * @param removeKey
     * @return
     */
    @Override
    public boolean merge(K remainKey, K removeKey){
        return false;
    }
}

/** Variant of <i>Node</i> for <i>TrieNode</i>
 * @param <N> Data type of identifier of <i>TrieNode</i>
 */
class TrieNode<N> extends Node<N> {
    /* key = actual word or string stored as a path of nodes in trie,
        nodeKey = a single character used as a key in each TrieNode<N> object's map
     */

    //MEMBER VARIABLES
    /** Data values, i.e. characters, that contribute 1 unit to a word
     */
    private ArrayList<Character> nodeKeys;

    /** Single child of <i>TrieNode</i>
     */
    private TrieNode<N> child;

    /** Whether current <i>TrieNode</i> is last character of some word
     */
    private boolean endOfWord;

    //MEMBER FUNCTIONS
    //CONSTRUCTORS
    /** Default constructor
     */
    public TrieNode(){
        super();
        this.setChildCapacity(1);
    }

    /** Constructor initializes an <i>ID</i> for <i>TrieNode</i>
     * @param identifier <i>ID</i> for <i>TrieNode</i>
     */
    public TrieNode(N identifier){ super(identifier, 1, Collections.unmodifiableList(new ArrayList<>(1))); }

    /** Constructor initializes an <i>ID</i> and <i>key</i> for <i>TrieNode</i>
     * @param identifier <i>ID</i> for <i>TrieNode</i>
     * @param nodeKeys Characters used to build up a word
     */
    public TrieNode(N identifier, ArrayList<Character> nodeKeys){
        super(identifier, 1, Collections.unmodifiableList(new ArrayList<>(1)));
        this.nodeKeys = nodeKeys;
    }

    /** Copy constructor
     * @param node <i>TrieNode</i> to copy members from
     */
    public TrieNode(TrieNode<N> node){
        super(node);
        this.nodeKeys = node.nodeKeys;
    }

    //ACCESSORS
    /** Finds out whether a given character is contained by <i>TrieNode</i> inside its <i>nodeKeys</i>
     * @param specificNodeKey Character to search for existence
     * @return Whether specified character exists in current <i>TrieNode</i>
     */
    public boolean search(char specificNodeKey){ return this.nodeKeys.contains(specificNodeKey); }

    //MUTATORS
    /** Inserts new child for current <i>TrieNode</i> and replaces any previous child
     * @param newChild New <i>TrieNode</i> to be child of current <i>TrieNode</i>
     */
    public void insertChild(TrieNode<N> newChild){ this.child = newChild; }

    /** Whether a child exists or not
     * @return The existence of a child <i>TrieNode</i>
     */
    public boolean hasChild(){ return this.child != null; }

}
