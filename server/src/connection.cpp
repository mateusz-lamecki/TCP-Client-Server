#include "connection.h"


namespace sk2 {

namespace input {

std::string read_input(int connection_desc) {
    char *buffer = new char[BUFFER_SIZE];
    memset(buffer, 0, sizeof(char)*BUFFER_SIZE);
    read(connection_desc, buffer, sizeof(char)*BUFFER_SIZE);

    std::string result(buffer);
    printf("%s\n", buffer);

    delete[] buffer;

    return result;
}

} // namespace input


Connection::Connection(int server_port) : server_port(server_port) {
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

void *Connection::thread_behavior(void *t_data) {
    pthread_detach(pthread_self());

    int client_fd = *((int*)t_data);

    while(true) {
        std::string input = input::read_input(client_fd);
        if(input.empty()) break;
        printf("Wczytano od %d: %s\n", client_fd, input.c_str());
        auto type = request::Request(input).detect_action();
        auto type2 = static_cast<std::underlying_type<request::Action>::type>(type);
        printf("TYP: %d\n\n", type2);
    }

    pthread_exit(NULL);
}


void Connection::handle_connection(int connection_desc) {
    pthread_t child;
    int create_result = pthread_create(&child, NULL, &Connection::thread_behavior, (void *)&connection_desc);
    if (create_result) {
       printf("Błąd przy próbie utworzenia wątku, kod błędu: %d\n", create_result);
       exit(-1);
    }
}

void Connection::run(std::shared_ptr<SystemService> system_service) {
    while(true) {
        int connection_desc = accept(server_desc, NULL, NULL);
        if (connection_desc < 0) {
            fprintf(stderr, "Błąd przy próbie utworzenia gniazda dla połączenia.\n");
            exit(1);
        }

        handle_connection(connection_desc);
    }
}

void sk2::Connection::close() {
    ::close(server_desc);
}


} // namespace sk2
