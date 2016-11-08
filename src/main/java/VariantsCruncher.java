import java.util.List;
import java.util.Map;

/**
 * Contract for variants cruncher
 *
 * @param <T> stored data type
 * @param <V> type of variant like data to process
 */
public interface VariantsCruncher<T, V> {

    Map<String, String> getTypeToValueMap(V variant);

    /**
     * @param typeToValueMap current point of slicing
     * @param type variant value to slice based on
     * @return map of variant value to variant data
     */
    Map<String, T> getDimensionSlice(Map<String, String> typeToValueMap, String type);

    interface VariantAdapter<T, V> {
        VariantsCruncher.Variant convert(V rawVariant);

        T toItem(V rawVariant);
    }

    interface Variant {
        List<? extends MatrixVariantsCruncher.Attribute> attributes();
    }

    interface Attribute {
        String type();

        String value();
    }
}
