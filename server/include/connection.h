#pragma once

#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <memory>
#include <unistd.h>
#include <netdb.h>
#include <csignal>
#include <cstring>
#include <cstdio>
#include <cstdlib>
#include <string>
#include <ctime>
#include <pthread.h>

#include "system_service.h"


namespace sk2 {

namespace input {

const int BUFFER_SIZE = 4096;
std::string read_input(int connection_desc);

} // namespace input


class Connection {
    public:
    Connection(int server_port, std::shared_ptr<SystemService> system_service);
    static void *wrap_pthread_create(void *content);
    static void handle_client(int client_fd);
    void run();
    void close();

    private:
    const int QUEUE_SIZE = 5;
    int server_port;
    int server_desc;

    std::shared_ptr<SystemService> system_service;
};

struct ThreadContent {
    int client_fd;
    Connection* connection;
};

} // namespace sk2
