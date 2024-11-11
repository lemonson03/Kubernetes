package com.example.demo;

import com.example.demo.entity.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
    List<ChecklistItem> findByDiaryId(Long diaryId);  // 다이어리 ID로 체크리스트 항목 조회
}
