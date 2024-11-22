package zerobase.weather;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherApplicationTests {

	@Test
	void equalTest () {
	    //given
		assertEquals(1, 1);
	    //when
	    //then
	}

	@Test
	void nullTest() {
	    //given
		assertNull(null);
	    //when
	    //then
	}

	@Test
	void trueTest() {
	    //given
		assertTrue(1==1);
	    //when
	    //then
	}

}
