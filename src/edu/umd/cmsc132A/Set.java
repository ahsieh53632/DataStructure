package edu.umd.cmsc132A;

import java.util.function.*;
import java.util.Optional;

// Sets of elements of type X
interface Set<X> {
    // Add x to this set
    Set<X> add(X x);

    // Apply f to every element of this set and collect results as a set.
    <R> Set<R> map(Function<X, R> f);

    // Accumulator of map
    <R> Set<R> mapAcc(Function<X, R> f, Set<R> acc);

    // Count the number of elements in this set
    Integer count();

    // Is this set the same as the given set?
    Boolean same(Set<X> s);

    // Does this ConsSet equals the given ConsSet
    Boolean sameCons(ConsSet<X> cs);

    // Does this EmptySet euqlas the given EmptySet
    Boolean sameEmpty(EmptySet<X> es);

    // remove x in the set
    Set<X> remove(X x);

    // Does this set contain all of the elements of the given set?
    Boolean superset(Set<X> s);

    // Is this ConsSet the subset of the given ConsSet?
    Boolean supersetCons(ConsSet<X> cs);

    // Is this EmptySet the subset of the given EmptySet?
    Boolean supersetEmpty(EmptySet<X> es);

    // Does the given set contain all of the elements of this set?
    Boolean subset(Set<X> s);

    // Does this set contain the given element?
    Boolean contains(X x);

    // Does there exist an element that satisfies p in this set?
    Boolean exists(Predicate<X> p);

    // Do all elements satisfy p in this set?
    Boolean forAll(Predicate<X> p);

    // Convert this set to a list of unique elements (order is unspecified)
    Listof<X> toList();

    // Produce an element of this set (if one exists)
    Optional<X> elem();

    // Choose an element from this (if one exists) and
    // produce the element and rest of the set
    Optional<Pairof<X, Set<X>>> choose();

    // Convert this set to a multiset
    MultiSet<X> toMultiSet();
}

abstract class ASet<X> implements Set<X> {

    // new EmptySet<Y>
    static <Y> Set<Y> empty() {
        return new EmptySet<Y>();
    }

    // Is the given Set is a ConsSet and equals this ConsSet
    public Boolean sameCons(Cons<X> cs) {
        return false;
    }

    // Does this EmptySet euqlas the given EmptySet
    public Boolean sameEmpty(EmptySet<X> es) {
        return false;
    }

    // Is this EmptySet the subset of the given EmptySet?
    public Boolean supersetEmpty(EmptySet<X> es) {
        return false;
    }

    // Is this ConsSet the subset of the given ConsSet?
    public Boolean supersetCons(ConsSet<X> cs) {
        return true;
    }
}

class EmptySet<X> extends ASet<X> {

    // Add x to this set
    public Set<X> add(X x) {
        return new ConsSet<X>(x, this);
    }

    // Does this set contain the given element?
    public Boolean contains(X x) {
        return false;
    }

    // Apply f to every element of this set and collect results as a set.
    public <R> Set<R> map(Function<X, R> f) {
        return new EmptySet<R>();
    }

    // Accumulator of map
    public <R> Set<R> mapAcc(Function<X, R> f, Set<R> acc) {
        return new EmptySet<R>();
    }

    // Count the number of elements in this set
    public Integer count() {
        return 0;
    }

    // Is this set the same as the given set?
    public Boolean same(Set<X> s) {
        return s.sameEmpty(this);
    }

    // Does this EmptySet euqlas the given EmptySet
    public Boolean sameEmpty(EmptySet<X> es) {
        return true;
    }

    // Is this EmptySet the subset of the given EmptySet?
    public Boolean supersetEmpty(EmptySet<X> es) {
        return true;
    }

    // is the given conset equal to this cons set
    public Boolean sameCons(ConsSet<X> cs) {
        return false;
    }

    // remove x in the set
    public Set<X> remove(X x) {
        return new EmptySet<X>();
    }

    // Does this set contain all of the elements of the given set?
    public Boolean superset(Set<X> s) {
        return s.supersetEmpty(this);
    }

    // Does the given set contain all of the elements of this set?
    public Boolean subset(Set<X> s) {
        return true;
    }

    // Does there exist an element that satisfies p in this set?
    public Boolean exists(Predicate<X> p) {
        return false;
    }

    // Do all elements satisfy p in this set?
    public Boolean forAll(Predicate<X> p) {
        return true;
    }

    // Produce an element of this set (if one exists)
    public Optional<X> elem() {
        return Optional.empty();
    }

    // Convert this set to a list of unique elements (order is unspecified)
    public Listof<X> toList() {
        return new Empty<X>();
    }

    // Choose an element from this (if one exists) and
    // produce the element and rest of the set
    public Optional<Pairof<X, Set<X>>> choose() {
        return Optional.empty();
    }

    // Convert this set to a multiset
    public MultiSet<X> toMultiSet() {
        return new EmptyMultiSet<X>();
    }
}

class ConsSet<X> extends ASet<X> {
    X first;
    Set<X> rest;

    ConsSet(X first, Set<X> rest) {
        this.first = first;
        this.rest = rest;
    }

    // Add x to this set
    public Set<X> add(X x) {
        if (this.contains(x)) {
            return this;
        } else {
            return new ConsSet<X>(x, this);
        }
    }

    // Does this set contain the given element?
    public Boolean contains(X x) {
        return this.first.equals(x) ||
                this.rest.contains(x);
    }

    // Apply f to every element of this set and collect results as a set.
    public <R> Set<R> map(Function<X, R> f) {
        Set<R> ACU = new EmptySet<R>();
        return this.mapAcc(f, ACU);
    }

    public <R> Set<R> mapAcc(Function<X, R> f, Set<R> acc) {
        if(acc.contains(f.apply(this.first))) {
            return this.rest.mapAcc(f, acc);
        } else {
            return new ConsSet<R>(f.apply(this.first), this.rest.mapAcc(f, acc.add(f.apply(this.first))));
        }
    }

    // Count the number of elements in this set
    public Integer count() {
        return 1 + this.rest.count();
    }

    // Is this set the same as the given set?
    public Boolean same(Set<X> s) {
        return s.sameCons(this);
    }

    // Is the given Set a ConsSet and equals this ConsSet
    public Boolean sameCons(ConsSet<X> cs) {

        return this.contains(cs.first) &&
                this.count().equals(cs.count()) &&
                cs.rest.same(this.remove(cs.first));

    }

    // remove the given x in the set
    public Set<X> remove(X x) {
        if (this.first.equals(x)) {
            return this.rest.remove(x);
        } else {
            return new ConsSet<X>(this.first, this.rest.remove(x));
        }
    }

    // Does this set contain all of the elements of the given set?
    public Boolean superset(Set<X> s) {
        return s.supersetCons(this);
    }

    // Is this ConsSet the subset of the given set?
    public Boolean supersetCons(ConsSet<X> cs) {
        return cs.contains(this.first) &&
                cs.remove(this.first).superset(this.rest);
    }

    // Does the given set contain all of the elements of this set?
    public Boolean subset(Set<X> s) {
        return s.contains(this.first) &&
                this.rest.subset(s);
    }

    // Does there exist an element that satisfies p in this set?
    public Boolean exists(Predicate<X> p) {
        return p.test(this.first) || this.rest.exists(p);
    }

    // Does every element that satisfies p in this set?
    public Boolean forAll(Predicate<X> p) {
        return p.test(this.first) &&
                this.rest.exists(p);
    }

    // Produce an element of this set (if one exists)
    public Optional<X> elem() {
        return Optional.of(this.first);
    }

    // Convert this set to a list of unique elements (order is unspecified)
    public Listof<X> toList() {
        return new Cons<X>(this.first, this.rest.toList());
    }

    // Choose an element from this (if one exists) and
    // produce the element and rest of the set
    public Optional<Pairof<X, Set<X>>> choose() {
        return Optional.of(new Pairof<X, Set<X>>(this.first, this.rest));
    }

    // Convert this set to a multiset
    public MultiSet<X> toMultiSet() {
        return new ConsMutliSet<X>(this.first, this.rest.toMultiSet());
    }

}





