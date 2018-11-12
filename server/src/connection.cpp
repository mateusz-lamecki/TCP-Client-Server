#include "connection.h"

namespace sk2 {

Connection::Connection() { }

void Connection::init() {
    struct sockaddr_in server_address;
    memset(&server_address, 0, sizeof(struct sockaddr));
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    server_address.sin_port = htons(SERVER_PORT);

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
    struct thread_data_t *th_data = (struct thread_data_t*)t_data;
    //TODO transmitting

    pthread_exit(NULL);
}

void Connection::handle_connection(int connection_desc) {
    pthread_t thread1;

    //TODO wypełnienie pól struktury
    struct thread_data_t t_data;

    int create_result = pthread_create(&thread1, NULL, &Connection::thread_behavior, (void *)&t_data);
    if (create_result) {
       printf("Błąd przy próbie utworzenia wątku, kod błędu: %d\n", create_result);
       exit(-1);
    }

    //TODO (przy zadaniu 1) odbieranie -> wyświetlanie albo klawiatura -> wysyłanie
}

void Connection::main_loop() {
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
