package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ItemException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceDB implements ItemService {

    private final ItemRepository itemRepo;
    private final UserService userService;
    private final CommentRepository commentRepo;
    private final BookingRepository bookingRepo;

    @Override
    public List<ItemDto> getUserItems(long id, Integer from, Integer size) {
        userService.getUser(id);
        List<Item> items = itemRepo.findAllByOwner(id, PageRequest.of(from / size, size)).getContent().stream()
                .sorted(Comparator.comparing(Item::getId))
                .collect(Collectors.toList());
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            List<Comment> comments = commentRepo.findAllByItemIdOrderByCreatedDesc(item.getId());
            ItemDto dto = ItemMapper.makeItemDto(item);
            fillWithBookings(dto);
            fillWithCommentDtos(comments);
            itemDtos.add(dto);
        }
        log.info("Getting all Items of User: {}", id);
        return itemDtos;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long id) {
        userService.getUser(id);
        Item item = ItemMapper.makeItem(itemDto);
        item.setOwner(id);
        log.info("Adding Item to User: {}", id);
        return ItemMapper.makeItemDto(itemRepo.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long id) {
        Item item = itemRepo.findById(itemId).orElseThrow(() ->
                new ItemException("Item with id: " + itemId + " not found"));
        if (item.getOwner() != id) {
            throw new ItemException(
                    String.format("Item with id: %s not belongs to user with id: %s", itemId, id));
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        log.info("Item {} has been updated", itemId);
        return ItemMapper.makeItemDto(itemRepo.save(item));
    }

    @Override
    public List<ItemDto> searchItem(String request, Integer from, Integer size) {
        if (request.isEmpty()) {
            return new ArrayList<>();
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<Item> items = itemRepo.findItemByText(request, pageable).getContent();
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items) {
            itemsDto.add(ItemMapper.makeItemDto(item));
        }
        log.info("Searching Item with keyword {}", request);
        return itemsDto;
    }

    @Override
    public ItemDto getItem(long itemId, Long userId) {
        Item item = itemRepo.findById(itemId).orElseThrow(() ->
                new ItemException("Item with id: " + itemId + " not found"));
        ItemDto dto = ItemMapper.makeItemDto(item);
        if (item.getOwner().equals(userId)) {
            fillWithBookings(dto);
        }
        dto.setComments(fillWithCommentDtos(commentRepo.findAllByItemIdOrderByCreatedDesc(itemId)));
        log.info("Getting Item with ID: {}", userId);
        return dto;
    }

    private ItemDto fillWithBookings(ItemDto itemDto) {
        Optional<Booking> lastBooking = bookingRepo.findLastBooking(itemDto.getId(), LocalDateTime.now());
        itemDto.setLastBooking(lastBooking.orElse(null));
        Optional<Booking> nextBooking = bookingRepo.findNextBooking(itemDto.getId(), LocalDateTime.now());
        itemDto.setNextBooking(nextBooking.orElse(null));
        return itemDto;
    }

    private List<CommentDto> fillWithCommentDtos(List<Comment> comments) {
        List<CommentDto> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            dtos.add(makeFullCommentDto(comment));
        }
        return dtos;
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long userId, Long itemId) {
        UserDto userDto = userService.getUser(userId);
        getItem(itemId, userId);
        Booking booking = bookingRepo.findItemIdByBooker(userId, itemId).orElseThrow(() ->
                new BadRequestException("User: " + userId + " not uses this item"));
        if (booking.getStatus().equals(Status.REJECTED)) {
            throw new BadRequestException("User: " + userId + " not uses this item");
        }
        if (!booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Comment only after booking end time expired");
        }
        Comment comment = CommentMapper.makeComment(commentDto);
        comment.setAuthorId(userId);
        comment.setItemId(itemId);
        CommentDto dto = makeFullCommentDto(commentRepo.save(comment));
        dto.setAuthorName(userDto.getName());
        log.info("Comment added with text {}", dto.getText());
        return dto;
    }

    private CommentDto makeFullCommentDto(Comment comment) {
        CommentDto dto = CommentMapper.makeCommentDto(comment);
        dto.setAuthorName(userService.getUser(comment.getAuthorId()).getName());
        return dto;
    }
}
