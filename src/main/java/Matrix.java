import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Matrix<T> implements Iterable<T> {

    private Map<String, Integer> variantTypes;
    private List<Map<String, Integer>> variantValues;
    private int dimCount;
    private int[] dims;
    private final Object data;

    public Matrix(@NotNull final Map<String, Integer> variantTypes,
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

    public int[] dimentionSizes() {
        return dims;
    }

    public int dimentionCount() {
        return dimCount;
    }

    public int capacity() {
        int product = 1;
        for (int dim : dims) {
            product *= dim;
        }
        return product;
    }

    public void insert(Map<String, String> coordinates, T value) {
        int[] indices = new int[dimCount];
        for (String type : coordinates.keySet()) {
            Integer typeIndex = variantTypes.get(type);
            indices[typeIndex] = variantValues.get(typeIndex).get(coordinates.get(type));
        }
        insetrByIndices(value, indices);
    }

    private void insetrByIndices(T value, int[] indices) {
        Object tempHolder = data;
        for (int i = 0, indicesLength = indices.length; i < indicesLength; i++) {
            int index = indices[i];
            if (i == indicesLength - 1) {
                ((Object[]) tempHolder)[index] = value;
            } else {
                tempHolder = ((Object[]) tempHolder)[index];
            }
        }
    }

    @Nullable
    public T get(Map<String, String> coordinates) {
        int[] indices = new int[dimCount];
        for (String type : coordinates.keySet()) {
            Integer typeIndex = variantTypes.get(type);
            indices[typeIndex] = variantValues.get(typeIndex).get(coordinates.get(type));
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
            T item = getByIndices(indices);
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
                insetrByIndices(null, indices);
                incrementIndices();
            }
        }
    }

    public static void main(String[] args) {

    }

}
