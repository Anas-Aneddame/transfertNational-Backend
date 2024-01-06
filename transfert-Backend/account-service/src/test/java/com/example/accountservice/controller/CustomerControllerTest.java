import com.example.accountservice.controller.CustomerController;
import com.example.accountservice.exception.CustomerNotFoundException;
import com.example.accountservice.model.Customer;
import com.example.accountservice.model.Transfer;
import com.example.accountservice.repository.CustomerRepository;
import com.example.accountservice.repository.TransferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    // Tests pour newCustomer
    @Test
    void testNewCustomer() throws Exception {
        Customer newCustomer = new Customer();
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newCustomer.getId()));
    }

    // Tests pour getAllCustomers
    @Test
    void testGetAllCustomers() throws Exception {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(new Customer(), new Customer()));

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/customers"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // Tests pour getCustomerById
    @Test
    void testGetCustomerById() throws Exception {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/customer/{customerId}", customerId));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerId));
    }

    @Test
    void testGetCustomerByIdNotFound() throws Exception {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/customer/{customerId}", customerId));

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Customer not found with id: " + customerId));
    }

    // Tests pour updateCustomer
    @Test
    void testUpdateCustomer() throws Exception {
        Long customerId = 1L;
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/customer/{customerId}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerId));
    }

    @Test
    void testUpdateCustomerNotFound() throws Exception {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/customer/{customerId}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Customer not found with id: " + customerId));
    }

    // Tests pour deleteCustomer
    @Test
    void testDeleteCustomer() throws Exception {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/customer/{customerId}", customerId));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value("Customer with id " + customerId + " has been deleted success."));
    }

    @Test
    void testDeleteCustomerNotFound() throws Exception {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/customer/{customerId}", customerId));

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Customer not found with id: " + customerId));
    }
}
