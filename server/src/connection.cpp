#include "connection.h"

#include <iostream>


namespace sk2 {

namespace input {

std::string read_input(int connection_desc) {
    char *buffer = new char[BUFFER_SIZE];
    memset(buffer, 0, sizeof(char)*BUFFER_SIZE);
    read(connection_desc, buffer, sizeof(char)*BUFFER_SIZE);

    std::string result(buffer);
    delete[] buffer;

    return result;
}

} // namespace input


Connection::Connection(int server_port, std::shared_ptr<SystemService> system_service) : server_port(server_port), system_service(system_service) {
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

void *Connection::wrap_pthread_create(void *content) {
    pthread_detach(pthread_self());

    ThreadContent *thread_content = (ThreadContent*)content;
    thread_content->connection->handle_client(thread_content->client_fd);

    pthread_exit(NULL);
}

void Connection::handle_client(int client_fd) {
    /* This function is called once during thread initiation */
    std::cout << "Established connection with client #" << client_fd << std::endl;

    while(true) {
        std::string input = input::read_input(client_fd);
        if(input.empty()) {
            /* connection lost */
            std::cout << "Lost connection with client #" << client_fd << std::endl;
            return;
        }

        request::Response response = system_service->handle_request(input);
        std::string response_str = response.to_string() std::string("\n");
        write(client_fd, response_str.c_str(), sizeof(char)*response_str.size());
    }
}


void Connection::run() {
    while(true) {
        int connection_desc = accept(server_desc, NULL, NULL);
        if (connection_desc < 0) {
            fprintf(stderr, "Błąd przy próbie utworzenia gniazda dla połączenia.\n");
            exit(1);
        }

        pthread_t child;
        ThreadContent thread_content { connection_desc, this };
        int create_result = pthread_create(&child, NULL, &Connection::wrap_pthread_create, (void *)&thread_content);
        if (create_result) {
           printf("Błąd przy próbie utworzenia wątku, kod błędu: %d\n", create_result);
           exit(-1);
        }
    }
}

void sk2::Connection::close() {
    ::close(server_desc);
}


} // namespace sk2
