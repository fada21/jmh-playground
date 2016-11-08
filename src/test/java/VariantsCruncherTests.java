import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class VariantsCruncherTests {

    MatrixVariantsCruncher.VariantAdapter<String, Variant> adapter = new MatrixVariantsCruncher.VariantAdapter<String, Variant>() {
        @Override public MatrixVariantsCruncher.Variant convert(Variant rawVariant) {
            return () -> rawVariant.attributes;
        }

        @Override public String toItem(Variant rawVariant) {
            return rawVariant.ref;
        }
    };

    @Test public void initTest() {
        assertNotNull(new MatrixVariantsCruncher<>(buildSimpleVariantsList(), adapter));
        assertNotNull(new MatrixVariantsCruncher<>(buildLongerVariantsList(), adapter));
        assertNotNull(new MatrixVariantsCruncher<>(buildExtraLongVariantsList(), adapter));
    }

    @Test public void capacityTest() {
        assertThat(new MatrixVariantsCruncher<>(buildSimpleVariantsList(), adapter).getMatrix().capacity(), equalTo(1));
        assertThat(new MatrixVariantsCruncher<>(buildLongerVariantsList(), adapter).getMatrix().capacity(), equalTo(6));
        assertThat(new MatrixVariantsCruncher<>(buildExtraLongVariantsList(), adapter).getMatrix().capacity(), equalTo(7560));
    }

    @Test public void variantTypeCountTest() {
        assertThat(new MatrixVariantsCruncher<>(buildSimpleVariantsList(), adapter).getMatrix().dimentionCount(), equalTo(2));
        assertThat(new MatrixVariantsCruncher<>(buildLongerVariantsList(), adapter).getMatrix().dimentionCount(), equalTo(2));
        assertThat(new MatrixVariantsCruncher<>(buildExtraLongVariantsList(), adapter).getMatrix().dimentionCount(), equalTo(6));
    }

    @Test public void insertTest_small() {
        MatrixVariantsCruncher<String, Variant> variantsCruncher = new MatrixVariantsCruncher<>(buildSimpleVariantsList(), adapter);
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(1));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(1));
        clearMatrix(variantsCruncher);
        assertThat(variantsCruncher.getMatrix().size(), equalTo(0));

        String colour = "Colour";
        String delivery = "service";
        Map<String, String> coordinates = new HashMap<>();
        coordinates.put(colour, "White");
        coordinates.put(delivery, "HomeDelivery");
        assertTrue(variantsCruncher.insert(coordinates, "Expected"));
        assertFalse(variantsCruncher.insert(coordinates, "Not expected"));

        assertThat(variantsCruncher.getMatrix().size(), equalTo(1));
        assertThat(variantsCruncher.getMatrix().iterator().next(), equalTo("Expected"));
        assertThat(variantsCruncher.getMatrix().fillRatio(), equalTo(1f));
    }

    @Test public void insertTest_medium() {
        MatrixVariantsCruncher<String, Variant> variantsCruncher = new MatrixVariantsCruncher<>(buildLongerVariantsList(), adapter);
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(6));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(6));
        clearMatrix(variantsCruncher);
        assertThat(variantsCruncher.getMatrix().size(), equalTo(0));


        String colour = "Colour";
        String delivery = "service";
        Map<String, String> coordinates = new HashMap<>();
        coordinates.put(colour, "Red");
        coordinates.put(delivery, "HomeDelivery");
        assertTrue(variantsCruncher.insert(coordinates, "Expected"));
        assertFalse(variantsCruncher.insert(coordinates, "Not expected"));

        assertThat(variantsCruncher.getMatrix().size(), equalTo(1));
        assertThat(variantsCruncher.getMatrix().iterator().next(), equalTo("Expected"));
        assertThat(variantsCruncher.getMatrix().fillRatio(), equalTo(1f / 6f));
    }


    @Test public void insertTest_large() {
        MatrixVariantsCruncher<String, Variant> variantsCruncher = new MatrixVariantsCruncher<>(buildExtraLongVariantsList(), adapter);
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(7560));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(7560));
        clearMatrix(variantsCruncher);
        assertThat(variantsCruncher.getMatrix().size(), equalTo(0));

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
        variantsCruncher.insert(coordinates, "Expected");

        assertThat(variantsCruncher.getMatrix().size(), equalTo(1));
        assertThat(variantsCruncher.getMatrix().iterator().next(), equalTo("Expected"));
    }

    private void clearMatrix(MatrixVariantsCruncher<String, Variant> variantsCruncher) {
        Iterator<String> iterator = variantsCruncher.getMatrix().iterator();
        while (iterator.hasNext()) {
            iterator.remove();
        }
    }

    @Test public void fillTest_large() {
        MatrixVariantsCruncher<String, Variant> variantsCruncher = new MatrixVariantsCruncher<>(buildExtraLongVariantsList(), adapter);
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(7560));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(7560));

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

        assertEquals(variantsCruncher.get(coordinates), "Green_S_Installation_XX_Premium_B");
        assertThat(variantsCruncher.getMatrix().size(), equalTo(7560));
        assertThat(variantsCruncher.getMatrix().fillRatio(), equalTo(1f));
        assertTrue(variantsCruncher.remove(coordinates));
        assertNull(variantsCruncher.get(coordinates));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(7559));
    }

    @Test public void sliceTest_large() {
        MatrixVariantsCruncher<String, Variant> variantsCruncher = new MatrixVariantsCruncher<>(buildExtraLongVariantsList(), adapter);
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(7560));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(7560));

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

        assertEquals("Green_S_Installation_XX_Premium_B", variantsCruncher.get(coordinates));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(7560));
        assertThat(variantsCruncher.getMatrix().fillRatio(), equalTo(1f));
        assertTrue(variantsCruncher.remove(coordinates));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(7559));
        assertNull(variantsCruncher.get(coordinates));
        assertEquals(variantsCruncher.getDimensionSlice(coordinates, size).size(), 7);
        assertThat(variantsCruncher.getDimensionSlice(coordinates, size), containsInAnyOrder(
                "Green_XS_Installation_XX_Premium_B",
                "Green_M_Installation_XX_Premium_B",
                "Green_L_Installation_XX_Premium_B",
                "Green_XL_Installation_XX_Premium_B",
                "Green_XXL_Installation_XX_Premium_B",
                "Green_XXXL_Installation_XX_Premium_B",
                null));
        assertThat(variantsCruncher.getDimensionSlice(coordinates, type), containsInAnyOrder(
                "Green_S_Installation_XX_Premium_A",
                "Green_S_Installation_XX_Premium_C",
                "Green_S_Installation_XX_Premium_D",
                "Green_S_Installation_XX_Premium_E",
                "Green_S_Installation_XX_Premium_F",
                "Green_S_Installation_XX_Premium_G",
                "Green_S_Installation_XX_Premium_H",
                "Green_S_Installation_XX_Premium_I",
                "Green_S_Installation_XX_Premium_J",
                null));
        coordinates.put(colour, "Red");
        coordinates.put(pattern, "OO");
        coordinates.put(type, "J");
        assertThat(variantsCruncher.getDimensionSlice(coordinates, delivery), containsInAnyOrder(
                "Red_S_Standard_OO_Premium_J",
                "Red_S_Installation_OO_Premium_J",
                "Red_S_Fasttrack_OO_Premium_J"));
    }

    @Test public void testThatCoordiantesMustBeWellFormed() {
        MatrixVariantsCruncher<String, Variant> variantsCruncher = new MatrixVariantsCruncher<>(buildExtraLongVariantsList(), adapter);
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(7560));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(7560));


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

        assertEquals(variantsCruncher.get(coordinates), "Green_S_Installation_XX_Premium_B");
        coordinates.remove(colour);
        assertNull(variantsCruncher.get(coordinates));
        assertFalse(variantsCruncher.insert(coordinates, "Anything"));
        assertFalse(variantsCruncher.remove(coordinates));
        coordinates.put(colour, "Green");
        coordinates.put("Trendiness", "Hot");
        assertNull(variantsCruncher.get(coordinates));
        assertFalse(variantsCruncher.insert(coordinates, "Anything"));
        assertFalse(variantsCruncher.remove(coordinates));
        coordinates = new HashMap<>();
        assertNull(variantsCruncher.get(coordinates));
        assertFalse(variantsCruncher.insert(coordinates, "Anything"));
        assertFalse(variantsCruncher.remove(coordinates));


    }


    @Test public void removeTest() {
        MatrixVariantsCruncher<String, Variant> variantsCruncher = new MatrixVariantsCruncher<>(buildSimpleVariantsList(), adapter);
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(1));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(1));
        clearMatrix(variantsCruncher);
        assertThat(variantsCruncher.getMatrix().size(), equalTo(0));


        String colour = "Colour";
        String delivery = "service";
        Map<String, String> coordinates = new HashMap<>();
        coordinates.put(colour, "White");
        coordinates.put(delivery, "HomeDelivery");
        assertTrue(variantsCruncher.insert(coordinates, "Expected"));
        assertFalse(variantsCruncher.insert(coordinates, "Not expected"));

        assertThat(variantsCruncher.getMatrix().size(), equalTo(1));
        assertThat(variantsCruncher.getMatrix().iterator().next(), equalTo("Expected"));
        assertThat(variantsCruncher.getMatrix().fillRatio(), equalTo(1f));

        assertTrue(variantsCruncher.remove(coordinates));
        assertFalse(variantsCruncher.remove(coordinates));

        assertThat(variantsCruncher.getMatrix().size(), equalTo(0));
        assertThat(variantsCruncher.getMatrix().iterator().next(), nullValue());
        assertThat(variantsCruncher.getMatrix().fillRatio(), equalTo(0f));
    }

    @Test public void testGetTypeToValueMap() {
        String colour = "Colour";
        String size = "Size";
        String delivery = "Delivery";
        String pattern = "Pattern";
        String quality = "Quality";
        String type = "Type";
        Map<String, String> coordinates = new HashMap<>();
        String colourVal = "Green";
        coordinates.put(colour, colourVal);
        String sizeVal = "S";
        coordinates.put(size, sizeVal);
        String deliveryVal = "Installation";
        coordinates.put(delivery, deliveryVal);
        String patternVal = "XX";
        coordinates.put(pattern, patternVal);
        String qualityVal = "Premium";
        coordinates.put(quality, qualityVal);
        String typeVal = "B";
        coordinates.put(type, typeVal);

        Variant variant = new Variant();
        addVariantAttribute(variant, colour, colourVal);
        addVariantAttribute(variant, size, sizeVal);
        addVariantAttribute(variant, delivery, deliveryVal);
        addVariantAttribute(variant, pattern, patternVal);
        addVariantAttribute(variant, quality, qualityVal);
        addVariantAttribute(variant, type, typeVal);
        StringBuilder sb = new StringBuilder();
        sb.append(colourVal).append("_");
        sb.append(sizeVal).append("_");
        sb.append(deliveryVal).append("_");
        sb.append(patternVal).append("_");
        sb.append(qualityVal).append("_");
        sb.append(typeVal);
        variant.ref = sb.toString();

        MatrixVariantsCruncher<String, Variant> variantsCruncher = new MatrixVariantsCruncher<>(buildExtraLongVariantsList(), adapter);
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(7560));
        clearMatrix(variantsCruncher);
        Map<String, String> typeToValueMap = variantsCruncher.getTypeToValueMap(variant);
        assertEquals(coordinates, typeToValueMap);
        variantsCruncher.insert(coordinates, "Green_S_Installation_XX_Premium_B");
        assertEquals(variantsCruncher.get(typeToValueMap), "Green_S_Installation_XX_Premium_B");
        assertThat(variantsCruncher.getMatrix().size(), equalTo(1));
    }

    private static List<Variant> buildSimpleVariantsList() {
        List<Variant> variants = new ArrayList<>();
        Variant v;

        v = new Variant();
        addVariantAttribute(v, "Colour", "White");
        addVariantAttribute(v, "service", "HomeDelivery");
        v.ref = "White_HomeDelivery";
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
        addVariantAttribute(v, "Colour", "White");
        addVariantAttribute(v, "service", "HomeDelivery");
        v.ref = "White_HomeDelivery";
        variants.add(v);

        v = new Variant();
        addVariantAttribute(v, "Colour", "White");
        addVariantAttribute(v, "service", "Installation");
        v.ref = "White_Installation";
        variants.add(v);

        v = new Variant();
        addVariantAttribute(v, "Colour", "Black");
        addVariantAttribute(v, "service", "HomeDelivery");
        v.ref = "Black_HomeDelivery";
        variants.add(v);

        v = new Variant();
        addVariantAttribute(v, "Colour", "Black");
        addVariantAttribute(v, "service", "Installation");
        v.ref = "Black_Installation";
        variants.add(v);

        v = new Variant();
        addVariantAttribute(v, "Colour", "Red");
        addVariantAttribute(v, "service", "HomeDelivery");
        v.ref = "Red_HomeDelivery";
        variants.add(v);

        v = new Variant();
        addVariantAttribute(v, "Colour", "Red");
        addVariantAttribute(v, "service", "Installation");
        v.ref = "Red_Installation";
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
                                StringBuilder sb = new StringBuilder();
                                sb.append(colourVal).append("_");
                                sb.append(sizeVal).append("_");
                                sb.append(deliveryVal).append("_");
                                sb.append(patternVal).append("_");
                                sb.append(qualityVal).append("_");
                                sb.append(typeVal);
                                variant.ref = sb.toString();
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
