package com.m_takahisa.taskapp.service;

import com.m_takahisa.taskapp.entity.Notification;
import com.m_takahisa.taskapp.entity.Task;
import com.m_takahisa.taskapp.entity.TaskStatus;
import com.m_takahisa.taskapp.repository.NotificationRepository;
import com.m_takahisa.taskapp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskBatchService {

    private final TaskRepository taskRepository;
    private final NotificationRepository notificationRepository;

    /**
     * 期限が近い未完了タスクを抽出し、通知テーブルに保存する
     * (cron = "秒 分 時 日 月 曜日")
     * (cron = "0 * * * * *") 毎分 0秒
     * (cron = "0 0 9 * * *") 毎日 9時00分00秒
     * (cron = "0 0 0 * * MON") 毎週月曜日の深夜0時
     */
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional // DB保存を伴うため追加
    public void reportUrgentTasks() {
        log.info("--- 定期タスクチェック開始 ---");

        // 期限切れ・期限間近の未完了タスクを取得
        List<Task> urgentTasks = taskRepository.findByDueDateLessThanEqualAndStatusNot(
                LocalDate.now(),
                TaskStatus.DONE
        );

        if (urgentTasks.isEmpty()) {
            log.info("通知対象のタスクはありません。");
        } else {
            // 抽出されたタスクごとに通知レコードを作成
            urgentTasks.forEach(task -> {
                // すでに同じタスクに対する未読通知があるか確認
                if (!notificationRepository.existsByTaskIdAndIsReadFalse(task.getId())) {
                    Notification notification = new Notification();
                    notification.setTask(task); // Taskをセット
                    notification.setUser(task.getUser());
                    notification.setMessage("【期限リマインド】タスク「" + task.getTitle() + "」が期限間近です。");

                    notificationRepository.save(notification);
                    log.info("新規通知を作成しました: {}", task.getTitle());
                } else {
                    log.info("既に未読通知があるためスキップしました: {}", task.getTitle());
                }
            });
            log.warn("{} 件の通知を作成しました。", urgentTasks.size());
        }

        log.info("--- 定期タスクチェック終了 ---");
    }
}