package com.github.robsonbittencourt.financialgoals.asset;

import static com.github.robsonbittencourt.financialgoals.asset.ProfitType.FIX;
import static com.github.robsonbittencourt.financialgoals.util.BigDecimalMatcher.equalsBigDecimal;
import static java.math.BigDecimal.valueOf;
import static java.time.Month.APRIL;
import static java.time.Month.FEBRUARY;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
		assertThat(valueOf(100), is(equalsBigDecimal(asset.getInitialValue())));
		assertThat(valueOf(100), is(equalsBigDecimal(asset.getLastUpdate().getGrossValue())));
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

		assertThat(valueOf(100), is(equalsBigDecimal(asset.getAssetDeposits().get(0).getValue())));
	}

	@Test
	public void shouldUpdateGrossValueWhenAddMoreMoneyOnAsset() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		asset.deposit(LocalDate.now(), valueOf(100));

		assertThat(valueOf(200), is(equalsBigDecimal(asset.getGrossValue())));
	}

	@Test
	public void shouldUpdateValuesOfAsset() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(15));
		
		assertThat(valueOf(120), is(equalsBigDecimal(asset.getLastUpdate().getGrossValue())));
		assertThat(valueOf(10), is(equalsBigDecimal(asset.getLastUpdate().getRates())));
		assertThat(valueOf(15), is(equalsBigDecimal(asset.getLastUpdate().getTaxes())));
	}

	@Test
	public void shouldCalculateGrossProfitWhenDoNotHaveUpdates() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		assertThat(valueOf(0), is(equalsBigDecimal(asset.getGrossProfit().getValue())));
		assertThat(valueOf(0), is(equalsBigDecimal(asset.getGrossProfit().getPercent())));
	}

	@Test
	public void shouldCalculateGrossProfitWhenDoNotHaveMoreThanOneUpdate() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(15));
		
		assertThat(valueOf(20), is(equalsBigDecimal(asset.getGrossProfit().getValue())));
		assertThat(valueOf(0.2), is(equalsBigDecimal(asset.getGrossProfit().getPercent())));
	}

	@Test
	public void shouldCalculateGrossProfitWhenHaveMoreThanOneUpdate() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.now(), valueOf(120), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.now(), valueOf(150), valueOf(10), valueOf(15));

		assertThat(valueOf(50), is(equalsBigDecimal(asset.getGrossProfit().getValue())));
		assertThat(valueOf(0.5), is(equalsBigDecimal(asset.getGrossProfit().getPercent())));
	}

	@Test
	public void shouldCalculateGrossProfitWhenHaveMoreThanOneUpdateAndConsiderAPeriod() {
		Asset asset = new Asset(LocalDate.of(2018, FEBRUARY, 10), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.of(2018, MARCH, 10), valueOf(120), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, APRIL, 10), valueOf(150), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, MAY, 10), valueOf(200), valueOf(10), valueOf(15));
		
		InvestmentReturn investmentReturn = asset.getGrossProfit(LocalDate.of(2018, APRIL, 10), LocalDate.of(2018, MAY, 20));

		assertThat(valueOf(50), is(equalsBigDecimal(investmentReturn.getValue())));
		assertThat(valueOf(0.3333333333), is(equalsBigDecimal(investmentReturn.getPercent())));
	}
	
	@Test
	public void shouldUseSameUpdateWhenHaveOnlyOneUpdateOnPeriod() {
		Asset asset = new Asset(LocalDate.of(2018, FEBRUARY, 10), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.of(2018, MARCH, 10), valueOf(120), valueOf(10), valueOf(15));
		
		InvestmentReturn investmentReturn = asset.getGrossProfit(LocalDate.of(2018, FEBRUARY, 10), LocalDate.of(2018, MARCH, 20));

		assertThat(valueOf(20), is(equalsBigDecimal(investmentReturn.getValue())));
		assertThat(valueOf(0.2), is(equalsBigDecimal(investmentReturn.getPercent())));
	}

	@Test
	public void shouldCalculateGrossProfit() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(500));

		asset.updateValues(LocalDate.now(), valueOf(550), valueOf(10), valueOf(15));
		
		assertThat(valueOf(50), is(equalsBigDecimal(asset.getGrossProfit().getValue())));
		assertThat(valueOf(0.1), is(equalsBigDecimal(asset.getGrossProfit().getPercent())));
	}
	
	@Test
	public void shouldReturnGrossProfitMinusRatesAndTaxesAsNetProfit() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(500));

		asset.updateValues(LocalDate.now(), valueOf(550), valueOf(10), valueOf(15));
		
		assertThat(valueOf(25), is(equalsBigDecimal(asset.getNetProfit().getValue())));
		assertThat(valueOf(0.05), is(equalsBigDecimal(asset.getNetProfit().getPercent())));
	}
	
	@Test
	public void shouldCalculateNetProfitWhenHaveMoreThanOneUpdateAndConsiderAPeriod() {
		Asset asset = new Asset(LocalDate.of(2018, FEBRUARY, 10), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.of(2018, MARCH, 10), valueOf(120), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, APRIL, 10), valueOf(150), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, MAY, 10), valueOf(200), valueOf(15), valueOf(20));
		
		InvestmentReturn investmentReturn = asset.getNetProfit(LocalDate.of(2018, APRIL, 10), LocalDate.of(2018, MAY, 20));
		
		assertThat(valueOf(15), is(equalsBigDecimal(investmentReturn.getValue())));
		assertThat(valueOf(0.1), is(equalsBigDecimal(investmentReturn.getPercent())));
	}
	
	@Test
	public void shouldReturnNetProfitMinusInflationAsRealProfit() {
		Asset asset = new Asset(LocalDate.now(), "CDB Itaú Bank", FIX, valueOf(500));

		asset.updateValues(LocalDate.now(), valueOf(550), valueOf(10), valueOf(15));
		
		assertThat(valueOf(22.03), is(equalsBigDecimal(asset.getRealProfit().getValue())));
		assertThat(valueOf(0.04406), is(equalsBigDecimal(asset.getRealProfit().getPercent())));
	}
	
	@Test
	public void shouldCalculateRealProfitWhenHaveMoreThanOneUpdateAndConsiderAPeriod() {
		Asset asset = new Asset(LocalDate.of(2018, FEBRUARY, 10), "CDB Itaú Bank", FIX, valueOf(100));

		asset.updateValues(LocalDate.of(2018, MARCH, 10), valueOf(120), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, APRIL, 10), valueOf(150), valueOf(10), valueOf(15));
		asset.updateValues(LocalDate.of(2018, MAY, 10), valueOf(200), valueOf(15), valueOf(20));
		
		InvestmentReturn investmentReturn = asset.getRealProfit(LocalDate.of(2018, APRIL, 10), LocalDate.of(2018, MAY, 20));
		
		assertThat(valueOf(13.92), is(equalsBigDecimal(investmentReturn.getValue())));
		assertThat(valueOf(0.0928), is(equalsBigDecimal(investmentReturn.getPercent())));
	}
}
