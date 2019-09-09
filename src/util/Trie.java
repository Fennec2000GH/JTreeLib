
public class Trie<T, N> extends Tree<T, N>{
    //CONSTRUCTORS
    public Trie(){
        super
    }
    public Trie(T identifier){
        this.setID(identifier);
    }

    //ACCESSORS
    //tells whether a given word is already stored
    public boolean search(String key){
        if(this.root == null)
            return false;
        TrieNode<N> current = this.root;
        for(int i = 1; i <= key.length(); i++){
            char nodeKey = key.charAt(i - 1);
            if(!current.containsKey(nodeKey) || current.getNode(nodeKey) == null)
                return false;
            current = current.getNode(nodeKey);
        }
        return current.getLeaf();
    }

    //MUTATORS
    //inserts new word in trie; returns false only if word already exists
    public boolean insert(String key){
        if(this.root == null)
            this.root = new TrieNode<>();
        TrieNode<N> current = this.root;
        for(int i = 1; i <= key.length(); i++){
            char nodeKey = key.charAt(i - 1);
            if(!this.root.containsKey(nodeKey))
                this.root.insertNode(nodeKey);
            current = current.getNode(nodeKey);
        }
        if(current.getLeaf())
            return false;
        current.setLeaf(true);
        return true;
    }

    //deletes given key (word or keying) without affecting other keys in trie
    public boolean delete(String key){
        if(this.root == null || !this.search(key))
            return false;
        TrieNode<N> uniqueSuffix = this.root; //the last TrieNode<N> object that multiple keys use as prefix before given key ends
        char uniqueSuffixChar = key.charAt(0);
        TrieNode<N> current = this.root;
        for(int i = 1; i <= key.length(); i++){
            char nodeKey = key.charAt(i - 1);
            if(current.size() > 1 || (current.getLeaf() && i < key.length())){
                uniqueSuffix = current;
                uniqueSuffixChar = nodeKey;
            }
            current = current.getNode(nodeKey);
        }
        return uniqueSuffix.deleteNode(uniqueSuffixChar);
    }

    //MEMBER VARIABLES
    private TrieNode<N> root;

}
