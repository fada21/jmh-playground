import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Matrix<T> {

    private final Object[] data;

    public Matrix(int... dimensionSizes) {
        checkIfNonZeroSizes(dimensionSizes);
        if (dimensionSizes == null || dimensionSizes.length == 0) {
            data = null;
        } else {
            data = fillDimention(dimensionSizes, 0);
        }
    }

    private void checkIfNonZeroSizes(int[] dimensionSizes) {
        for (int dimensionSize : dimensionSizes) {
            if (dimensionSize <= 0) throw new IllegalArgumentException("Dimension size must be positive");
        }
    }


    private Object[] fillDimention(int[] dimensionSizes, int level) {
        Object[] dim = new Object[dimensionSizes[level]];
        if (dimensionSizes.length - 1 < level) {
            for (int i = 0; i < dim.length; i++) {
                dim[i] = fillDimention(dimensionSizes, level + 1);
            }
        }
        return dim;
    }

    public List<Integer> dimentions() {
        if (data == null) return Collections.emptyList();
        List<Integer> dims = new ArrayList<>();
        dimentions(dims, data);
        return dims;
    }

    private List<Integer> dimentions(List<Integer> dims, Object[] dim) {
            Object[] dimArray = (Object[]) dim;
            dims.add(dimArray.length);
            return dimentions(dims, dimArray[0]);
    }

    private List<Integer> dimentions(List<Integer> dims, Object dim) {
        return dims;
    }

    public int capacity() {
        int capacity = 1;
        for (Integer dimSize : dimentions()) {
            capacity *= dimSize;
        }
        return capacity;
    }

    public static void main(String[] args) {
        int capacity = new Matrix<String>(1, 2, 3).capacity();
        System.out.println(capacity);
    }
}
