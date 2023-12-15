package com.verificationsys.sorting;

import java.util.List;

import com.verificationsys.entities.User;

public interface UserSortStrategy {
	void sort(List<User> users);
}
