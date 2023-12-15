package com.verificationsys.sorting;

import java.util.List;

import com.verificationsys.entities.User;


public class SortByAge implements UserSortStrategy {
	private String sortOrder;

	public SortByAge(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public void sort(List<User> users) {
		users.sort((user1, user2) -> {
			int age1 = user1.getAge();
			int age2 = user2.getAge();
			if (sortOrder.equalsIgnoreCase("even")) {
				if (age1 % 2 == age2 % 2) {
					return Integer.compare(age1, age2);
				} else {
					return age1 % 2 == 0 ? -1 : 1;
				}
			} else {
				if (age1 % 2 == age2 % 2) {
					return Integer.compare(age1, age2);
				} else {
					return age1 % 2 != 0 ? -1 : 1;
				}
			}
		});
	}
}