import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.List;
import java.util.Map;

public class Matrix<T> {

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
        switch (size) {
            case 1:
                return new Object[dims[0]];
            case 2:
                return new Object[dims[0]][dims[1]];
            case 3:
                return new Object[dims[0]][dims[1]][dims[2]];
            case 4:
                return new Object[dims[0]][dims[1]][dims[2]][dims[3]];
            case 5:
                return new Object[dims[0]][dims[1]][dims[2]][dims[3]][dims[4]];
            case 6:
                return new Object[dims[0]][dims[1]][dims[2]][dims[3]][dims[4]][dims[5]];
            default:
                throw new IllegalStateException("Matrix must be of no more than 6 dimentions");
        }
    }

    public int[] dimentions() {
        return dims;
    }

    public int dimentionsCount() {
        return dimCount;
    }

    public int capacity() {
        int product = 1;
        for (int dim : dims) {
            product *= dim;
        }
        return product;
    }

    @Nullable
    public T get(Map<String, String> coordinates) {
        int[] indices = new int[dimCount];
        for (String type : coordinates.keySet()) {
            Integer typeIndex = variantTypes.get(type);
            indices[typeIndex] = variantValues.get(typeIndex).get(coordinates.get(type));
        }
        Object tempHolder = data;
        for (int i = 0, indicesLength = indices.length; i < indicesLength; i++) {
            int index = indices[i];
            if (i == indicesLength - 1) {
                return (T) ((Object[]) tempHolder)[index];
            } else {
                tempHolder = ((Object[]) tempHolder)[index];
            }
        }
        return null;
    }

    public void insert(Map<String, String> coordinates, T value) {
        int[] indices = new int[dimCount];
        for (String type : coordinates.keySet()) {
            Integer typeIndex = variantTypes.get(type);
            indices[typeIndex] = variantValues.get(typeIndex).get(coordinates.get(type));
        }
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

    public static void main(String[] args) {

    }
}
