import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MatrixTests {

    @Test
    public void testInit() {
        new Matrix<Void>();
        new Matrix<Void>(1);
        new Matrix<Void>(2);
        new Matrix<Void>(1, 2);
        new Matrix<Void>(1, 2, 3);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testThatInitWithZeroDimensionSizeWillThrow() {
        new Matrix<Void>(1, 0, 1);
    }

    @Test
    public void testCapacity() {
        assertThat(new Matrix<Void>(1, 1, 1).capacity(), equalTo(1));
        assertThat(new Matrix<Void>(1, 2, 1).capacity(), equalTo(2));
        assertThat(new Matrix<Void>(1, 2, 2).capacity(), equalTo(4));
        assertThat(new Matrix<Void>(2, 1, 2).capacity(), equalTo(4));
        assertThat(new Matrix<Void>(2, 1, 1).capacity(), equalTo(4));
        assertThat(new Matrix<Void>(2, 2, 2).capacity(), equalTo(8));
    }
}
