import java.util.List;
import java.util.Map;

/**
 * Contract for variants cruncher
 *
 * @param <T> stored data getType
 * @param <V> getType of variant like data to process
 */
public interface VariantsCruncher<T, V> {

    Map<String, String> getTypeToValueMap(V variant);

    /**
     * @param typeToValueMap current point of slicing
     * @param type variant getValue to slice based on
     * @return map of variant getValue to variant data
     */
    Map<String, T> getDimensionSlice(Map<String, String> typeToValueMap, String type);

    interface VariantAdapter<T, V> {
        VariantsCruncher.Variant convert(V rawVariant);

        T toItem(V rawVariant);
    }

    interface Variant {
        List<? extends VariantsCruncher.Attribute> attributes();
    }

    interface Attribute {
        String getType();

        String getValue();
    }
}
