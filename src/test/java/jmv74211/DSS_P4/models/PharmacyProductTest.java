package jmv74211.DSS_P4.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PharmacyProductTest {

	@Test
	void cantidadPositivaEsValida() {
		assertTrue(new PharmacyProduct(2, 2, 3).hasValidAttributes());
	}

	@Test
	void cantidadCeroEsValida() {
		assertTrue(new PharmacyProduct(2, 2, 0).hasValidAttributes());
	}

	@Test
	void cantidadNegativaEsInvalida() {
		assertFalse(new PharmacyProduct(2, 2, -1).hasValidAttributes());
	}

	@Test
	void constructorGuardaLosIdentificadores() {
		PharmacyProduct pp = new PharmacyProduct(4, 9, 15);
		assertEquals(4, pp.getPharmacyId());
		assertEquals(9, pp.getProductId());
		assertEquals(15, pp.getQuantity());
	}
}
