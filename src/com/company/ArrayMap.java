package com.company;

import java.util.*;

public class ArrayMap<K, V> extends AbstractMap<K, V> {

    static class ArrayMapEntry<K, V> implements Map.Entry<K, V>{
        K key;
        V value;
        public ArrayMapEntry(K key, V value){
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey(){
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        @Override
        public String toString(){
            return key + " " + value;
        }

        @Override
        public boolean equals(Object o) {
            if (getClass() != o.getClass())
                return false;
            ArrayMapEntry obj = (ArrayMapEntry) o;
            return (this.getKey() == null ? obj.getKey() == null : this.getKey().equals(obj.getKey())) && (this.getValue() == null ? obj.getValue() == null : this.getValue().equals(obj.getValue()));
        }

        @Override
        public int hashCode(){
            return super.hashCode();
        }
    }


    private Vector<Entry<K, V>> entry;

    public ArrayMap(){
            entry = new Vector<>();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        HashSet<Entry<K, V>> set = new HashSet<>();
        set.addAll(entry);
        return set;
    }

    public int size(){
        return entry.size();
    }

    public V put(K key, V value){
        int ok = 0;
        int i;
        for( i = 0; i < entry.size(); i++){
            if(entry.get(i).getKey().equals(key)){
                ok = 1;
                break;
            }
        }
        if(ok == 0)
            entry.add(new ArrayMapEntry(key, value));
        else{
                value = entry.get(i).setValue(value);
        }

        return value;
    }

    public V get(Object key){
        K c = (K) key;
        int i;
        for(i = 0; i < entry.size(); i++){
            if(entry.get(i).getKey().equals(c))
                break;
        }
        return entry.get(i).getValue();
    }

    public boolean containsKey(Object key){
        K c = (K) key;
        int i;
        for(i = 0; i < entry.size(); i++){
            if(entry.get(i).getKey().equals(c))
                return true;
        }

        return false;
    }

}

