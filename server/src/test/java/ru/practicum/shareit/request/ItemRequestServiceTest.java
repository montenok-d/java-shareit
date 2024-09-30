package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.EntityNotFoundException;
import ru.practicum.shareit.request.dto.ItemWebResponceDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class ItemRequestServiceTest {
    private final ItemRequestServiceImpl itemRequestService;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Name")
                .email("mail@mail.ru")
                .build();
        userRepository.save(user);
    }

    @Test
    void findByIdTest() {
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Description")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();
        itemRequestRepository.save(itemRequest);

        ItemWebResponceDto returnedRequest = itemRequestService.findById(itemRequest.getId());

        assertThat(returnedRequest.getId()).isEqualTo(itemRequest.getId());
        assertThat(returnedRequest.getDescription()).isEqualTo("Description");
    }

    @Test
    void findByIdNotFoundTest() {
        EntityNotFoundException thrown = org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () ->
                itemRequestService.findById(9999));
        assertEquals(String.format("ItemRequest № %d not found", 9999), thrown.getMessage());
    }

    @Test
    void findAllByOwnerTest() {
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Description New")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();
        itemRequestRepository.save(itemRequest);

        List<ItemWebResponceDto> requests = itemRequestService.findAllByOwner(user.getId());

        assertThat(requests).hasSize(1);
        assertThat(requests.getFirst().getDescription().equals("Description New"));
    }

    @Test
    void createItemRequestTest() {
        ItemWebResponceDto requestDto = ItemWebResponceDto.builder()
                .description("Description")
                .build();

        ItemWebResponceDto savedRequestDto = itemRequestService.create(user.getId(), requestDto);

        Optional<ItemRequest> requestFromRepository = itemRequestRepository.findById(savedRequestDto.getId());
        assertThat(requestFromRepository).isPresent();
        assertThat(requestFromRepository.get().getDescription()).isEqualTo("Description");
    }

    @Test
    void getAllTest() {
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Description")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();
        itemRequestRepository.save(itemRequest);

        List<ItemWebResponceDto> requests = itemRequestService.findAll();

        assertThat(requests).hasSize(1);
    }
}