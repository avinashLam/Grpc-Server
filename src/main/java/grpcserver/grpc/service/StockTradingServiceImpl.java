package grpcserver.grpc.service;

import com.java.grpc.StockRequest;
import com.java.grpc.StockResponse;
import com.java.grpc.StockTradingServiceGrpc;
import grpcserver.grpc.entity.Stock;
import grpcserver.grpc.repository.StockRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;


import java.time.Instant;
import java.util.concurrent.TimeUnit;

@GrpcService
public class StockTradingServiceImpl  extends StockTradingServiceGrpc.StockTradingServiceImplBase {
    private final StockRepository stockRepository;

    public StockTradingServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void getStockPrice(StockRequest stockRequest, StreamObserver<StockResponse> responseStreamObserver){
        String stockSymbol=stockRequest.getStockSymbol();
        Stock s=stockRepository.findByStockSymbol(stockSymbol);
        StockResponse stockResponse=StockResponse.newBuilder().setStockSymbol(s.getStockSymbol())
                .setPrice(s.getPrice())
                .setTimestamp(s.getLastUpdated().toString())
                .build();
        responseStreamObserver.onNext(stockResponse);
        responseStreamObserver.onCompleted();

    }

    @Override
    public void getStreamStockprice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        String symbol=request.getStockSymbol();
try{
        for(int i=0;i<10;i++){
            StockResponse stockResponse=StockResponse.newBuilder().setStockSymbol(symbol)
                    .setPrice(Math.random())
                    .setTimestamp(Instant.now().toString())
                    .build();
            responseObserver.onNext(stockResponse);

                TimeUnit.SECONDS.sleep(1);
            }
        responseObserver.onCompleted();

        }catch (Exception e){
              responseObserver.onError(e);
        }

    }
}
