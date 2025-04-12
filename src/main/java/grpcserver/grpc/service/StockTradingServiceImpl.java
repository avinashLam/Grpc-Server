package grpcserver.grpc.service;

import com.javatechie.grpc.StockRequest;
import com.javatechie.grpc.StockResponse;
import com.javatechie.grpc.StockTradingServiceGrpc;
import grpcserver.grpc.entity.Stock;
import grpcserver.grpc.repository.StockRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

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

}
