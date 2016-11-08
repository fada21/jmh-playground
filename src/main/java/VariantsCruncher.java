import com.sun.istack.internal.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Contract for variants cruncher
 * @param <T> stored data type
 */
public interface VariantsCruncher<T, V> {

    @NotNull Map<String, String> getTypeToValueMap(@NotNull V variant);

    @NotNull List<T> getDimensionSlice(@NotNull Map<String, String> typeToValueMap, String type);

    interface VariantAdapter<T, V> {
        MatrixVariantsCruncher.Variant convert(V rawVariant);
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
