package com.github.robsonbittencourt.financialgoals.asset;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;

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
		
		assertEquals(manager.getLastUpdate().getGrossValue(), valueOf(120));
		assertEquals(manager.getLastUpdate().getRates(), valueOf(10));
		assertEquals(manager.getLastUpdate().getTaxes(), valueOf(20));
	}
	
	@Test
	public void shouldUpdateValuesOfAssetKeepingHistory() {
		manager.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.now(), valueOf(140), valueOf(10), valueOf(30));
		
		assertEquals(manager.getUpdates().get(0).getGrossValue(), valueOf(120));
		assertEquals(manager.getUpdates().get(0).getRates(), valueOf(10));
		assertEquals(manager.getUpdates().get(0).getTaxes(), valueOf(20));
		
		assertEquals(manager.getLastUpdate().getGrossValue(), valueOf(140));
		assertEquals(manager.getLastUpdate().getRates(), valueOf(10));
		assertEquals(manager.getLastUpdate().getTaxes(), valueOf(30));
	}
	
	@Test
	public void shouldNotUpdateValueWhenPassedNullValue() {
		manager.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(20));
		manager.updateValues(LocalDate.now(), null, null, null);
		
		assertEquals(manager.getUpdates().size(), 2);
		assertEquals(manager.getUpdates().get(1).getGrossValue(), valueOf(120));
		assertEquals(manager.getUpdates().get(1).getRates(), valueOf(10));
		assertEquals(manager.getUpdates().get(1).getTaxes(), valueOf(20));
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
