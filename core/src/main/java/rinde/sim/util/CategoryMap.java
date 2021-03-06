/**
 * 
 */
package rinde.sim.util;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.newLinkedHashSet;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

/**
 * A category can have multiple values, a value can be in only one category.
 * 
 * Trades memory for cpu speed. Lookups are O(1) on both keys and values.
 * 
 * This datastructure is useful in case you want to regularly look up:
 * <ul>
 * <li>the category of a value</li>
 * <li>all values in a category</li>
 * </ul>
 * 
 * @author Rinde van Lon <rinde.vanlon@cs.kuleuven.be>
 * 
 */
public class CategoryMap<C, V> implements Multimap<C, V> {

    final Multimap<C, V> categoryValueMultiMap;
    final Map<V, C> valueCategoryMap;

    public CategoryMap() {
        valueCategoryMap = createMap();
        categoryValueMultiMap = createMultimap();
    }

    protected Map<V, C> createMap() {
        return newLinkedHashMap();
    }

    protected Multimap<C, V> createMultimap() {
        return LinkedHashMultimap.create();
    }

    @Override
    public int size() {
        return categoryValueMultiMap.size();
    }

    @Override
    public boolean isEmpty() {
        return categoryValueMultiMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return categoryValueMultiMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return valueCategoryMap.containsKey(value);
    }

    @Override
    public boolean containsEntry(Object key, Object value) {
        return valueCategoryMap.containsKey(value)
                && valueCategoryMap.get(value).equals(key);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return categoryValueMultiMap.remove(key, value)
                && valueCategoryMap.remove(value) != null;
    }

    @Override
    public void clear() {
        categoryValueMultiMap.clear();
        valueCategoryMap.clear();
    }

    // TODO
    @Override
    public boolean putAll(C key, Iterable<? extends V> values) {
        throw new UnsupportedOperationException();
    }

    // TODO
    @Override
    public boolean putAll(Multimap<? extends C, ? extends V> multimap) {
        throw new UnsupportedOperationException();
    }

    // TODO
    @Override
    public Collection<V> replaceValues(C key, Iterable<? extends V> values) {
        throw new UnsupportedOperationException();
    }

    // TODO should return an unmodifiable view? currently edits in the returned
    // structure result in an invalid state
    @Override
    public Collection<V> get(C key) {
        return categoryValueMultiMap.get(key);
    }

    public Collection<V> getMultiple(C... keys) {
        final Collection<V> values = newLinkedHashSet();
        for (final C k : keys) {
            for (final V v : categoryValueMultiMap.get(k)) {
                values.add(v);
            }
            // values.addAll(categoryValueMultiMap.get(k));
        }
        return values;
    }

    public C getKeys(V value) {
        return valueCategoryMap.get(value);
    }

    @Override
    public boolean put(C key, V value) {
        // if same value is already contained in this map (possibly in another
        // category), remove it first, then add it in using the specified
        // category
        if (valueCategoryMap.containsKey(value)) {
            categoryValueMultiMap.remove(valueCategoryMap.get(value), value);
        }
        valueCategoryMap.put(value, key);
        return categoryValueMultiMap.put(key, value);
    }

    // TODO
    @Override
    public Collection<V> removeAll(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<C> keySet() {
        return categoryValueMultiMap.keySet();
    }

    @Override
    public Multiset<C> keys() {
        return categoryValueMultiMap.keys();
    }

    @Override
    public Collection<V> values() {
        return valueCategoryMap.keySet();
    }

    @Override
    public Collection<Entry<C, V>> entries() {
        return categoryValueMultiMap.entries();
    }

    @Override
    public Map<C, Collection<V>> asMap() {
        return categoryValueMultiMap.asMap();
    }

    public static <C, V> CategoryMap<C, V> create() {
        return new CategoryMap<C, V>();
    }
}
