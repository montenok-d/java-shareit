package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.EntityNotFoundException;
import ru.practicum.shareit.error.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public ItemDto getById(long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Item № %d not found", id)));
        ItemDto itemDto = ItemMapper.mapToItemDto(item);
        List<Comment> comments = commentRepository.findByItemId(id);
        List<CommentDto> commentsDto = comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .toList();
        itemDto.setComments(commentsDto);
        Booking lastBooking = bookingRepository.findLastBooking(itemDto.getId(), LocalDateTime.now());
        Booking nextBooking = bookingRepository.findNextBooking(itemDto.getId(), LocalDateTime.now());
        itemDto.setLastBooking((lastBooking != null) ? lastBooking.getStart() : null);
        itemDto.setNextBooking((nextBooking != null) ? nextBooking.getStart() : null);
        return itemDto;
    }

    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User № %d not found", ownerId)));
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() > 0) {
            itemRequest = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Item № %d not found", itemDto.getRequestId())));
        }
        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(owner);
        item.setRequest(itemRequest);
        Item newItem = itemRepository.save(item);
        return ItemMapper.mapToItemDto(newItem);
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, long itemId, long userId) {
        Item item = checkItemExists(itemId);
        if (item.getOwner().getId() != userId) {
            throw new EntityNotFoundException("Access denied");
        }
        itemDto.setId(itemId);
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        Item updatedItem = itemRepository.save(item);
        return ItemMapper.mapToItemDto(updatedItem);
    }

    @Override
    public void delete(long itemId, long userId) {
        Item item = checkItemExists(itemId);
        if (item.getOwner().getId() != userId) {
            throw new EntityNotFoundException("Access denied");
        }
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemWithBookingDto> getAll(long ownerId) {
        List<Item> allItems = itemRepository.findByOwnerId(ownerId);
        List<ItemWithBookingDto> allItemDtos = allItems.stream()
                .map(ItemMapper::mapToItemWithBookingDto)
                .toList();
        LocalDateTime currentTime = LocalDateTime.now();
        for (ItemWithBookingDto itemDto : allItemDtos) {
            Booking lastBooking = bookingRepository.findLastBooking(itemDto.getId(), currentTime);
            Booking nextBooking = bookingRepository.findNextBooking(itemDto.getId(), currentTime);
            itemDto.setLastBooking((lastBooking != null) ? lastBooking.getStart() : null);
            itemDto.setNextBooking((nextBooking != null) ? nextBooking.getStart() : null);
        }
        return allItemDtos;
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> searchResults = itemRepository.search(text);
        return searchResults.stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto addComment(long itemId, long userId, CommentDto commentDto) {
        Item item = checkItemExists(itemId);
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User № %d not found", userId)));
        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(userId, itemId, Status.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("This user cannot create comment on item");
        }
        Comment comment = CommentMapper.mapToComment(commentDto);
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        return CommentMapper.mapToCommentDto(comment);
    }

    public Item checkItemExists(long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Item № %d not found", id)));
    }
}
