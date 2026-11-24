package jmv74211.DSS_P4.models.Users;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UsersTest {

	@Test
	void customerCompletoEsValido() {
		Customer customer = new Customer("customer1@gmail.com", "pwddss", "Felipe", "Ortega Romero",
				"1996-06-05", "771989912");
		assertTrue(customer.hasValidAttributes());
	}

	@Test
	void customerSinTarjetaEsInvalido() {
		Customer customer = new Customer("customer1@gmail.com", "pwddss", "Felipe", "Ortega Romero",
				"1996-06-05", null);
		assertFalse(customer.hasValidAttributes());
	}

	@Test
	void customerSinEmailEsInvalido() {
		Customer customer = new Customer(null, "pwddss", "Felipe", "Ortega Romero", "1996-06-05", "771989912");
		assertFalse(customer.hasValidAttributes());
	}

	@Test
	void managerCompletoEsValido() {
		Manager manager = new Manager("manager1@gmail.com", "pwddss", "Juan", "Martínez Valera", "1999-11-07");
		assertTrue(manager.hasValidAttributes());
	}

	@Test
	void managerSinPasswordEsInvalido() {
		Manager manager = new Manager("manager1@gmail.com", null, "Juan", "Martínez Valera", "1999-11-07");
		assertFalse(manager.hasValidAttributes());
	}

	@Test
	void administratorCompletoEsValido() {
		Administrator admin = new Administrator("admin1@gmail.com", "pwddss", "Jonathan", "Martín Valera",
				"1993-01-12");
		assertTrue(admin.hasValidAttributes());
	}

	@Test
	void administratorSinNombreEsInvalido() {
		Administrator admin = new Administrator("admin1@gmail.com", "pwddss", null, "Martín Valera", "1993-01-12");
		assertFalse(admin.hasValidAttributes());
	}
}
