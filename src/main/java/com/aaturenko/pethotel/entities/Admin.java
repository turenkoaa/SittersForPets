package com.aaturenko.pethotel.entities;

import com.aaturenko.pethotel.repositories.ReviewRepository;
import com.aaturenko.pethotel.repositories.UserRepository;
import com.aaturenko.pethotel.strategies.BlockStrategy;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Admin {

    private BlockStrategy blockStrategy = new AllDislikesBlockStrategy();

    public List<User> showUsersToBlock() {
        return blockStrategy.showUsersToBlock();
    }

    public User blockUser(User user){
        user.block();
        return user;
    }

    public class TenDislikesBlockStrategy implements BlockStrategy {

        public List<User> showUsersToBlock() {
            List<User> usersToBlock = new ArrayList<>();

            User.findAll().forEach(u -> {
                long dislikes = u.getReviewsAboutMe().stream()
                        .filter(r -> !r.isLike())
                        .count();
                if (dislikes > 10) usersToBlock.add(u);
            });

            return usersToBlock;
        }
    }

    public class AllDislikesBlockStrategy implements BlockStrategy {

        public List<User> showUsersToBlock() {
            return Review.showDislikedReviews()
                    .stream()
                    .map(Review::getUser)
                    .collect(toList());
        }
    }
}
