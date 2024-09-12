package com.fthon.save_track.event.application.service;

import com.fthon.save_track.event.application.dto.request.EventCreateRequest;
import com.fthon.save_track.event.application.dto.request.EventUpdateRequest;
import com.fthon.save_track.event.application.dto.response.getDatailEventResponse;
import com.fthon.save_track.event.persistence.Event;
import com.fthon.save_track.event.persistence.EventRepository;
import com.fthon.save_track.event.persistence.Subscription;
import com.fthon.save_track.event.persistence.SubscriptionRepository;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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
        eventRepository.save(event);

    }

    @Transactional
    public void deleteEvent(final Long eventId) {
        Event data = getEvent(eventId);
        eventRepository.deleteById(data.getId());
    }

    // 돌아오면 여기서부터 개발하면 됨

    public List<Event> getListEvent() {
        //Event event = getEvent(eventId);
        List<Event> event= eventRepository.findAll();
        return event;
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



}
