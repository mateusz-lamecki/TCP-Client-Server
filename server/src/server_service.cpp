#include "server_service.h"

#include <cstdio>
#include "resources.h"


namespace sk2 {

ServerService::ServerService(int server_port) {
    system_service = std::make_shared<SystemService>();
    connection = std::make_unique<Connection>(server_port, system_service);
}

void ServerService::run() {
    std::thread pings_thread(handle_pings, system_service);
    connection->run();
}

void ServerService::handle_pings(std::shared_ptr<SystemService> system_service) {
    while(true) {
        std::this_thread::sleep_for(std::chrono::milliseconds(200));
        auto &res = system_service->get_res();

        res.mutex_ping_user_topic.lock();

        for(auto x : res.get_logged_clients()) {
            auto &ping_user_topic = res.get_ping_user_topic();
            for(std::string topic_id : ping_user_topic[x.second]) {
                std::string message = "PING" + request::DELIMITER + topic_id + "\n";
                Connection::write_to_client(x.first, message);
            }

            ping_user_topic[x.second].clear();
        }

        res.mutex_ping_user_topic.unlock();
    }
}

} // namespace sk2

