#include "serverservice.h"

namespace sk2 {

namespace input {
std::string read_input(int connection_desc) {
    char *buffer = new char[BUFFER_SIZE];
    memset(buffer, 0, BUFFER_SIZE);
    read(connection_desc, buffer, BUFFER_SIZE-1);

    std::string result(buffer);

    delete[] buffer;

    return result;
}
} // namespace input



ServerService::ServerService(int server_port) : server_port(server_port) { }

void ServerService::init() {
    struct sockaddr_in server_address;
    memset(&server_address, 0, sizeof(struct sockaddr));
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    server_address.sin_port = htons(server_port);

    server_desc = socket(AF_INET, SOCK_STREAM, 0);
    if (server_desc < 0) {
        fprintf(stderr, "Błąd przy próbie utworzenia gniazda..\n");
        exit(1);
    }
    const char reuse_addr_val = 1;
    setsockopt(server_desc, SOL_SOCKET, SO_REUSEADDR, (char*)&reuse_addr_val, sizeof(reuse_addr_val));

    int bind_result = bind(server_desc, (struct sockaddr*)&server_address, sizeof(struct sockaddr));
    if (bind_result < 0) {
        fprintf(stderr, "Błąd przy próbie dowiązania adresu IP i numeru portu do gniazda.\n");
        exit(1);
    }

    int listen_result = listen(server_desc, QUEUE_SIZE);
    if (listen_result < 0) {
        fprintf(stderr, "Błąd przy próbie ustawienia wielkości kolejki.\n");
        exit(1);
    }
}

void *ServerService::thread_behavior(void *t_data) {
    pthread_detach(pthread_self());

    int *a = (int*)t_data;

    std::string input = input::read_input(*a);
    printf("Wczytano: %s\n", input.c_str());

    pthread_exit(NULL);
}


void ServerService::handle_connection(int connection_desc) {
    pthread_t child;
    int create_result = pthread_create(&child, NULL, &ServerService::thread_behavior, (void *)&connection_desc);
    if (create_result) {
       printf("Błąd przy próbie utworzenia wątku, kod błędu: %d\n", create_result);
       exit(-1);
    }
}

void ServerService::main_loop() {
    while(true) {
        int connection_desc = accept(server_desc, NULL, NULL);
        if (connection_desc < 0) {
            fprintf(stderr, "Błąd przy próbie utworzenia gniazda dla połączenia.\n");
            exit(1);
        }

        handle_connection(connection_desc);
    }
}

void sk2::ServerService::close() {
    ::close(server_desc);
}


} // namespace sk2
