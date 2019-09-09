package JTreeLib.util;
import java.util.Objects;
import javolution.util.FastSortedMap;
import javolution.util.FastSortedSet;
import org.javatuples.Pair;

/** Tree-specific variants of <i>Node</i>
 * @param <N> Data type of identifier of <i>this</i>
 */
public class Node<N> implements Cloneable {
    //CONSTRUCTORS
    /** Default constructor constructs nameless <i>Node</i> with weight 1
     */
    Node(){
        this.id = null;
        this.weight = 1;
        this.leaf = false;
    }

    /** Constructor initializes an <i>ID</i> for <i>this</i>
     * @param identifier <i>ID</i> for <i>this</i>
     */
    Node(N identifier){
        this();
        this.id = identifier;
    }

    /** Constructor initializes an <i>ID</i> and nonnegtive weight for <i>this</i>
     * @param identifier <i>ID</i> for <i>this</i>
     * @param weight Nonnegative numerical value
     */
    Node(N identifier, double weight){
        this(identifier);
        if(weight >= 0)
            this.weight = weight;
    }

    /** Copy constructor; does not change <i>leaf</i> in original
     * @param node <i>Node</i> to copy members from
     */
    Node(Node<N> node){
        this.id = node.id;
        this.weight = node.weight;
    }

    //ACCESSORS
    /** Finds <i>ID</i> of <i>Node</i>
     * @return <i>ID</i> of <i>this</i>
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

    /** Determines whether <i>this</i> is a leaf or not
     * @return Leaf status
     */
    public boolean getLeaf(){
        return this.leaf;
    }

    //MUTATORS
    /** Sets new <i>ID</i> for <i>this</i>
     * @param identifier <i>ID</i> for <i>this</i>
     */
    public void setID(N identifier){
        this.id = identifier;
    }

    /** Sets new <i>weight</i> for <i>this</i>
     * @param weight Nonnegative numerical value
     * @return Whether setting new <i>weight</i> is successful or not
     */
    public boolean setWeight(double weight){
        if(weight <= 0)
            return false;
        this.weight = weight;
        return true;
    }

    /** Manually claim <i>Node</i> to be a leaf
     * @param leaf Whether <i>Node</i> is recognized as a leaf or not
     */
    public void setLeaf(boolean leaf){
        this.leaf = leaf;
    }

    //MEMBER VARIABLES
    /** <i>ID</i> for <i>this</i>
     */
    protected N id;

    /** Weight of <i>Node</i>
     */
    protected double weight;

    /** Whether <i>Node</i> is a leaf or not
     */
    protected boolean leaf;
}

/** Variant of <i>Node</i> for <i>BinaryTree</i>
 * @param <N> Data type of identifier of <i>this</i>
 * @param <K> Data type of identifier of <i>key</i> for each <i>BinaryNode</i>
 */
class BinaryNode<N, K> extends Node<N> {
    //CONSTRUCTORS

    /** Constructor initializes <i>key</i>
     * @param key Key for <i>this</i>
     */
    BinaryNode(K key){
        super();
        this.key = key;
        this.children = Pair.with(null, null);
    }

    /** Constructor initializes <i>ID</i> and <i>key</i>
     * @param identifier <i>ID</i> for <i>this</i>
     * @param key Key for <i>this</i>
     */
    BinaryNode(N identifier, K key){
        super(identifier);
        this.key = key;
        this.children = Pair.with(null, null);
    }

    /** Constructor initializes <i>ID</i> , <i>weight</i>, and <i>key</i>
     * @param identifier <i>ID</i> for <i>this</i>
     * @param weight Nonnegative numerical value
     * @param key Key for <i>this</i>
     */
    BinaryNode(N identifier, double weight, K key){
        super(identifier, weight);
        this.key = key;
        this.children = Pair.with(null, null);
    }

    /** Copy constructor; does not change <i>leaf</i> in original
     * @param node <i>BinaryNode</i> to copy members from
     */
    BinaryNode(BinaryNode<N, K> node){
        super(node);
        this.key = node.key;
    }

    //ACCESSORS
    /** Finds <i>key</i> for <i>this</i>
     * @return Key for <i>this</i>
     */
    public K getKey(){
        return key;
    }

    /** Finds status of <i>this</i> as a leaf or non-leaf
     * @return Whether <i>this</i> is a leaf or not
     */
    @Override
    public boolean getLeaf(){
        if(this.countChildren() == 0)
            this.leaf = true;
        else
            this.leaf = false;
        return this.leaf;
    }

    /** Finds left child of <i>this</i>
     * @return Left child if exists, otherwise returns null
     */
    public BinaryNode<N, K> getLeftChild(){
        return this.children.getValue0();
    }

    /** Finds right child of <i>this</i>
     * @return Right child if exists, otherwise returns null
     */
    public BinaryNode<N, K> getRightChild(){
        return this.children.getValue1();
    }

    /** Counts number of non-nulll children in <i>this</i>
     * @return How many child <i>BinaryNode</i> of <i>this</i> exist
     */
    public int countChildren(){
        return (int)this.children.toList().stream().filter(Objects::nonNull).count();
    }

    //MUTATORS
    /** Sets new <i>key</i>
     * @param key Key for <i>this</i>
     */
    public void setKey(K key){
        this.key = key;
    }

    //MEMBER VARIABLES
    /** Key for <i>this</i>
     */
    protected K key;

    /** Contains both children of <i>this</i>
     */
    protected Pair<BinaryNode<N, K>, BinaryNode<N, K>> children;

}

/** Variant of <i>BinaryNode</i> for <i>AVLTree</i>
 * @param <N> Data type of identifier of <i>this</i>
 * @param <K> Data type of identifier of <i>key</i> for each <i>AVLNode</i>
 */
class AVLNode<N, K> extends BinaryNode<N, K> {
    //CONSTRUCTORS
    /** Constructor initializes <i>key</i> for <i>this</i>
     * @param key Key for <i>this</i>
     */
    AVLNode(K key){
        super(key);
    }

    /** Constructor initializes <i>ID</i> and <i>key</i> for <i>this</i>
     * @param identifier <i>ID</i> for <i>this</i>
     * @param key Key for <i>this</i>
     */
    AVLNode(N identifier, K key){
        super(identifier, key);
    }

    /** Constructor initializes <i>ID</i>, <i>weight</i>, and <i>key</i> for <i>this</i>
     * @param identifier <i>ID</i> for <i>this</i>
     * @param weight Nonnegative numerical value
     * @param key Key for <i>this</i>
     */
    AVLNode(N identifier, double weight, K key){
        super(identifier, weight, key);
    }

    /** Copy constructor; does not change <i>leaf</i> in original
     * @param node <i>AVLNode</i> to copy members from
     */
    AVLNode(AVLNode<N, K> node){
        super(node);
    }

    //ACCESSORS
    /** Checks whether <i>node</i> has balance factor equal either to -1, 0, or 1
     * @param node <i>AVLNode</i> to check balance factor on
     * @return Whether balance factor of <i>node</i> is acceptable
     */
    public static <N, K> boolean acceptableBalanceFactor(AVLNode<N, K> node, AVLTree<N, K> avlTree){
        return Math.abs(node.getBalanceFactorSecurely(avlTree)) <= 1;
    }

    /** Checks whether <i>this</i> has balance factor equal either to -1, 0, or 1
     * @param avlTree <i>AVLTree</i> that <i>this</i> belongs under
     * @return Whether balance factor of <i>this</i> is acceptable
     */
    public boolean acceptableBalanceFactor(AVLTree<N, K> avlTree){
        return AVLNode.acceptableBalanceFactor(this, avlTree);
    }

    /** Gets balance factor, which is height of right subtree minus height of left subtree;
     * Note: only returns currently stored balance factor, regardless of correctness within any <i>avlTree</i>
     * @return Balance factor
     */
    public int getBalanceFactor(){
        return this.balanceFactor;
    }

    /** Gets correct balance factor in context of <i>avlTree</i>
     * Note: if <i>this</i> is not in <i>avlTree</i>, 0 will be returned
     * @param avlTree <i>AVLTree</i> that <i>this</i> belongs under
     * @return Balance factor
     */
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
        if(bf < 0)
            return false;
        this.balanceFactor = bf;
        return true;
    }

    /** Sets correct balance factor in context of <i>avlTree</i>
     * Note: Returns false only if <i>this</i> is not a <i>AVLNode</i> in <i>avlTree</i>
     * @param avlTree <i>AVLTree</i> that <i>this</i> belongs under
     * @return Whether setting balance factor is successful or not
     */
    public boolean setBalanceFactorSecurely(AVLTree<N, K> avlTree){
        int index = avlTree.tree.indexOf(this);
        if(index == -1)
            return false;
        int bf = avlTree.getBalanceFactor(index);
        this.balanceFactor = bf;
        return true;
    }

    //MEMBER VARIABLES
    /** Difference in height between right subtree and left subtree;
     */
    private int balanceFactor;

}

/** Variant of <i>BinaryNode</i> for <i>MultiNode</i>
 * @param <N> Data type of identifier of <i>this</i>
 * @param <K> Data type of identifier of <i>key</i> for each <i>MultiNode</i>
 */
class MultiNode<N, K> extends Node<N> {
    //CONSTRUCTORS
    /** Default constructor constructs nameless <i>Node</i> with weight 1
     */
    MultiNode(){
        super();
        this.keyCapacity = 1;
        this.lastChild = null;
        this.data = new FastSortedMap<>();
    }

    /** Constructor initializes an <i>ID</i> for <i>this</i>
     * @param identifier <i>ID</i> for <i>this</i>
     */
    MultiNode(N identifier){
        super(identifier);
    }

    /** Constructor initializes an <i>ID</i>, <i>weight</i>, and <i>keyCapacity</i> for <i>this</i>
     * @param identifier <i>ID</i> for <i>this</i>
     * @param weight Nonnegative numerical value
     * @param kc Maximum number of keys allowed per <i>MultiNode</i>
     */
    MultiNode(N identifier, double weight, int kc){
        super(identifier, weight);
        this.keyCapacity = kc;
    }
    /** Copy constructor; does not change <i>leaf</i> in original
     * @param node <i>MultiNode</i> to copy members from
     */
    MultiNode(MultiNode<N, K> node){
        super(node);
        this.keyCapacity = node.keyCapacity;
    }

    //ACCESSORS
    /** Finds number of <i>key</i> currently in <i>data</i>
     * @return How many keys <i>this</i> has
     */
    public int getKeySize(){
        return this.data.size();
    }

    /** Finds child in <i>data</i> attached to given <i>key</i>
     * @param key Key of <i>MultiNode</i> to find
     * @return <i>MultiNode</i> of <i>key</i> if exists, otherwise returns null
     */
    public MultiNode<N, K> getNode(K key){
        if(!this.data.containsKey(key))
            return null;
        return this.data.get(key);
    }

    /** Finds left child of <i>this</i> based on <i>key</i>
     * @param key One of the keys for <i>this</i>
     * @return Left child if exists, otherwise returns null
     */
    public MultiNode<N, K> getLeftChild(K key){
        if(!this.data.containsKey(key))
            return null;
        return this.data.get(key);
    }

    /** Finds right child of <i>this</i> based on <i>key</i>
     * @param key One of the keys for <i>this</i>
     * @return Right child if exists, otherwise returns null
     */
    public MultiNode<N, K> getRightChild(K key) {
        if (!this.data.containsKey(key))
            return null;
        if (key.equals(this.data.lastKey()))
            return this.lastChild;
        FastSortedSet<K> tail = this.data.tailMap(key).keySet();
        tail.remove(key);
        K nextKey = tail.first();
        return this.data.get(nextKey);
    }

    /** Finds <i>key</i> immediately left to given <i>key</i>
     * @param key One of the keys for <i>this</i>
     * @return Left <i>key</i>
     */
    public K getLeftNeighborKey(K key){
        if(!this.data.containsKey(key) || this.data.firstKey().equals(key))
            return null;
        return this.data.headMap(key).lastKey();
    }

    /** Finds <i>key</i> immediately right to given <i>key</i>
     * @param key One of the keys for <i>this</i>
     * @return Right <i>key</i>
     */
    public K getRightNeighborKey(K key){
        if(!this.data.containsKey(key) || this.data.lastKey().equals(key))
            return null;
        FastSortedSet<K> tail = this.data.tailMap(key).keySet();
        tail.remove(key);
        return tail.first();
    }

    //MUTATORS
    /** Set a limit to number of keys allowed in <i>this</i>
     * @param kc Maximum number of keys allowed in <i>this</i>
     * @return Whether setting <i>keyCapacity</i> is successful or not
     */
    public boolean setKeyCapacity(int kc){
        if(kc < 0)
            return false;
        this.keyCapacity = kc;
        return true;
    }

    /** Puts new <i>key</i> - <i>MultiNode</i> pair in <i>this</i> without violating <i>keyCapacity</i>
     * @param key One of the keys for <i>this</i>
     * @return
     */
    public boolean put(K key){
        if(this.data.size() >= this.keyCapacity)
            return false;
        this.data.put(key, new MultiNode<>());
        return true;
    }

    /** Replace old <i>key</i> with new <i>key</i> without changing child
     * @param oldKey Previously existing <i>key</i> in <i>this</i>
     * @param newKey New <i>key</i>
     * @return Whether replacement of old <i>key</i> is successful or not
     */
    public boolean replaceKey(K oldKey, K newKey){
        if(!this.data.containsKey(oldKey))
            return false;
        MultiNode<N, K> temp = this.data.get(oldKey);
        this.data.remove(oldKey);
        this.data.put(newKey, temp);
        return true;
    }

    /** Merge 2 <i>MultiNode</i> to the same remaining <i>key</i>
     * @param remainKey 1 of 2 original keys to hold merged <i>MultiNode</i>
     * @param removeKey 1 of 2 original keys to be deleted after merging
     * @return Whether merging is successful or not
     */
    public boolean merge(K remainKey, K removeKey){
        if(!this.data.containsKey(removeKey)
                || !this.data.containsKey(remainKey)
                || !this.data.containsKey(removeKey)
                || this.data.get(removeKey).getKeySize() + this.data.get(remainKey).getKeySize() > this.keyCapacity)
            return false;
        if(removeKey.equals(remainKey))
            return true;
        MultiNode<N, K> removed = this.data.remove(remainKey);
        for(K key : removed.data.keySet())
            this.data.put(key, removed.getNode(key));
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

    /** Insert a child before a given <i>key</i>;
     * if <i>rightKey</i> already exists, then value of <i>rightKey</i> will be replaced
     * @param rightKey <i>key</i> right of child to be inserted
     * @param node <i>MultiNode</i> to be inserted
     * @return Whether insertion of child is successful or not
     */
    public boolean addChild(K rightKey, MultiNode<N, K> node){
        if(!this.data.containsKey(rightKey))
            return false;
        this.data.put(rightKey, node);
        return true;
    }

    /** Deletes a child left of given <i>key</i> and all descendents
     * @param rightKey <i>key</i> right of child to be deleted
     * @return Whether deletion is successful or
     */
    public boolean deleteChild(K rightKey){
        if(!this.data.containsKey(rightKey))
            return false;
        this.data.remove(rightKey);
        return true;
    }

    //MEMBER VARIABLES
    /** Maximum number of keys allowed per <i>MultiNode</i>
     */
    protected int keyCapacity;

    /** <i>MultiNode</i> child located at end of <i>data</i>; it has no right neighboring <i>key</i>
     */
    protected MultiNode<N, K> lastChild;

    /** Contains each <i>key</i>, in which each child <i>MultiNode</i> stems from its right neigboring key
     */
    protected FastSortedMap<K, MultiNode<N, K>> data;

}

/** Variant of <i>BinaryNode</i> for <i>BNode</i>
 * @param <N> Data type of identifier of <i>this</i>
 * @param <K> Data type of identifier of <i>key</i> for each <i>BNode</i>
 */
class BNode<N, K> extends MultiNode<N, K> {
    //CONSTRUCTORS
    /** Default constructor constructs nameless <i>Node</i> with weight 1
     */
    BNode(){
        super();
        this.data = new FastSortedMap<>();
    }

    /** Constructor initializes an <i>ID</i> for <i>this</i>
     * @param identifier <i>ID</i> for <i>this</i>
     */
    BNode(N identifier){
        super(identifier);
    }

    /** Copy constructor; does not change <i>leaf</i> in original
     * @param node <i>BNode</i> to copy members from
     */
    BNode(BNode<N, K> node){
        super(node);
    }

    //ACCESSORS
    /** Determines whether <i>this</i> is a leaf or not
     * @return Leaf status
     */
    @Override
    public boolean getLeaf(){
        if(this.data.values().stream().allMatch(Objects::isNull) && this.lastChild == null)
            this.leaf = true;
        else
            this.leaf = false;
        return this.leaf;
    }

    //MUTATORS

    /** Canceled function; See <i>BTree</i> to uniformly set <i>keyCapacity</i>  using <i></i>
     * @param kc Maximum number of keys allowed in <i>this</i>
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

/** Variant of <i>BinaryNode</i> for <i>TrieNode</i>
 * @param <N> Data type of identifier of <i>this</i>
 */
class TrieNode<N> extends Node<N> {
    /* key = actual word or string stored as a path of nodes in trie,
        nodeKey = a single character used as a key in each TrieNode<N> object's map
     */
    //CONSTRUCTORS
    /** Default constructor constructs nameless <i>Node</i> with weight 1
     */
    public TrieNode(){
        super();
        this.data = new FastSortedMap<>();
    }

    /** Constructor initializes an <i>ID</i> for <i>this</i>
     * @param identifier <i>ID</i> for <i>this</i>
     */
    public TrieNode(N identifier){
        super(identifier);
        this.data = new FastSortedMap<>();
    }

    /** Copy constructor; does not change <i>leaf</i> in original
     * @param node <i>TrieNode</i> to copy members from
     */
    public TrieNode(TrieNode<N> node){
        super(node);
        this.data = new FastSortedMap<>();
    }

    //ACCESSORS
    /** Finds <i>TrieNode</i> for given <i>key</i> if exists
     * @param nodeKey Single <i>char</i> in collection of <i>char</i> in <i>data</i>
     * @return <i>TrieNode</i> stemming from given <i>nodeKey</i>
     */
    public TrieNode<N> getNode(char nodeKey){
        if(!this.data.containsKey(nodeKey))
            return null;
        return this.data.get(nodeKey);
    }

    /** Determines existence of given <i>nodeKey</i> in <i>data</i>
     * @param nodeKey Single <i>char</i> in collection of <i>char</i> in <i>data</i>
     * @return Whether given <i>nodeKey</i> exists or not
     */
    public boolean containsKey(char nodeKey) {
        return this.data.containsKey(nodeKey);
    }

    /** Count of non-null children
     * @return Number of non-null children exist
     */
    public int size(){
        return this.data.values().filtered(Objects::nonNull).size();
    }

    //MUTATORS
    /** Adds nodeKey-value pair to <i>data</i> only if key is originally absent
     * @param nodeKey Single <i>char</i> in collection of <i>char</i> in <i>data</i>
     * @return Whether adding <i>nodeKey</i> is successful or not
     */
    public boolean insertNode(char nodeKey){
        if(this.data.containsKey(nodeKey))
            return false;
        this.data.put(nodeKey, new TrieNode<>());
        return true;
    }

    /** Replaces child stemming from given <i>nodeKey</i> with new <i>TrieNode</i>; descendents of previous key are lost
     * @param nodeKey Single <i>char</i> in collection of <i>char</i> in <i>data</i>
     * @return Whether replacement is successful or not
     */
    public boolean replaceNode(char nodeKey){
        return this.replaceNode(nodeKey, new TrieNode<>());
    }

    /** Replaces child stemming from given <i>nodeKey</i> with another <i>TrieNode</i>; descendents of replaced key are lost
     * @param nodeKey Single <i>char</i> in collection of <i>char</i> in <i>data</i>
     * @param node New <i>TrieNode</i> to be under given <i>nodeKey</i>
     * @return Whether replacement is successful or not
     */
    public boolean replaceNode(char nodeKey, TrieNode<N> node){
        if(!this.data.containsKey(nodeKey))
            return false;
        this.data.replace(nodeKey, node);
        return true;
    }

    /** Removes corresponding <i>TrieNode</i> if given <i>nodeKey</i> exists; descendents of deleted key are lost
     * @param nodeKey Single <i>char</i> in collection of <i>char</i> in <i>data</i>
     * @return Whether deletion is successful or not
     */
    public boolean deleteNode(char nodeKey){
        if(!this.containsKey(nodeKey))
            return false;
        this.data.remove(nodeKey);
        return true;
    }

    //MEMBER VARIABLES
    /** Container to alphabetically keep track of <i>nodeKey</i> in <i>this</i>
     */
    private FastSortedMap<Character, TrieNode<N>> data;

}