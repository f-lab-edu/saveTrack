package com.fthon.save_track.event.application.service;

import com.fthon.save_track.common.exceptions.BadRequestException;
import com.fthon.save_track.event.application.dto.request.EventCreateRequest;
import com.fthon.save_track.event.application.dto.request.EventUpdateRequest;
import com.fthon.save_track.event.application.dto.response.EventSearchResponse;
import com.fthon.save_track.event.application.dto.response.getDatailEventResponse;
import com.fthon.save_track.event.persistence.Event;
import com.fthon.save_track.event.persistence.EventRepository;
import com.fthon.save_track.event.persistence.Subscription;
import com.fthon.save_track.event.persistence.SubscriptionRepository;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.persistence.UserEventLog;
import com.fthon.save_track.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ModelMapper modelMapper;
    @Autowired
    ApplicationEventPublisher publisher;

    @Transactional
    public void createEvent(final EventCreateRequest request) {
        Event entity = modelMapper.map(request, Event.class);
        eventRepository.save(entity);
    }

    @Transactional
    public void updateEvent(final EventUpdateRequest request, final Long eventId) {
        Event data = getEvent(eventId);
        Event event = data.update(request);
    }

    @Transactional
    public void deleteEvent(final Long eventId) {
        eventRepository.softDeleteEvent(eventId);
    }

    // 돌아오면 여기서부터 개발하면 됨

    public List<EventSearchResponse> getListEvent(int page, int size, Long categoryId) {
        //Event event = getEvent(eventId);
        if (categoryId == null) {
            Page<Event> data = eventRepository.findAll(PageRequest.of(page, size));

            return data.getContent().stream().map(e->new EventSearchResponse(
                    e.getId(),
                    e.getEventName(),
                    e.getEventContent(),
                    e.getDaysOfWeek(),
                    e.getSubscribeEntity().size()
            )).toList();
        }
            Page<Event> data = eventRepository.findByCategoryId(PageRequest.of(page, size), categoryId);
        return data.getContent().stream().map(e->new EventSearchResponse(
                e.getId(),
                e.getEventName(),
                e.getEventContent(),
                e.getDaysOfWeek(),
                e.getSubscribeEntity().size()
        )).toList();
    }

    public Event getDetailEvent(Long eventId) {
        Event event = getEvent(eventId);
        return event;
    }

    private Event getEvent(Long eventId) {
        return eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("no found eventId : " + eventId));
    }

    @Transactional
    public void subscribe(Long evnetId, Long userId) {

        Event event = eventRepository.findById(evnetId)
                .orElseThrow(() -> new EntityNotFoundException());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException());

        Subscription subscription = Subscription.builder()
                .eventEntity(event)
                .userEntity(user)
                .build();
        subscriptionRepository.save(subscription);
    }
    // 여기서 Event사용해서 이벤트 목표를 달성했다면 ? isChecked를 false -> true로 바꾸는 로직 추가해 보려고 합니다.


    @Transactional
    public void finishEvent(Long eventId, Long userId) throws BadRequestException {
        Optional<Subscription> data = userRepository.findCurrentSubscription(userId, eventId, ZonedDateTime.now());
        if (data.isEmpty()){
            throw new BadRequestException("구독하지 않은 이벤트입니다.");
        }
        Subscription subscription = data.get();

        subscription.getLogs().stream().filter(l-> LocalDate.now().isEqual(l.getCreatedAt().toLocalDate()))
                .findAny().ifPresent((s)->{
                    throw new BadRequestException("이미 완료 체크한 이벤트입니다.");
                });

        subscription.addLog(true);
    }


}
