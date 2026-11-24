package jmv74211.DSS_P4.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jmv74211.DSS_P4.models.Users.Manager;

class PharmacyTest {

	private Manager manager() {
		return new Manager("manager1@gmail.com", "pwddss", "Juan", "Martínez Valera", "1999-11-07");
	}

	@Test
	void farmaciaCompletaEsValida() {
		Pharmacy pharmacy = new Pharmacy("Farmacia 1", 137.15, 135.12, manager());
		assertTrue(pharmacy.hasValidAttributes());
	}

	@Test
	void farmaciaSinNombreEsInvalida() {
		Pharmacy pharmacy = new Pharmacy(null, 137.15, 135.12, manager());
		assertFalse(pharmacy.hasValidAttributes());
	}

	@Test
	void farmaciaSinManagerEsInvalida() {
		Pharmacy pharmacy = new Pharmacy("Farmacia 1", 137.15, 135.12, null);
		assertFalse(pharmacy.hasValidAttributes());
	}

	@Test
	void farmaciaConLatitudCeroEsInvalida() {
		Pharmacy pharmacy = new Pharmacy("Farmacia 1", 0, 135.12, manager());
		assertFalse(pharmacy.hasValidAttributes());
	}

	@Test
	void farmaciaConLongitudCeroEsInvalida() {
		Pharmacy pharmacy = new Pharmacy("Farmacia 1", 137.15, 0, manager());
		assertFalse(pharmacy.hasValidAttributes());
	}

	@Test
	void constructorVacioInicializaCoordenadasEnCero() {
		Pharmacy pharmacy = new Pharmacy();
		assertEquals(0, pharmacy.getLatitude());
		assertEquals(0, pharmacy.getLength());
		assertFalse(pharmacy.hasValidAttributes());
	}

	@Test
	void settersActualizanLosValores() {
		Pharmacy pharmacy = new Pharmacy();
		pharmacy.setId(7);
		pharmacy.setName("Farmacia Central");
		assertEquals(7, pharmacy.getPharmacyId());
		assertEquals("Farmacia Central", pharmacy.getName());
	}
}
