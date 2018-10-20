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

struct thread_data_t {
};

class Connection {
    public:
    Connection(int server_port);
    void init();
    static void *thread_behavior(void *t_data);
    void handle_connection(int connection_desc);
    void main_loop();
    void close();

    private:
    const int QUEUE_SIZE = 5;
    int server_port;
    int server_desc;
};

} // namespace sk2
