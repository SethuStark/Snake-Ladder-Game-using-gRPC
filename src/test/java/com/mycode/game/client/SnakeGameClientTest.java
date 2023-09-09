package com.mycode.game.client;

import com.myCode.game.Die;
import com.myCode.game.SnakeGameServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SnakeGameClientTest {

    private SnakeGameServiceGrpc.SnakeGameServiceStub stub;

    @BeforeAll
    public void setup(){

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();
        this.stub = SnakeGameServiceGrpc.newStub(channel);
    }

    @Test
    public void ClientGame () throws InterruptedException {
        System.out.println("Game Starts !!!");
        CountDownLatch latch = new CountDownLatch(1);
        GameStateStreamingResponse gameStateStreamingResponse = new GameStateStreamingResponse(latch);
        StreamObserver<Die> dieStreamObserver = this.stub.roll(gameStateStreamingResponse);
        gameStateStreamingResponse.setDieStreamObserve(dieStreamObserver);
        gameStateStreamingResponse.roll();
        latch.await();

    }
}
