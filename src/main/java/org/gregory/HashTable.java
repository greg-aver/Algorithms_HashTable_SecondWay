package org.gregory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.*;

public class HashTable
{
    public int size;
    public int step;
    public String [] slots;

    public HashTable(int sz, int stp)
    {
        size = sz;
        step = stp;
        slots = new String[size];
        for(int i=0; i<size; i++) slots[i] = null;
    }

    public int hashFun(String value)
    {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        int sum = IntStream.range(0, bytes.length).map(x -> bytes[x]).sum();
        return sum % getSize();
    }

    public int seekSlot(String value)
    {
        int slot = hashFun(value);

        for (int i=0; i< getStep(); i++) {
            while (slot < getSize()) {
                if (getSlots()[slot] == null
                        || value.equals(getSlots()[slot])) {
                    return slot;
                }
                slot += getStep();
            }
            slot = slot - getSize();
        }
        return -1;
    }

    public int put(String value)
    {
        int slot = seekSlot(value);

        if (slot != -1) {
            getSlots()[slot] = value;
            return slot;
        }
        return -1;
    }

    public int find(String value)
    {
        int slot = hashFun(value);

        for (int i=0; i< getStep(); i++) {
            slot +=i;
            while (slot < getSize()) {
                if (getSlots()[slot].equals(value)) {
                    return slot;
                }
                slot += getStep();
            }
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashTable hashTable = (HashTable) o;

        if (size != hashTable.size) return false;
        if (step != hashTable.step) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(slots, hashTable.slots);
    }

    public int getSize() {
        return size;
    }

    public int getStep() {
        return step;
    }

    public String[] getSlots() {
        return slots;
    }
}
