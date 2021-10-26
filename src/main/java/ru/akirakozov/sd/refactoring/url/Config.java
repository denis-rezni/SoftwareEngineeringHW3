package ru.akirakozov.sd.refactoring.url;

public class Config {
    final String host;
    final int port;
    final String request;

    public Config(String host, int port, String request) {
        this.host = host;
        this.port = port;
        this.request = request;
    }
}