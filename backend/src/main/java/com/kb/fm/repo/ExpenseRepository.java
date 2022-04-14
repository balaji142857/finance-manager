package com.kb.fm.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kb.fm.entities.Expense;
import com.kb.fm.web.model.ChartData;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {
	
	public List<Expense> findAllByOrderByTransactionDate();
	
	@Query("select new com.kb.fm.web.model.ChartData(to_char(e.transactionDate, 'YYYY-MM'), sum(e.amount))"
			+ "from Expense e "
			+ "group by to_char(e.transactionDate, 'YYYY-MM') "
			+ "order by to_char(e.transactionDate,'YYYY-MM')")
	public List<ChartData> getExpensesByYearMonth();
	
	
	@Query("select new com.kb.fm.web.model.ChartData(to_char(e.transactionDate, 'YYYY-MM'), sum(e.amount)) "
			+ "from Expense e "
			+ "where e.transactionDate  > ?1 "
			+ "group by to_char(e.transactionDate, 'YYYY-MM') "
			+ "order by to_char(e.transactionDate, 'YYYY-MM')")
	public List<ChartData> getExpensesByYearMonthFrom(Date fromDate);
	
	@Query("select new com.kb.fm.web.model.ChartData(to_char(e.transactionDate, 'YYYY-MM'), sum(e.amount)) "
			+ "from Expense e "
			+ "where e.transactionDate  < ?1 "
			+ "group by to_char(e.transactionDate, 'YYYY-MM') "
			+ "order by to_char(e.transactionDate, 'YYYY-MM')")
	public List<ChartData> getExpensesByYearMonthUpto(Date to);
	
	@Query("select new com.kb.fm.web.model.ChartData(to_char(e.transactionDate, 'YYYY-MM'), sum(e.amount)) "
			+ "from Expense e "
			+ "where e.transactionDate  between ?1 and ?2 "
			+ "group by to_char(e.transactionDate, 'YYYY-MM') "
			+ "order by to_char(e.transactionDate, 'YYYY-MM')")
	public List<ChartData> getExpensesByYearMonthDuring(Date fromDate, Date toDate);
	
	
	@Query("select new com.kb.fm.web.model.ChartData(to_char(e.transactionDate, 'MON-DD'), sum(e.amount), e.transactionDate) "
			+ "from Expense e "
			+ "group by e.transactionDate "
			+ "order by e.transactionDate")
	public List<ChartData> getExpensesByMonthDate();
	
	@Query("select new com.kb.fm.web.model.ChartData(to_char(e.transactionDate, 'MON-DD'), sum(e.amount), e.transactionDate) "
			+ "from Expense e "
			+ "where e.transactionDate  > ?1 "
			+ "group by e.transactionDate "
			+ "order by e.transactionDate")
	public List<ChartData> getExpensesByMonthDateFrom(Date fromDate);
	
	@Query("select new com.kb.fm.web.model.ChartData(to_char(e.transactionDate, 'MON-DD'), sum(e.amount), e.transactionDate) "
			+ "from Expense e "
			+ "where e.transactionDate  < ?1 "
			+ "group by e.transactionDate "
			+ "order by e.transactionDate")
	public List<ChartData> getExpensesByMonthDateUpto(Date to);
	
	@Query("select new com.kb.fm.web.model.ChartData(to_char(e.transactionDate, 'MON-DD'), sum(e.amount), e.transactionDate) "
			+ "from Expense e "
			+ "where e.transactionDate  between ?1 and ?2 "
			+ "group by e.transactionDate "
			+ "order by e.transactionDate")
	public List<ChartData> getExpensesByMonthDateDuring(Date fromDate, Date toDate);

}
