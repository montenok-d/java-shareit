package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.EntityNotFoundException;
import ru.practicum.shareit.error.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class ItemServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private User user;
    private User owner;
    private Item item;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Name")
                .email("mail2@mail.ru")
                .build();

        owner = User.builder()
                .name("Name")
                .email("mail3@mail.ru")
                .build();

        item = Item.builder()
                .name("Item")
                .description("Description")
                .available(true)
                .owner(owner)
                .request(null)
                .build();

        request = ItemRequest.builder()
                .description("Request description")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        userRepository.save(user);
        userRepository.save(owner);
        itemRepository.save(item);
        itemRequestRepository.save(request);
    }

    @Test
    void findByIdTest() {
        ItemDto itemDto = itemService.getById(item.getId());

        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
    }

    @Test
    void findByIdNotFoundTest() {
        EntityNotFoundException thrown = org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () ->
                itemService.getById(9999));
        assertEquals(String.format("Item № %d not found", 9999), thrown.getMessage());
    }

    @Test
    void createTest() {
        ItemDto itemCreateDto = ItemDto.builder()
                .name("New Item")
                .description("New description")
                .available(true)
                .requestId(request.getId())
                .build();

        ItemDto createdItem = itemService.create(itemCreateDto, owner.getId());

        assertEquals("New Item", createdItem.getName());
        assertEquals("New description", createdItem.getDescription());
    }

    @Test
    void itemCreateWithUnavailableRequestIdTest() {
        ItemDto itemCreateDto = ItemDto.builder()
                .name("TestCreatedItem")
                .description("Test created item description")
                .available(true)
                .requestId(2L)
                .build();

        EntityNotFoundException thrown = Assertions.assertThrows(EntityNotFoundException.class, () ->
                itemService.create(itemCreateDto, owner.getId()));
        assertEquals(String.format("Item № %d not found", itemCreateDto.getRequestId()), thrown.getMessage());
    }

    @Test
    void updateTest() {
        ItemDto itemUpdateDto = ItemDto.builder()
                .id(item.getId())
                .name("Updated Item")
                .description("Updated description")
                .available(true)
                .owner(owner.getId())
                .build();

        ItemDto updatedItem = itemService.update(itemUpdateDto, itemUpdateDto.getId(), itemUpdateDto.getOwner());

        assertEquals("Updated Item", updatedItem.getName());
        assertEquals("Updated description", updatedItem.getDescription());
    }

    @Test
    void deleteTest() {
        ItemDto item = ItemDto.builder()
                .name("New Item")
                .description("New description")
                .available(true)
                .build();
        ItemDto createdItem = itemService.create(item, owner.getId());

        itemService.delete(createdItem.getId(), createdItem.getOwner());

        AssertionsForClassTypes.assertThat(itemRepository.findById(user.getId()).isEmpty());
    }

    @Test
    void deleteWrongUserTest() {
        ItemDto item = ItemDto.builder()
                .name("New Item")
                .description("New description")
                .available(true)
                .build();
        ItemDto createdItem = itemService.create(item, owner.getId());

        EntityNotFoundException thrown = Assertions.assertThrows(EntityNotFoundException.class, () ->
                itemService.delete(createdItem.getId(), user.getId()));
        assertEquals("Access denied", thrown.getMessage());
    }

    @Test
    void getAllByOwnerTest() {
        List<ItemWithBookingDto> items = itemService.getAll(owner.getId());

        assertThat(items).hasSize(1);
        assertThat(items.getFirst().getId()).isEqualTo(item.getId());
        assertEquals(items.getFirst().getName(), item.getName());
    }

    @Test
    void searchItemByTextTest() {
        List<ItemDto> itemDtoList = itemService.search("Item");

        assertThat(itemDtoList).hasSize(1);
        assertEquals(itemDtoList.getFirst().getName(), item.getName());
        assertEquals(itemDtoList.getFirst().getDescription(), item.getDescription());
    }

    @Test
    void searchItemByTextIsBlankTest() {
        List<ItemDto> itemDtoList = itemService.search("");

        assertThat(itemDtoList).hasSize(0);
    }

    @Test
    void addCommentTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), item, user, Status.APPROVED);
        bookingRepository.save(booking);
        CommentDto commentCreateDto = CommentDto.builder()
                .text("comment")
                .created(LocalDateTime.now())
                .authorName(user.getName())
                .item(item.getId())
                .build();

        CommentDto savedComment = itemService.addComment(item.getId(), user.getId(), commentCreateDto);

        assertEquals("comment", savedComment.getText());
        assertEquals(user.getName(), savedComment.getAuthorName());
        assertEquals(item.getId(), savedComment.getItem());
    }

    @Test
    void addCommentWrongUserTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), item, user, Status.REJECTED);
        bookingRepository.save(booking);
        CommentDto commentCreateDto = CommentDto.builder()
                .text("comment")
                .created(LocalDateTime.now())
                .authorName(user.getName())
                .item(item.getId())
                .build();

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () ->
                itemService.addComment(item.getId(), user.getId(), commentCreateDto));
        assertEquals("This user cannot create comment on item", thrown.getMessage());
    }
}