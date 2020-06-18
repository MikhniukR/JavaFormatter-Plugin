package ru.ifmo.genericsbasics;

class HashTable<K, V> {
  private int size;
  private Entry<K, V>[] arr;
  private int capacity;
  private double loadFactor;

  HashTable(int initialCapacity) {
    this(initialCapacity, 0.5);
  }

  HashTable(int initialCapacity, double loadFactor) {
    this.loadFactor = loadFactor;
    this.capacity = initialCapacity;
    this.arr = new Entry[initialCapacity];
    this.size = 0;
  }

  V put(K key, V value) {
    optimizeTable();
    int index = getIndex(key);
    if (arr[index] == null) {
      index = getFirstCleanPairIndex(key);
    }
    V prevValue = getValueUsingIndex(index);
    size += (getKeyUsingIndex(index) == null ? 1 : 0);
    arr[index] = new Entry<>(key, value);
    return prevValue;
  }

  V get(K key) {
    return getValueUsingIndex(getIndex(key));
  }

  V remove(K key) {
    optimizeTable();
    int index = getIndex(key);
    if (arr[index] == null) {
      return null;
    }
    V prevValue = getValueUsingIndex(index);
    size -= (arr[index].getKey() == null ? 0 : 1);
    arr[index] = new Entry<>(null, null);
    return prevValue;
  }

  int size() {
    return size;
  }

  private int getHashIndex(K key) {
    return (key.hashCode() % capacity + capacity) % capacity;
  }

  private int getNextHashIndex(int index) {
    return (index + 12347) % capacity;
  }

  private void optimizeTable() {
    if (size != (int) (loadFactor * capacity)) {
      return;
    }
    Entry[] oldArr = this.arr;
    this.arr = new Entry[capacity * 2];
    this.capacity = capacity * 2;
    this.size = 0;
    for (Entry<K, V> entry : oldArr) {
      if (entry != null && entry.getKey() != null) {
        put(entry.getKey(), entry.getValue());
      }
    }
  }

  private int getIndex(K key) {
    int index = getHashIndex(key);
    while (arr[index] != null
        && (arr[index].getKey() == null || !arr[index].getKey().equals(key))) {
      index = getNextHashIndex(index);
    }
    return index;
  }

  private int getFirstCleanPairIndex(K key) {
    int index = getHashIndex(key);
    while (arr[index] != null && arr[index].getKey() != null) {
      index = getNextHashIndex(index);
    }
    return index;
  }

  private K getKeyUsingIndex(int index) {
    return (arr[index] == null ? null : arr[index].getKey());
  }

  private V getValueUsingIndex(int index) {
    return (arr[index] == null ? null : arr[index].getValue());
  }

  private static class Entry<K, V> {
    private K key;
    private V value;

    Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }

    K getKey() {
      return key;
    }

    V getValue() {
      return value;
    }
  }
}
