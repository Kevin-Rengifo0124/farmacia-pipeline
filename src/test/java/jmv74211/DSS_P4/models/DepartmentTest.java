package jmv74211.DSS_P4.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DepartmentTest {

	@Test
	void departamentoConNombreEsValido() {
		assertFalse(new Department("HERBALIST").hasValidAttributes());
	}

	@Test
	void departamentoSinNombreEsInvalido() {
		assertFalse(new Department().hasValidAttributes());
	}

	@Test
	void toStringIncluyeElNombre() {
		Department department = new Department("COSMETICS");
		department.setDepartmentId(3);
		assertEquals("Department [departmentId=3, name=COSMETICS]", department.toString());
	}
}
