import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Matrix<T> implements Iterable<T> {

    private Map<String, Integer> variantTypes;
    private List<Map<String, Integer>> variantValues;
    private int dimCount;
    private int[] dims;
    private final Object data;
    private int size;

    Matrix(@NotNull final Map<String, Integer> variantTypes,
           @NotNull final List<Map<String, Integer>> variantValues) {
        if (variantTypes.size() == 0 || variantValues.size() == 0) {
            throw new IllegalArgumentException("Dimension size must be positive");
        }
        this.variantTypes = variantTypes;
        this.variantValues = variantValues;
        data = init();
    }

    private Object init() {
        int size = dimCount = variantTypes.size();
        dims = new int[size];
        for (int i = 0, variantValuesSize = variantValues.size(); i < variantValuesSize; i++) {
            Map<String, Integer> variantValue = variantValues.get(i);
            dims[i] = variantValue.size();
        }
        return initDimen(0);
    }

    private Object initDimen(int level) {
        if (level == dimCount - 1) {
            return new Object[dims[level]];
        } else {
            int dimSize = dims[level];
            Object[] dimLevel = new Object[dimSize];
            for (int i = 0, dimLength = dimLevel.length; i < dimLength; i++) {
                dimLevel[i] = initDimen(level + 1);
            }
            return dimLevel;
        }
    }

    public int dimentionCount() {
        return dimCount;
    }

    int capacity() {
        int product = 1;
        for (int dim : dims) {
            product *= dim;
        }
        return product;
    }

    int size() {
        return size;
    }

    float fillRatio() {
        return (float) size / capacity();
    }

    boolean insert(Map<String, String> coordinates, @NotNull T value) {
        if (coordinates.size() != dimCount) return false;
        int[] indices = new int[dimCount];
        for (Map.Entry<String, String> entry : coordinates.entrySet()) {
            Integer typeIndex = variantTypes.get(entry.getKey());
            if (typeIndex == null) return false;
            indices[typeIndex] = variantValues.get(typeIndex).get(entry.getValue());
        }
        return insertByIndices(value, indices);
    }

    boolean remove(Map<String, String> coordinates) {
        if (coordinates.size() != dimCount) return false;
        int[] indices = new int[dimCount];
        for (Map.Entry<String, String> entry : coordinates.entrySet()) {
            Integer typeIndex = variantTypes.get(entry.getKey());
            if (typeIndex == null) return false;
            indices[typeIndex] = variantValues.get(typeIndex).get(entry.getValue());
        }
        return insertByIndices(null, indices);
    }

    private boolean insertByIndices(@Nullable T value, int[] indices) {
        Object tempHolder = data;
        for (int i = 0, indicesLength = indices.length; i < indicesLength; i++) {
            int index = indices[i];
            if (i == indicesLength - 1) {
                boolean doRemove = value == null;
                boolean isCellEmpty = ((Object[]) tempHolder)[index] == null;
                if (isCellEmpty && !doRemove) {
                    size++;
                    ((Object[]) tempHolder)[index] = value;
                    return true;
                } else if (!isCellEmpty && doRemove) {
                    size--;
                    ((Object[]) tempHolder)[index] = value;
                    return true;
                } else {
                    return false;
                }
            } else {
                tempHolder = ((Object[]) tempHolder)[index];
            }
        }
        throw new IllegalStateException("Insert method should never reach that state");
    }

    @Nullable
    public T get(Map<String, String> coordinates) {
        if (coordinates.size() != dimCount) return null;
        int[] indices = new int[dimCount];
        for (Map.Entry<String, String> entry : coordinates.entrySet()) {
            Integer typeIndex = variantTypes.get(entry.getKey());
            if (typeIndex == null) return null;
            indices[typeIndex] = variantValues.get(typeIndex).get(entry.getValue());
        }
        return getByIndices(indices);
    }

    private T getByIndices(int[] indices) {
        Object tempHolder = data;
        for (int i = 0, indicesLength = indices.length; i < indicesLength; i++) {
            int index = indices[i];
            if (i == indicesLength - 1) {
                //noinspection unchecked
                return (T) ((Object[]) tempHolder)[index];
            } else {
                tempHolder = ((Object[]) tempHolder)[index];
            }
        }
        return null;
    }

    public List<T> getDimensionSlice(@NotNull Map<String, String> coordinates, String dimenType) {
        int sliceDimenTypeIndex = 0;
        int[] indices = new int[dimCount];
        for (Map.Entry<String, String> entry : coordinates.entrySet()) {
            String type = entry.getKey();
            Integer typeIndex = variantTypes.get(type);
            if (type.equals(dimenType)) sliceDimenTypeIndex = typeIndex;
            if (typeIndex == null) return null;
            indices[typeIndex] = variantValues.get(typeIndex).get(entry.getValue());
        }

        List<T> itemsDimenSlice = new ArrayList<>();
        for (int i = 0, limit = dims[sliceDimenTypeIndex]; i < limit; i++) {
            indices[sliceDimenTypeIndex] = i;
            T item = getByIndices(indices);
            itemsDimenSlice.add(item);
        }
        return itemsDimenSlice;
    }

    @Override
    public Iterator<T> iterator() {
        return new MatrixIterator();
    }

    private class MatrixIterator implements Iterator<T> {

        int[] indices = new int[dimCount];
        T currItem;

        @Override
        public boolean hasNext() {
            return iterate(true);
        }

        private boolean iterate(boolean justCheck) {
            T item = currItem = getByIndices(indices);
            while (item == null) {
                if (!incrementIndices()) {
                    return false;
                }
                item = getByIndices(indices);
                currItem = item;
            }
            if (justCheck) {
                return true;
            } else {
                return incrementIndices();
            }
        }

        private boolean incrementIndices() {
            return incrementLevel(0);
        }

        private boolean incrementLevel(int level) {
            if (level < dimCount) {
                if (indices[level] + 1 < dims[level]) {
                    indices[level]++;
                    return true;
                } else {
                    indices[level] = 0;
                    return incrementLevel(level + 1);
                }
            } else {
                return false;
            }
        }

        @Override
        public T next() {
            iterate(false);
            return currItem;
        }

        @Override
        public void remove() {
            if (hasNext()) {
                insertByIndices(null, indices);
                incrementIndices();
            }
        }
    }

}
