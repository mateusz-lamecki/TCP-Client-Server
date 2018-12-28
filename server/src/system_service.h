#pragma once

#include <string>

#include "resources.h"
#include "request.h"


namespace sk2 {


class SystemService {
    public:
    request::Response handle_request(std::string request_raw, int client_fd);

    private:
    resources::Resources res;
};

}
