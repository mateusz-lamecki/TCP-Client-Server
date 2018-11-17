#pragma once

#include <vector>
#include <string>


namespace sk2 {

namespace resources {

class Topic {
    std::string name;
    std::vector<std::string> description;
};

class User {
    public:
    User(std::string login, std::string password);

    void handle_login(std::string login, std::string password);
    void handle_register(std::string login, std::string password);

    private:
    std::string generate_token();
    std::string login;
    std::string password;
    std::string token;
};

struct Resources {
    std::vector<Topic> topic;
    std::vector<User> users;
};

} // namespace resources

} // namespace sk2
