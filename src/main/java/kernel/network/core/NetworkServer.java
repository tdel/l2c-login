package kernel.network.core;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract public class NetworkServer {

    private static final Logger logger = LogManager.getLogger();

    private int port;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ChannelFuture channel;


    abstract protected ChannelInitializer<SocketChannel> generateChannelInitializer();
    abstract protected int getPort();

    public void start() {
        this.port = this.getPort();
        logger.info("Starting server on port " + this.port);

        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap(); // (2)
        b.group(this.bossGroup, this.workerGroup)
                .channel(NioServerSocketChannel.class) // (3)
                .childHandler(this.generateChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
        this.channel = b.bind(this.port);

        try {
            this.channel.sync(); // Bind and start to accept incoming connections.
        } catch (InterruptedException ie) {
            logger.error(ie.getMessage());
            this.shutdownWorkers();

            return;
        }

        logger.info("Server started");
    }

    public void stop() {
        logger.info("Stopping server");

        try {
            this.channel.channel().closeFuture().sync();
        } catch (InterruptedException ie) {
            logger.error(ie.getMessage());
        } finally {
            this.shutdownWorkers();
        }

        logger.info("Server stopped");
    }

    private void shutdownWorkers() {
        this.workerGroup.shutdownGracefully();
        this.bossGroup.shutdownGracefully();

        logger.info("Workers has been shutdown");
    }
}
