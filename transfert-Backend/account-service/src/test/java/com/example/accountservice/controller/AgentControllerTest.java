import com.example.accountservice.exception.AgentNotFoundException;
import com.example.accountservice.model.Agent;
import com.example.accountservice.repository.AgentRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//En utilisant Mockito pour simuler le comportement du repository.

@ExtendWith(MockitoExtension.class)
class AgentControllerTest {

    @Mock
    private AgentRepository agentRepository;

    @InjectMocks
    private AgentController agentController;

    private MockMvc mockMvc;

//Testing les getters des agents
    @Test
    void testGetAllAgents() throws Exception {
        when(agentRepository.findAll()).thenReturn(Arrays.asList(new Agent(), new Agent()));

        mockMvc = MockMvcBuilders.standaloneSetup(agentController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/agents"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetAgentById() throws Exception {
        Long agentId = 1L;
        Agent agent = new Agent();
        agent.setId(agentId);
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(agent));

        mockMvc = MockMvcBuilders.standaloneSetup(agentController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/agent/{agentId}", agentId));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(agentId));
    }

    @Test
    void testGetAgentByIdNotFound() throws Exception {
        Long agentId = 1L;
        when(agentRepository.findById(agentId)).thenReturn(Optional.empty());

        mockMvc = MockMvcBuilders.standaloneSetup(agentController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/agent/{agentId}", agentId));

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Agent not found with id: " + agentId));
    }

    //Les tests pour les autres méthodes (newAgent, updateAgent, deleteAgent) 
// Tests pour newAgent
    @Test
    void testNewAgent() throws Exception {
        Agent newAgent = new Agent();
        when(agentRepository.save(any(Agent.class))).thenReturn(newAgent);

        mockMvc = MockMvcBuilders.standaloneSetup(agentController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/agent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newAgent.getId()));
    }

    // Tests pour updateAgent
    @Test
    void testUpdateAgent() throws Exception {
        Long agentId = 1L;
        Agent updatedAgent = new Agent();
        updatedAgent.setId(agentId);
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(new Agent()));
        when(agentRepository.save(any(Agent.class))).thenReturn(updatedAgent);

        mockMvc = MockMvcBuilders.standaloneSetup(agentController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/agent/{agentId}", agentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(agentId));
    }

    @Test
    void testUpdateAgentNotFound() throws Exception {
        Long agentId = 1L;
        when(agentRepository.findById(agentId)).thenReturn(Optional.empty());

        mockMvc = MockMvcBuilders.standaloneSetup(agentController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/agent/{agentId}", agentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Agent not found with id: " + agentId));
    }

    // Tests pour deleteAgent
    @Test
    void testDeleteAgent() throws Exception {
        Long agentId = 1L;
        when(agentRepository.existsById(agentId)).thenReturn(true);

        mockMvc = MockMvcBuilders.standaloneSetup(agentController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/agent/{agentId}", agentId));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value("Agent with id " + agentId + " has been deleted success."));
    }

    @Test
    void testDeleteAgentNotFound() throws Exception {
        Long agentId = 1L;
        when(agentRepository.existsById(agentId)).thenReturn(false);

        mockMvc = MockMvcBuilders.standaloneSetup(agentController).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/agent/{agentId}", agentId));

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Agent not found with id: " + agentId));
    }

}
