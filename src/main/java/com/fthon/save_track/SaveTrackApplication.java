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
			Category category1 = new Category("category-001", "전기 절약");
			Category category2 = new Category("category-002", "돈 절약");
			Category category3 = new Category("category-003", "시간 절약");

			Event event1 = new Event(category1, List.of(), false, "물 아껴쓰기", "내용", "메시지1", "메시지2", "메시지3", List.of(DayOfWeek.SATURDAY));
			Event event2 = new Event(category2, List.of(), false, "배달 음식 참기", "내용", "메시지1", "메시지2", "메시지3", List.of(DayOfWeek.SATURDAY));

			List<Badge> badge1 = List.of(
					new Badge("electric1", new IndividualCategoryCountStrategy(1, category1)),
					new Badge("electric5", new IndividualCategoryCountStrategy(5, category1)),
					new Badge("electric10", new IndividualCategoryCountStrategy(10, category1)),
					new Badge("electric50", new IndividualCategoryCountStrategy(50, category1)),
					new Badge("electric100", new IndividualCategoryCountStrategy(100, category1))
			);

			List<Badge> badge2 = List.of(
					new Badge("money1", new IndividualCategoryCountStrategy(1, category2)),
					new Badge("money5", new IndividualCategoryCountStrategy(5, category2)),
					new Badge("money10", new IndividualCategoryCountStrategy(10, category2)),
					new Badge("money50", new IndividualCategoryCountStrategy(50, category2)),
					new Badge("money100", new IndividualCategoryCountStrategy(100, category2))
			);

			List<Badge> badge3 = List.of(
					new Badge("time1", new IndividualCategoryCountStrategy(1, category3)),
					new Badge("time5", new IndividualCategoryCountStrategy(5, category3)),
					new Badge("time10", new IndividualCategoryCountStrategy(10, category3)),
					new Badge("time50", new IndividualCategoryCountStrategy(50, category3)),
					new Badge("time100", new IndividualCategoryCountStrategy(100, category3))
			);

			categoryRepository.saveAll(List.of(category1, category2, category3));
			eventRepository.saveAll(List.of(event1, event2));

			badgeRepository.saveAll(badge1);
			badgeRepository.saveAll(badge2);
			badgeRepository.saveAll(badge3);
			userRepository.save(user);
		}
	}

}