#pragma once

#include <string>

#include "resources.h"
#include "request.h"


namespace sk2 {


class SystemService {
    public:
    SystemService();
    SystemService(const SystemService&) = delete;
    request::Response handle_request(std::string request_raw, int client_fd);
    resources::Resources& get_res();

    private:
    resources::Resources res;
};

}
