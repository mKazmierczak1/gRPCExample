import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import org.example.model.GRPCRequest;
import org.example.model.GRPCResponse;
import org.example.model.GRPCServiceGrpc;

public class ServerExample {

  private static final int PORT = 50051;

  public static void main(String[] args) {
    Server server = ServerBuilder.forPort(PORT).addService(new GRPCServiceImpl()).build();

    try {
      server.start();
      System.out.println("Server started");
      server.awaitTermination();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  static class GRPCServiceImpl extends GRPCServiceGrpc.GRPCServiceImplBase {

    public void gRPCProcedure(GRPCRequest req, StreamObserver<GRPCResponse> responseObserver) {
      System.out.println("...called GrpcProcedure");
      String msg;

      if (req.getAge() > 18) {
        msg = "Mr/Ms " + req.getName();
      } else {
        msg = "Boy/Girl";
      }

      GRPCResponse response = GRPCResponse.newBuilder().setMessage("Hello " + msg).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }
}
