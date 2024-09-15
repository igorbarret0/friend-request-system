package com.ms_ma_backend_test.ms_ma.repository;

import com.ms_ma_backend_test.ms_ma.entity.FriendShip;
import com.ms_ma_backend_test.ms_ma.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {

    Optional<FriendShip> findByRequesterAndFriend(User requester, User friend);

}
