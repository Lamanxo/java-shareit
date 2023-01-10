package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.ItemException;
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

@Service
@Qualifier("ItemServiceDB")
public class ItemServiceDB implements ItemService {

    private final ItemRepository itemRepo;
    private final UserService userService;
    private final CommentRepository commentRepo;
    private final BookingRepository bookingRepo;

    public ItemServiceDB(ItemRepository itemRepo, @Qualifier("UserServiceDB") UserService userService, CommentRepository commentRepo, BookingRepository bookingRepo) {
        this.itemRepo = itemRepo;
        this.userService = userService;
        this.commentRepo = commentRepo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public List<ItemDto> getUserItems(long id) {
        userService.getUser(id);
        List<Item> items = itemRepo.findAllByOwner(id).stream()
                .sorted(Comparator.comparing(Item::getId))
                .collect(Collectors.toList());
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            List<Comment> comments = commentRepo.findAllByItemIdOrderByCreatedDesc(item.getId());
            ItemDto dto = makeItemDto(item);
            fillWithBookings(dto);
            fillWithCommentDtos(comments);
            itemDtos.add(dto);
        }
        return itemDtos;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long id) {
        userService.getUser(id);
        Item item = makeItem(itemDto);
        item.setOwner(id);
        return makeItemDto(itemRepo.save(item));
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

        return makeItemDto(itemRepo.save(item));
    }

    @Override
    public List<ItemDto> searchItem(String request) {
        if (request.isEmpty()) {
            return new ArrayList<>();
        }
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : itemRepo.findItemByText(request)) {
            itemsDto.add(makeItemDto(item));
        }
        return itemsDto;
    }

    @Override
    public ItemDto getItem(long itemId, Long userId) {
        Item item = itemRepo.findById(itemId).orElseThrow(() ->
                new ItemException("Item with id: " + itemId + " not found"));
        ItemDto dto = makeItemDto(item);
        if(item.getOwner().equals(userId)) {
            fillWithBookings(dto);
        }
        dto.setComments(fillWithCommentDtos(commentRepo.findAllByItemIdOrderByCreatedDesc(itemId)));
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
            dtos.add(makeCommentDto(comment));
        }
        return dtos;
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long userId, Long itemId) {
        UserDto userDto = userService.getUser(userId);
        getItem(itemId, userId);
        Booking booking = bookingRepo.findItemByBooker(userId, itemId).orElseThrow(() ->
                new BadRequestException("User: " + userId + " not uses this item"));
        if (booking.getStatus().equals(Status.REJECTED)) {
            throw new BadRequestException("User: " + userId + " not uses this item");
        }
        if (!booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Comment only after booking end time expired");
        }
        Comment comment = makeComment(commentDto);
        comment.setAuthorId(userId);
        comment.setItemId(itemId);
        CommentDto dto = makeCommentDto(commentRepo.save(comment));
        dto.setAuthorName(userDto.getName());
        return dto;
    }

    private ItemDto makeItemDto(Item item) {
        return ItemDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(new ArrayList<CommentDto>())
                .build();
    }

    private Item makeItem(ItemDto itemDto) {
        return Item.builder().id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    private CommentDto makeCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .authorName(userService.getUser(comment.getAuthorId()).getName())
                .build();
    }

    private Comment makeComment(CommentDto cDto) {
        return Comment.builder()
                .text(cDto.getText())
                .created(LocalDateTime.now())
                .build();
    }
}
