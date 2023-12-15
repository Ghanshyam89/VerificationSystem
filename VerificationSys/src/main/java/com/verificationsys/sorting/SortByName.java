package com.verificationsys.sorting;

import java.util.List;

import com.verificationsys.entities.User;

public class SortByName implements UserSortStrategy {
    private String sortOrder;

    public SortByName(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public void sort(List<User> users) {
    	
        users.sort((user1, user2) -> {
            int name1 = user1.getName().length();
            int name2 = user2.getName().length();
            
//            System.out.println(user1.getName()+" "+name1+"\n");
            
            if (sortOrder.equalsIgnoreCase("even")) {
				if (name1 % 2 == name2 % 2) {
					return Integer.compare(name1, name2);
				} else {
					return name1 % 2 == 0 ? -1 : 1;
				}
			} else {
				if (name1 % 2 == name2 % 2) {
					return Integer.compare(name1, name2);
				} else {
					return name1 % 2 != 0 ? -1 : 1;
				}
			}
        });
    }
}