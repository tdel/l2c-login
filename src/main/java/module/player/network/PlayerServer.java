package module.player.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.Logger;

public class PlayerServer {

    private Logger logger = Logger.getLogger(PlayerServer.class.getName());

    private int port;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ChannelFuture channel;



    public PlayerServer(int _port) {
        this.port = _port;
    }

    public void start() {
        this.logger.info("Starting server on port " + this.port);

        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap(); // (2)
        b.group(this.bossGroup, this.workerGroup)
                .channel(NioServerSocketChannel.class) // (3)
                .childHandler(new ClientChannelInitializer(new ClientHandler()))
                .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
        this.channel = b.bind(this.port);

        try {
            this.channel.sync(); // Bind and start to accept incoming connections.
        } catch (InterruptedException ie) {
            this.logger.severe(ie.getMessage());
            this.shutdownWorkers();

            return;
        }

        this.logger.info("Server started");
    }

    public void stop() {
        this.logger.info("Stopping server");

        try {
            this.channel.channel().closeFuture().sync();
        } catch (InterruptedException ie) {
            this.logger.warning(ie.getMessage());
        } finally {
            this.shutdownWorkers();
        }

        this.logger.info("Server stopped");
    }

    private void shutdownWorkers() {
        this.workerGroup.shutdownGracefully();
        this.bossGroup.shutdownGracefully();

        this.logger.fine("Workers has been shutdown");
    }
}
