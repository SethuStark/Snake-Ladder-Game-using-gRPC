package com.mycode.game.server;

import com.myCode.game.Die;
import com.myCode.game.GameState;
import com.myCode.game.Player;
import com.myCode.game.SnakeGameServiceGrpc;
import io.grpc.stub.StreamObserver;

public class SnakeGame extends SnakeGameServiceGrpc.SnakeGameServiceImplBase {

    @Override
    public StreamObserver<Die> roll(StreamObserver<GameState> responseObserver) {
        Player client = Player.newBuilder().setName("Client").setPosition(0).build();
        Player server = Player.newBuilder().setName("Server").setPosition(0).build();
        return new DieStreamingRequest(client, server, responseObserver);
    }
}
