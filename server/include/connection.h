#pragma once

#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <csignal>
#include <cstring>
#include <cstdio>
#include <cstdlib>
#include <string>
#include <ctime>
#include <pthread.h>

namespace sk2 {

const int BUFFER_SIZE = 4096;
char buffer[BUFFER_SIZE];

class Connection {
    public:
    Connection(int server_port);
    void init();
    void *thread_behavior(void *t_data);
    std::string read_input(int connection_desc);
    void handle_connection(int connection_desc);
    void main_loop();
    void close();

    private:
    const int QUEUE_SIZE = 5;
    int server_port;
    int server_desc;
};

} // namespace sk2
