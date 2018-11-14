#include "server_service.h"


namespace sk2 {

ServerService::ServerService(int server_port) {
    connection = std::make_unique<Connection>(Connection(server_port));
    system_service = std::make_shared<SystemService>(SystemService());
}

void ServerService::run() {
    connection->run(system_service);
}


} // namespace sk2

