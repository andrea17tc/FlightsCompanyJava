package ro.mpp2024.utils;


import ro.mpp2024.network.rpc.ClientRpcWorker;
import ro.mpp2024.service.IService;

import java.net.Socket;


public class RpcConcurrentServer extends AbsConcurrentServer {
    private IService server;
    public RpcConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
        System.out.println("RpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcWorker worker=new ClientRpcWorker(server, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
