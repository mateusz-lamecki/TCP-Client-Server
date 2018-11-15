#pragma once

#include <string>

#include "resources.h"
#include "request.h"


namespace sk2 {


class SystemService {
    public:
    request::Response login_user(std::string login, std::string password);
    request::Response register_user(std::string login, std::string password);

    private:
    std::vector<resources::Topic> topics;
    std::vector<resources::User> users;
};

}
