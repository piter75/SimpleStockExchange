# Simple Stock Exchange

## Overview

A very simple stock exchange application implemented in plain Java.

## Requirements

- JDK 8
- Maven 3.x

## Building with Maven

```
mvn clean install
```

## Running a simple example with Maven

```
mvn exec:java
```

## Implementation details

This simple application is implemented using separate queues (FIFO) for *Trades* on every stock that is managed by *SimpleStockExchange*.

This implementation is accompanied by unit and integration tests that cover 97% of the code.

### SimpleStockExchange

This is the central class and a single point of interaction between client and *Simple Stock Exchange* application. This interaction is defined by *StockExchange* interface.

This implementation allows registration of *Trades* on managed stocks and calculation of various simplified properties interesting to investors.

Price of the stock is undefined until there is at least one *Trade* registered on it. If there were no trades during the last *15 minutes* then last calculated value is returned.

Most of the methods exposed by this class are implemented in the following way:
 - find a *StockManager* implementation for requested stock symbol;
   - if the implementation is found then delegate to the method implementing operation requested by client to the *StockManager* implementation found;
   - if no *StockManager* is found then *StockNotFound* exception is thrown.

The method *calculateAllStockIndex()* is implemented by traversing all stocks managed by *SimpleStockExchange* and calculating geometric mean of those which have defined price.

### AbstractStockManager

This is the abstract class that implements most of the methods defined in *StockManager* interface except for the *calculateDividendYield* which is to be implemented in concrete *StockManager* implementations *CommonStockManager* and *PreferredStockManager* or any other in the future if there is a need for that.

*AbstractStockManager* uses a first-in-first-out (FIFO) queue to register trades happening on stock. The *LinkedList* has been chosen as a concrete *Queue* implementation.

The *Trades* are registered in *registerTrade* method which uses a synchronized block to add a *Trade* to the queue. It also performs some simple calculations in the same synchronized block. It adds the registered *Trade* quantity and value (quantity multiplied by price) to two accumulators which holds amount and value of stock sold in recent time. This simple calculations are performed during the *registerTrade* phase to optimize and reduce the queue traversals and calculations performed for calculating the price and other properties of stock.

The current price of stock is calculated in *calculateStockPrice* method which also uses a synchronized block in which it:
 - peeks into the queue in a loop until it find a *Trade* that is not registered earlier than *15 minutes* ago;
   - removes obsolete *Trades* from the queue;
   - decreases values of the mentioned earlier accumulators holding quantity and value of recent stock trades according to the quantity and value of the *Trades* removed from the queue;
 - calculates the current price of the stock by dividing accumulated value by quantity.

Both synchronized blocks in *registerTrade* and *calculateStockPrice* methods are synchronized on the same variable - the queue that holds *Trades* for queue consistency.

**NOTICE** It is important to note that this solution may not be ideal in the real world application with high trade volumes because of possible lock contention but this application is a simple implementation for which it is good enough.

#### CommonStockManager and PreferredStockManager

Both of these classes extend AbstractStockManager and implement StockManager interface.

Each of them adds its specialized constructor taking only required for concrete implementation parameters which are:
 - *lastDividend* for *CommonStockManager*;
 - *fixedDividend* and *parValue* in case of *PreferredStockManager*.

*CommonStockManager* and *PreferredStockManager* implement its own versions of *calculateDividendYield()* method.

### StockNotFound

The exception that is thrown by *SimpleStockRepository* in case of client trying to operate on stock which symbol is unknown to the *SimpleStockExchange*.

### Classes summary

Application consists of the following classes:
 - *StockType* stock type enum with values for common and preferred stock;
 - *TradeType* trade type enum with values for buy and sell trade types;
 - *Trade* an entity that holds the actual trade properties for every trade;
 - *StockManager* defines interface for managing stock properties and trades on stock for both common and preferred stock:
   - *AbstractStockManager* implements methods that are common to both common and preferred stock:
     - *CommonStockManager* a manager for common stock;
     - *PreferredStockManager* a manager for preferred stock;
 - *StockManagerFactory* a factory that creates appropriate *StockManager* for use by *SimpleStockExchange* class;
 - *StockExchange* defines interface between outside world and the SimpleStockExchange application;
 - *SimpleStockExchange* implements *StockExchange* interface;
 - *SimpleStockRepository* a repository holding a *Map* of stock symbols mapped to *StockManager* that is managing the operations on stock associated with the symbol;
 - *StockNotFound* exception that is thrown by the *SimpleStockRepository* and passed through *SimpleStockExchange* to clients that try to operate on stock which symbol is not known to the repository;
