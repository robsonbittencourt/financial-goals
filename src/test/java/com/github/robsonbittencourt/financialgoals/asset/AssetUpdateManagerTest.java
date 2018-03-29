package com.github.robsonbittencourt.financialgoals.asset;

import static com.github.robsonbittencourt.financialgoals.util.BigDecimalMatcher.equalsBigDecimal;
import static java.math.BigDecimal.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

public class AssetUpdateManagerTest {
	
	private AssetUpdateManager manager;
	
	@Before
	public void setUp() {
		manager = new AssetUpdateManager();
	}
	
	@Test
	public void shouldUpdateValuesOfAsset() {
		manager.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(20));
		
		assertThat(valueOf(120), is(equalsBigDecimal(manager.getLastUpdate().getGrossValue())));
		assertThat(valueOf(10), is(equalsBigDecimal(manager.getLastUpdate().getRates())));
		assertThat(valueOf(20), is(equalsBigDecimal(manager.getLastUpdate().getTaxes())));
	}
	
	@Test
	public void shouldUpdateValuesOfAssetKeepingHistory() {
		manager.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.now(), valueOf(140), valueOf(10), valueOf(30));
		
		assertThat(valueOf(120), is(equalsBigDecimal(manager.getUpdates().get(0).getGrossValue())));
		assertThat(valueOf(10), is(equalsBigDecimal(manager.getUpdates().get(0).getRates())));
		assertThat(valueOf(20), is(equalsBigDecimal(manager.getUpdates().get(0).getTaxes())));
		
		assertThat(valueOf(140), is(equalsBigDecimal(manager.getLastUpdate().getGrossValue())));
		assertThat(valueOf(10), is(equalsBigDecimal(manager.getLastUpdate().getRates())));
		assertThat(valueOf(30), is(equalsBigDecimal(manager.getLastUpdate().getTaxes())));
	}
	
	@Test
	public void shouldNotUpdateValueWhenPassedNullValue() {
		manager.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.now(), null, null, null);
		
		assertEquals(manager.getUpdates().size(), 2);
		
		assertThat(valueOf(120), is(equalsBigDecimal(manager.getUpdates().get(1).getGrossValue())));
		assertThat(valueOf(10), is(equalsBigDecimal(manager.getUpdates().get(1).getRates())));
		assertThat(valueOf(20), is(equalsBigDecimal(manager.getUpdates().get(1).getTaxes())));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void shouldThrowExceptionWhenGetLastUpdateAndDoNotHaveAnyUpdates() {
		manager.getLastUpdate();
	}
	
	@Test
	public void shouldReturnFirstUpdateByDateWithEqualDate() {
		manager.updateValues(LocalDate.of(2018, 3, 20), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 4, 15), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 6, 22), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 8, 20), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 10, 20), valueOf(120), valueOf(10), valueOf(20));
		
		AssetUpdate firstUpdateByDate = manager.getFirstUpdateByDate(LocalDate.of(2018, 6, 22));
		
		assertEquals(LocalDate.of(2018, 6, 22), firstUpdateByDate.getDate());
	}
	
	@Test
	public void shouldReturnFirstUpdateByDateWithProximityDate() {
		manager.updateValues(LocalDate.of(2018, 3, 20), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 4, 15), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 6, 22), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 8, 20), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 10, 20), valueOf(120), valueOf(10), valueOf(20));
		
		AssetUpdate firstUpdateByDate = manager.getFirstUpdateByDate(LocalDate.of(2018, 6, 10));
		
		assertEquals(LocalDate.of(2018, 6, 22), firstUpdateByDate.getDate());
	}
	
	@Test(expected=UpdateNotFoundException.class)
	public void shoultThrowExceptionWhenDoNotExistAnUpdateWithInformedDate() {
		manager.updateValues(LocalDate.of(2018, 3, 20), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 4, 15), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 6, 22), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 8, 20), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 10, 20), valueOf(120), valueOf(10), valueOf(20));
		
		manager.getFirstUpdateByDate(LocalDate.of(2019, 2, 10));
	}
	
	@Test
	public void shouldReturnLastUpdateByDateWithEqualDate() {
		manager.updateValues(LocalDate.of(2018, 3, 20), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 4, 15), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 6, 22), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 8, 20), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 10, 20), valueOf(120), valueOf(10), valueOf(20));
		
		AssetUpdate firstUpdateByDate = manager.getLastUpdateByDate(LocalDate.of(2018, 8, 20));
		
		assertEquals(LocalDate.of(2018, 8, 20), firstUpdateByDate.getDate());
	}
	
	@Test
	public void shouldReturnLastUpdateByDateWithProximityDate() {
		manager.updateValues(LocalDate.of(2018, 3, 20), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 4, 15), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 6, 22), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 8, 20), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.of(2018, 10, 20), valueOf(120), valueOf(10), valueOf(20));
		
		AssetUpdate firstUpdateByDate = manager.getLastUpdateByDate(LocalDate.of(2018, 7, 10));
		
		assertEquals(LocalDate.of(2018, 6, 22), firstUpdateByDate.getDate());
	}
	
}
