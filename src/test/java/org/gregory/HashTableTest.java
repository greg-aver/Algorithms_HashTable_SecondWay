package org.gregory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class HashTableTest {
    HashTable hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable(19, 3);
    }

    @AfterEach
    void tearDown() {
        hashTable = null;
    }

    @Test
    void hashFun1Element() {
        int hash = hashTable.hashFun("a");
        assertThat(hash, is("a".getBytes(StandardCharsets.UTF_8)[0] % hashTable.getSize()));
        assertThat(hash, is(97 % hashTable.getSize()));
        assertThat(hash, is(2));
    }

    @Test
    void hashFun2Elements() {
        int hash = hashTable.hashFun("ab");
        assertThat(hash, is((97 + 98) % hashTable.getSize()));
        assertThat(hash, is(5));
    }


    @Test
    void seekSlotNotCollisions1Element() {
        assertThat(hashTable.seekSlot("a"), is(2));
    }

    @Test
    void seekSlotNotCollisions2Element() {
        assertThat(hashTable.seekSlot("a"), is(2));
        assertThat(hashTable.seekSlot("ab"), is(5));
    }

    @Test
    void seekSlotCollisions2Element() {
        assertThat(hashTable.put("a"), is(2));
        assertThat(hashTable.put("a"), is(2 + hashTable.getStep()));
        assertThat(hashTable.seekSlot("a"), is(2 + 2 * hashTable.getStep()));
    }

    @Test
    void putCollisions2Element() {
        assertThat(hashTable.put("a"), is(2));
        assertThat(hashTable.put("a"), is(2 + hashTable.getStep()));
        assertThat(hashTable.put("a"), is(2 + 2 * hashTable.getStep()));
    }

    @Test
    void putNotCollisions2Element() {
        assertThat(hashTable.put("a"), is(2));
        assertThat(hashTable.put("ab"), is(5));

        String[] arr = new String[19];
        arr[2] = "a";
        arr[5] = "ab";
        assertThat(hashTable.getSlots(), is(arr));
    }

    @Test
    void putCollisions() {
        for (int i = 0; i < hashTable.getSize(); i++) {
            hashTable.put("a");
        }
        assertThat(hashTable.put("a"), is(-1));
        assertThat(hashTable.seekSlot("a"), is(-1));
        assertThat(hashTable.put("ab"), is(-1));
        assertThat(hashTable.seekSlot("ab"), is(-1));
    }

    @Test
    void findNoElement() {
        for (int i = 0; i < hashTable.getSize(); i++) {
            hashTable.put("a");
        }
        assertThat(hashTable.find("ab"), is(-1));
        assertThat(hashTable.find("a"), is(2));
        assertThat(hashTable.find("abc"), is(-1));
    }

    @Test
    void find() {
        assertThat(hashTable.put("a"), is(2));
        assertThat(hashTable.find("a"), is(2));
        assertThat(hashTable.put("a"), is(2 + hashTable.getStep()));
        assertThat(hashTable.find("a"), is(2));
    }

    //same values - no collision

    @Test
    void seekSlots() {
        assertThat(hashTable.seekSlot("ab"), is(5));
        assertThat(hashTable.put("ab"), is(5));
        assertThat(hashTable.seekSlot("ab"), is(5));
        assertThat(hashTable.put("ab"), is(5));
        assertThat(hashTable.hashFun("ba"), is(5));
        assertThat(hashTable.seekSlot("ba"), is(8));
        assertThat(hashTable.put("ba"), is(8));
    }
}