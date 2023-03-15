import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.Iterator;
import java.util.concurrent.Executors;
import org.example.model.GRPCRequest;
import org.example.model.GRPCResponse;
import org.example.model.GRPCServiceGrpc;

public class ClientExample {

  private static final String ADDRESS = "localhost";
  private static final int PORT = 50051;

  public static void main(String[] args) {
    var executorService = Executors.newFixedThreadPool(10);
    var channel =
        ManagedChannelBuilder.forAddress(ADDRESS, PORT)
            .executor(executorService)
            .usePlaintext()
            .build();

    var bStub = GRPCServiceGrpc.newBlockingStub(channel);
    var request = GRPCRequest.newBuilder().setName("name").setAge(21).build();

    System.out.println("...calling UnaryProcedure");
    var response = bStub.gRPCProcedure(request);
    System.out.println("...after calling UnaryProcedure");
    System.out.println("--> Response: " + response);

    var nbStub = GRPCServiceGrpc.newStub(channel);

    System.out.println("...calling async UnaryProcedure");
    nbStub.gRPCProcedure(request, new UnaryObs());
    System.out.println("...after calling UnaryProcedure");

    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    System.out.println("...calling StreamProcedure");
    Iterator<GRPCResponse> respIterator = bStub.gRPCStreamProcedure(request);
    System.out.println("...after calling UnaryProcedure");

    while (respIterator.hasNext()) {
      System.out.println("--> " + respIterator.next().getMessage());
    }

    System.out.println("...calling async StreamProcedure");
    nbStub.gRPCStreamProcedure(request, new UnaryObs());
    System.out.println("...after calling StreamProcedure");

    executorService.shutdown();
    channel.shutdown();
  }

  private static class UnaryObs implements StreamObserver<GRPCResponse> {

    @Override
    public void onNext(GRPCResponse grpcResponse) {
      System.out.println("--> async unary onNext: " + grpcResponse.getMessage());
    }

    @Override
    public void onError(Throwable throwable) {
      System.out.println("--> async unary onError");
    }

    @Override
    public void onCompleted() {
      System.out.println("--> async unary onCompleted");
    }
  }
}
