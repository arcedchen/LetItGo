package com.letitgo.shop.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letitgo.shop.model.entity.Report;

public interface ReportDao extends JpaRepository<Report, Integer> {

	// 根據 report 內容修改 report 狀態
	Report findByReportId(Integer reportId);

}
