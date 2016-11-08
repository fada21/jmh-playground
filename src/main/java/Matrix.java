import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Matrix<T> implements Iterable<T> {
    /**
     * Dimension getType with index of list of dimention values stored
     */
    private Map<String, Integer> dimensionTypes;
    /**
     * Dimension values with index of list of dimention values stored
     */
    private List<Map<String, Integer>> dimensionValues;
    private int dimCount;
    private int[] dims;
    private final Object data;
    private int size;

    Matrix(final Map<String, Integer> dimensionTypes,
           final List<Map<String, Integer>> dimensionValues,
           Map<Map<String, String>, T> coordinationsWithItmes) {
        if (dimensionTypes.size() == 0 || dimensionValues.size() == 0) {
            throw new IllegalArgumentException("Dimension size must be positive");
        }
        this.dimensionTypes = dimensionTypes;
        this.dimensionValues = dimensionValues;
        data = init();
        for (Map.Entry<Map<String, String>, T> entry : coordinationsWithItmes.entrySet()) {
            insert(entry.getKey(), entry.getValue());
        }
    }

    private Object init() {
        int size = dimCount = dimensionTypes.size();
        dims = new int[size];
        for (int i = 0, variantValuesSize = dimensionValues.size(); i < variantValuesSize; i++) {
            Map<String, Integer> variantValue = dimensionValues.get(i);
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

    public int getDimentionSize(String type) {
        Integer index = dimensionTypes.get(type);
        int i = index == null ? 0 : index;
        return dimensionValues.get(i).size();
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

    boolean insert(Map<String, String> coordinates, T value) {
        if (coordinates.size() != dimCount) return false;
        int[] indices = new int[dimCount];
        for (Map.Entry<String, String> entry : coordinates.entrySet()) {
            Integer typeIndex = dimensionTypes.get(entry.getKey());
            if (typeIndex == null) return false;
            indices[typeIndex] = dimensionValues.get(typeIndex).get(entry.getValue());
        }
        return insertByIndices(value, indices);
    }

    boolean remove(Map<String, String> coordinates) {
        if (coordinates.size() != dimCount) return false;
        int[] indices = new int[dimCount];
        for (Map.Entry<String, String> entry : coordinates.entrySet()) {
            Integer typeIndex = dimensionTypes.get(entry.getKey());
            if (typeIndex == null) return false;
            indices[typeIndex] = dimensionValues.get(typeIndex).get(entry.getValue());
        }
        return insertByIndices(null, indices);
    }

    private boolean insertByIndices(T value, int[] indices) {
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

    public T get(Map<String, String> coordinates) {
        if (coordinates.size() != dimCount) return null;
        int[] indices = new int[dimCount];
        for (Map.Entry<String, String> entry : coordinates.entrySet()) {
            Integer typeIndex = dimensionTypes.get(entry.getKey());
            if (typeIndex == null) return null;
            indices[typeIndex] = dimensionValues.get(typeIndex).get(entry.getValue());
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

    public Map<String, T> getDimensionSlice(Map<String, String> coordinates, String dimenType) {
        int sliceDimenTypeIndex = 0;
        int[] indices = new int[dimCount];
        for (Map.Entry<String, String> entry : coordinates.entrySet()) {
            String type = entry.getKey();
            Integer typeIndex = dimensionTypes.get(type);
            if (type.equals(dimenType)) sliceDimenTypeIndex = typeIndex;
            if (typeIndex == null) return null;
            indices[typeIndex] = dimensionValues.get(typeIndex).get(entry.getValue());
        }
        Map<String, T> itemsDimenSlice = new LinkedHashMap<>();
        Map<String, Integer> valuesWithIndex = dimensionValues.get(dimensionTypes.get(dimenType));
        for (Map.Entry<String, Integer> entry : valuesWithIndex.entrySet()) {
            indices[sliceDimenTypeIndex] = entry.getValue();
            T item = getByIndices(indices);
            itemsDimenSlice.put(entry.getKey(), item);
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
