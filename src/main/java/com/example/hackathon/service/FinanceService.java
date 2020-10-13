package com.example.hackathon.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hackathon.model.StockWrapper;
import com.example.hackathon.model.Trade;
import com.example.hackathon.repository.StockRepository;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;


@Service
public class FinanceService {
	
	@Autowired
	private StockRepository stockRepository;
	private Trade t;
	
	public Trade getID(ObjectId id) {
		return stockRepository.findBy_id(id);
	}
	
	public List<Trade> getTrades() {
		return stockRepository.findAll();
	}
	
	public List<Trade> getTradeTicker(String ticker) {
		return stockRepository.findByTicker(ticker);
	}
	
	public List<Trade> getTradeBuy(String type) {
		return stockRepository.findByType(type);
	}
	
	public List<String> getTradebyName() {
		int i = stockRepository.findAll().size();
		List<String> ticks = new ArrayList<>();
		for(int j = 0; j<i; j++) {
			ticks.add(stockRepository.findAll().get(j).getTicker());
		}
		return ticks;
	}
	
	public void sellTrade(Trade trade) {
		stockRepository.save(trade);
	}
	
	public Trade buyTrade(Trade trade) {
		stockRepository.save(trade);
		return trade;
	}
	
	public StockWrapper findStock(final String ticker) {
        try {
            return new StockWrapper(YahooFinance.get(ticker));
        }
        catch (IOException e) {
            System.out.println("Error");
        }
        return null;
    }
	
	
	public BigDecimal findPrice(final StockWrapper stock) throws IOException {
        return stock.getStock().getQuote(true).getPrice();
    }
	
	public List<HistoricalQuote> findHistory(final StockWrapper stock) throws IOException {
        return stock.getStock().getHistory();
    }

}
