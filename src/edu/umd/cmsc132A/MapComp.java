package edu.umd.cmsc132A;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

// Similar to a Map, but requires keys implement Comparable interface
interface MapComp<K extends Comparable<K>, V> {

    // Add given key-value to this map
    MapComp<K, V> add(K k, V v);

    // Count the number of key-values in this map
    Integer count();

    // Look up the value associated with given key in this map (if it exists)
    Optional<V> lookup(K k);

    // Produce a set of key-values in this map
    Set<Pairof<K, V>> keyVals();

    // Produce a list of key-values in ascending sorted order
    Listof<Pairof<K, V>> sortedKeyVals();

    // Produce a set of keys in this map
    Set<K> keys();

    // Produce a set of keys in ascending sorted order
    Listof<K> sortedKeys();

    // Produce a list of values in this map (order is unspecified)
    Listof<V> vals();

    // Produce a list of values in the ascending sorted order
    // of their keys in this map
    // NOTE: list is not itself sorted
    Listof<V> sortedVals();

    // Is this map the same as m?
    // (Does it have the same keys mapping to the same values?)
    Boolean same(MapComp<K, V> m);

    // Remove any key-value with the given key from this map
    MapComp<K, V> remove(K k);

    // Remove all key-values with keys that satisfy given predicate
    MapComp<K, V> removeAll(Predicate<K> p);

    // Remove all key-values w/ key-values that satisfy given binary predicate
    MapComp<K, V> removeAllPairs(BiPredicate<K, V> p);

    // Join this map and given map (entries in this map take precedence)
    MapComp<K, V> join(MapComp<K, V> m);
}

// Implementation of MapComp that uses a BST for efficient operations
class MapBST<K extends Comparable<K>, V> implements MapComp<K, V> {

    // A Pairof where comparisons are done on the left component
    static class KVPairof<K extends Comparable<K>, V> extends Pairof<K, V>
            implements Comparable<KVPairof<K, V>> {

        KVPairof(K left, V right) {
            super(left, right);
        }

        public int compareTo(KVPairof<K, V> p) {
            return this.left.compareTo(p.left);
        }

        public int compareK(K key) {
            return this.left.compareTo(key);
        }
    }

    BST<KVPairof<K, V>> bst;


    MapBST() {
        this.bst = new Leaf<>();
    }

    MapBST(BST<KVPairof<K, V>> bst) {
        this.bst = bst;
    }


    // Add given key-value to this map
    public MapComp<K, V> add(K k, V v) {
        return new MapBST<K, V>(this.bst.add(new KVPairof<>(k, v)));
    }

    // Count the number of key-values in this map
    public Integer count() {
        return this.bst.count();
    }

    // Look up the value associated with given key in this map (if it exists)
    public Optional<V> lookup(K k) {
        if (this.bst.lookup(new KVPairof<>(k, null)).isPresent()) {
            return Optional.of(this.bst.lookup(new KVPairof<>(k, null)).get().right);
        } else {
            return Optional.empty();
        }
    }

    // Produce a set of key-values in this map
    public Set<Pairof<K, V>> keyVals() {
        return this.bst.toSortedList().toset().map(i -> new Pairof<>(i.left, i.right));
    }

    // Produce a list of key-values in ascending sorted order
    public Listof<Pairof<K, V>> sortedKeyVals() {
        return this.bst.toSortedList().map(i -> new Pairof<>(i.left, i.right));
    }

    // Produce a set of keys in this map
    public Set<K> keys() {
        return this.bst.toSortedList().toset().map(i -> i.left);
    }

    // Produce a set of keys in ascending sorted order
    public Listof<K> sortedKeys() {
        return this.bst.toSortedList().map(i -> i.left);
    }

    // Produce a list of values in this map (order is unspecified)
    public Listof<V> vals() {
        return this.bst.toSortedList().map(i -> i.right);
    }

    // Produce a list of values in the ascending sorted order
    // of their keys in this map
    // NOTE: list is not itself sorted
    public Listof<V> sortedVals() {
        return this.bst.toSortedList().map(i -> i.right);
    }

    // Is this map the same as m?
    // (Does it have the same keys mapping to the same values?)
    public Boolean same(MapComp<K, V> m) {
        return m.vals().same(this.vals());
    }

    // Remove any key-value with the given key from this map
    public MapComp<K, V> remove(K k) {
        return new MapBST<K, V>(this.bst.remove(new KVPairof<>(k, null)));
    }

    // Remove all key-values with keys that satisfy given predicate
    public MapComp<K, V> removeAll(Predicate<K> p) {
        return new MapBST<K, V>(this.bst.removeAll(i -> p.test(i.left)));
    }

    // Remove all key-values w/ key-values that satisfy given binary predicate
    public MapComp<K, V> removeAllPairs(BiPredicate<K, V> p) {
        return new MapBST<K, V>(this.bst.removeAll(i -> p.test(i.left, i.right)));
    }

    // Join this map and given map (entries in this map take precedence)
    public MapComp<K, V> join(MapComp<K, V> m) {
        return new MapBST<K, V>(this.joinSetup(m));
    }

    public BST<KVPairof<K, V>> joinSetup(MapComp<K, V> m) {
        Listof<KVPairof<K, V>> YLIST = m.sortedKeyVals().map(i -> new KVPairof(i.left, i.right));

        if(m.sortedKeyVals().getfirst().isPresent() && !this.bst.contains(YLIST.getfirst().get())) {
            return this.bst.add(YLIST.getfirst().get());
        } else {
            return this.bst;
        }
    }
}
