import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrixVariantsCruncher<T, V> implements VariantsCruncher<T, V> {

    private final VariantAdapter<T, V> adapter;
    private Map<String, Integer> variantTypes;
    private List<Map<String, Integer>> variantValues = new ArrayList<>();
    private Matrix<T> matrix;

    public MatrixVariantsCruncher(final List<V> variants, VariantAdapter<T, V> adapter) {
        this.adapter = adapter;
        buildVariantsData(variants);
    }

    private void buildVariantsData(final List<V> variants) {
        variantTypes = new HashMap<>();
        Map<Map<String, String>, T> coordinationsWithItmes = new HashMap<>();
        Map<String, String> coordinates;
        for (V rawVariant : variants) {
            Variant variant = adapter.convert(rawVariant);
            coordinates = new HashMap<>();
            for (Attribute attribute : variant.attributes()) {
                coordinates.put(attribute.type(), attribute.value());
                if (variantTypes.containsKey(attribute.type())) {
                    Integer typeIndex = variantTypes.get(attribute.type());
                    Map<String, Integer> variantValuesWithIndex = variantValues.get(typeIndex);
                    if (!variantValuesWithIndex.containsKey(attribute.value())) {
                        variantValuesWithIndex.put(attribute.value(), variantValuesWithIndex.size());
                    }
                } else {
                    int currentIndex = variantTypes.size();
                    variantTypes.put(attribute.type(), currentIndex);
                    Map<String, Integer> values = new HashMap<>();
                    values.put(attribute.value(), 0);
                    variantValues.add(currentIndex, values);
                }
            }
            coordinationsWithItmes.put(coordinates, adapter.toItem(rawVariant));
        }
        matrix = new Matrix<>(variantTypes, variantValues, coordinationsWithItmes);
    }

    public boolean insert(Map<String, String> coordinates, T value) {
        return matrix.insert(coordinates, value);
    }

    public T get(Map<String, String> coordinates) {
        return matrix.get(coordinates);
    }

    public boolean remove(Map<String, String> coordinates) {
        return matrix.remove(coordinates);
    }

    public Matrix<T> getMatrix() {
        return matrix;
    }

    @Override
    public Map<String, String> getTypeToValueMap(V rawVariant) {
        Variant variant = adapter.convert(rawVariant);
        Map<String, String> coordinates = new HashMap<>();
        for (Attribute attribute : variant.attributes()) {
            coordinates.put(attribute.type(), attribute.value());
        }
        return coordinates;
    }

    public List<T> getDimensionSlice(Map<String, String> typeToValueMap, String type) {
        return matrix.getDimensionSlice(typeToValueMap, type);
    }

    @Override
    public String toString() {
        return "Types: " + variantTypes.toString() + "\nValues: " + variantValues.toString();
    }

}
