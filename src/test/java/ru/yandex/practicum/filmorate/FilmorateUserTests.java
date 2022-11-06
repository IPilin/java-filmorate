package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateUserTests {
	private final UserDbStorage userStorage;

	private static User userOne;
	private static User userTwo;
	private static User userThree;

	@BeforeAll
	public static void beforeAll() {
		userOne = User.builder()
				.email("1@ya.ru")
				.login("user1")
				.name("Alex")
				.birthday(LocalDate.of(1999, 12, 18))
				.build();
		userTwo = User.builder()
				.email("2@ya.ru")
				.login("user2")
				.name("Nik")
				.birthday(LocalDate.of(1999, 10, 18))
				.build();
		userThree = User.builder()
				.email("3@ya.ru")
				.login("user3")
				.name("Vera")
				.birthday(LocalDate.of(1999, 1, 18))
				.build();
	}

	@Test
	@Order(1)
	public void createAndFindUser() throws IncorrectIdException {
		userStorage.add(userOne);
		assertThat(userOne.getId()).isEqualTo(1);
		assertThat(userStorage.find(userOne.getId())).isEqualTo(userOne);
	}

	@Test
	@Order(2)
	public void changeUser() throws IncorrectIdException {
		userOne.setName("Max");
		userStorage.update(userOne);
		assertThat(userStorage.find(userOne.getId())).isEqualTo(userOne);
	}

	@Test
	@Order(3)
	public void createAndFindFriends() throws IncorrectIdException {
		userStorage.add(userTwo);

		userStorage.addFriend(userOne.getId(), userTwo.getId());
		userStorage.addFriend(userTwo.getId(), userOne.getId());

		assertThat(userStorage.find(userOne.getId()).getFriends()).contains(userTwo.getId());
		assertThat(userStorage.find(userTwo.getId()).getFriends()).contains(userOne.getId());
	}

	@Test
	@Order(4)
	public void removeFriend() throws IncorrectIdException {
		userStorage.removeFriend(userOne.getId(), userTwo.getId());

		assertThat(userStorage.find(userOne.getId()).getFriends()).isEmpty();
		assertThat(userStorage.find(userTwo.getId()).getFriends()).isEmpty();
	}

	@Test
	@Order(5)
	public void getCommonFriends() throws IncorrectIdException {
		userStorage.add(userThree);

		userStorage.addFriend(userOne.getId(), userTwo.getId());
		userStorage.addFriend(userTwo.getId(), userOne.getId());

		userStorage.addFriend(userOne.getId(), userThree.getId());
		userStorage.addFriend(userThree.getId(), userOne.getId());

		userStorage.addFriend(userThree.getId(), userTwo.getId());
		userStorage.addFriend(userTwo.getId(), userThree.getId());

		assertThat(userStorage.getCommonFriends(userOne.getId(), userTwo.getId())).contains(userThree);
	}
}
