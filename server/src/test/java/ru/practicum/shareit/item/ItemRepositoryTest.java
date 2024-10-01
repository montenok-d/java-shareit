package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository requestRepository;

    private User user;
    private ItemRequest request;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Test User");
        user.setEmail("testuser@example.com");
        userRepository.save(user);

        request = new ItemRequest();
        request.setRequestor(user);
        request.setDescription("description");
        request.setCreated(LocalDateTime.now());
        requestRepository.save(request);

        item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequest(request);
        itemRepository.save(item);
    }

    @AfterEach
    void tearDown() {
        requestRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByRequestId() {
        List<Item> items = itemRepository.findByRequestId(request.getId());

        assertThat(items.size(), equalTo(1));
        assertThat(items.getFirst(), equalTo(item));
    }

    @Test
    void search() {
        List<Item> items = itemRepository.search("Test");

        assertThat(items.size(), equalTo(1));
        assertThat(items.getFirst(), equalTo(item));
    }
}