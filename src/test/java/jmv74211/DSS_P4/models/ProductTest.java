package jmv74211.DSS_P4.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProductTest {

	private final Department department = new Department("HYGIENE");

	@Test
	void productoCompletoEsValido() {
		Product product = new Product(department, "Producto1", "Imagen1", "Descripción1", false, 35);
		assertTrue(product.hasValidAttributes());
	}

	@Test
	void productoConPrecioCeroEsValido() {
		Product product = new Product(department, "Producto1", "Imagen1", "Descripción1", false, 0);
		assertTrue(product.hasValidAttributes());
	}

	@Test
	void productoConPrecioNegativoEsInvalido() {
		Product product = new Product(department, "Producto1", "Imagen1", "Descripción1", false, -1);
		assertFalse(product.hasValidAttributes());
	}

	@Test
	void productoSinDepartamentoEsInvalido() {
		Product product = new Product(null, "Producto1", "Imagen1", "Descripción1", false, 10);
		assertFalse(product.hasValidAttributes());
	}

	@Test
	void productoSinNombreEsInvalido() {
		Product product = new Product(department, null, "Imagen1", "Descripción1", false, 10);
		assertFalse(product.hasValidAttributes());
	}

	@Test
	void productoSinDescripcionEsInvalido() {
		Product product = new Product(department, "Producto1", "Imagen1", null, false, 10);
		assertFalse(product.hasValidAttributes());
	}

	@Test
	void productoSinImagenEsInvalido() {
		Product product = new Product(department, "Producto1", null, "Descripción1", false, 10);
		assertFalse(product.hasValidAttributes());
	}

	@Test
	void constructorVacioCreaDepartamentoPorDefecto() {
		Product product = new Product();
		assertNotNull(product.getDepartment());
	}
}
