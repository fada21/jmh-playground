import com.sun.istack.internal.NotNull;

import java.util.*;

public class VariantsCruncher {

    Map<String, Ordered<Set<Ordered<String>>>> metaData;

    public VariantsCruncher(@NotNull final List<Variant> variants) {
        metaData = buildVariantsData(variants);
    }

    private Map<String, Ordered<Set<Ordered<String>>>> buildVariantsData(@NotNull final List<Variant> variants) {
        Map<String, Ordered<Set<Ordered<String>>>> matrix = new LinkedHashMap<>();
        for (Variant variant : variants) {
            for (Variant.Attribute attribute : variant.attributes) {
                if (matrix.containsKey(attribute.type)) {
                    Set<Ordered<String>> set = matrix.get(attribute.type).content;
                    if (!set.contains(new Ordered<>(0, attribute.value))) {
                        set.add(new Ordered<>(set.size(), attribute.value));
                    }
                } else {
                    Set<Ordered<String>> set = new LinkedHashSet<>();
                    set.add(new Ordered<>(0, attribute.value));
                    matrix.put(attribute.type, new Ordered<>(matrix.size(), set));
                }
            }
        }
        return matrix;
    }

    public List<String> getSlice(String type) {
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

    @Override
    public String toString() {
        return metaData.toString();
    }

    public static void main(String[] args) {
        List<Variant> variants = buildExtraLongVariantsList();

        VariantsCruncher variantsCruncher = new VariantsCruncher(variants);

        System.out.println(variantsCruncher);
    }

    private static List<Variant> buildSimpleVariantsList() {
        List<Variant> variants = new ArrayList<>();
        Variant v;

        v = new Variant();
        v.partNumber = 1;
        addVariantAttribute(v, "Colour", "White");
        addVariantAttribute(v, "service", "HomeDelivery");
        variants.add(v);

        v = new Variant();
        addVariantAttribute(v, "Colour", "White");
        variants.add(v);

        v = new Variant();
        addVariantAttribute(v, "Colour", "White");
        variants.add(v);
        return variants;
    }

    private static List<Variant> buildLongerVariantsList() {
        List<Variant> variants = new ArrayList<>();
        Variant v;

        v = new Variant();
        v.partNumber = 1;
        addVariantAttribute(v, "Colour", "White");
        addVariantAttribute(v, "service", "HomeDelivery");
        variants.add(v);

        v = new Variant();
        v.partNumber = 2;
        addVariantAttribute(v, "Colour", "White");
        addVariantAttribute(v, "service", "Installation");
        variants.add(v);

        v = new Variant();
        v.partNumber = 3;
        addVariantAttribute(v, "Colour", "Black");
        addVariantAttribute(v, "service", "HomeDelivery");
        variants.add(v);

        v = new Variant();
        v.partNumber = 4;
        addVariantAttribute(v, "Colour", "Black");
        addVariantAttribute(v, "service", "Installation");
        variants.add(v);

        v = new Variant();
        v.partNumber = 5;
        addVariantAttribute(v, "Colour", "Red");
        addVariantAttribute(v, "service", "HomeDelivery");
        variants.add(v);

        v = new Variant();
        v.partNumber = 6;
        addVariantAttribute(v, "Colour", "Red");
        addVariantAttribute(v, "service", "Installation");
        variants.add(v);
        return variants;
    }

    private static List<Variant> buildExtraLongVariantsList() {
        List<Variant> variants = new ArrayList<>();
        Map<String, List<String>> variantsMap = new HashMap<>();
        String colour = "Colour";
        String size = "Size";
        String delivery = "Delivery";
        String pattern = "Pattern";
        variantsMap.put(colour, Arrays.asList("Red", "Green", "Blue"));
        variantsMap.put(size, Arrays.asList("XS", "S", "M", "L", "XL", "XXL", "XXXL"));
        variantsMap.put(delivery, Arrays.asList("Standard", "Installation", "Fasttrack"));
        variantsMap.put(pattern, Arrays.asList("WW", "XX", "OO", "NN"));
        for (String colourVal : variantsMap.get(colour)) {
            for (String sizeVal : variantsMap.get(size)) {
                for (String deliveryVal : variantsMap.get(delivery)) {
                    for (String patternVal : variantsMap.get(pattern)) {
                        Variant variant = new Variant();
                        addVariantAttribute(variant, colour, colourVal);
                        addVariantAttribute(variant, size, sizeVal);
                        addVariantAttribute(variant, delivery, deliveryVal);
                        addVariantAttribute(variant, pattern, patternVal);
                        variants.add(variant);
                    }
                }
            }
        }
        return variants;
    }

    private static void addVariantAttribute(Variant v, String type, String value) {
        if (v.attributes == null) v.attributes = new ArrayList<>();
        Variant.Attribute a = new Variant.Attribute();
        a.type = type;
        a.value = value;
        v.attributes.add(a);
    }
}
