package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapping;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto findById(long id) {
        ItemRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("ItemRequest № %d not found", id)));
        List<ItemDto> itemsDto = itemRepository.findByRequestId(request.getId()).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
        ItemRequestDto requestDto = RequestMapping.mapToRequestDto(request);
        requestDto.setItems(itemsDto);
        return requestDto;
    }

    @Override
    public List<ItemRequestDto> findAllByOwner(long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new EntityNotFoundException(String.format("User № %d not found", ownerId));
        }
        List<ItemRequest> requests = requestRepository.findByRequestorId(ownerId);
        List<ItemRequestDto> requestsDto = requests.stream()
                .map(RequestMapping::mapToRequestDto)
                .toList();
        for (ItemRequestDto request : requestsDto) {
            List<ItemDto> itemsDto = itemRepository.findByRequestId(request.getId()).stream()
                    .map(ItemMapper::mapToItemDto)
                    .toList();
            request.setItems(itemsDto);
        }
        return requestsDto;
    }

    @Override
    public List<ItemRequestDto> findAll() {
        List<ItemRequest> requests = requestRepository.findAll();
        List<ItemRequestDto> requestsDto = requests.stream()
                .map(RequestMapping::mapToRequestDto)
                .toList();
        return requestsDto;
    }

    @Transactional
    @Override
    public ItemRequestDto create(long ownerId, ItemRequestDto requestDto) {
        User requestor = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User № %d not found", ownerId)));
        ItemRequest itemRequest = RequestMapping.mapToRequest(requestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(requestor);
        itemRequest = requestRepository.save(itemRequest);
        return RequestMapping.mapToRequestDto(itemRequest);
    }
}
