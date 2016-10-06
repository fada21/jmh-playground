import com.sun.istack.internal.NotNull;

import java.util.*;

public class VariantsCruncher {

    public static Map<String, Ordered<Set<Ordered<String>>>> buildVariantsData(@NotNull final List<Variant> variants) {
        Map<String, Ordered<Set<Ordered<String>>>> matrix = new LinkedHashMap<>();
        for (Variant variant : variants) {
            for (Variant.Attribute attribute : variant.attributes) {
                if (matrix.containsKey(attribute.type)) {
                    Set<Ordered<String>> set = matrix.get(attribute.type).content;
                    if (!set.contains(new Ordered<>(0, attribute.value))) {
                        set.add(new Ordered<>(set.size(), attribute.value));
                    }
                } else {
                    Set<Ordered<String>> set = new HashSet<>();
                    set.add(new Ordered<>(0, attribute.value));
                    matrix.put(attribute.type, new Ordered<>(matrix.size(), set));
                }
            }
        }
        return matrix;
    }

    public List<String> getSlice(String dimention) {
        return null;
    }

    public static class Ordered<T> {

        public final int index;
        public final T content;

        public Ordered(int index, T content) {
            this.index = index;
            this.content = content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Ordered<?> ordered = (Ordered<?>) o;

            return content != null ? content.equals(ordered.content) : ordered.content == null;

        }

        @Override
        public int hashCode() {
            return content != null ? content.hashCode() : 0;
        }

        @Override
        public String toString() {
            return String.format("%s[%d]", content.toString(), index);
        }
    }

    public static class Variant {
        int partNumber;
        List<Attribute> attributes;

        public static class Attribute {
            String type;
            String value;
        }

    }

    public static void main(String[] args) {
        List<Variant> variants = new ArrayList<>();
        Variant v;
        Variant.Attribute a;

        v = new Variant();
        v.attributes = new ArrayList<>();
        a = new Variant.Attribute();
        a.type = "Colour";
        a.value = "White";
        v.attributes.add(a);
        a = new Variant.Attribute();
        a.type = "service";
        a.value = "HomeDelivery";
        v.attributes.add(a);
        variants.add(v);

        v = new Variant();
        v.attributes = new ArrayList<>();
        a = new Variant.Attribute();
        a.type = "Colour";
        a.value = "White";
        v.attributes.add(a);
        variants.add(v);

        v = new Variant();
        v.attributes = new ArrayList<>();
        a = new Variant.Attribute();
        a.type = "Colour";
        a.value = "White";
        v.attributes.add(a);
        variants.add(v);

        Map<String, Ordered<Set<Ordered<String>>>> variantsData = buildVariantsData(variants);
        System.out.println(variantsData.toString());
    }
}
