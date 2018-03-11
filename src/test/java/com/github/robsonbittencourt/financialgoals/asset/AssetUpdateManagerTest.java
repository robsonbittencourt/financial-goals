package com.github.robsonbittencourt.financialgoals.asset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class AssetUpdateManagerTest {
	
	@Test
	public void shouldUpdateValuesOfAsset() {
		AssetUpdateManager manager = new AssetUpdateManager();
		
		BigDecimal grossValue = BigDecimal.valueOf(120);
		BigDecimal rates = BigDecimal.valueOf(10);
		BigDecimal taxes = BigDecimal.valueOf(20);
		
		manager.updateValues(grossValue, rates, taxes);
		
		assertEquals(manager.getLastUpdate().getGrossValue(), BigDecimal.valueOf(120));
		assertEquals(manager.getLastUpdate().getRates(), BigDecimal.valueOf(10));
		assertEquals(manager.getLastUpdate().getTaxes(), BigDecimal.valueOf(20));
	}
	
	@Test
	public void shouldUpdateValuesOfAssetKeepingHistory() {
		AssetUpdateManager manager = new AssetUpdateManager();
		
		BigDecimal grossValue = BigDecimal.valueOf(120);
		BigDecimal rates = BigDecimal.valueOf(10);
		BigDecimal taxes = BigDecimal.valueOf(20);
		
		manager.updateValues(grossValue, rates, taxes);
		
		BigDecimal newGrossValue = BigDecimal.valueOf(140);
		BigDecimal newRates = BigDecimal.valueOf(10);
		BigDecimal newTaxes = BigDecimal.valueOf(30);
		
		manager.updateValues(newGrossValue, newRates, newTaxes);
		
		assertEquals(manager.getUpdates().get(0).getGrossValue(), BigDecimal.valueOf(120));
		assertEquals(manager.getUpdates().get(0).getRates(), BigDecimal.valueOf(10));
		assertEquals(manager.getUpdates().get(0).getTaxes(), BigDecimal.valueOf(20));
		
		assertEquals(manager.getLastUpdate().getGrossValue(), BigDecimal.valueOf(140));
		assertEquals(manager.getLastUpdate().getRates(), BigDecimal.valueOf(10));
		assertEquals(manager.getLastUpdate().getTaxes(), BigDecimal.valueOf(30));
	}
	
	@Test
	public void shouldReturnTrueWhenExistUpdates() {
		AssetUpdateManager manager = new AssetUpdateManager();
		
		manager.updateValues(BigDecimal.valueOf(120), BigDecimal.valueOf(10), BigDecimal.valueOf(20));
		
		assertTrue(manager.hasUpdates());
	}
	
	@Test
	public void shouldReturnFalseWhenDoNotExistUpdates() {
		AssetUpdateManager manager = new AssetUpdateManager();
		
		assertFalse(manager.hasUpdates());
	}
	
	@Test
	public void shouldNotUpdateValueWhenPassedNullValue() {
		AssetUpdateManager manager = new AssetUpdateManager();
		
		BigDecimal grossValue = BigDecimal.valueOf(120);
		BigDecimal rates = BigDecimal.valueOf(10);
		BigDecimal taxes = BigDecimal.valueOf(20);
		
		manager.updateValues(grossValue, rates, taxes);
		
		manager.updateValues(null, null, null);
		
		assertEquals(manager.getUpdates().size(), 2);
		assertEquals(manager.getUpdates().get(1).getGrossValue(), BigDecimal.valueOf(120));
		assertEquals(manager.getUpdates().get(1).getRates(), BigDecimal.valueOf(10));
		assertEquals(manager.getUpdates().get(1).getTaxes(), BigDecimal.valueOf(20));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void shouldThrowExceptionWhenGetLastUpdateAndDoNotHaveAnyUpdates() {
		AssetUpdateManager manager = new AssetUpdateManager();
		
		manager.getLastUpdate();
	}

}
