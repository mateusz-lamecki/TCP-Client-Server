#pragma once

#include <memory>
#include <vector>
#include <string>
#include <thread>

#include "connection.h"
#include "system_service.h"
#include "resources.h"


namespace sk2 {

class ServerService {
    public:
    ServerService(int server_port);
    void run();

    private:
    static void handle_pings();
    std::unique_ptr<Connection> connection;
    std::shared_ptr<SystemService> system_service;
};

} // namespace sk2
