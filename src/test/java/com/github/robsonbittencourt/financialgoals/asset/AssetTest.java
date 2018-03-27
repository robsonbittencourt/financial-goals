package com.github.robsonbittencourt.financialgoals.asset;

import static com.github.robsonbittencourt.financialgoals.asset.ProfitType.FIX;
import static java.math.BigDecimal.valueOf;
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
		BigDecimal initialValue = valueOf(100);

		Asset asset = new Asset(LocalDate.now(), description, profitType, initialValue);

		assertEquals("CDB Itaú Bank", asset.getDescription());
		assertEquals(FIX, asset.getProfitType());
		assertEquals(new BigDecimal("100.0000"), asset.getInitialValue());
		assertEquals(new BigDecimal("100.0000"), asset.getLastUpdate().getGrossValue());
	}
	
	@Test
	public void shouldUseActualDateAsInitialDateWhenCreateAnAsset() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		assertEquals(LocalDate.now(), asset.getInitialDate());
	}

	@Test
	public void shouldAddMoreMoneyOnAsset() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		asset.deposit(LocalDate.now(), valueOf(100));

		assertEquals(asset.getAssetDeposits().get(0).getValue(), valueOf(100));
	}

	@Test
	public void shouldUpdateGrossValueWhenAddMoreMoneyOnAsset() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		asset.deposit(LocalDate.now(), valueOf(100));

		assertEquals(new BigDecimal("200.0000"), asset.getGrossValue());
	}

	@Test
	public void shouldUpdateValuesOfAsset() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(15));

		assertEquals(new BigDecimal("120.0000"), asset.getLastUpdate().getGrossValue());
		assertEquals(new BigDecimal("10.0000"), asset.getLastUpdate().getRates());
		assertEquals(new BigDecimal("15.0000"), asset.getLastUpdate().getTaxes());
	}

	@Test
	public void shouldCalculateGrossProfitWhenDoNotHaveUpdates() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		assertEquals(new BigDecimal("0.0000"), asset.getGrossProfit().getValue());
		assertEquals(new BigDecimal("0.0000"), asset.getGrossProfit().getPercent());
	}

	@Test
	public void shouldCalculateGrossProfitWhenDoNotHaveMoreThanOneUpdate() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(15));

		assertEquals(new BigDecimal("20.0000"), asset.getGrossProfit().getValue());
		assertEquals(new BigDecimal("0.2000"), asset.getGrossProfit().getPercent());
	}

	@Test
	public void shouldCalculateGrossProfitWhenHaveMoreThanOneUpdate() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.now(), valueOf(150), valueOf(10), valueOf(15));

		assertEquals(new BigDecimal("50.0000"), asset.getGrossProfit().getValue());
		assertEquals(new BigDecimal("0.5000"), asset.getGrossProfit().getPercent());
	}

	@Test
	public void shouldCalculateGrossProfitWhenHaveMoreThanOneUpdateAndConsiderAPeriod() {
		Asset asset = new Asset(LocalDate.of(2018, FEBRUARY, 10), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.of(2018, MARCH, 10), valueOf(120), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, APRIL, 10), valueOf(150), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, MAY, 10), valueOf(200), valueOf(10), valueOf(15));
		
		InvestmentReturn investmentReturn = asset.getGrossProfit(LocalDate.of(2018, APRIL, 10), LocalDate.of(2018, MAY, 20));

		assertEquals(new BigDecimal("50.0000"), investmentReturn.getValue());
		assertEquals(new BigDecimal("0.3333"), investmentReturn.getPercent());
	}
	
	@Test
	public void shouldUseSameUpdateWhenHaveOnlyOneUpdateOnPeriod() {
		Asset asset = new Asset(LocalDate.of(2018, FEBRUARY, 10), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.of(2018, MARCH, 10), valueOf(120), valueOf(10), valueOf(15));
		
		InvestmentReturn investmentReturn = asset.getGrossProfit(LocalDate.of(2018, FEBRUARY, 10), LocalDate.of(2018, MARCH, 20));

		assertEquals(new BigDecimal("20.0000"), investmentReturn.getValue());
		assertEquals(new BigDecimal("0.2000"), investmentReturn.getPercent());
	}

	@Test
	public void shouldCalculateGrossProfit() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(500));

		asset.updateValues(LocalDate.now(), valueOf(550), valueOf(10), valueOf(15));
		
		assertEquals(new BigDecimal("50.0000"), asset.getGrossProfit().getValue());
		assertEquals(new BigDecimal("0.1000"), asset.getGrossProfit().getPercent());
	}
	
	@Test
	public void shouldReturnGrossProfitMinusRatesAndTaxesAsNetProfit() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(500));

		asset.updateValues(LocalDate.now(), valueOf(550), valueOf(10), valueOf(15));
		
		assertEquals(new BigDecimal("25.0000"), asset.getNetProfit().getValue());
		assertEquals(new BigDecimal("0.0500"), asset.getNetProfit().getPercent());
	}
	
	@Test
	public void shouldCalculateNetProfitWhenHaveMoreThanOneUpdateAndConsiderAPeriod() {
		Asset asset = new Asset(LocalDate.of(2018, FEBRUARY, 10), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.of(2018, MARCH, 10), valueOf(120), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, APRIL, 10), valueOf(150), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, MAY, 10), valueOf(200), valueOf(15), valueOf(20));
		
		InvestmentReturn investmentReturn = asset.getNetProfit(LocalDate.of(2018, APRIL, 10), LocalDate.of(2018, MAY, 20));

		assertEquals(new BigDecimal("15.0000"), investmentReturn.getValue());
		assertEquals(new BigDecimal("0.1000"), investmentReturn.getPercent());
	}
	
	@Test
	public void shouldReturnNetProfitMinusInflationAsRealProfit() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(500));

		asset.updateValues(LocalDate.now(), valueOf(550), valueOf(10), valueOf(15));
		
		assertEquals(new BigDecimal("22.0300"), asset.getRealProfit().getValue());
		assertEquals(new BigDecimal("0.0441"), asset.getRealProfit().getPercent());
	}
	
	@Test
	public void shouldCalculateRealProfitWhenHaveMoreThanOneUpdateAndConsiderAPeriod() {
		Asset asset = new Asset(LocalDate.of(2018, FEBRUARY, 10), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.of(2018, MARCH, 10), valueOf(120), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, APRIL, 10), valueOf(150), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, MAY, 10), valueOf(200), valueOf(15), valueOf(20));
		
		InvestmentReturn investmentReturn = asset.getRealProfit(LocalDate.of(2018, APRIL, 10), LocalDate.of(2018, MAY, 20));

		assertEquals(new BigDecimal("13.9200"), investmentReturn.getValue());
		assertEquals(new BigDecimal("0.0928"), investmentReturn.getPercent());
	}
}
