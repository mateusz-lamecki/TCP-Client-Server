#pragma once

#include <string>

#include "resources.h"


namespace sk2 {

class SystemService {
    public:
    void handle_request(std::string request);

    private:
    std::vector<resources::Topic> topics;
    std::vector<resources::User> users;
};

}
