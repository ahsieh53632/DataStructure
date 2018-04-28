package edu.umd.cmsc132A;

import javassist.compiler.ast.Pair;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

interface Map<K, V> {
    // Add given key-value to this map
    Map<K, V> add(K k, V v);

    // Count the number of key-values in this map
    Integer count();

    // Look up the value associated with given key in this map (if it exists)
    Optional<V> lookup(K k);

    // Produce a set of key-values in this map
    Set<Pairof<K, V>> keyVals();

    // Produce a set of keys in this map
    Set<K> keys();

    // Produce a list of values in this map (order is unspecified)
    Listof<V> vals();

    // Is this map the same as m?
    // (Does it have the same keys mapping to the same values?)
    Boolean same(Map<K, V> m);

    // Is this Empty same as the given empty map
    Boolean sameEmpty(EmptyMap<K, V> em);

    // Is this Cons same as the given Cons map
    Boolean sameCons(ConsMap<K, V> cm);

    // Remove any key-value with the given key from this map
    Map<K, V> remove(K k);

    // Remove all key-values with keys that satisfy given predicate
    Map<K, V> removeAll(Predicate<K> p);

    // Remove all key-values w/ key-values that satisfy given binary predicate
    Map<K, V> removeAllPairs(BiPredicate<K, V> p);

    // Join this map and given map (entries in this map take precedence)
    Map<K, V> join(Map<K, V> m);

    // Join this map with the given empty map
    Map<K, V> joinEmpty(EmptyMap<K, V> em);

    // Join this map with the given cons map
    Map<K, V> joinCons(ConsMap<K, V> cm);

}

abstract class AMap<K, V> implements Map<K, V> {
    static <J, U> Map<J, U> empty() {
        return new EmptyMap<J, U>();
    }

    // Is this empty map same as the given empty map
    public Boolean sameEmpty(EmptyMap<K, V> em) {
        return false;
    }

    // Is this Cons map same as the given cons map
    public Boolean sameCons(ConsMap<K, V> cm) {
        return false;
    }
}

class EmptyMap<K, V> extends AMap<K, V>{

    // Add given key-value to this map
    public Map<K, V> add(K k, V v) {
        return new ConsMap<K, V>(new Pairof<K, V>(k, v), this);
    }

    // Count the number of key-values in this map
    public Integer count() {
        return 0;
    }

    // Look up the value associated with given key in this map (if it exists)
    public Optional<V> lookup(K k) {
        return Optional.empty();
    }

    // Produce a set of key-values in this map
    public Set<Pairof<K, V>> keyVals() {
        return new EmptySet<Pairof<K, V>>();
    }

    // Produce a set of keys in this map
    public Set<K> keys() {
        return new EmptySet<K>();
    }

    // Produce a list of values in this map (order is unspecified)
    public Listof<V> vals() {
        return new Empty<V>();
    }

    // Is this map the same as m?
    // (Does it have the same keys mapping to the same values?)
    public Boolean same(Map<K, V> m) {
        return m.sameEmpty(this);
    }

    // Is this empty map same as the given empty map
    public Boolean sameEmpty(EmptyMap<K, V> em) {
        return true;
    }

    // Remove any key-value with the given key from this map
    public Map<K, V> remove(K k) {
        return this;
    }

    // Remove all key-values with keys that satisfy given predicate
    public Map<K, V> removeAll(Predicate<K> p) {
        return this;
    }

    // Remove all key-values w/ key-values that satisfy given binary predicate
    public Map<K, V> removeAllPairs(BiPredicate<K, V> p) {
        return this;
    }

    // Join this map and given map (entries in this map take precedence)
    public Map<K, V> join(Map<K, V> m) {
        return m.joinEmpty(this);
    }

    // Join this empty map with the given empty map
    public Map<K, V> joinEmpty(EmptyMap<K, V> em) {
        return this;
    }

    // Join this empty map with the given cons map
    public Map<K, V> joinCons(ConsMap<K, V> cm) {
        return cm;
    }
}

class ConsMap<K, V> extends AMap<K, V> {
    Pairof<K, V> first;
    Map<K, V> rest;

    ConsMap(Pairof<K, V> first, Map<K, V> rest) {
        this.first = first;
        this.rest = rest;
    }

    // Add given key-value to this map
    public Map<K, V> add(K k, V v) {
        return new ConsMap<K, V>(new Pairof<K, V>(k, v), this.remove(k));
    }

    // Count the number of key-values in this map
    public Integer count() {
        return 1 + this.rest.count();
    }

    // Look up the value associated with given key in this map (if it exists)
    public Optional<V> lookup(K k) {
        if (this.first.left.equals(k)) {
            return Optional.of(this.first.right);
        } else {
            return this.rest.lookup(k);
        }
    }

    // Produce a set of key-values in this map
    public Set<Pairof<K, V>> keyVals() {
        return new ConsSet<Pairof<K, V>>(this.first, this.rest.keyVals());
    }

    // Produce a set of keys in this map
    public Set<K> keys() {
        return new ConsSet<K>(this.first.left, this.rest.keys());
    }

    // Produce a list of values in this map (order is unspecified)
    public Listof<V> vals() {
        return new Cons<V>(this.first.right, this.rest.vals());
    }

    // Is this map the same as m?
    // (Does it have the same keys mapping to the same values?)
    public Boolean same(Map<K, V> m) {
        return m.sameCons(this);
    }

    // Is this Cons map same as the given cons map
    public Boolean sameCons(ConsMap<K, V> cm) {
        return cm.lookup(this.first.left).isPresent() &&
                this.first.right.equals(cm.lookup(this.first.left).get()) &&
                this.count().equals(cm.count()) &&
                cm.remove(this.first.left).same(this.rest);
    }

    // Remove any key-value with the given key from this map
    public Map<K, V> remove(K k) {
        if(this.first.left.equals(k)) {
            return this.rest.remove(k);
        } else {
            return new ConsMap<K, V>(this.first, this.rest.remove(k));
        }
    }

    // Remove all key-values with keys that satisfy given predicate
    public Map<K, V> removeAll(Predicate<K> p) {
        if(p.test(this.first.left)) {
            return this.rest.removeAll(p);
        } else {
            return new ConsMap<K, V>(this.first, this.rest.removeAll(p));
        }
    }

    // Remove all key-values w/ key-values that satisfy given binary predicate
    public Map<K, V> removeAllPairs(BiPredicate<K, V> p) {
        if(p.test(this.first.left, this.first.right)) {
            return this.rest.removeAllPairs(p);
        } else {
            return new ConsMap<K, V>(this.first, this.rest.removeAllPairs(p));
        }
    }

    // Join this map and given map (entries in this map take precedence)
    public Map<K, V> join(Map<K, V> m) {
        return m.joinCons(this);
    }

    // Join this map with the given empty map
    public Map<K, V> joinEmpty(EmptyMap<K, V> em) {
        return this;
    }

    // Join this map with the given cons map
    public Map<K, V> joinCons(ConsMap<K, V> cm) {
        if(cm.lookup(this.first.left).isPresent()) {
            return new ConsMap<K, V>(new Pairof<>(this.first.left, cm.lookup(this.first.left).get()),
                    cm.remove(this.first.left).join(this.remove(this.first.left)));
        } else {
                return new ConsMap<K, V>(this.first, cm.join(this.rest));
            }
        }
    }



