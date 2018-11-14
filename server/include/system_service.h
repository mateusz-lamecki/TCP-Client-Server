#pragma once

#include <string>

#include "resources.h"
#include "request.h"


namespace sk2 {


class SystemService {
    public:
    void handle_request(std::string request);

    private:
    request::Request* detect_request(std::string request);
    std::vector<resources::Topic> topics;
    std::vector<resources::User> users;
};

}
