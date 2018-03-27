package com.github.robsonbittencourt.financialgoals.asset;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
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
		
		assertEquals(new BigDecimal("120.00"), manager.getLastUpdate().getGrossValue());
		assertEquals(new BigDecimal("10.00"), manager.getLastUpdate().getRates());
		assertEquals(new BigDecimal("20.00"), manager.getLastUpdate().getTaxes());
	}
	
	@Test
	public void shouldUpdateValuesOfAssetKeepingHistory() {
		manager.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.now(), valueOf(140), valueOf(10), valueOf(30));
		
		assertEquals(new BigDecimal("120.00"), manager.getUpdates().get(0).getGrossValue());
		assertEquals(new BigDecimal("10.00"), manager.getUpdates().get(0).getRates());
		assertEquals(new BigDecimal("20.00"), manager.getUpdates().get(0).getTaxes());
		
		assertEquals(new BigDecimal("140.00"), manager.getLastUpdate().getGrossValue());
		assertEquals(new BigDecimal("10.00"), manager.getLastUpdate().getRates());
		assertEquals(new BigDecimal("30.00"), manager.getLastUpdate().getTaxes());
	}
	
	@Test
	public void shouldNotUpdateValueWhenPassedNullValue() {
		manager.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.now(), null, null, null);
		
		assertEquals(manager.getUpdates().size(), 2);
		assertEquals(new BigDecimal("120.00"), manager.getUpdates().get(1).getGrossValue());
		assertEquals(new BigDecimal("10.00"), manager.getUpdates().get(1).getRates());
		assertEquals(new BigDecimal("20.00"), manager.getUpdates().get(1).getTaxes());
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
