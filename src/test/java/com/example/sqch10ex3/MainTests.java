package com.example.sqch10ex3;

import com.example.sqch10ex3.exceptions.NotEnoughMoneyException;
import com.example.sqch10ex3.model.PaymentDetails;
import com.example.sqch10ex3.model.ErrorDetails;
import com.example.sqch10ex3.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PaymentService paymentService;

	@Test
	@Disabled //TODO: change test so it work with last version
	void testMakePaymentSuccessful() throws Exception {
		var mapper = new ObjectMapper();

		PaymentDetails p = new PaymentDetails();
		p.setAmount(1000);

		given(paymentService.processPayment()).willReturn(p);

		//when(paymentService.processPayment()).thenReturn(p);

		var expected = mapper.writeValueAsString(p);

		mockMvc.perform(post("/payment"))
				.andExpect(status().isAccepted())
				.andExpect(content().json(expected));
	}

	@Test
	@Disabled // TODO: change test so it work with last version
	void testMakePaymentNotEnoughMoney() throws Exception {
		var mapper = new ObjectMapper();

		ErrorDetails e = new ErrorDetails();
		e.setMessage("Not enough money to make the payment.");

		//when(paymentService.processPayment()).thenThrow(new NotEnoughMoneyException());

		given(paymentService.processPayment()).willThrow(new NotEnoughMoneyException());

		var expected = mapper.writeValueAsString(e);

		mockMvc.perform(post("/payment"))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(expected));
	}

	@Test
	public void testPayment() throws Exception {
		var mapper = new ObjectMapper();

		PaymentDetails p = new PaymentDetails();
		p.setAmount(1000);

		var expected = mapper.writeValueAsString(p);

		mockMvc.perform(post("/payment")
						.content(expected)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(content().json(expected));
	}
}
