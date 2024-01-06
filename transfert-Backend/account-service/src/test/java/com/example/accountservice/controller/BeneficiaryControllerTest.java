import com.example.accountservice.exception.BeneficiaryNotFoundException;
import com.example.accountservice.model.Beneficiary;
import com.example.accountservice.repository.BeneficiaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficiaryControllerTest {

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private BeneficiaryController beneficiaryController;

    // Test pour getAllBeneficiaries
    @Test
    void testGetAllBeneficiaries() {
        when(beneficiaryRepository.findAll()).thenReturn(Arrays.asList(new Beneficiary(), new Beneficiary()));

        assertEquals(2, beneficiaryController.getAllBeneficiaries().size());
    }

    // Test pour getBeneficiaryById
    @Test
    void testGetBeneficiaryById() {
        Long beneficiaryId = 1L;
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(beneficiaryId);
        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(beneficiary));

        assertEquals(beneficiary, beneficiaryController.getBeneficiaryById(beneficiaryId));
    }

    @Test
    void testGetBeneficiaryByIdNotFound() {
        Long beneficiaryId = 1L;
        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.empty());

        assertThrows(BeneficiaryNotFoundException.class, () -> beneficiaryController.getBeneficiaryById(beneficiaryId));
    }

    // Test pour createBeneficiary
    @Test
    void testCreateBeneficiary() {
        Beneficiary newBeneficiary = new Beneficiary();
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(newBeneficiary);

        assertEquals(newBeneficiary, beneficiaryController.createBeneficiary(newBeneficiary));
    }

    // Test pour updateBeneficiary
    @Test
    void testUpdateBeneficiary() {
        Long beneficiaryId = 1L;
        Beneficiary updatedBeneficiary = new Beneficiary();
        updatedBeneficiary.setId(beneficiaryId);
        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(new Beneficiary()));
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(updatedBeneficiary);

        assertEquals(updatedBeneficiary, beneficiaryController.updateBeneficiary(updatedBeneficiary, beneficiaryId));
    }

    @Test
    void testUpdateBeneficiaryNotFound() {
        Long beneficiaryId = 1L;
        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.empty());

        assertThrows(BeneficiaryNotFoundException.class, () -> beneficiaryController.updateBeneficiary(new Beneficiary(), beneficiaryId));
    }

    // Test pour deleteBeneficiary
    @Test
    void testDeleteBeneficiary() {
        Long beneficiaryId = 1L;
        when(beneficiaryRepository.existsById(beneficiaryId)).thenReturn(true);

        ResponseEntity<Void> response = beneficiaryController.deleteBeneficiary(beneficiaryId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(beneficiaryRepository, times(1)).deleteById(beneficiaryId);
    }

    @Test
    void testDeleteBeneficiaryNotFound() {
        Long beneficiaryId = 1L;
        when(beneficiaryRepository.existsById(beneficiaryId)).thenReturn(false);

        ResponseEntity<Void> response = beneficiaryController.deleteBeneficiary(beneficiaryId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(beneficiaryRepository, never()).deleteById(beneficiaryId);
    }
}
