#include "server_service.h"


namespace sk2 {

ServerService::ServerService(int server_port) {
    system_service = std::make_shared<SystemService>(SystemService());
    connection = std::make_unique<Connection>(Connection(server_port, system_service));
}

void ServerService::run() {
    std::thread pings_thread(handle_pings);
    connection->run();
}

void ServerService::handle_pings() {
    while(true) {
    }
}

} // namespace sk2

