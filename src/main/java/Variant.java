import java.util.List;

public class Variant {
    String ref;
    List<Attribute> attributes;

    public static class Attribute implements VariantsCruncher.Attribute {
        String type;
        String value;

        @Override public String getType() {
            return type;
        }

        @Override public String getValue() {
            return value;
        }
    }
}