package JTreeLib.util;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Queue;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

public abstract class MultiTree<T, N, K> implements Cloneable {
    //ACCESSORS
    public T getID(){
        return this.id;
    }

    //counts the total number of keys in all nodes in tree
    public int getKeyCount(){
        int sum = 0;
        if(this.getRoot() == null)
            return 0;
        Queue<MultiNode<N, K>> Q = new LinkedBlockingQueue<>();
        Q.offer(this.getRoot());
        MultiNode<N, K> top = null;
        while(!Q.isEmpty()){
            top = Q.poll();
            sum += top.data.size();
            if(top.getLeaf())
                continue;
            for(K key : top.data.keySet().filtered(Objects::nonNull))
                Q.offer(top.data.get(key));
        }
        return sum;
    }

    //counts the total number of nodes in tree
    public int getNodeCount(){
        int sum = 0;
        if(this.getRoot() == null)
            return 0;
        Queue<MultiNode<N, K>> Q = new LinkedBlockingQueue<>();
        Q.offer(this.getRoot());
        MultiNode<N, K> top = null;
        while(!Q.isEmpty()){
            top = Q.poll();
            ++sum;
            if(top.getLeaf())
                continue;
            for(K key : top.data.keySet().filtered(Objects::nonNull))
                Q.offer(top.data.get(key));
        }
        return sum;
    }

    public MultiNode<N, K> getRoot(){
        return this.root;
    }

    public void traverseByBreath(){
        this.traverseByBreadth_Array();
        for(MultiNode<N, K> node : this.visitedNodes)
            System.out.println(node.data.keySet());
        return;
    }

    public void traverseByBreadth_Array(){
        if(this.getRoot() == null){
            this.visitedNodes = null;
            return;
        }
        int index = 0;
        this.visitedNodes = new MultiNode[this.getNodeCount()];
        Queue<MultiNode<N, K>> Q = new LinkedBlockingQueue<>();
        Q.offer(this.getRoot());
        MultiNode<N, K> top = null;
        while(!Q.isEmpty()){
            top = Q.poll();
            for(K key : top.data.keySet().filtered(Objects::nonNull))
                Q.offer(top.data.get(key));
            if(top.lastChild != null)
                Q.offer(top.lastChild);
            this.visitedNodes[index] = top;
        }
        return;
    }

    public void traverseByDepth_Array(MultiNode<N, K> root){
        if(this.visitedNodes.length != this.getNodeCount() || Arrays.stream(this.visitedNodes).noneMatch(Objects::nonNull)){
            this.visitedNodes = new MultiNode[this.getKeyCount()];
            Arrays.fill(this.visitedNodes, null);
        }
        int index = 0;
        Stack<MultiNode<N, K>> S = new Stack<>();
        S.push(this.getRoot());
        MultiNode<N, K> top = null;
        while(!S.isEmpty()){
            top = S.peek();
            if(top.getLeaf()){
                this.visitedNodes[index] = top;
                ++index;
                continue;
            }
            if(top.data.values().stream().filter(Objects::nonNull).anyMatch(n -> !Arrays.asList(this.visitedNodes).contains(n)))
                S.push(top.data.values()
                    .filtered(Objects::nonNull)
                    .stream()
                    .filter(n -> !Arrays.asList(this.visitedNodes)
                    .contains(n))
                    .collect(Collectors.toUnmodifiableList())
                    .get(0));
            else {
                S.pop();
                this.visitedNodes[index] = top;
                ++index;
            }
        }
        return;
    }

    //returns the node that contains given key     //O(log(n) * h)
    public MultiNode<N, K> search(K key){
        MultiNode<N, K> p = this.getRoot();
        while(p != null){
            int index = Arrays.binarySearch(p.data.keySet().toArray(), key);
            if(index != -1)
                return p;
            p.data.put(key, null);
            K rightNeighborKey = p.getRightNeighborKey(key);
            p.data.remove(key);
            p = p.data.get(rightNeighborKey);
        }
        return p;
    }

    //MUTATORS
    public void setID(T identifier){
        this.id = identifier;
        return;
    }

    public void setRoot(K key){
        this.root = new MultiNode<N, K>(key);
    }

    //MEMBER VARIABLES
    protected T id;
    protected MultiNode<N, K>[] visitedNodes = null;
    protected MultiNode<N, K> root = null;


}

class BTree<T, N, K> extends MultiTree<T, N, K> {
    //CONSTRUCTORS
    BTree(){
        this.id = null;
    }

    BTree(T identifier){
        this.id = identifier;
    }

    //ACCESSORS
    public int getMaxKeysAllowed(){
        return 2 * this.degree - 1;
    }

    public int getMinKeysAllowed(){
        return this.degree - 1;
    }

    @Override
    public BNode<N, K> search(K key){
        MultiNode<N, K> node = super.search(key);
        return (node == null) ? null : (BNode<N, K>)node;
    }

    //MUTATORS
    public boolean insert(K key){
        if(this.getRoot() == null) {
            this.root = new BNode<>();
            this.root.setLeaf(true);
            this.root.data.put(key, null);
            return true;
        }

        MultiNode<N, K> p = this.root;
        while(!p.getLeaf()){
            p.data.put(key, null);
            K rightKey = p.getRightNeighborKey(key);
            p.data.remove(key);
            if(p.data.get(rightKey).getNodeSize() >= this.getMaxKeysAllowed())
                this.split()

        }

    }

    //MEMBER VARIABLES
    private int degree = 1;

}

class RTree<T, N, K> extends MultiTree<T, N, K> {
    //CONSTRUCTORS

    //ACESSORS

    //MUTATORS

    //MEMBER VARIABLES

}

class TwoThreeTree<T, N, K> extends MultiTree<T, N, K> {
    //CONSTRUCTORS


    //ACCESSORS



    //MUTATORS


    //MEMBER VARIABLES





}