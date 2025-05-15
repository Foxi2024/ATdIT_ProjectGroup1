package com.atdit.booking.frontend.abstract_controller;

/**
 * The Cacheable interface defines methods for caching and restoring data.
 * Classes that implement this interface can provide functionality to
 * temporarily store data in a cache and restore it when needed.
 */
public interface Cacheable {

    /**
     * Stores the current data in a cache.
     * Implementing classes should define how the data is cached
     * and what specific data needs to be stored.
     */
    void cacheData();

    /**
     * Restores previously cached data.
     * Implementing classes should define how to retrieve and restore
     * the data from the cache.
     */
    void restoreData();
}