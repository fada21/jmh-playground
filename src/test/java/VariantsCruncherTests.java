import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class VariantsCruncherTests {

    @Test public void initTest() {
        assertNotNull(new VariantsCruncher(buildSimpleVariantsList()));
        assertNotNull(new VariantsCruncher(buildLongerVariantsList()));
        assertNotNull(new VariantsCruncher(buildExtraLongVariantsList()));
    }

    @Test public void capacityTest() {
        assertThat(new VariantsCruncher(buildSimpleVariantsList()).getMatrix().capacity(), equalTo(1));
        assertThat(new VariantsCruncher(buildLongerVariantsList()).getMatrix().capacity(), equalTo(6));
        assertThat(new VariantsCruncher(buildExtraLongVariantsList()).getMatrix().capacity(), equalTo(7560));
    }

    @Test public void variantTypeCountTest() {
        assertThat(new VariantsCruncher(buildSimpleVariantsList()).getMatrix().dimentionCount(), equalTo(2));
        assertThat(new VariantsCruncher(buildLongerVariantsList()).getMatrix().dimentionCount(), equalTo(2));
        assertThat(new VariantsCruncher(buildExtraLongVariantsList()).getMatrix().dimentionCount(), equalTo(6));
    }

    @Test public void insertTest_small() {
        VariantsCruncher<String> variantsCruncher = new VariantsCruncher<>(buildSimpleVariantsList());
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(1));
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
        VariantsCruncher<String> variantsCruncher = new VariantsCruncher<>(buildLongerVariantsList());
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(6));
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
        VariantsCruncher<String> variantsCruncher = new VariantsCruncher<>(buildExtraLongVariantsList());
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(7560));
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

    @Test public void fillTest_large() {
        VariantsCruncher<String> variantsCruncher = new VariantsCruncher<>(buildExtraLongVariantsList());
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(7560));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(0));

        for (Map.Entry<Map<String, String>, String> entry : fillCoordinatesWithItems().entrySet()) {
            variantsCruncher.insert(entry.getKey(), entry.getValue());
        }

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
        VariantsCruncher<String> variantsCruncher = new VariantsCruncher<>(buildExtraLongVariantsList());
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(7560));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(0));

        for (Map.Entry<Map<String, String>, String> entry : fillCoordinatesWithItems().entrySet()) {
            variantsCruncher.insert(entry.getKey(), entry.getValue());
        }

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
        VariantsCruncher<String> variantsCruncher = new VariantsCruncher<>(buildExtraLongVariantsList());
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(7560));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(0));

        for (Map.Entry<Map<String, String>, String> entry : fillCoordinatesWithItems().entrySet()) {
            variantsCruncher.insert(entry.getKey(), entry.getValue());
        }

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
        VariantsCruncher<String> variantsCruncher = new VariantsCruncher<>(buildSimpleVariantsList());
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(1));
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


    @Test public void insertTest2() {
        VariantsCruncher<String> variantsCruncher = new VariantsCruncher<>(buildSimpleVariantsList());
        assertThat(variantsCruncher.getMatrix().capacity(), equalTo(1));
        assertThat(variantsCruncher.getMatrix().size(), equalTo(0));

        String colour = "Colour";
        String delivery = "service";
        Map<String, String> coordinates = new HashMap<>();
        coordinates.put(colour, "White");
        coordinates.put(delivery, "HomeDelivery");
        variantsCruncher.insert(coordinates, "Expected");

        assertThat(variantsCruncher.getMatrix().size(), equalTo(1));
        assertThat(variantsCruncher.getMatrix().iterator().next(), equalTo("Expected"));
        assertThat(variantsCruncher.getMatrix().fillRatio(), equalTo(1f));
    }

    private static List<VariantsCruncher.Variant> buildSimpleVariantsList() {
        List<VariantsCruncher.Variant> variants = new ArrayList<>();
        VariantsCruncher.Variant v;

        v = new VariantsCruncher.Variant();
        v.partNumber = 1;
        addVariantAttribute(v, "Colour", "White");
        addVariantAttribute(v, "service", "HomeDelivery");
        variants.add(v);

        v = new VariantsCruncher.Variant();
        addVariantAttribute(v, "Colour", "White");
        variants.add(v);

        v = new VariantsCruncher.Variant();
        addVariantAttribute(v, "Colour", "White");
        variants.add(v);
        return variants;
    }

    private static List<VariantsCruncher.Variant> buildLongerVariantsList() {
        List<VariantsCruncher.Variant> variants = new ArrayList<>();
        VariantsCruncher.Variant v;

        v = new VariantsCruncher.Variant();
        v.partNumber = 1;
        addVariantAttribute(v, "Colour", "White");
        addVariantAttribute(v, "service", "HomeDelivery");
        variants.add(v);

        v = new VariantsCruncher.Variant();
        v.partNumber = 2;
        addVariantAttribute(v, "Colour", "White");
        addVariantAttribute(v, "service", "Installation");
        variants.add(v);

        v = new VariantsCruncher.Variant();
        v.partNumber = 3;
        addVariantAttribute(v, "Colour", "Black");
        addVariantAttribute(v, "service", "HomeDelivery");
        variants.add(v);

        v = new VariantsCruncher.Variant();
        v.partNumber = 4;
        addVariantAttribute(v, "Colour", "Black");
        addVariantAttribute(v, "service", "Installation");
        variants.add(v);

        v = new VariantsCruncher.Variant();
        v.partNumber = 5;
        addVariantAttribute(v, "Colour", "Red");
        addVariantAttribute(v, "service", "HomeDelivery");
        variants.add(v);

        v = new VariantsCruncher.Variant();
        v.partNumber = 6;
        addVariantAttribute(v, "Colour", "Red");
        addVariantAttribute(v, "service", "Installation");
        variants.add(v);
        return variants;
    }

    private static List<VariantsCruncher.Variant> buildExtraLongVariantsList() {
        List<VariantsCruncher.Variant> variants = new ArrayList<>();
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
                                VariantsCruncher.Variant variant = new VariantsCruncher.Variant();
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

    private static Map<Map<String, String>, String> fillCoordinatesWithItems() {
        Map<Map<String, String>, String> coordinationsWithItmes = new HashMap<>();
        List<VariantsCruncher.Variant> variants = new ArrayList<>();
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
                                Map<String, String> coords = new HashMap<>(6);
                                coords.put(colour, colourVal);
                                coords.put(size, sizeVal);
                                coords.put(delivery, deliveryVal);
                                coords.put(pattern, patternVal);
                                coords.put(quality, qualityVal);
                                coords.put(type, typeVal);
                                StringBuilder sb = new StringBuilder();
                                sb.append(colourVal).append("_");
                                sb.append(sizeVal).append("_");
                                sb.append(deliveryVal).append("_");
                                sb.append(patternVal).append("_");
                                sb.append(qualityVal).append("_");
                                sb.append(typeVal);
                                coordinationsWithItmes.put(coords, sb.toString());
                            }
                        }
                    }
                }
            }
        }
        return coordinationsWithItmes;
    }

    private static void addVariantAttribute(VariantsCruncher.Variant v, String type, String value) {
        if (v.attributes == null) v.attributes = new ArrayList<>();
        VariantsCruncher.Variant.Attribute a = new VariantsCruncher.Variant.Attribute();
        a.type = type;
        a.value = value;
        v.attributes.add(a);
    }
}
