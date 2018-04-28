package edu.umd.cmsc132A;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

// MultiSets of elements of type X
// A multiset is like a set, but allows multiple instances of elements
interface MultiSet<X> {
    // Add x to this multiset
    MultiSet<X> add(X x);

    // Apply f to every element of this multiset, collect results as a multiset
    <R> MultiSet<R> map(Function<X, R> f);

    // Count the number of elements in this multiset
    Integer count();

    // Count the Time one element appears in this Multiset
    Integer countElem(X x);

    // Is this multiset the same as the given multiset?
    Boolean same(MultiSet<X> s);

    // Is the given MultiSet is a ConsSet and equals this ConsMultiSet
    Boolean sameCons(ConsMutliSet<X> cs);

    // Does this EmptyMultiSet euqlas the given EmptyMultiSet
    Boolean sameEmpty(EmptyMultiSet<X> es);

    // Does this multiset contain all of the elements of the given multiset?
    Boolean superset(MultiSet<X> s);

    // Is this EmptyMultiSet the subset of the given EmptyMultiSet?
    Boolean supersetEmpty(EmptyMultiSet<X> es);

    // Is this ConsMultiSet the subset of the given ConsMultiSet?
    Boolean supersetCons(ConsMutliSet<X> cs);

    // Does the given multiset contain all of the elements of given multiset?
    Boolean subset(MultiSet<X> s);

    // Does this mutliset contain the given element?
    Boolean contains(X x);

    // Does there exist an element that satisfies p in this multiset?
    Boolean exists(Predicate<X> p);

    // Do all elements satisfy p in this multiset?
    Boolean forAll(Predicate<X> p);

    // Convert this multiset to a list of elements (order is unspecified)
    Listof<X> toList();

    // Produce an element of this multiset (if one exists)
    Optional<X> elem();

    // Choose an element from this (if one exists) and
    // produce the element and rest of the multiset
    Optional<Pairof<X, MultiSet<X>>> choose();

    // Convert this multiset into a set
    Set<X> toSet();

    // remove xs in this multiset
    MultiSet<X> remove(X x);

    // Convert this multiset into a set
    Set<X> toSetACC(MultiSet<X> ACC);
}

abstract class AMultiSet<X> implements MultiSet<X> {

    // Is the given ConsMultiSet equal to this ConsMultiSet
    public Boolean sameCons(ConsMutliSet<X> cs) {
        return false;
    }

    // Does this EmptyMultSet euquals to the given EmptyMultiSet
    public Boolean sameEmpty(EmptyMultiSet<X> es) {
        return false;
    }

    // Is this EmptyMultiSet the subset of the given EmptyMultiSet?
    public Boolean supersetEmpty(EmptyMultiSet<X> es) {
        return false;
    }

    // Is this ConsMultiSet the subset of the given ConsMultiSet?
    public Boolean supersetCons(ConsMutliSet<X> cs) {
        return true;
    }
}

class EmptyMultiSet<X> extends AMultiSet<X> {

    // Add x to this Multiset
    public MultiSet<X> add(X x) {
        return new ConsMutliSet<X>(x, this);
    }

    // Does this Multiset contain the given element?
    public Boolean contains(X x) {
        return false;
    }

    // Apply f to every element of this Multiset and collect results as a Multiset.
    public <R> MultiSet<R> map(Function<X, R> f) {
        return new EmptyMultiSet<R>();
    }

    // Count the number of elements in this Multiset
    public Integer count() {
        return 0;
    }

    // Count the time that x appears in this multiset
    public Integer countElem(X x) {
        return 0;
    }

    // Is this set the same as the given Multiset?
    public Boolean same(MultiSet<X> s) {
        return s.sameEmpty(this);
    }

    // Does this EmptyMultSet euquals to the given EmptyMultiSet
    public Boolean sameEmpty(EmptyMultiSet<X> es) {
        return true;
    }

    // Is this EmptyMultiSet the subset of the given EmptyMultiSet?
    public Boolean supersetEmpty(EmptyMultiSet<X> es) {
        return true;
    }

    // Does this set contain all of the elements of the given Multiset?
    public Boolean superset(MultiSet<X> s) {
        return s.supersetEmpty(this);
    }

    // Does the given set contain all of the elements of this Multiset?
    public Boolean subset(MultiSet<X> s) {
        return true;
    }

    // Does there exist an element that satisfies p in this Multiset?
    public Boolean exists(Predicate<X> p) {
        return false;
    }

    // Do all elements satisfy p in this Multiset?
    public Boolean forAll(Predicate<X> p) {
        return true;
    }

    // Produce an element of this multiset (if one exists)
    public Optional<X> elem() {
        return Optional.empty();
    }

    // Convert this set to a list of unique elements (order is unspecified)
    public Listof<X> toList() {
        return new Empty<X>();
    }

    // Choose an element from this (if one exists) and
    // produce the element and rest of the set
    public Optional<Pairof<X, MultiSet<X>>> choose() {
        return Optional.empty();
    }

    // Convert this multiset into a set
    public Set<X> toSet() {
        return new EmptySet<X>();
    }

    // Convert this multiset into a set
    public Set<X> toSetACC(MultiSet<X> ACC) {
        return new EmptySet<X>();
    }

    //remove xs from this emptymultiset
    public MultiSet<X> remove(X x) {
        return new EmptyMultiSet<X>();
    }
}


class ConsMutliSet<X> extends AMultiSet<X> {
    X first;
    MultiSet<X> rest;

    ConsMutliSet(X first, MultiSet<X> rest) {
        this.first = first;
        this.rest = rest;
    }

    // Add x to this multiset
    public MultiSet<X> add(X x) {
        return new ConsMutliSet<X>(x, this);
    }

    // Does this multiset contains x
    public Boolean contains(X x) {
        return this.first.equals(x) ||
                this.rest.contains(x);
    }

    // Apply f to every element of this Multiset and collect results as a Multiset.
    public <R> MultiSet<R> map(Function<X, R> f) {
        return new ConsMutliSet<R>(f.apply(this.first), this.rest.map(f));
    }

    // Count the number of elements in this Multiset
    public Integer count() {
        return 1 + this.rest.count();
    }

    // Count the time that x appears in the multiset
    public Integer countElem(X x) {
        if (this.first.equals(x)) {
            return 1 + this.rest.countElem(x);
        } else {
            return this.rest.countElem(x);
        }
    }

    // Is this set the same as the given Multiset?
    public Boolean same(MultiSet<X> s) {
        return s.sameCons(this);
    }

    // Is the given Set is a ConsMultiSet and equals this ConsMultiSet
    public Boolean sameCons(ConsMutliSet<X> cs) {
        return this.contains(cs.first) &&
                this.countElem(cs.first).equals(cs.countElem(cs.first)) &&
                this.count().equals(cs.count()) &&
                cs.rest.remove(cs.first).same(this.remove(cs.first));
    }

    // Does this set contain all of the elements of the given multiset?
    public Boolean superset(MultiSet<X> s) {
        return s.supersetCons(this);
    }

    // Is this ConsMultiSet the subset of the given Multiset?
    public Boolean supersetCons(ConsMutliSet<X> cs) {
        return cs.contains(this.first) &&
                cs.countElem(this.first) >= (this.countElem(this.first)) &&
                cs.rest.remove(this.first).superset(this.remove(this.first));
    }

    // Does the given multiset contain all of the elements of this multiset?
    public Boolean subset(MultiSet<X> s) {
        return s.contains(this.first) &&
                s.countElem(this.first) >= (this.countElem(this.first)) &&
                this.remove(this.first).subset(s);
    }

    // Does there exist an element that satisfies p in this multiset?
    public Boolean exists(Predicate<X> p) {
        return p.test(this.first) || this.rest.exists(p);
    }

    // Does every element that satisfies p in this multiset?
    public Boolean forAll(Predicate<X> p) {
        return p.test(this.first) &&
                this.rest.exists(p);
    }

    // Produce an element of this multiset (if one exists)
    public Optional<X> elem() {
        return Optional.of(this.first);
    }

    // Convert this multiset to a list of unique elements (order is unspecified)
    public Listof<X> toList() {
        return new Cons<X>(this.first, this.rest.toList());
    }

    // Choose an element from this (if one exists) and
    // produce the element and rest of the set
    public Optional<Pairof<X, MultiSet<X>>> choose() {
        return Optional.of(new Pairof<X, MultiSet<X>>(this.first, this.rest));
    }

    // convert this multiset into a set
    public Set<X> toSet() {
        return this.toSetACC(new EmptyMultiSet<X>());
    }

    // Convert this multiset into a set
    public Set<X> toSetACC(MultiSet<X> ACC) {
        if (ACC.contains(this.first)) {
            return this.rest.toSetACC(ACC);
        } else {
            MultiSet<X> ACC2 = new ConsMutliSet<X>(this.first, ACC);
            return new ConsSet<X>(this.first, this.rest.toSetACC(ACC2));
        }
    }

    // remove xs from this consmultiset
    public MultiSet<X> remove(X x) {
        if (this.first.equals(x)) {
            return this.rest.remove(x);
        } else {
            return new ConsMutliSet<X>(this.first, this.rest.remove(x));
        }
    }
}
