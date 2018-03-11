package com.github.robsonbittencourt.financialgoals.asset;

import static com.github.robsonbittencourt.financialgoals.asset.ProfitType.FIX;
import static java.time.Month.APRIL;
import static java.time.Month.FEBRUARY;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;

public class AssetTest {

	@Test
	public void shouldCreateAnAsset() {
		String description = "CDB Itaú Bank";
		ProfitType profitType = FIX;
		BigDecimal initialValue = BigDecimal.valueOf(100);

		Asset asset = new Asset(LocalDate.now(), description, profitType, initialValue);

		assertEquals("CDB Itaú Bank", asset.getDescription());
		assertEquals(FIX, asset.getProfitType());
		assertEquals(BigDecimal.valueOf(100), asset.getInitialValue());
		assertEquals(BigDecimal.valueOf(100), asset.getLastUpdate().getGrossValue());
	}
	
	@Test
	public void shouldUseActualDateAsInitialDateWhenCreateAnAsset() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, BigDecimal.valueOf(100));

		assertEquals(LocalDate.now(), asset.getInitialDate());
	}

	@Test
	public void shouldAddMoreMoneyOnAsset() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, BigDecimal.valueOf(100));

		asset.deposit(LocalDate.now(), BigDecimal.valueOf(100));

		assertEquals(asset.getAssetDeposits().get(0).getValue(), BigDecimal.valueOf(100));
	}

	@Test
	public void shouldUpdateGrossValueWhenAddMoreMoneyOnAsset() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, BigDecimal.valueOf(100));

		asset.deposit(LocalDate.now(), BigDecimal.valueOf(100));

		assertEquals(asset.getGrossValue(), BigDecimal.valueOf(200));
	}

	@Test
	public void shouldUpdateValuesOfAsset() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, BigDecimal.valueOf(100));

		asset.updateValues(LocalDate.now(), BigDecimal.valueOf(120), BigDecimal.valueOf(10), BigDecimal.valueOf(15));

		assertEquals(BigDecimal.valueOf(120), asset.getLastUpdate().getGrossValue());
		assertEquals(BigDecimal.valueOf(10), asset.getLastUpdate().getRates());
		assertEquals(BigDecimal.valueOf(15), asset.getLastUpdate().getTaxes());
	}

	@Test
	public void shouldCalculateGrossProfitWhenDoNotHaveUpdates() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, BigDecimal.valueOf(100));

		assertEquals(BigDecimal.valueOf(0), asset.getGrossProfit());
	}

	@Test
	public void shouldCalculateGrossProfitWhenDoNotHaveMoreThanOneUpdate() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, BigDecimal.valueOf(100));

		asset.updateValues(LocalDate.now(), BigDecimal.valueOf(120), BigDecimal.valueOf(10), BigDecimal.valueOf(15));

		assertEquals(BigDecimal.valueOf(20), asset.getGrossProfit());
	}

	@Test
	public void shouldCalculateGrossProfitWhenHaveMoreThanOneUpdate() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, BigDecimal.valueOf(100));

		asset.updateValues(LocalDate.now(), BigDecimal.valueOf(120), BigDecimal.valueOf(10), BigDecimal.valueOf(15));
		asset.updateValues(LocalDate.now(), BigDecimal.valueOf(150), BigDecimal.valueOf(10), BigDecimal.valueOf(15));

		assertEquals(BigDecimal.valueOf(50), asset.getGrossProfit());
	}

	@Test
	public void shouldCalculateGrossProfitWhenHaveMoreThanOneUpdateAndConsiderAPeriod() {
		Asset asset = new Asset(LocalDate.of(2018, FEBRUARY, 10), "CDB Itaú Bank", FIX, BigDecimal.valueOf(100));

		asset.updateValues(LocalDate.of(2018, MARCH, 10), BigDecimal.valueOf(120), BigDecimal.valueOf(10),
				BigDecimal.valueOf(15));
		asset.updateValues(LocalDate.of(2018, APRIL, 10), BigDecimal.valueOf(150), BigDecimal.valueOf(10),
				BigDecimal.valueOf(15));
		asset.updateValues(LocalDate.of(2018, MAY, 10), BigDecimal.valueOf(200), BigDecimal.valueOf(10),
				BigDecimal.valueOf(15));

		assertEquals(BigDecimal.valueOf(50), asset.getGrossProfit(LocalDate.of(2018, FEBRUARY, 10), LocalDate.of(2018, APRIL, 20)));
	}
	
	@Test
	public void shouldUseSameUpdateWhenHaveOnlyOneUpdateOnPeriod() {
		Asset asset = new Asset(LocalDate.of(2018, FEBRUARY, 10), "CDB Itaú Bank", FIX, BigDecimal.valueOf(100));

		asset.updateValues(LocalDate.of(2018, MARCH, 10), BigDecimal.valueOf(120), BigDecimal.valueOf(10),
				BigDecimal.valueOf(15));

		assertEquals(BigDecimal.valueOf(20), asset.getGrossProfit(LocalDate.of(2018, FEBRUARY, 10), LocalDate.of(2018, MARCH, 20)));
	}

	@Test
	public void shouldCalculateGrossProfitPercent() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, BigDecimal.valueOf(500));

		asset.updateValues(LocalDate.now(), BigDecimal.valueOf(550), BigDecimal.valueOf(10), BigDecimal.valueOf(15));

		assertEquals(BigDecimal.valueOf(0.1), asset.getGrossProfitPercent());
	}
	
}
