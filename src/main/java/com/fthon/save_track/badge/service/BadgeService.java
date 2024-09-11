package com.fthon.save_track.badge.service;


import com.fthon.save_track.badge.dto.AcquiredBadgeDto;
import com.fthon.save_track.badge.persistence.Badge;
import com.fthon.save_track.badge.repository.BadgeRepository;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BadgeService {

    private final BadgeRepository badgeRepository;

    /**
     * 특정 이벤트를 완료한 후 실행하는 메서드로, 사용자가 뱃지를 얻을 수 있는지 확인하고 얻은 뱃지를 DB에 저장 후 해당 뱃지 정보를 반환합니다
     * @author minseok kim
     * @return 사용자가 이 메서드를 실행함으로써 얻은 뱃지의 정보를 반환합니다.
    */
    public List<AcquiredBadgeDto> updateUserBadges(User user){
        List<Badge> badges = badgeRepository.findBadgesNotAcquired(user);

        List<AcquiredBadgeDto> result = new ArrayList<>(badges.size());

        badges.stream().filter((b)->b.getStrategy().check(user)).forEach(acquireableBadge ->{
            user.addBadge(acquireableBadge);
            result.add(AcquiredBadgeDto.of(acquireableBadge));
        });


        return result;
    }


}