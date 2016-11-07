import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariantsCruncher<T> {

    private Map<String, Integer> variantTypes;
    private List<Map<String, Integer>> variantValues = new ArrayList<>();
    private Matrix<T> matrix;

    public VariantsCruncher(@NotNull final List<Variant> variants) {
        buildVariantsData(variants);
    }

    private void buildVariantsData(@NotNull final List<Variant> variants) {
        variantTypes = new HashMap<>();
        for (Variant variant : variants) {
            for (Variant.Attribute attribute : variant.attributes) {
                if (variantTypes.containsKey(attribute.type)) {
                    Integer typeIndex = variantTypes.get(attribute.type);
                    Map<String, Integer> variantValuesWithIndex = variantValues.get(typeIndex);
                    if (!variantValuesWithIndex.containsKey(attribute.value)) {
                        variantValuesWithIndex.put(attribute.value, variantValuesWithIndex.size());
                    }
                } else {
                    int currentIndex = variantTypes.size();
                    variantTypes.put(attribute.type, currentIndex);
                    Map<String, Integer> values = new HashMap<>();
                    values.put(attribute.value, 0);
                    variantValues.add(currentIndex, values);
                }
            }
        }
        matrix = new Matrix<>(variantTypes, variantValues);
    }

    public boolean insert(@NotNull Map<String, String> coordinates, @NotNull T value) {
        return matrix.insert(coordinates, value);
    }

    public T get(@NotNull Map<String, String> coordinates) {
        return matrix.get(coordinates);
    }

    public boolean remove(@NotNull Map<String, String> coordinates) {
        return matrix.remove(coordinates);
    }

    public int getVariantTypeCount() {
        return matrix.dimentionCount();
    }

    @NotNull public Matrix<T> getMatrix() {
        return matrix;
    }

    @NotNull public List<T> getDimensionSlice(@NotNull Map<String, String> coordinates, String type) {
        return matrix.getDimensionSlice(coordinates, type);
    }

    public static class Variant {
        int partNumber;
        List<Attribute> attributes;

        public static class Attribute {
            String type;
            String value;
        }
    }

    @Override
    public String toString() {
        return "Types: " + variantTypes.toString() + "\nValues: " + variantValues.toString();
    }

}
