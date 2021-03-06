(ns itmo-functional-programming.lab2.hash-set
  (:import (clojure.lang IPersistentSet Counted IPersistentCollection ISeq)
           (java.io Writer))
  (:require [itmo-functional-programming.lab2.hash-map :refer :all]))

(defn list-replicate [num list]
  (vec (mapcat (partial repeat num) list)))

(deftype MyHashSet [impl]
  IPersistentCollection
  (seq [_] (keys impl))
  (cons [_ key] (if (contains? impl key)
                  (MyHashSet. (apply hash-map (list-replicate 2 (keys impl))))
                  (MyHashSet. (assoc impl key key))))
  (empty [_] (MyHashSet. (empty my-hash-map)))
  (equiv [_ o] (if (not (instance? IPersistentSet o))
                 false
                 (if (not= (count o) (count impl))
                   false
                   (empty? (filter #(not (contains? impl %1)) (vec o))))))

  IPersistentSet
  (disjoin [_ key] (if (contains? impl key)
                     (MyHashSet. (dissoc impl key))
                     (MyHashSet. (apply hash-map (list-replicate 2 (keys impl))))))
  (contains [_ key] (contains? impl key))
  (get [_ key] (get impl key))

  ISeq
  (first [_] (first (keys impl)))
  (next [_] (next (keys impl)))
  (more [_] (rest (keys impl)))
  Object
  (toString [_] (str (keys impl)))

  Counted
  (count [_] (count impl)))

(defmethod print-method MyHashSet [s, ^Writer w]
  (.write w (str "#{" s "}")))

(defn my-hash-set
  ([] (MyHashSet. (hash-map)))
  ([& keys] (MyHashSet. (my-hash-map (apply hash-map keys)))))

(def example (my-hash-set 1 1 2 2 3 3 4 4 5 5 6 6 6 7 ))
(def hashset (hash-set 1 1 2 2 3 3 4 4 5 5 6 6 6 7))

(defn- bubble-max-key
  [k coll]
  (let [max (apply max-key k coll)]
    (cons max (remove #(identical? max %) coll))))

(defn intersection
  ([set1] set1)
  ([set1 set2]
   (if (< (count set2) (count set1))
     (recur set2 set1)
     (reduce (fn [result item]
               (if (contains? set2 item)
                 result
                 (disj result item)))
             set1 set1)))
  ([s1 s2 & sets]
   (let [bubbled-sets (bubble-max-key #(- (count %)) (conj sets s2 s1))]
     (reduce intersection (first bubbled-sets) (rest bubbled-sets)))))

(defn union
  ([] #{})
  ([set1] set1)
  ([set1 set2]
   (if (< (count set1) (count set2))
     (reduce conj set2 set1)
     (reduce conj set1 set2)))
  ([s1 s2 & sets]
   (let [bubbled-sets (bubble-max-key count (conj sets s2 s1))]
     (reduce into (first bubbled-sets) (rest bubbled-sets)))))

(defn print-example []
  (println "example: " example)
  (println "hashset: " hashset)
  (println "----------------------------")
  (println "example first: " (first example))
  (println "hashset first: " (first hashset))
  (println "----------------------------")
  (println "example next: " (next example))
  (println "hashset next: " (next hashset))
  (println "----------------------------")
  (println "example more: " (rest example))
  (println "hashset more: " (rest hashset))
  (println "----------------------------")
  (println "example count: " (count example))
  (println "hashset count: " (count hashset))
  (println "----------------------------")
  (println "example get: " (get example 1))
  (println "hashset get: " (get hashset 1))
  (println "----------------------------")
  (println "example contains: " (.contains example 1))
  (println "hashset contains: " (contains? hashset 1))
  (println "----------------------------")
  (println "example empty: " (empty example))
  (println "hashset empty: " (empty hashset))
  (println "----------------------------")
  (println "example seq: " (seq example))
  (println "hashset seq: " (seq hashset))
  (println "----------------------------")
  (println "example cons: " (.cons example 99))
  (println "hashset cons: " (cons hashset (seq '(99))))
  (println "----------------------------")
  (println "example disjoin: " (disj example 1))
  (println "hashset disjoin: " (disj hashset 1)))
