import com.example.operationservice.Enum.OperationType;
import com.example.operationservice.Enum.TransferStatus;
import com.example.operationservice.Enum.TransferType;
import com.example.operationservice.Model.Beneficiary;
import com.example.operationservice.Model.Customer;
import com.example.operationservice.Model.Operation;
import com.example.operationservice.Model.Transfer;
import com.example.operationservice.Repository.BeneficiaryRepository;
import com.example.operationservice.Repository.CustomerRepository;
import com.example.operationservice.Repository.OperationRepository;
import com.example.operationservice.Repository.TransferRepository;
import com.example.operationservice.Request.OperationBody;
import com.example.operationservice.Request.TransferBody;
import com.example.operationservice.Request.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmissionRestControllerTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private EmissionRestController emissionRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testEmettreTransfer() {
        // Mock data
        TransferRequest transferRequest = new TransferRequest();
        TransferBody transferBody = new TransferBody();
        transferBody.setSenderId(1L);
        transferBody.setReceiverId(2L);
        transferBody.setAmount(1000.0);
        transferRequest.setTransferBody(transferBody);

        OperationBody operationBody = new OperationBody();
        operationBody.setTransferType(TransferType.EMISSION);
        operationBody.setOperationType(OperationType.ESPECE_CONSOLE_AGENT);
        transferRequest.setOperationBody(operationBody);

        Customer customer = Customer.builder().balance(2000.0).plafondAnnuel(3000.0).build();
        Beneficiary beneficiary = Beneficiary.builder().build();

        when(customerRepository.findById(transferBody.getSenderId())).thenReturn(Optional.of(customer));
        when(beneficiaryRepository.findById(transferBody.getReceiverId())).thenReturn(Optional.of(beneficiary));

        // Test
        ResponseEntity<String> response = emissionRestController.emettreTransfer(transferRequest);

        // Assertions
        assertNotNull(response);
        assertEquals("Transfert ajouté en attendant l'otp", response.getBody());
        verify(transferRepository, times(1)).save(any(Transfer.class));
        verify(operationRepository, times(1)).save(any(Operation.class));
    }

    @Test
    void testEmettreTransferExceedsAmount() {
        // Mock data
        TransferRequest transferRequest = new TransferRequest();
        TransferBody transferBody = new TransferBody();
        transferBody.setSenderId(1L);
        transferBody.setReceiverId(2L);
        transferBody.setAmount(90000.0); // Exceeds the limit
        transferRequest.setTransferBody(transferBody);

        OperationBody operationBody = new OperationBody();
        operationBody.setTransferType(TransferType.EMISSION);
        operationBody.setOperationType(OperationType.ESPECE_CONSOLE_AGENT);
        transferRequest.setOperationBody(operationBody);

        Customer customer = Customer.builder().build();
        Beneficiary beneficiary = Beneficiary.builder().build();

        when(customerRepository.findById(transferBody.getSenderId())).thenReturn(Optional.of(customer));
        when(beneficiaryRepository.findById(transferBody.getReceiverId())).thenReturn(Optional.of(beneficiary));

        // Test
        ResponseEntity<String> response = emissionRestController.emettreTransfer(transferRequest);

        // Assertions
        assertNotNull(response);
        assertEquals("Dépasse 80000", response.getBody());
        verify(transferRepository, never()).save(any(Transfer.class));
        verify(operationRepository, never()).save(any(Operation.class));
    }


}
