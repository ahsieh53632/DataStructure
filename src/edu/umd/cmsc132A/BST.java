package edu.umd.cmsc132A;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

interface BST<X extends Comparable<X>> {

    // Add the given element into this BST
    BST<X> add(X x);

    // Inserts the given element into this BST,
    // replacing the first element that compares equally to x with x
    // if such an element exists
    BST<X> insertComp(X x);

    // Apply f to every element of this BST and collect results as a BST
    // NOTE: cannot assume f is monotonic
    <R extends Comparable<R>> BST<R> map(Function<X, R> f);

    // Apply f to every element of this BST and collect results as a BST
    // ASSUME: f is monotonic
    <R extends Comparable<R>> BST<R> mapMono(Function<X, R> f);

    // Count the number of elements in this BST
    Integer count();

    // Does this BST contain the given element?
    Boolean contains(X x);

    // Does there exist an element that satisfies p in this BST?
    Boolean exists(Predicate<X> p);

    // Do all elements satisfy p in this set?
    Boolean forAll(Predicate<X> p);

    // Convert this BST to a list of elements sorted in ascending order
    Listof<X> toSortedList();

    // Look up the value
    Optional<X> lookup(X x);

    // turn this BST into a set
    Set<X> toset();

    // get the value, if its a node
    Optional<X> getValue();

    // Remove the given value in the BST
    BST<X> remove(X x);

    // return the biggest value in the BST
    Optional<X> biggest();

    // returns true if the given BST is same as this BST
    Boolean sameBST(BST<X> bst);

    Boolean sameLeaf(Leaf<X> leaf);

    Boolean sameNode(Node<X> node);

    // remove all elements that satisfies the predicate
    BST<X> removeAll(Predicate<X> p);


}

class Leaf<X extends Comparable<X>> implements BST<X> {
    Leaf<X> val;

    Leaf() {
        this.val = val;
    }

    // Add the given element into this BST
    public BST<X> add(X x) {
        return new Node<>(x, this, this);
    }

    // Inserts the given element into this BST,
    // replacing the first element that compares equally to x with x
    // if such an element exists
    public BST<X> insertComp(X x) {
        return new Node<>(x, this, this);
    }

    // Apply f to every element of this BST and collect results as a BST
    // NOTE: cannot assume f is monotonic
    public <R extends Comparable<R>> BST<R> map(Function<X, R> f) {
        return new Leaf<R>();
    }

    // Apply f to every element of this BST and collect results as a BST
    // ASSUME: f is monotonic
    public <R extends Comparable<R>> BST<R> mapMono(Function<X, R> f) {
        return new Leaf<R>();
    }

    // Count the number of elements in this BST
    public Integer count() {
        return 0;
    }

    // Does this BST contain the given element?
    public Boolean contains(X x) {
        return false;
    }

    // Does there exist an element that satisfies p in this BST?
    public Boolean exists(Predicate<X> p) {
        return false;
    }

    // Do all elements satisfy p in this set?
    public Boolean forAll(Predicate<X> p) {
        return true;
    }

    // Convert this BST to a list of elements sorted in ascending order
    public Listof<X> toSortedList() {
        return new Empty<>();
    }

    // look up the value
    public Optional<X> lookup(X x) {
        return Optional.empty();
    }

    public Optional<X> getValue() {
        return Optional.empty();
    }

    // turn this BST into a set
    public Set<X> toset() {
        return new EmptySet<>();
    }

    public BST<X> remove(X x) {
        return this;
    }

    // return the biggest value in the BST
    public Optional<X> biggest() {
        return Optional.empty();
    }

    public Boolean sameBST(BST<X> bst) {
        return bst.sameLeaf(this);
    }

    public Boolean sameLeaf(Leaf<X> leaf) {
        return true;
    }

    public Boolean sameNode(Node<X> node) {
        return false;
    }

    // remove all elements that satisfies the predicate
    public BST<X> removeAll(Predicate<X> p) {
        return this;
    }

}

class Node<X extends Comparable<X>> implements BST<X> {
    X val;
    BST<X> left;
    BST<X> right;

    Node(X val, BST<X> left, BST<X> right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }


    // Add the given element into this BST
    public BST<X> add(X x) {
        return (x.compareTo(this.val) <= 0) ?
                new Node<>(this.val, this.left.add(x), this.right) :
                new Node<>(this.val, this.left, this.right.add(x));
    }


    // Inserts the given element into this BST,
    // replacing the first element that compares equally to x with x
    // if such an element exists
    public BST<X> insertComp(X x) {
        if (this.val.compareTo(x) == 0) {
            return new Node<>(x, this.left, this.right);
        } else {
            return this.add(x);
        }
    }

    // Apply f to every element of this BST and collect results as a BST
    // NOTE: cannot assume f is monotonic
    public <R extends Comparable<R>> BST<R> map(Function<X, R> f) {
        R NewV = f.apply(this.val);

        if (this.left.getValue().isPresent() && f.apply(this.left.getValue().get()).compareTo(NewV) > 0 &&
                this.right.getValue().isPresent() && f.apply(this.right.getValue().get()).compareTo(NewV) <= 0) {
            R NewLeftV = f.apply(this.left.getValue().get());
            X OldLeftV = this.left.getValue().get();
            R NewRV = f.apply(this.right.getValue().get());
            X OldRV = this.right.getValue().get();

            return new Node<R>(NewV, this.left.remove(OldLeftV).add(OldRV).map(f), this.right.add(OldLeftV).remove(OldRV).map(f));

        } else if (this.right.getValue().isPresent() && f.apply(this.right.getValue().get()).compareTo(NewV) <= 0) {
            R NewRV = f.apply(this.right.getValue().get());
            X OldRV = this.right.getValue().get();
            return new Node<R>(NewV, this.left.add(OldRV).map(f), this.right.remove(OldRV).map(f));
        } else if (this.left.getValue().isPresent() && f.apply(this.left.getValue().get()).compareTo(NewV) > 0) {
            R NewLeftV = f.apply(this.left.getValue().get());
            X OldLeftV = this.left.getValue().get();

            return new Node<R>(NewV, this.left.remove(OldLeftV).map(f), this.right.add(OldLeftV).map(f));
        } else {
            return new Node<R>(NewV, this.left.map(f), this.right.map(f));
        }

    }

    // Remove the given value in the BST
    public BST<X> remove(X x) {
        if (this.val.compareTo(x) == 0 && this.left.biggest().isPresent()) {
            X NewV = this.left.biggest().get();
            return new Node<>(NewV, this.left.remove(NewV), this.right.remove(NewV));
        } else if (this.val.compareTo(x) == 0 && !this.right.equals(new Leaf<>())) {
            return this.right;
        } else if (this.val.compareTo(x) == 0 & this.left.equals(new Leaf<>()) && this.right.equals(new Leaf<>())) {
            return new Leaf<>();
        } else {

            return new Node<>(this.val, this.left.remove(x), this.right.remove(x));
        }
    }

    // return the biggest value in the BST
    public Optional<X> biggest() {
        if (this.right.getValue().isPresent()) {
            return this.right.biggest();
        } else if (this.left.getValue().isPresent() && this.right.equals(new Leaf<>())) {
            return Optional.of(this.left.getValue().get());
        } else {
            return Optional.of(this.val);
        }
    }


    // turn this BST into a set
    public Set<X> toset() {
        return this.toSortedList().toset();
    }


    // Apply f to every element of this BST and collect results as a BST
    // ASSUME: f is monotonic
    public <R extends Comparable<R>> BST<R> mapMono(Function<X, R> f) {
        return new Node<R>(f.apply(this.val), this.left.mapMono(f), this.right.mapMono(f));
    }


    // Count the number of elements in this BST
    public Integer count() {
        return 1 + this.left.count() + this.right.count();
    }

    // Does this BST contain the given element?
    public Boolean contains(X x) {
        return this.val.compareTo(x) == 0 || this.left.contains(x) || this.right.contains(x);
    }

    // Does there exist an element that satisfies p in this BST?
    public Boolean exists(Predicate<X> p) {
        return p.test(this.val) || this.left.exists(p) || this.right.exists(p);
    }

    // Do all elements satisfy p in this set?
    public Boolean forAll(Predicate<X> p) {
        return p.test(this.val) &&
                this.left.forAll(p) &&
                this.right.forAll(p);
    }

    // Convert this BST to a list of elements sorted in ascending order
    public Listof<X> toSortedList() {
        return this.left.toSortedList().append(new Cons<>(this.val, new Empty<>())).append(this.right.toSortedList());
    }

    // look up and return the value if it has one
    public Optional<X> lookup(X x) {
        if (this.val.compareTo(x) == 0) {
            return Optional.of(this.val);
        } else if (this.val.compareTo(x) < 0) {
            return this.right.lookup(x);
        } else {
            return this.left.lookup(x);
        }
    }

    // get the value, if its a node
    public Optional<X> getValue() {
        return Optional.of(this.val);
    }

    public Boolean sameBST(BST<X> bst) {
        return bst.sameNode(this);
    }

    public Boolean sameLeaf(Leaf<X> leaf) {
        return false;
    }

    public Boolean sameNode(Node<X> node) {
        return node.exists(i -> i.compareTo(this.val) == 0) &&
                node.left.sameBST(this.left) &&
                node.right.sameBST(this.right);
    }

    // remove all elements that satisfies the predicate
    public BST<X> removeAll(Predicate<X> p) {
        if (p.test(this.val)) {
            return this.remove(this.val);
        } else {
            return new Node<>(this.val, this.left.removeAll(p), this.right.removeAll(p));
        }
    }

}