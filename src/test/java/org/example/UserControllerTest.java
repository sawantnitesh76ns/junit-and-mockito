package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    User RECORD1 = new User("suresh@gmail.com", "suresh", "yadav");
    User RECORD2 = new User("james@gmail.com", "James", "Kathi");
    User RECORD3 = new User("kamlesh@gmail.com", "kamlesh", "dalvi");

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetUsers() throws Exception {
        User user1 = new User("suresh@gmail.com", "suresh", "yadav");
        User user2 = new User("james@gmail.com", "James", "Kathi");
        User user3 = new User("kamlesh@gmail.com", "kamlesh", "dalvi");

        List<User> userRecords = List.of(user1, user2, user3);

        when(userRepository.findAll()).thenReturn(userRecords);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3))).andDo(print());

    }

    @Test
    void testGetUserData() throws Exception {

        List<User> userRecords = List.of(RECORD1, RECORD2, RECORD3);

        when(userRepository.findAll()).thenReturn(userRecords);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].email", is("james@gmail.com"))).andDo(print());

    }

    @Test
    public void getUserById_success() throws Exception {
        User user = new User("suresh@gmail.com", "suresh", "yadav");

        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/suresh@gmail.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("suresh@gmail.com")));
    }

    @Test
    public void createUser_success() throws Exception {
        User user = new User("james@gmail.com", "James", "Kathi");

        when(userRepository.save(user)).thenReturn(user);
        String content = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.email", is("james@gmail.com")));
    }


    @Test
    public void putUser_success() throws Exception {
        User user = new User("james@gmail.com", "kashu", "wentu");

        when(userRepository.findById("james@gmail.com")).thenReturn(Optional.of(RECORD2));
        when(userRepository.save(user)).thenReturn(user);
        String content = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/james@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("kashu")));
    }

    @Test
    public void deleteUser_success() throws Exception {

        when(userRepository.findById(RECORD2.getEmail())).thenReturn(Optional.of(RECORD2));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/james@gmail.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser_notFoundException() throws Exception {

        when(userRepository.findById(RECORD2.getEmail())).thenReturn(Optional.of(RECORD2));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/dddd@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
