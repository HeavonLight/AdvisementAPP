package com.example.heavon.interfaceClasses;

import java.util.Map;

/**
 * Created by heavon on 2017/2/15.
 */

public interface Filter<T> {
    void setFilter(Map<String, T> filter);
    void put(String k, T v);
    void delete(String k);
    Map<String, T> getFilter();
}
