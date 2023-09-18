package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.NotFoundException;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.utils.ItemRequestServiceUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final RequestRepository repository;
    private final ItemRequestServiceUtils utils;

    @Override
    @Transactional
    public ItemRequestDto addNewRequest(long userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = utils.checkAndConvertToRequest(userId, itemRequestDto);

        log.debug("Sending to DAO new ItemRequest from user {}.", userId);
        return utils.convertToDto(repository.save(itemRequest));
    }

    @Override
    @Transactional
    public List<ItemRequestDto> getUserRequests(long userId) {
        utils.checkIsUserPresent(userId);

        log.debug("Sending to DAO request to get user {} item requests.", userId);
        return repository.findAllByPublisher(userId).stream()
                .map(utils::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ItemRequestDto> getOtherUsersRequests(long userId) {
        log.debug("Sending to DAO request from user {} to get other users ItemRequests.", userId);

        return repository.findAllByPublisherIsNotOrderByCreationDateDesc(userId).stream()
                .map(utils::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ItemRequestDto> getOtherUsersRequestsPagination(long userId, int from, int size) {
        log.debug("Sending to DAO request from user {} to get other users ItemRequests.", userId);
        if (size < 1) {
            throw new ValidationException("Size is too small.");
        }

        /*return repository.findAll().stream()
                .sorted(Comparator.comparing(ItemRequest::getCreationDate))
                .limit(from+size)
                .skip(from)
                .map(utils::convertToDto)
                .collect(Collectors.toList());*/
        return repository.findAllByPublisherIsNotOrderByCreationDateDesc(userId, PageRequest.of(from, size))
                .stream()
                .map(utils::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemRequestDto getRequest(long userId, long requestId) {
        utils.checkIsUserPresent(userId);
        log.debug("Sending to DAO request to get ItemRequest.");
        return utils.convertToDto(repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Item with id " + requestId + " does not present in repository.")));
    }
}
