import com.sun.istack.internal.NotNull;

import java.util.*;

public class VariantsCruncher {

    private Map<String, Integer> variantTypes;
    private List<Map<String, Integer>> variantValues = new ArrayList<>();
    Matrix<String> matrix;

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

    public List<String> getSlice(String type) {
        return null;
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
        return "Types: " + variantTypes.toString() + "  & values: \n" + variantValues.toString();
    }

    public static void main(String[] args) {
        List<Variant> variants = buildExtraLongVariantsList();

        VariantsCruncher variantsCruncher = new VariantsCruncher(variants);
        System.out.println("Capacity: " + variantsCruncher.matrix.capacity());
        System.out.println(variantsCruncher);


        String colour = "Colour";
        String size = "Size";
        String delivery = "Delivery";
        String pattern = "Pattern";
        String quality = "Quality";
        String type = "Type";
        Map<String, String> coordinates = new HashMap<>();
        coordinates.put(colour, "Green");
        coordinates.put(size, "S");
        coordinates.put(delivery, "Installation");
        coordinates.put(pattern, "XX");
        coordinates.put(quality, "Premium");
        coordinates.put(type, "B");
        variantsCruncher.matrix.insert(coordinates, "Elem test");
        System.out.println(variantsCruncher.matrix.get(coordinates));

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
        String quality = "Quality";
        String type = "Type";
        variantsMap.put(colour, Arrays.asList("Red", "Green", "Blue"));
        variantsMap.put(size, Arrays.asList("XS", "S", "M", "L", "XL", "XXL", "XXXL"));
        variantsMap.put(delivery, Arrays.asList("Standard", "Installation", "Fasttrack"));
        variantsMap.put(pattern, Arrays.asList("WW", "XX", "OO", "NN"));
        variantsMap.put(quality, Arrays.asList("Value", "Premium", "Best"));
        variantsMap.put(type, Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"));
        for (String colourVal : variantsMap.get(colour)) {
            for (String sizeVal : variantsMap.get(size)) {
                for (String deliveryVal : variantsMap.get(delivery)) {
                    for (String patternVal : variantsMap.get(pattern)) {
                        for (String qualityVal : variantsMap.get(quality)) {
                            for (String typeVal : variantsMap.get(type)) {
                                Variant variant = new Variant();
                                addVariantAttribute(variant, colour, colourVal);
                                addVariantAttribute(variant, size, sizeVal);
                                addVariantAttribute(variant, delivery, deliveryVal);
                                addVariantAttribute(variant, pattern, patternVal);
                                addVariantAttribute(variant, quality, qualityVal);
                                addVariantAttribute(variant, type, typeVal);
                                variants.add(variant);
                            }
                        }
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
