package com.gui.jhand;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public abstract class TestBase {

	@BeforeEach
	public void setUpTest() {
		MockitoAnnotations.openMocks(this);

		this.init();
	}

	public abstract void init();

	public static <T> T valid(Class<T> type) {
		return Instancio.of(type).create();
	}

}
