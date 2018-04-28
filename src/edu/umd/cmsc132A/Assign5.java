// Authors: zytux526
// Assignment 5

package edu.umd.cmsc132A;

import javassist.compiler.ast.Pair;
import tester.Tester;

public class Assign5 {

}

// Write test cases here, run "Test" configuration to test
class Tests {

    // A few sets to help write tests
    Set<Integer> mti = ASet.empty();
    Set<Integer> s1 = mti.add(1).add(2).add(3);
    Set<Integer> s2 = mti.add(3).add(2).add(1);
    Set<Integer> super1 = mti.add(1).add(2).add(3).add(5);
    Set<Integer> Notsuper1 = mti.add(1);

    MultiSet<Integer> mlti = new EmptyMultiSet<Integer>();
    MultiSet<Integer> ms1 = mlti.add(1).add(2).add(3);
    MultiSet<Integer> ms2 = mlti.add(3).add(2).add(1);
    MultiSet<Integer> ms3 = mlti.add(1).add(1).add(2).add(3);
    MultiSet<Integer> superms3 = mlti.add(1).add(1).add(2).add(3);


    Map<String, Integer> mtMap = new EmptyMap<String, Integer>();
    Map<Integer, String> mmap = new EmptyMap<>();
    Pairof<String, Integer> Key1 = new Pairof<String, Integer>("A", 1);
    Map<String, Integer> map1 = mtMap.add("A", 1);
    Map<String, Integer> map2 = mtMap.add("A", 1).add("B", 2).add("C", 3);
    Map<String, Integer> map3 = mtMap.add("C", 3).add("B", 2).add("A", 1);
    Map<Integer, String> m1 = mmap.add(1, "one").add(2, "two").add(3, "three");
    Map<String, Integer> map4 = mtMap.add("A", 2);
    Map<Integer, String> sing = mmap.add(1, "eins");

    BST<Integer> Leaf = new Leaf<>();
    BST<Integer> BST1 = new Node<>(3, new Node<>(2, Leaf, Leaf), new Node<>(4, Leaf, Leaf));
    BST<Integer> BST2 = new Node<>(5, new Node<>(3, new Node<>(2, Leaf, Leaf), new Node<>(4, Leaf, Leaf)), new Node<>(7, Leaf, Leaf));
    BST<Integer> BST3 = new Node<>(3, Leaf, new Node<>(4, Leaf, Leaf));
    BST<Integer> BST4 = new Node<>(5, new Node<>(2, Leaf, new Node<>(4, Leaf, Leaf)), new Node<>(7, Leaf, Leaf));
    Node<Integer> BST5 = new Node<>(5, new Node<>(2, Leaf, new Node<>(4, Leaf, Leaf)), new Node<>(7, Leaf, Leaf));
    Comparable<Integer> C1 = -3;

    BST<MapBST.KVPairof<Integer, String>> NewLeaf = new Leaf<>();
    MapBST.KVPairof<Integer, String> KVP3 = new MapBST.KVPairof<>(3, "three");
    MapBST.KVPairof<Integer, String> KVP2 = new MapBST.KVPairof<>(2, "two");
    MapBST.KVPairof<Integer, String> KVP4 = new MapBST.KVPairof<>(4, "four");
    MapBST.KVPairof<Integer, String> KVP3PRIME = new MapBST.KVPairof<>(3, "RPIME");
    BST<MapBST.KVPairof<Integer, String>> BST6 = new Node<MapBST.KVPairof<Integer, String>>(KVP3, new Node<MapBST.KVPairof<Integer, String>>(KVP2, NewLeaf, NewLeaf),
            new Node<MapBST.KVPairof<Integer, String>>(KVP4, NewLeaf, NewLeaf));
    BST<MapBST.KVPairof<Integer, String>> BST7 = new Node<MapBST.KVPairof<Integer, String>>(KVP3, NewLeaf,
            new Node<MapBST.KVPairof<Integer, String>>(KVP4, NewLeaf, NewLeaf));
    BST<MapBST.KVPairof<Integer, String>> BST8 = new Node<MapBST.KVPairof<Integer, String>>(KVP3PRIME, NewLeaf,
            new Node<MapBST.KVPairof<Integer, String>>(KVP4, NewLeaf, NewLeaf));



    MapComp<Integer, String> MC1 = new MapBST<Integer, String>(BST6);
    MapComp<Integer, String> MC2 = new MapBST<Integer, String>(BST7);
    MapComp<Integer, String> MC3 = new MapBST<Integer, String>(BST8);

    void testAdd(Tester t) {
        t.checkExpect(MC2.add(2, "two"), MC1);
    }

    void testSame4MAPCOMP(Tester t) {
        t.checkExpect(MC1.same(MC2), false);
        t.checkExpect(MC2.add(2, "two"), MC1);
    }

    void testJoin(Tester t) {
        t.checkExpect(MC3.join(MC2), MC3);
        t.checkExpect(MC2.join(MC1).same(MC1), true);
    }


    void testRemove(Tester t) {
        t.checkExpect(BST1.remove(2), BST3);
        t.checkExpect(BST2.remove(3), BST4);
    }

    void testbiggest(Tester t) {
        t.checkExpect(BST1.biggest().get(), 4);
        t.checkExpect(BST4.biggest().get(), 7);
        t.checkExpect(BST5.left.biggest().get(), 4);
        t.checkExpect(new Node<Integer>(2, Leaf, Leaf).biggest().get(), 2);
    }

    void testBSTMAP(Tester t) {
        t.checkExpect(BST1.map(i -> i * -1), new Node<>(-3, new Node<>(-4, Leaf, Leaf), new Node<>(-2, Leaf, Leaf)));
        t.checkExpect(BST1.map(i -> i * 5), new Node<>(15, new Node<>(10, Leaf, Leaf), new Node<>(20, Leaf, Leaf)));
    }

    void testCompareTO(Tester t) {
        t.checkExpect(BST1.getValue().get().compareTo(3) == 0, true);
        t.checkExpect(C1.compareTo(-2) < 0 , true);
    }

    void testtoSortedList(Tester t) {
        t.checkExpect(BST1.toSortedList(), new Cons<>(2, new Cons<>(3, new Cons<>(4, new Empty<>()))));
        t.checkExpect(BST2.toSortedList(), new Cons<>(2, new Cons<>(3, new Cons<>(4, new Cons<>(5, new Cons<>(7, new Empty<>()))))));
    }


    Boolean testMapAdd(Tester t) {
        return t.checkExpect(mtMap.add("A", 1), map1) &&
                t.checkExpect(map1.add("B", 2).add("C", 3), map2);
    }

    Boolean testMapCount(Tester t) {
        return t.checkExpect(mtMap.count(), 0) &&
                t.checkExpect(map1.count(), 1);
    }

    Boolean testMapLookup(Tester t) {
        return t.checkExpect(map1.lookup("A").isPresent(), true) &&
                t.checkExpect(map1.lookup("B").isPresent(), false) &&
                t.checkExpect(map1.lookup("A").get(), 1);
    }

    Boolean testKeyVals(Tester t) {
        return t.checkExpect(mtMap.keyVals(), new EmptySet<Pairof<String, Integer>>()) &&
                t.checkExpect(map1.keyVals(), new ConsSet<Pairof<String, Integer>>(new Pairof<String, Integer>("A", 1), new EmptySet<>()));

    }

    Boolean testKeys(Tester t) {
        return t.checkExpect(mtMap.keys(), new EmptySet<String>()) &&
                t.checkExpect(map1.keys(), new ConsSet<String>("A", new EmptySet<String>()));
    }

    Boolean testvals(Tester t) {
        return t.checkExpect(mtMap.vals(), new Empty<Integer>()) &&
                t.checkExpect(map1.vals(), new Cons<Integer>(1, new Empty<Integer>()));
    }

    Boolean testSameMap(Tester t) {
        return t.checkExpect(mtMap.same(map1), false) &&
                t.checkExpect(map2.same(map3), true) &&
                t.checkExpect(map3.same(map2), true);
    }

    Boolean testRemoveMap(Tester t) {
        return t.checkExpect(map1.remove("A"), mtMap) &&
                t.checkExpect(map2.remove("B").remove("C"), map1);
    }

    Boolean testRemoveAll(Tester t) {
        return t.checkExpect(mtMap.removeAll(i -> i.equals("A")), mtMap) &&
                t.checkExpect(map1.removeAll(i -> i.equals("A")), mtMap) &&
                t.checkExpect(map1.removeAll(i -> i.equals("B")), map1);
    }

    Boolean testRemoveAllParis(Tester t) {
        return t.checkExpect(mtMap.removeAllPairs((k, v) -> v >= 0), mtMap) &&
                t.checkExpect(map1.removeAllPairs((k, v) -> v.equals(1)), mtMap) &&
                t.checkExpect(map2.removeAllPairs((k, v) -> v > 1), map1);
    }

    void testJoin1(Tester t) {
        t.checkExpect(map1.join(map4), map1);
        t.checkExpect(map4.join(map2).same(mtMap.add("A", 2).add("B", 2).add("C", 3)), true);
        t.checkExpect(m1.join(m1).count().equals(3), true);
        t.checkExpect(m1.join(sing).count().equals(3), true);
        t.checkExpect(sing.join(m1).count().equals(3), true);
        t.checkExpect(m1.join(sing).lookup(1).get().equals("one"), true);
        t.checkExpect(sing.join(m1).lookup(1).get().equals("eins"), true);
        t.checkExpect(m1.join(sing).lookup(2).get().equals("two"), true);
    }


    Boolean testSame(Tester t) {
        return t.checkExpect(s1.same(s2), true) &&
                t.checkExpect(s2.same(s1), true) &&
                t.checkExpect(s1.add(1).same(s2), true) &&
                t.checkExpect(s1.add(4).same(s2), false) &&
                t.checkExpect(s2.same(s1.add(4)), false) &&
                t.checkExpect(ms1.add(1).same(ms3), true) &&
                t.checkExpect(ms1.same(ms2), true) &&
                t.checkExpect(ms1.add(1).same(ms2), false);
    }

    Boolean testContains(Tester t) {
        return t.checkExpect(s1.contains(1), true) &&
                t.checkExpect(s1.contains(2), true) &&
                t.checkExpect(s1.contains(3), true) &&
                t.checkExpect(s1.contains(4), false) &&
                t.checkExpect(ms1.contains(1), true) &&
                t.checkExpect(ms1.contains(2), true) &&
                t.checkExpect(ms1.contains(3), true) &&
                t.checkExpect(ms1.contains(4), false) &&


                t.checkExpect(mti.contains(0), false) &&
                t.checkExpect(s1.contains(1), true) &&
                t.checkExpect(s1.contains(2), true) &&
                t.checkExpect(s1.contains(3), true) &&
                t.checkExpect(s1.contains(4), false) &&
                t.checkExpect(s1.add(4).contains(4), true);

    }

    Boolean testMap(Tester t) {
        Set<Integer> s3 = mti.add(2).add(3).add(4);
        MultiSet<Integer> ms4 = mlti.add(2).add(3).add(4);
        return t.checkExpect(mti.map(x -> x + 1).same(mti), true) &&
                t.checkExpect(s1.map(x -> x + 1).same(s3), true) &&
                t.checkExpect(s1.map(x -> 0), new ConsSet<Integer>(0, new EmptySet<Integer>())) &&
                t.checkExpect(s1.map(x -> 0).same(mti.add(0)), true) &&
                t.checkExpect(mlti.map(x -> x + 1).same(mlti), true) &&
                t.checkExpect(ms1.map(x -> x + 1).same(ms4), true);
    }

    Boolean testElem(Tester t) {
        return t.checkExpect(mti.elem().isPresent(), false) &&
                t.checkExpect(s1.elem().isPresent(), true) &&
                t.checkExpect(s1.elem().filter(i -> 0 <= i && i <= 3)
                        .isPresent(), true) &&
                t.checkExpect(mti.add(1).elem().get(), 1) &&
                t.checkExpect(mlti.elem().isPresent(), false) &&
                t.checkExpect(ms1.elem().isPresent(), true) &&
                t.checkExpect(ms1.elem().filter(i -> 0 <= i && i <= 3).isPresent(), true) &&
                t.checkExpect(mlti.add(1).elem().get(), 1);
    }

    Boolean testChoose(Tester t) {
        return t.checkExpect(mti.choose().isPresent(), false) &&
                t.checkExpect(s1.choose().isPresent(), true) &&
                t.checkExpect(s1.choose().filter(i -> 0 <= i.left && i.left <= 3).isPresent(), true) &&
                t.checkExpect(mti.add(1).choose().get(), new Pairof<Integer, Set<Integer>>(1, new EmptySet<Integer>())) &&
                t.checkExpect(mlti.choose().isPresent(), false) &&
                t.checkExpect(ms1.choose().isPresent(), true) &&
                t.checkExpect(ms1.choose().filter(i -> 0 <= i.left && i.left <= 3).isPresent(), true) &&
                t.checkExpect(mlti.add(1).choose().get(), new Pairof<Integer, MultiSet<Integer>>(1,
                        new EmptyMultiSet<Integer>()));
    }

    Boolean testExists(Tester t) {
        return t.checkExpect(mti.exists(x -> true), false) &&
                t.checkExpect(s1.exists(x -> x.equals(2)), true) &&
                t.checkExpect(s1.exists(x -> x > 3), false) &&
                t.checkExpect(mlti.exists(x -> true), false) &&
                t.checkExpect(ms1.exists(x -> x.equals(2)), true) &&
                t.checkExpect(ms1.exists(x -> x > 3), false) &&

                t.checkExpect(mti.exists(x -> true), false) &&
                t.checkExpect(s1.exists(x -> true), true) &&
                t.checkExpect(s1.exists(x -> false), false) &&
                t.checkExpect(s1.exists(x -> x.equals(2)), true);
    }

    Boolean testForAll(Tester t) {
        return
                t.checkExpect(s1.forAll(x -> x >= 0), true) &&
                t.checkExpect(s1.forAll(x -> x > 2), false) &&
                t.checkExpect(mti.toMultiSet().forAll(i -> false), true) &&


                t.checkExpect(mti.forAll(x -> false), true) &&
                t.checkExpect(s1.forAll(x -> false), false) &&
                t.checkExpect(s1.forAll(x -> x.equals(2)), false) &&
                t.checkExpect(s1.forAll(x -> x > 0), true);
    }

    Boolean testToList(Tester t) {
        return t.checkExpect(mti.toList(), new Empty<Integer>()) &&
                t.checkExpect(s1.toList(), new Cons<Integer>(3, new Cons<Integer>(2,
                        new Cons<Integer>(1, new Empty<Integer>())))) &&
                t.checkExpect(ms3.toList(), new Cons<Integer>(3, new Cons<Integer>(2,
                        new Cons<Integer>(1, new Cons<Integer>(1, new Empty<Integer>())))));
    }

    Boolean testToMulti(Tester t) {
        return t.checkExpect(s1.toMultiSet(), ms1) &&
                t.checkExpect(s2.toMultiSet(), ms2);
    }

    Boolean testToSet(Tester t) {
        return t.checkExpect(ms1.toSet(), s1) &&
                t.checkExpect(ms2.toSet(), s2) &&
                t.checkExpect(ms3.toSet(), s1);
    }

    Boolean testsuperSet(Tester t) {
        return t.checkExpect(super1.superset(s1), true) &&
                t.checkExpect(Notsuper1.superset(s1), false) &&
                t.checkExpect(s1.superset(s2), true) &&
                t.checkExpect(s2.superset(s1), true) &&
                t.checkExpect(superms3.superset(ms3), true) &&
                t.checkExpect(ms3.superset(ms1), true);
    }

    Boolean testSubSet(Tester t) {
        return t.checkExpect(s1.subset(super1), true) &&
                t.checkExpect(s1.subset(Notsuper1), false) &&
                t.checkExpect(ms1.add(1).subset(ms1), false) &&
                t.checkExpect(ms1.subset(ms1.add(1)), true);
    }

    // You do not need to add more tests for lists unless you add list methods
    Boolean testLists(Tester t) {
        Listof<Integer> mti = AListof.empty();
        Listof<Integer> i1 = mti.cons(1).cons(2).cons(3); // [3,2,1]
        Listof<Integer> i2 = mti.cons(2).cons(3).cons(4); // [4,3,2]
        Listof<Pairof<Integer, Integer>> mtp = AListof.empty();
        Listof<Pairof<Integer, Integer>> i1i2 = mtp
                .cons(new Pairof<>(1, 2))
                .cons(new Pairof<>(2, 3))
                .cons(new Pairof<>(3, 4));

        return t.checkExpect(i1.length(), 3) &&
                t.checkExpect(i1.map(i -> i + 1), i2) &&
                t.checkExpect(i1.foldr((i, s) -> s + i, 3), 9) &&
                t.checkExpect(mti.append(i1), i1) &&
                t.checkExpect(i1.append(mti), i1) &&
                t.checkExpect(i1.same(i1), true) &&
                t.checkExpect(i1.same(i2), false) &&
                t.checkExpect(i2.same(i1), false) &&
                t.checkExpect(i1.zip(mti), mtp) &&
                t.checkExpect(mti.zip(i1), mtp) &&
                t.checkExpect(i1.zip(i2), i1i2);
    }

}
