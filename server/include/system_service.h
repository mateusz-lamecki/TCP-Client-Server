#pragma once

#include <string>

#include "resources.h"
#include "request.h"


namespace sk2 {


class SystemService {
    public:
    request::Response handle_request(std::string request_raw);

    private:
    request::Response login_user(std::string login, std::string password);
    request::Response register_user(std::string login, std::string password);

    std::vector<resources::Topic> topics;
    std::vector<resources::User> users;
};

}
