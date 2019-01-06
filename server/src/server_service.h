#pragma once

#include <memory>
#include <vector>
#include <string>
#include <thread>
#include <mutex>

#include "connection.h"
#include "request.h"
#include "system_service.h"


namespace sk2 {

class ServerService {
    public:
    ServerService(int server_port);
    void run();

    std::unique_ptr<Connection> connection;
    std::shared_ptr<SystemService> system_service;

    private:
    static void handle_pings(std::shared_ptr<SystemService> system_service);
};

} // namespace sk2
