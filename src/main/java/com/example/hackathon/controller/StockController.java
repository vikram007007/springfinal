package com.example.hackathon.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hackathon.model.StockWrapper;
import com.example.hackathon.model.Trade;
import com.example.hackathon.model.TradeState;
import com.example.hackathon.model.TradeType;
import com.example.hackathon.repository.StockRepository;
import com.example.hackathon.service.FinanceService;

import javax.validation.Valid;


@RestController
@RequestMapping("/stocks")
@CrossOrigin
public class StockController {
	
	@Autowired
	private FinanceService service;
	private StockRepository stockRepository;
	
	@GetMapping("/list")
	public List<Trade> getAll(){
		return service.getTrades();	
		}
	/*@GetMapping("/date/{date}")
	public List<Trade> getByDate(@PathVariable String date){
		return stockRepository.findByDate(date); 
	}*/
	
	@GetMapping("/ticker/{ticker}")
	public List<Trade> getByTicker(@PathVariable String ticker){
		return service.getTradeTicker(ticker); 
	}
	
	@GetMapping("/tickername")
	public List<String> getByTickerName(){
		return service.getTradebyName(); 
	}
	//@RequestMapping(method = RequestMethod.GET, value="/id/{id}")
	@GetMapping("/id/{id}")
	public Trade getById(@PathVariable("id") String id) {
		return service.getID(new ObjectId(""+id));
	}
	
	
	//@GetMapping("/buy")
	@RequestMapping(method = RequestMethod.GET, value="/buy")
	public List<Trade> getByType() {
		String t = "BUY";
		return service.getTradeBuy(t);
	}
	
	@GetMapping("/sell")
	public List<Trade> getByTypeSold() {
		String t = "SELL";
		return service.getTradeBuy(t);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/create")
	public Trade create(@Valid @RequestBody Trade stock) {
				stock.setDate(new Date(System.currentTimeMillis()));
		TradeState contactType = TradeState.valueOf("CREATED");
		TradeType TradType = TradeType.valueOf("BUY");
		stock.setState(contactType);
		stock.setType(TradType);
		stock.set_id(ObjectId.get());
		return service.buyTrade(stock);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/sell/{id}")
	public void sell(@Valid @RequestBody Trade trade,@PathVariable ObjectId id) {
		trade.set_id(id);
		trade.setType(TradeType.valueOf("SELL"));
		service.sellTrade(trade);
	}
	
	@GetMapping("/price/{symbol}")
	public BigDecimal stockPrice(@PathVariable String symbol) throws IOException {
		//FinanceService service=new FinanceService();
        StockWrapper w = service.findStock(symbol);
        return service.findPrice(w);
    }
	
	@GetMapping(value = "/symbol/{symbol}")
    public StockWrapper stockSymbol(@PathVariable String symbol) throws IOException {
        return service.findStock(symbol);
    }
	@GetMapping(value = "/history/{symbol}")
    public List<HistoricalQuote> stockHistory(@PathVariable String symbol) throws IOException {
        StockWrapper w = service.findStock(symbol);
        return service.findHistory(w);
    }
	
	@RequestMapping(method=RequestMethod.PUT, value="/{id}")
	public String update(@Valid @RequestBody Trade stock, @PathVariable ObjectId id) {
		Trade test = stockRepository.findBy_id(id);
		if(test.getState().equals("CREATED")) {
		stock.set_id(id);
		stockRepository.save(stock);
		return "Successfully updated";
		}
		else
			return "Status already updated";
	}
	/*@RequestMapping(method=RequetMethod.DELETE, value="/stocks")
	public String deleteAll() {
		stockService.deleteAll();
		return "deleted all stocks";
	}*/
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public String deleteById(@Valid @PathVariable ObjectId id) {
		stockRepository.delete(stockRepository.findBy_id(id));
		return "Deleted the " + id + "stock";
	}
}
