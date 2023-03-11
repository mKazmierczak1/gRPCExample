import io.grpc.stub.StreamObserver;
import org.example.model.GRPCRequest;
import org.example.model.GRPCResponse;
import org.example.model.GRPCServiceGrpc;

public class Server {

  static class GRPCServiceImpl extends GRPCServiceGrpc.GRPCServiceImplBase {

    public void gRPCProcedure(GRPCRequest req, StreamObserver<GRPCResponse> responseObserver) {
      System.out.println("...called GrpcProcedure");
      String msg;

      if (req.getAge() > 18) {
        msg = "Mr/Ms " + req.getName();
      } else {
        msg = "Boy/Girl ";
      }

      GRPCResponse response = GRPCResponse.newBuilder().setMessage("Hello " + msg).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }
}
