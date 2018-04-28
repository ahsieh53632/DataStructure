package edu.umd.cmsc132A;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

interface Listof<X> {
    // Cons given element on to this list
    Listof<X> cons(X x);

    // Compute the length of this list
    Integer length();

    // Zip together this list and given list into a list of pairs
    // Stop zipping at end of shortest list
    <Y> Listof<Pairof<X, Y>> zip(Listof<Y> ls2);

    // Zip this list on the RIGHT of the given cons
    <Y> Listof<Pairof<Y, X>> zipCons(Cons<Y> xs);

    // Apply f to every element of this list collecting results as a list
    <Y> Listof<Y> map(Function<X, Y> f);

    // Fundamental list abstraction method
    <Y> Y foldr(BiFunction<X, Y, Y> f, Y b);

    // Append this list and xs
    Listof<X> append(Listof<X> xs);

    // Is this list the same as the given list
    Boolean same(Listof<X> xs);

    // Is this list the same as the given empty?
    Boolean sameEmpty(Empty<X> mt);

    // Is this list the same as the given cons?
    Boolean sameCons(Cons<X> xs);

    // Turn this list to a set
    Set<X> toset();

    // Turn this list to a set
    MultiSet<X> toMset();

    Set<X> tosetACC(Set<X> now);

    // get the first element if there is one
    Optional<X> getfirst();

}

abstract class AListof<X> implements Listof<X> {

    static <Y> Listof<Y> empty() {
        return new Empty<Y>();
    }

    public Listof<X> cons(X x) {
        return new Cons<X>(x, this);
    }

    public Integer length() {
        return this.foldr((x, i) -> i + 1, 0);
    }

    public <Y> Listof<Y> map(Function<X, Y> f) {
        return this.foldr(((X x, Listof<Y> ys) -> new Cons<Y>(f.apply(x), ys)),
                new Empty<Y>());
    }

    // Append this list and xs
    public Listof<X> append(Listof<X> xs) {
        return this.foldr((x, ys) -> new Cons<X>(x, ys), xs);
    }

    public Boolean sameCons(Cons<X> xs) {
        return false;
    }

    public Boolean sameEmpty(Empty<X> xs) {
        return false;
    }
}



class Empty<X> extends AListof<X> {

    public <Y> Listof<Pairof<X, Y>> zip(Listof<Y> ls2) {
        return new Empty<Pairof<X, Y>>();
    }

    public <Y> Listof<Pairof<Y, X>> zipCons(Cons<Y> xs) {
        return new Empty<Pairof<Y, X>>();
    }

    public <Y> Y foldr(BiFunction<X, Y, Y> f, Y b) {
        return b;
    }

    public Boolean same(Listof<X> xs) {
        return xs.sameEmpty(this);
    }

    public Boolean sameEmpty(Empty<X> mt) {
        return true;
    }

    // add the given element base on how it compares to the first of the list
    public Listof<Comparable<X>> insert(Comparable<X> x) {
        return new Cons<>(x, new Empty<>());
    }

    public Listof<X> sort() {
        return this;
    }

    public Listof<Comparable<X>> sortACC(Cons<Comparable<X>> lox) {
        return lox;
    }

    public Set<X> toset() {
        return new EmptySet<>();
    }

    public Set<X> tosetACC(Set<X> now){
        return now;
    }

    // get the first element if there is one
    public Optional<X> getfirst() {
        return Optional.empty();
    }

    // Turn this list to a set
    public MultiSet<X> toMset() {
        return new EmptyMultiSet<>();
    }
}

class Cons<X> extends AListof<X> {
    X first;
    Listof<X> rest;

    Cons(X first, Listof<X> rest) {
        this.first = first;
        this.rest = rest;
    }

    public Integer length() {
        return 1 + this.rest.length();
    }

    public <Y> Listof<Pairof<X, Y>> zip(Listof<Y> ls2) {
        return ls2.zipCons(this);
    }

    public <Y> Listof<Pairof<Y, X>> zipCons(Cons<Y> xs) {
        return new Cons<Pairof<Y, X>>(new Pairof<Y, X>(xs.first, this.first),
                xs.rest.zip(this.rest));
    }

    public <Y> Y foldr(BiFunction<X, Y, Y> f, Y b) {
        return f.apply(this.first, this.rest.foldr(f, b));
    }

    public Boolean same(Listof<X> xs) {
        return xs.sameCons(this);
    }

    public Boolean sameCons(Cons<X> xs) {
        return this.first.equals(xs.first) &&
                this.rest.same(xs.rest);
    }

    public Set<X> toset() {
        return this.tosetACC(new EmptySet<>());
    }

    public Set<X> tosetACC(Set<X> now){
        if(now.contains(this.first)) {
            return this.rest.tosetACC(now);
        } else {
            return this.rest.tosetACC(now = now.add(this.first));
        }
    }

    // get the first element if there is one
    public Optional<X> getfirst() {
        return Optional.of(this.first);
    }

    // Turn this list to a set
    public MultiSet<X> toMset() {
        return new ConsMutliSet<>(this.first, this.rest.toMset());
    }
}