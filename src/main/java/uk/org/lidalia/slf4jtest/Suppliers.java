package uk.org.lidalia.slf4jtest;

import com.google.common.base.Supplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


final class Suppliers
{

    static <T> Supplier<List<T>> makeEmptyMutableList()
    {
        return () -> 
        {
            return new ArrayList<>();
        };
    }


    static <K, V> Supplier<Map<K, V>> makeEmptyMutableMap()
    {
        return () -> 
        {
            return new HashMap<K, V>();
        };
    }


    private Suppliers()
    {
        throw new UnsupportedOperationException("Not instantiable");
    }
}
