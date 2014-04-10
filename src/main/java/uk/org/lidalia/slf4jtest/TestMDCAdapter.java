package uk.org.lidalia.slf4jtest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.spi.MDCAdapter;

import uk.org.lidalia.lang.ThreadLocal;


public class TestMDCAdapter implements MDCAdapter
{

    private final ThreadLocal<Map<String, String>> value = new ThreadLocal<>(Suppliers
            .<String, String> makeEmptyMutableMap());


    @Override
    public void put(final String key,
                    final String val)
    {
        value.get().put(key, Optional.ofNullable(val).orElse("null"));
    }


    @Override
    public String get(final String key)
    {
        return value.get().get(key);
    }


    @Override
    public void remove(final String key)
    {
        value.get().remove(key);
    }


    @Override
    public void clear()
    {
        value.get().clear();
    }


    @Override
    public Map<String, String> getCopyOfContextMap()
    {
        return Collections.unmodifiableMap(new HashMap<String, String>(value.get()));
    }


    @SuppressWarnings("unchecked")
    @Override
    public void setContextMap(@SuppressWarnings("rawtypes") final Map contextMap)
    {
        value.set(new HashMap<String, String>(contextMap));
    }
}
