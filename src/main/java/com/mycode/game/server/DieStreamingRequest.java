package com.mycode.game.server;

import com.myCode.game.Die;
import com.myCode.game.GameState;
import com.myCode.game.Player;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ThreadLocalRandom;

public class DieStreamingRequest implements StreamObserver<Die> {

    private Player client;
    private Player server;
    private StreamObserver<GameState> gameStateStreamObserver;

    public DieStreamingRequest(Player client, Player server, StreamObserver<GameState> gameStateStreamObserver) {
        this.client = client;
        this.server = server;
        this.gameStateStreamObserver = gameStateStreamObserver;
    }

    @Override
    public void onNext(Die die) {
        this.client = getNewPlayerPosition(client, die.getValue());
        if (this.client.getPosition() != 100){
            this.server = getNewPlayerPosition(server, ThreadLocalRandom.current().nextInt(1, 7));
        }
        this.gameStateStreamObserver.onNext(this.getGameState());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        this.gameStateStreamObserver.onCompleted();;
    }

    private GameState getGameState(){
        return GameState.newBuilder()
                .addPlayer(this.client)
                .addPlayer(this.server)
                .build();
    }

    private Player getNewPlayerPosition(Player player, int dieCount){
        int position = player.getPosition() + dieCount;
        if(position <= 100){
            player = player.toBuilder().setPosition(position).build();
        }
        return player;
    }
}
