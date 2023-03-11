import io.grpc.ManagedChannelBuilder;
import org.example.model.GRPCRequest;
import org.example.model.GRPCServiceGrpc;

public class ClientExample {

  private static final String ADDRESS = "localhost";
  private static final int PORT = 50051;

  public static void main(String[] args) {
    var channel = ManagedChannelBuilder.forAddress(ADDRESS, PORT).usePlaintext().build();
    var stub = GRPCServiceGrpc.newBlockingStub(channel);
    var request = GRPCRequest.newBuilder().setName("name").setAge(21).build();
    var response = stub.gRPCProcedure(request);

    System.out.println(response);
    channel.shutdown();
  }
}
