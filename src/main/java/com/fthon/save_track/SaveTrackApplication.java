package com.fthon.save_track;

import com.fthon.save_track.badge.persistence.Badge;
import com.fthon.save_track.badge.persistence.IndividualCategoryCountStrategy;
import com.fthon.save_track.badge.persistence.TotalCategoryCountStrategy;
import com.fthon.save_track.badge.repository.BadgeRepository;
import com.fthon.save_track.event.persistence.Category;
import com.fthon.save_track.event.persistence.CategoryRepository;
import com.fthon.save_track.event.persistence.Event;
import com.fthon.save_track.event.persistence.EventRepository;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class SaveTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaveTrackApplication.class, args);
	}


	@Component
	@RequiredArgsConstructor
	@Profile("!test")
	protected class InitCommandLineRunner implements CommandLineRunner{

		private final UserRepository userRepository;
		private final CategoryRepository categoryRepository;
		private final EventRepository eventRepository;
		private final BadgeRepository badgeRepository;


		@Override
		public void run(String... args) throws Exception {
			userRepository.deleteAll();
			badgeRepository.deleteAll();
			eventRepository.deleteAll();
			categoryRepository.deleteAll();

			User user = new User("테스트 유저", 12312421L, "test@email.com", "asdsadsad");
			Category category1 = new Category("category-001", "물 절약");
			Category category2 = new Category("category-001", "돈 절약");

			Event event1 = new Event(category1, List.of(), false, "물 아껴쓰기", "내용", "메시지1", "메시지2", "메시지3", List.of(DayOfWeek.SATURDAY));
			Event event2 = new Event(category2, List.of(), false, "배달 음식 참기", "내용", "메시지1", "메시지2", "메시지3", List.of(DayOfWeek.SATURDAY));

			Badge badge1 = new Badge("물 절약왕", new IndividualCategoryCountStrategy(2, category1));
			Badge badge2 = new Badge("돈 절약왕", new IndividualCategoryCountStrategy(2, category2));
			Badge badge3 = new Badge("전체 절약왕", new TotalCategoryCountStrategy(3));

			categoryRepository.saveAll(List.of(category1, category2));
			eventRepository.saveAll(List.of(event1, event2));
			badgeRepository.saveAll(List.of(badge1, badge2, badge3));
			userRepository.save(user);
		}
	}

}