package com.m_takahisa.taskapp.task.notification;

import com.m_takahisa.taskapp.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("通知が見つかりません ID: " + id));
        notification.setRead(true);
        // @Transactional があるため、saveを呼ばなくても自動で更新されます
    }
}
