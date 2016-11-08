import java.util.List;

public class Variant {
    String ref;
    List<Attribute> attributes;

    public static class Attribute implements MatrixVariantsCruncher.Attribute {
        String type;
        String value;

        @Override public String type() {
            return type;
        }

        @Override public String value() {
            return value;
        }
    }
}